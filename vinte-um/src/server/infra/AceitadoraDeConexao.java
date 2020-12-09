package server.infra;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import server.Dealer;
import server.Parceiro;
import server.models.Carta;
import server.models.PropriedadeCartas;

public class AceitadoraDeConexao extends Thread{
	private ServerSocket servidor;
	private ArrayList<Parceiro> usuarios;
	private ArrayList<Carta> baralho = new ArrayList<Carta>();
	private Dealer dealer ;

	public AceitadoraDeConexao(String porta, ArrayList<Parceiro> usuarios) throws Exception {
		if(porta == null)
			throw new Exception("Insira uma porta v�lida");
		
		try {
			servidor = new ServerSocket(Integer.parseInt(porta));
		}catch(Exception e) {
			System.err.println("Porta inv�lida");
		}
		
		if(usuarios == null)
			throw new Exception("Usu�rios ausentes");
		
		this.usuarios = usuarios;

		this.dealer = new Dealer();
	}
	
	public void run() {
		while(true) {
			//Barra conex�o se cont�m 3 playes
			if(usuarios.size() == 3)
				break;
			
			Socket conexao = null;
			try {
				conexao = servidor.accept();
			}catch(Exception e) {
				continue;
			}
			
			SupervisoraDeConexao supervisora = null; 
			
			try {
				supervisora = new SupervisoraDeConexao(conexao, usuarios,dealer);
			}catch(Exception e) {	
			
			}
			
			supervisora.start();
			System.out.println(usuarios.size());
		}
		
		//TODO startar patida
	}

	/*
	private void instanciaBaralho() {
		for (int i = 0; i < PropriedadeCartas.NOMES.length; i++) {
			for (int j = 0; j < PropriedadeCartas.SIMBOLOS.length; j++) {
				baralho.add(new Carta(PropriedadeCartas.NOMES[i], PropriedadeCartas.SIMBOLOS[j]));
			}
		}
	}
	*/

}
