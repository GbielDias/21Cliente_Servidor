package server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import commons.*;

public class AceitadoraDeConexao extends Thread{
	private ServerSocket servidor;
	private ArrayList<Parceiro> usuarios;
	private Dealer dealer;
	private Semaphore mutEx = new Semaphore(1, true);
	private ArrayList<Carta> baralho = new ArrayList<>();

	public AceitadoraDeConexao(String porta, ArrayList<Parceiro> usuarios) throws Exception {
		if(porta == null)
			throw new Exception("Insira uma porta valida");
		
		try {
			servidor = new ServerSocket(Integer.parseInt(porta));
		}catch(Exception e) {
			System.err.println("Porta invalida");
		}
		
		if(usuarios == null)
			throw new Exception("Usuarios ausentes");
		
		this.usuarios = usuarios;

		this.dealer = new Dealer();
	}
	
	public void run() {
		while(true) {
			//Barra conex�o se cont�m 3 playes
//			if(usuarios.size() == 3)
//				break;
//			
			Socket conexao = null;
			try {
				conexao = servidor.accept();
			}catch(Exception e) {
				continue;
			}
			
			SupervisoraDeConexao supervisora = null; 
			
			try {
				supervisora = new SupervisoraDeConexao(conexao, usuarios, dealer, mutEx);
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
