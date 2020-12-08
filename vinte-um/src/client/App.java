package client;

import server.Parceiro;

import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class App {

	public static final String HOST_PADRAO = "localhost";
	public static final int PORTA_PADRAO = 3333;

	public static void main(String[] args)
	{
		if(args.length > 2)
		{
			System.err.println("Uso esperado: java app [HOST[PORTA]]");
			return;
		}
		//Criando os objetos que vão ser instanciado
		Socket 				conexao 	= null;
		ObjectOutputStream 	transmissor = null;
		ObjectInputStream 	receptor 	= null;
		Parceiro			servidor	= null;
		TratadoraDeComunicadoDeDesligamento
				tratadoraDeDesligamento = null;

		try
		{
			conexao 	= instanciarConexao(args);
			transmissor = instanciarTransmissor(conexao);
			receptor 	= instanciarReceptor(conexao);
			servidor	= instanciarServidor(conexao,receptor,transmissor);
			tratadoraDeDesligamento = instanciarTratadora(servidor);
		}
		catch (Exception err)
		{
			System.err.println(err.getMessage());
			System.err.println("Indique o servidor e a porta corretos!\n");
			return;
		}

		//Jogo começa aqui
		tratadoraDeDesligamento.start();

		char opcao = ' ';
		do
		{
			//faça algo aqui
		}
		while(opcao != 'T');








	}

	public static Socket instanciarConexao(String[] args) throws Exception
	{
		Socket ret = null;
		try
		{
			String host = App.HOST_PADRAO;
			int porta = App.PORTA_PADRAO;

			if(args.length > 0)
				host = args[1];
			if(args.length == 2)
				porta = Integer.parseInt(args[1]);

			ret = new Socket(host, porta);
		}
		catch(Exception err)
		{
			throw new Exception("Ocorreu um erro na instanciação de \"conexao\".");
		}

		return ret;
	}

	public static ObjectOutputStream instanciarTransmissor(Socket conexao) throws Exception
	{
		ObjectOutputStream ret = null;
		try
		{
			ret = new ObjectOutputStream(conexao.getOutputStream());
		}
		catch(Exception err)
		{
			throw new Exception("Ocorreu um erro na instanciação do \"transmissor\".");
		}

		return ret;
	}

	public static ObjectInputStream instanciarReceptor(Socket conexao) throws Exception
	{
		ObjectInputStream ret = null;
		try
		{
			ret = new ObjectInputStream(conexao.getInputStream());
		}
		catch(Exception err)
		{
			throw new Exception("Ocorreu um erro na instanciação do \"receptor\".");
		}

		return ret;
	}

	public static Parceiro  instanciarServidor(Socket conexao, ObjectInputStream receptor, ObjectOutputStream transmissor) throws Exception
	{
		Parceiro ret = null;
		try
		{
			ret = new Parceiro(conexao,receptor,transmissor);
		}
		catch(Exception err)
		{
			throw new Exception("Ocorreu um erro na instanciação do \"servidor\".");
		}

		return ret;
	}

	public static TratadoraDeComunicadoDeDesligamento instanciarTratadora(Parceiro servidor) throws Exception
	{
		TratadoraDeComunicadoDeDesligamento ret = null;
		try
		{
			ret = new TratadoraDeComunicadoDeDesligamento(servidor);
		}
		catch(Exception err)
		{
			throw new Exception("Ocorreu um erro na instanciação da \"tratadoraDeDesligamento\"");
		}
		return ret;
	}


}