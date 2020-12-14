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
	
<<<<<<< HEAD
	public void run() {
		while(true) {
=======
	public void run()
	{
		while(true)
		{


>>>>>>> a9f2e8a071da7e525a83b8b231948c92aadc36fe
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
<<<<<<< HEAD
//			TratadoraDeVencedor tratadoraDeVencedor = null;
=======

>>>>>>> a9f2e8a071da7e525a83b8b231948c92aadc36fe

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
