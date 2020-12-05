package server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.Semaphore;
import server.infra.Comunicado;

public class Parceiro {
	private Socket conexao;
	private ObjectInputStream receptor;
	private ObjectOutputStream transmissor;
	
	private Comunicado proximoComunicado;
	private Semaphore mutuaExclusao;
	
	public Parceiro(Socket conexao, ObjectInputStream receptor, ObjectOutputStream transmissor) throws Exception {
		if (conexao == null)
			throw new Exception("Conex�o nula");
		
		if (receptor == null)
			throw new Exception("Receptor nulo");

		if (transmissor == null)
			throw new Exception("transmissor nulo");
		
		this.conexao = conexao;
		this.receptor = receptor;
		this.transmissor = transmissor;
	}
	
	//Cliente recebe
	public void Receba(Comunicado c) throws Exception {
		try {
			transmissor.writeObject(c);
			transmissor.flush();
		}catch(Exception e) {
			throw new Exception("Erro de recep��o");
		}
	}

	public Comunicado espiar() throws Exception {
		try {
			mutuaExclusao.acquireUninterruptibly();
			
			if(proximoComunicado == null)
				proximoComunicado = (Comunicado) receptor.readObject();
			
			mutuaExclusao.release();
			
			return proximoComunicado;
		} catch(Exception e) {
			throw new Exception ("Erro de recep��o");
		}
	}
	
	//Cliente envie
	public Comunicado servidorEnvie() throws Exception {
		try {
			if(proximoComunicado == null)
				proximoComunicado = (Comunicado) receptor.readObject();
				
			Comunicado cm = proximoComunicado;
			proximoComunicado = null;
			
			return cm;
		}catch(Exception e) {
			throw new Exception("Erro de recep��o");
		}
	}
	
	public void encerrar() throws Exception {
		try {
			receptor.close();
			transmissor.close();
			conexao.close();
		} catch (Exception e) {
			
		}
	}
}
