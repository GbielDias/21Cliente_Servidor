package client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import commons.*;

public class Aplicacao {

	public static final String HOST_PADRAO = "localhost";
	public static final int PORTA_PADRAO = 3333;

	public static void main(String[] args) // Cliente
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
		Parceiro servidor	= null;
		TratadoraDeComunicadoDeDesligamento tratadoraDeDesligamento = null;
		MaoDoJogador maoDoJogador = null;

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

		//Aguarde os usuarios entrarem





		//Jogo começa aqui
		tratadoraDeDesligamento.start();
/*
		Comunicado comunicado = null;
		do
		{
			System.out.println("Aguarde os jogares entrarem na partida");
			try
			{
				comunicado = (Comunicado) servidor.espiar();
			}
			catch(Exception err)
			{
			}
		}
		while (!(comunicado instanceof ComunicadoDeAguarde));


 */

		Comunicado comn = null;
		do
		{
			try
			{
				comn = (Comunicado) servidor.espiar();
			}
			catch(Exception err)
			{
				System.err.println(err.getMessage() + " Erro no espiar");
			}
		}
		while (!(comn instanceof MaoDoJogador));

		try {
			maoDoJogador = (MaoDoJogador) servidor.envie();
		}
		catch (Exception e)
		{
			System.err.println(e.getMessage());
		}

		System.out.println(maoDoJogador);
		char opcao = ' ';

		do
		{
			try
			{
				System.out.println("Opções:");
				System.out.println("C. Comprar do baralho e descartar");
				System.out.println("D. Comprar da pilha de descarte e descartar");
				System.out.println("S. Sair da partida");

				opcao = Character.toUpperCase(Teclado.getUmChar()); // A, B, C, 1, 0 // AS, 10 , Um , Es, Palavra

				if(!(opcao == 'C' || opcao == 'D' || opcao == 'S' ))
				{
					throw new Exception("Opcao Inválida");
				}
				if (opcao == 'C') {
					servidor.receba(new PedidoDeCarta());


					// Visualização da mao do jogador

					int desc;
					System.out.print("Escolha uma carta para ser descartada: ");
					desc = Teclado.getUmInt();
					servidor.receba(new PedidoDeDescarte(desc));

				}
				else if (opcao == 'D')
				{
					servidor.receba(new PedidoDeCompraDescarte());

					// Visualização da mao do jogador

					int desc;
					System.out.print("Escolha uma carta para ser descartada: ");
					desc = Teclado.getUmInt();
					servidor.receba(new PedidoDeDescarte(desc));
				}
			}
			catch(Exception erro)
			{
				System.err.println("Opção inválida");
			}

		}
		while(opcao != 'S');

		try
		{
			servidor.receba (new PedidoParaSair ());
		}
		catch (Exception erro)
		{}
	}

	public static Socket instanciarConexao(String[] args) throws Exception
	{
		Socket ret = null;
		try
		{
			String host = Aplicacao.HOST_PADRAO;
			int porta = Aplicacao.PORTA_PADRAO;

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