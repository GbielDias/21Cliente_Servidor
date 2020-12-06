package server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import server.infra.*;

//import temp.server.models.Card;

public class SupervisoraDeConexao extends Thread {

	ArrayList<String> cartas = new ArrayList<String>();
	private double pontos=0;
	private Parceiro usuario;
	private Socket conexao;
	private ArrayList<Parceiro> usuarios;

	public SupervisoraDeConexao (Socket conexao, ArrayList<Parceiro> usuarios) throws Exception
		{
			if (conexao==null)
				throw new Exception ("Conexao ausente");

			if (usuarios==null)
				throw new Exception ("Usuarios ausentes");

			this.conexao  = conexao;
			this.usuarios = usuarios;
		}
	/*
		int cont = 0;
		
		while(cont < 53) {
			Card carta = new Card();
			if(!cartas.contains(carta))
				cartas.add(carta);
			
		}
		*/
	public void run ()
	{

		ObjectOutputStream transmissor;
		try
		{
			transmissor = new ObjectOutputStream(this.conexao.getOutputStream());
		}
		catch (Exception erro)
		{
			return;
		}

		ObjectInputStream receptor;
		try
		{
			receptor= new ObjectInputStream(this.conexao.getInputStream());
		}
		catch (Exception err0)
		{
			try
			{
				transmissor.close();
			}
			catch (Exception falha)
			{} // so tentando fechar antes de acabar a thread

			return;
		}

		try
		{
			this.usuario = new Parceiro (this.conexao,receptor,transmissor);
		}
		catch (Exception erro)
		{} // sei que passei os parametros corretos

		try
		{
			synchronized (this.usuarios)
			{
				this.usuarios.add (this.usuario);
			}


			for(;;) {
				Comunicado comunicado = this.usuario.envie();

				if (comunicado == null)
					return;
				/* remodelar para funcionar com o jogo
				else if (comunicado instanceof PedidoDeCarta)
				{
					//Ações quando for pedida uma carta
				}
				else if (comunicado instanceof PedidoDeDescarte)
				{
					//Ações quando for pedido para se descartar uma carta, na classe pode ser implementada para se ter como parâmetro
					// qual carta irá ser descartada
				}
				else if (comunicado instanceof PedidoParaJogarNovamente)
				{
					//Ações para recomeçar a partida
				}
				else if (comunicado instanceof PedidoParaSair)
				{
					synchronized (this.usuarios)
					{
						this.usuarios.remove (this.usuario);
					}
					this.usuario.adeus();
				}
					 */
				}
			}

		catch (Exception erro)
		{
			try
			{
				transmissor.close ();
				receptor   .close ();
			}
			catch (Exception falha)
			{} // so tentando fechar antes de acabar a thread

			return;
		}
	}
	}

