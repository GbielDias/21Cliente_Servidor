package server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import commons.*;

public class AceitadoraDeConexao extends Thread{
	private ServerSocket servidor;
	private ArrayList<Parceiro> usuarios;
	private GerenciadoraDeRodada gerenciadoraDeRodada;
	private Dealer dealer;
	private Semaphore mutEx = new Semaphore(1, true);
	private ArrayList<Carta> baralho = new ArrayList<>();

	public AceitadoraDeConexao(String porta, ArrayList<Parceiro> usuarios) throws Exception {
		if(porta == null)
			throw new Exception("Insira uma porta valida");
		
		try
		{
			servidor = new ServerSocket(Integer.parseInt(porta));
		}
		catch(Exception e)
		{
			System.err.println("Porta invalida");
		}
		
		if(usuarios == null)
			throw new Exception("Usuarios ausentes");
		
		this.usuarios = usuarios;
		this.dealer = new Dealer();
		this.gerenciadoraDeRodada = new GerenciadoraDeRodada(this.usuarios);


	}
	
	public void run()
	{
		while(true)
		{


			Socket conexao = null;
			try
			{
				conexao = servidor.accept();
			}
			catch (Exception e)
			{
				continue;
			}


			SupervisoraDeConexao supervisora = null;


			try {
				supervisora = new SupervisoraDeConexao(conexao, usuarios, dealer, gerenciadoraDeRodada);


			}
			catch (Exception e)
			{
				System.err.println(e.getMessage());
			}

			supervisora.start(); //Teoricamente add um no size de usuarios


			try
			{
				Thread.sleep(500);
			}
			catch(Exception e)
			{}


			synchronized (usuarios)
			{
				if (usuarios.size() == 2) //TODO Colocar o num exato de usuario
				{
					try
					{
						for (Parceiro usuario : usuarios)
							usuario.receba(new ComunicadoDeComecar());



					} catch (Exception e) {}

				}
			}



		}
	}
}
