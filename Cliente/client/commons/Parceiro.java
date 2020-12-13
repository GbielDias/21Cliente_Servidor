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

	@Override
	public String toString() //TODO VERIFICAR toString - Parceiro
	{
		return "Parceiro{ " + "conexao=" + conexao + " }";
	}

	@Override
	public boolean equals(Object o) //TODO VERIFICAR  - Parceiro
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Parceiro parceiro = (Parceiro) o;

		if (!conexao.equals(parceiro.conexao)) return false;
		if (!receptor.equals(parceiro.receptor)) return false;
		if (!transmissor.equals(parceiro.transmissor)) return false;
		if (!proximoComunicado.equals(parceiro.proximoComunicado)) return false;
		return mutuaExclusao.equals(parceiro.mutuaExclusao);
	}

	@Override
	public int hashCode() //TODO VERIFICAR  hashCode - Parceiro
	{
		int result = conexao.hashCode();
		result = 31 * result + receptor.hashCode();
		result = 31 * result + transmissor.hashCode();
		result = 31 * result + proximoComunicado.hashCode();
		result = 31 * result + mutuaExclusao.hashCode();
		return result;
	}


	//TODO VERIFICACOES de Getters e Setters - Parceiro

	public Comunicado getProximoComunicado() {
		return proximoComunicado;
	}

	public ObjectInputStream getReceptor() {
		return receptor;
	}

	public ObjectOutputStream getTransmissor() {
		return transmissor;
	}

	public Semaphore getMutuaExclusao() {
		return mutuaExclusao;
	}

	public Socket getConexao() {
		return conexao;
	}

	public void setConexao(Socket conexao) {
		this.conexao = conexao;
	}

	public void setMutuaExclusao(Semaphore mutuaExclusao) {
		this.mutuaExclusao = mutuaExclusao;
	}

	public void setProximoComunicado(Comunicado proximoComunicado) {
		this.proximoComunicado = proximoComunicado;
	}

	public void setReceptor(ObjectInputStream receptor) {
		this.receptor = receptor;
	}

	public void setTransmissor(ObjectOutputStream transmissor) {
		this.transmissor = transmissor;
	}
}
