package server.infra;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import server.Parceiro;
import server.SupervisoraDeConexao;

public class AceitadoraDeConexao extends Thread{
	private ServerSocket servidor;
	private ArrayList<Parceiro> usuarios;
	
	
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
				
			}catch(Exception e) {
				
			}
		}
	}
}
