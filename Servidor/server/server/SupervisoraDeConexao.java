package server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;


import commons.*;
public class SupervisoraDeConexao extends Thread {
	private Socket conexao;
	private GerenciadoraDeRodada gerenciadora;
	private Parceiro usuario;
	private ArrayList<Parceiro> usuarios;
	private Dealer dealer;
	public MaoDoJogador mao;
	private ObjectOutputStream transmissor;
	private ObjectInputStream receptor;

	public SupervisoraDeConexao(Socket conexao, ArrayList<Parceiro> usuarios, Dealer dealer, GerenciadoraDeRodada gerenciadora) throws Exception {
		if (conexao == null)
			throw new Exception("Conexao ausente");

		if (usuarios == null)
			throw new Exception("Usuarios ausentes");

		if (dealer == null)
			throw new Exception("Dealer Invalido");

		if (gerenciadora == null)
			throw new Exception("Gerenciadora Invalida");

		this.conexao = conexao;
		this.usuarios = usuarios;
		this.dealer = dealer;
		this.mao = new MaoDoJogador(dealer.getBaralho());
		this.gerenciadora = gerenciadora;
	}

	public void run() {
		try
		{
			transmissor = new ObjectOutputStream(this.conexao.getOutputStream());
		}
		catch (Exception erro)
		{
			return;
		}


		try {
			receptor = new ObjectInputStream(this.conexao.getInputStream());
		}
		catch (Exception erro)
		{
			try
			{
				transmissor.close();
			}
			catch (Exception falha)
			{}

			return;
		}

		try {
			this.usuario = new Parceiro(this.conexao, receptor, transmissor);
		} catch (Exception erro) {}

		try
		{

			synchronized (usuarios)
			{
				this.usuarios.add(this.usuario);
			}

		}
		catch (Exception erro) {
			try {
				transmissor.close();
				receptor.close();
			} catch (Exception falha) {
			} // so tentando fechar antes de acabar a thread

			return;
		}

		try {
			do {}
			while (!(usuario.espiar() instanceof ComunicadoDeComecar));

			usuario.envie();
		}catch(Exception e){
			System.out.println(e.getMessage());
		}

		while(true) {
			try {
				if (gerenciadora.pode(usuario)) {
					vezDoUsuario();
				}
			}
			catch (Exception e){
				return;
			}
		}

	}
	
	private void vezDoUsuario() {
		try {

			usuario.receba(new PermissaoDeRodada());

			usuario.receba(this.mao);

			if(dealer.getDescartada() == null){
                usuario.receba(new Carta("Nula", "Nula"));
            }
			else
				usuario.receba(dealer.getDescartada());

			Comunicado comunicado = null;

			comunicado = this.usuario.envie();

			if (comunicado == null)
				return;

			else if (comunicado instanceof Pedido){
				Pedido pedido = (Pedido) comunicado;

				switch (pedido.getPedido()){
					case "C":
						mao = dealer.comprarBaralho(pedido.getMao());
						usuario.receba(mao);

						pedido = (Pedido) usuario.envie();

						mao = dealer.descartar(pedido.getMao(), pedido.getPedido());
						usuario.receba(mao);
						break;
					case "D":
						if(dealer.getDescartada() == null){
							mao = dealer.comprarBaralho(pedido.getMao());

							usuario.receba(new Pedido(mao, "Você recebeu uma carta do baralho porque não há descartada"));
							usuario.receba(mao);

							pedido = (Pedido) usuario.envie();

							mao = dealer.descartar(pedido.getMao(), pedido.getPedido());
							usuario.receba(mao);
							break;
						}
						else
						{
							mao = dealer.comprarDescartada(pedido.getMao());
							usuario.receba(mao);

							pedido = (Pedido) usuario.envie();

							mao = dealer.descartar(pedido.getMao(), pedido.getPedido());
							usuario.receba(mao);
						}
						break;
				}
			} else if (comunicado instanceof PedidoParaSair) {
				synchronized (this.usuarios)
				{
					if (this.usuario == usuarios.get(2))
					gerenciadora.proximoJogador();

					this.usuarios.remove (this.usuario);
				}
				this.usuario.encerrar();
				return;
			}

			if(mao.contar() == 21)
			{
				this.usuario.receba(new ComunicadoDeVitoria());

				synchronized (usuarios)
				{
					for (int i = usuarios.size()-1; i >= 0;i--)
					{
						if (this.usuario != usuarios.get(i))
							usuarios.get(i).receba(new ComunicadoDeDerrota());

						if(usuarios.get(i) == usuarios.get(0))
						{
							usuarios.get(i).receba(new ComunicadoDeRestart());

							Comunicado comReiniciar;
							do
							{
								comReiniciar = usuarios.get(i).espiar();
							}
							while(!(comReiniciar instanceof Pedido));

							Pedido reiniciar = (Pedido) usuarios.get(i).envie();

							if(reiniciar.getPedido().equals("REINICIAR"))
							{
								dealer.resetDealer();
								gerenciadora.resetarMao(dealer);
								gerenciadora.setJ(0);

								synchronized (usuarios)
								{
									for (Parceiro par:usuarios)
										par.receba(new ComunicadoDeReinicio());
								}

								return;
							}
							else if(reiniciar.getPedido().equals("DESLIGAR"))
							{
								for (Parceiro usuario: usuarios){

									usuario.receba(new ComunicadoDeFimDeJogo());
								}


								for (int j = usuarios.size() - 1;  j >= 0; j--){
									usuarios.remove(j);
								}

								dealer.resetDealer();
								gerenciadora.setJ(0);

								return;
							}
						}
					}
				}
			}
			gerenciadora.proximoJogador();

		}catch(Exception e) {
			try
			{
				transmissor.close();
				receptor.close();
				System.err.println(e.getMessage());
			}
			catch (Exception falha){}
		}
	}

	public MaoDoJogador getMaoDoJogador() {
		return mao;
	}
}
