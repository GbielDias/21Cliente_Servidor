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
	
	private Parceiro usuario;
	private ArrayList<Parceiro> usuarios;
	private Dealer dealer;
	private MaoDoJogador mao;
	

	public SupervisoraDeConexao(Socket conexao, ArrayList<Parceiro> usuarios, Dealer dealer) throws Exception {
		if (conexao == null)
			throw new Exception("Conexao ausente");

		if (usuarios == null)
			throw new Exception("Usuarios ausentes");

		if (dealer == null)
			throw new Exception("Dealer Invalido");
		
		// if(baralho == null)
		// throw new Exception("Cartas ausentes");

		this.conexao = conexao;
		this.mutEx = mutEx;
		this.usuarios = usuarios;
		this.dealer = dealer;
		this.mao = new MaoDoJogador(dealer.getBaralho());
		// this.baralho = baralho;
	}

	public void run() {

		ObjectOutputStream transmissor;
		try {
			transmissor = new ObjectOutputStream(this.conexao.getOutputStream());
		} catch (Exception erro) {
			return;
		}

		ObjectInputStream receptor;
		try {
			receptor = new ObjectInputStream(this.conexao.getInputStream());
		} catch (Exception erro) {
			try {
				transmissor.close();
			} catch (Exception falha) {
			}

			return;
		}

		try {
			this.usuario = new Parceiro(this.conexao, receptor, transmissor);
		} catch (Exception erro) {
		}

		try {
			synchronized (this.usuarios) {
				this.usuarios.add(this.usuario);

				if (usuarios.size() > 0) // está 1 só pra teste
				{
					try {
						for (int o = 0; o < usuarios.size(); o++)
							usuarios.get(o).receba(new ComunicadoDeComecar());
					} catch (Exception e) {
					}

					//Start uma thread gerenciar a partida
				}

			}

			vezDoUsuario();

		}

		catch (Exception erro) {
			try {
				transmissor.close();
				receptor.close();
			} catch (Exception falha) {
			} // so tentando fechar antes de acabar a thread

			return;
		}
	}
	
	private void vezDoUsuario() {
		try {

//			mutEx.acquireUninterruptibly();

			usuario.receba(this.mao);

			while (true) {
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
								//break;
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
			}
			
			//Acaba a vez do jogador
//			mutEx.release();
		}catch(Exception e) {
			System.err.println("aqui " + e.getMessage());
		}
	}
}
