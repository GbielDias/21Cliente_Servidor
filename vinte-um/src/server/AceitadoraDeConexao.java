package server;

import java.net.ServerSocket;
import java.util.ArrayList;

public class AceitadoraDeConexao extends Thread{
	private ServerSocket servidor;
	private ArrayList<String> usuarios;
	
	public AceitadoraDeConexao(String porta, ArrayList<String> usuarios) throws Exception {
		if(porta == null)
			throw new Exception("Insira uma porta válida");
		
		try {
			servidor = new ServerSocket(Integer.parseInt(porta));
		}catch(Exception e) {
			System.err.println("Porta inválida");
		}
		
		if(usuarios == null)
			throw new Exception("Usuários ausentes");
		
		this.usuarios = usuarios;
	}
}
