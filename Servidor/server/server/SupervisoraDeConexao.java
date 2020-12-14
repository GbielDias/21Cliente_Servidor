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
	private MaoDoJogador mao;
	private ObjectOutputStream transmissor;
	private ObjectInputStream receptor;

	public SupervisoraDeConexao(Socket conexao, ArrayList<Parceiro> usuarios, Dealer dealer, GerenciadoraDeRodada gerenciadora) throws Exception {
		if (conexao == null)
			throw new Exception("Conexao ausente");

		if (usuarios == null)
			throw new Exception("Usuarios ausentes");

		if (dealer == null)
			throw new Exception("Dealer Invalido");

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
		}catch(Exception e){}

		while(true) {
			try {
				if (gerenciadora != null && gerenciadora.pode(usuario)) {
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
						//Estou usando Pedido para informar o cliente, porém isso poderá mudar
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
					for (Parceiro parceiro: usuarios)
					{
						if (!(this.usuario == parceiro))
							parceiro.receba(new ComunicadoDeDerrota());

						if(parceiro == usuarios.get(0))
						{
							parceiro.receba(new ComunicadoDeRestart());
						}
//						usuarios.remove(parceiro);
//						parceiro.encerrar();
					}
				}
			}
			gerenciadora.proximoJogador();

		}catch(Exception e) {
			try
			{
				transmissor.close();
				receptor.close();
			}
			catch (Exception falha){}
		}
	}
}
