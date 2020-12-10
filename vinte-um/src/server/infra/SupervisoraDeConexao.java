package server.infra;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import server.Comunicado;
import server.Dealer;
import server.MaoDoJogador;
import server.Parceiro;
import server.cases.PedidoDeCompraDescarte;

public class SupervisoraDeConexao extends Thread {
	private Socket conexao;
	private Semaphore mutEx;
	
	private Parceiro usuario;
	private ArrayList<Parceiro> usuarios;
	// private ArrayList<Carta> baralho;
	private Dealer dealer;
	private MaoDoJogador mao;
	

	public SupervisoraDeConexao(Socket conexao, ArrayList<Parceiro> usuarios, Dealer dealer, Semaphore mutEx) throws Exception {
		if (conexao == null)
			throw new Exception("Conexao ausente");

		if (usuarios == null)
			throw new Exception("Usuarios ausentes");

		if (dealer == null)
			throw new Exception("Dealer Inv√°lido");

		if (mutEx == null)
			throw new Exception("Sem·foro inv·lido");
		
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
			}

			/*
			 * while (usuarios.size() < 3) { for(Parceiro usuario: usuarios) {
			 * usuario.receba(new ComunicadoDeAguarde()); } }
			 */

			
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
			//ComeÁa a vez do jogador
			mutEx.acquireUninterruptibly();
		
			while (true) {
				Comunicado comunicado = this.usuario.envie();

				if (comunicado == null)
					continue;
				
				else if (comunicado instanceof PedidoDeCompraDescarte) {
					dealer.comprarDescarte(mao);
//					usuario.receba(mao);
					break;
				}
			}
			
			//Acaba a vez do jogador
			mutEx.release();
		}catch(Exception e) {
			System.err.println(e.getMessage());
		}
	}
}
