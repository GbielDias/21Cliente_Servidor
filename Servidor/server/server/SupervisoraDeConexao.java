package server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;


import commons.*;
public class SupervisoraDeConexao extends Thread {
	private Socket conexao;
	private Semaphore mutEx;
	private GerenciadoraDeRodada gerenciadora;
	private Parceiro usuario;
	private ArrayList<Parceiro> usuarios;
	private Dealer dealer;
	private MaoDoJogador mao;
	

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

		ObjectOutputStream transmissor;
		try
		{
			transmissor = new ObjectOutputStream(this.conexao.getOutputStream());
		}
		catch (Exception erro)
		{
			return;
		}

		ObjectInputStream receptor;
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

		while(true)
		{

			if(gerenciadora != null && gerenciadora.pode(usuario))
			{
				try
				{
					usuario.receba(new PermissaoDeRodada());

					vezDoUsuario();

				}catch(Exception e){}
			}
		}



	}
	
	private void vezDoUsuario() {
		try {

//			mutEx.acquireUninterruptibly();

			usuario.receba(this.mao);


			Comunicado comunicado = this.usuario.envie();

			if (comunicado == null)
				return;

			else if (comunicado instanceof Pedido)
			{
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
							usuario.receba(new Pedido(mao, "Descartada inexistente"));
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

			}


			//Acaba a vez do jogador
//			mutEx.release();


			gerenciadora.proximoJogador();


		}catch(Exception e) {
			System.err.println("aqui " + e.getMessage());
		}
	}
}