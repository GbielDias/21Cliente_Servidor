package server.infra;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import server.Comunicado;
import server.ComunicadoDeAguarde;
import server.Parceiro;
import server.cases.PedidoDeDescarte;
import server.models.Carta;


public class SupervisoraDeConexao extends Thread {
	private Parceiro usuario;
	private Socket conexao;
	private ArrayList<Parceiro> usuarios;
	private ArrayList<Carta> baralho;

	public SupervisoraDeConexao(Socket conexao, ArrayList<Parceiro> usuarios, ArrayList<Carta> baralho) throws Exception {
		if (conexao == null)
			throw new Exception("Conexao ausente");

		if (usuarios == null)
			throw new Exception("Usuarios ausentes");

		if(baralho == null)
			throw new Exception("Cartas ausentes");
		
		this.conexao = conexao;
		this.usuarios = usuarios;
		this.baralho = baralho;
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
		} catch (Exception err0) {
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

			while (usuarios.size() < 3) {
				for(Parceiro usuario: usuarios) {
					usuario.Receba(new ComunicadoDeAguarde());
				}
			}

			while(true) {
				Comunicado comunicado = this.usuario.envie();
				
				if (comunicado == null)
					return;
				else if (comunicado instanceof PedidoDeDescarte) {
					
					
				}
			}
			
			/*
			 * remodelar para funcionar com o jogo else if (comunicado instanceof
			 * PedidoDeCarta) { //Ações quando for pedida uma carta } else if (comunicado
			 * instanceof PedidoDeDescarte) { //Ações quando for pedido para se descartar
			 * uma carta, na classe pode ser implementada para se ter como parâmetro //
			 * qual carta irá ser descartada } else if (comunicado instanceof
			 * PedidoParaJogarNovamente) { //Ações para recomeçar a partida } else if
			 * (comunicado instanceof PedidoParaSair) { synchronized (this.usuarios) {
			 * this.usuarios.remove (this.usuario); } this.usuario.adeus(); }
			 */
			
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
}
