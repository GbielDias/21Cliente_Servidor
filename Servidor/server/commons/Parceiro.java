package commons;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.Semaphore;

public class Parceiro {
	private Socket conexao;
	private ObjectInputStream receptor;
	private ObjectOutputStream transmissor;
	
	private Comunicado proximoComunicado;
	private Semaphore mutuaExclusao = new Semaphore(1, true);
	
	public Parceiro(Socket conexao, ObjectInputStream receptor, ObjectOutputStream transmissor) throws Exception {
		if (conexao == null)
			throw new Exception("Conexao nula");
		
		if (receptor == null)
			throw new Exception("Receptor nulo");

		if (transmissor == null)
			throw new Exception("transmissor nulo");
		
		this.conexao = conexao;
		this.receptor = receptor;
		this.transmissor = transmissor;
	}
	
	//Cliente recebe
	public void receba(Comunicado c) throws Exception {
		try {
			transmissor.writeObject(c);
			transmissor.flush();
		}catch(Exception e) {
			throw new Exception("Erro de recepcao (receba)");
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
			throw new Exception (e.getMessage());
		}
	}
	
	//Cliente envie
	public Comunicado envie() throws Exception {
		try {
			if(proximoComunicado == null)
				proximoComunicado = (Comunicado) receptor.readObject();
				
			Comunicado cm = proximoComunicado;
			proximoComunicado = null;
			
			return cm;
		}catch(Exception e) {
			throw new Exception("Erro de recepcao (envie): " + e.getMessage());
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
