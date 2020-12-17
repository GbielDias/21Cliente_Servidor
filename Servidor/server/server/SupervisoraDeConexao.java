package server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Objects;
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

		Comunicado comunicado;
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
								dealer.resetDealer(); //Acredito que essa parte não vá ficar aqui e sim em outro for para ser mandada pra todos os jogadores
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

	@Override
	public String toString() //TODO Implementar toString na Supervisora
	{
		String ret = "Supervisora do usuário: "+ usuario;
		return ret;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;

		if (o == null || getClass() != o.getClass()) return false;

		SupervisoraDeConexao sup = (SupervisoraDeConexao) o;

		if (!this.usuario.equals(sup.usuario)) return false;

		if (!this.mao.equals(sup.mao)) return false;

		if (!this.conexao.equals(sup.conexao)) return false;

		if (!this.dealer.equals(sup.dealer)) return false;

		if (!this.gerenciadora.equals(sup.gerenciadora)) return  false;

		if (!this.receptor.equals(sup.receptor)) return false;

		if (!this.transmissor.equals(sup.transmissor)) return false;

		if (this.usuarios.size() != sup.usuarios.size()) return false;

		for(int i = 0; i < this.usuarios.size();i++)
			if (!this.usuarios.get(i).equals(sup.usuarios.get(i)))
				return false;

			return true;
	}

	@Override
	public int hashCode()
	{
		int ret = 255;

		ret = ret * 11 + this.gerenciadora.hashCode();
		ret = ret * 11 + this.usuario.hashCode();
		ret = ret * 11 + this.dealer.hashCode();
		ret = ret * 11 + this.mao.hashCode();
		ret = ret * 11 + this.conexao.hashCode();
		ret = ret * 11 + this.receptor.hashCode();
		ret = ret * 11 + this.transmissor.hashCode();

		for (Parceiro parc:usuarios)
			ret = ret * 11 + parc.hashCode();

		return Math.abs(ret);
	}
}
