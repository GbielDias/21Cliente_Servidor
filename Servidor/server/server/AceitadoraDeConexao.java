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
	private Boolean isComecou = false;

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

			synchronized (usuarios){
				if(usuarios.size() == 3)
				{
					try {
						conexao.close();
						continue;
					}catch (Exception e){

					}
				}
			}

			SupervisoraDeConexao supervisora = null;

			try {
				supervisora = new SupervisoraDeConexao(conexao, usuarios, dealer, gerenciadoraDeRodada);

				synchronized (gerenciadoraDeRodada.getSupervisoras())
				{
					gerenciadoraDeRodada.addSupervisora(supervisora);
				}
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
				if (usuarios.size() == 3 && !isComecou)
				{
					isComecou = true;
					try
					{
						for (Parceiro usuario : usuarios)
							usuario.receba(new ComunicadoDeComecar());

					} catch (Exception e) {}

				}
				else if (isComecou)
				{
					try
					{
						if (usuarios.size() ==2)
						usuarios.get(1).receba(new ComunicadoDeComecar());
						else
							if (usuarios.size() ==3)
								usuarios.get(2).receba(new ComunicadoDeComecar());
					}
					catch (Exception e)
					{}
				}
			}

		}
	}
}
