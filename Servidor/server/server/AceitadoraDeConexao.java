package server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

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
	
	public void run() {
		int capacidade = 3;

		while(true) {
			Socket conexao;

			try
			{
				conexao = servidor.accept();
			}
			catch (Exception e)
			{
				continue;
			}

			if(usuarios.size() == 3)
			{
				try {
					conexao.close();

				}catch (Exception e){

				}
			}

			SupervisoraDeConexao supervisora = null;

			try
			{
				if (usuarios.size()==0)
				supervisora = new SupervisoraDeConexao(conexao, usuarios, dealer, gerenciadoraDeRodada,true);
				else
					supervisora = new SupervisoraDeConexao(conexao, usuarios, dealer, gerenciadoraDeRodada,false);
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
			catch(Exception ignored){}


			synchronized (usuarios)
			{
				if (usuarios.size() == 2 ) //TODO Colocar o num exato de usuario
				{
					try
					{
						for (Parceiro usuario : usuarios)
							usuario.receba(new ComunicadoDeComecar());



					} catch (Exception e) {}

				}
			}
//			synchronized (usuarios) {
//
//				try
//				{
//					Comunicado com = usuarios.get(0).espiar();
//					if(com instanceof ComunicadoDeRestart)
//					{
//						for (Parceiro usuario: usuarios)
//						{
//							usuario.receba(new ComunicadoDeRestart());
//						}
//					}
//					else if(com instanceof ComunicadoDeDesligamento)
//					{
//						for (Parceiro usuario: usuarios)
//						{
//							usuario.receba(new ComunicadoDeDesligamento());
//						}
//					}
//				}
//				catch(Exception e){}
//
//			}

		}
	}
}
