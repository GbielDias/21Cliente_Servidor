package server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

import commons.*;

public class AceitadoraDeConexao extends Thread{
	private ServerSocket servidor;
	private ArrayList<Parceiro> usuarios;
	private Dealer dealer;
	private Semaphore mutEx = new Semaphore(1, true);
	private ArrayList<Carta> baralho = new ArrayList<>();

	public AceitadoraDeConexao(String porta, ArrayList<Parceiro> usuarios) throws Exception {
		if(porta == null)
			throw new Exception("Insira uma porta valida");
		
		try {
			servidor = new ServerSocket(Integer.parseInt(porta));
		}catch(Exception e) {
			System.err.println("Porta invalida");
		}
		
		if(usuarios == null)
			throw new Exception("Usuarios ausentes");
		
		this.usuarios = usuarios;

		this.dealer = new Dealer();
	}
	
	public void run() {
		while(true)
		{


			Socket conexao = null;
			try {
				conexao = servidor.accept();
			}catch(Exception e) {
				continue;
			}
			
			SupervisoraDeConexao supervisora = null; 
			
			try {
				supervisora = new SupervisoraDeConexao(conexao, usuarios, dealer);
			}catch(Exception e) {	
			
			}
			
			supervisora.start(); //Teoricamente add um no size de usuario


			//TODO colocar size() == 3
			//Barra conexao se contem 3 players

			synchronized (this.usuarios)
			{

				if (usuarios.size() > 0) // está 1 só pra teste
				{
					try {
						for (int o = 0; o < usuarios.size(); o++)
							usuarios.get(o).receba(new ComunicadoDeComecar());
					} catch (Exception e) {
					}

					//Start uma thread gerenciar a partida
					break;
				}
			}
		}
		
		//TODO startar patida

		//Por enquanto é while(true), mas muito provavelmente vai ser modificado no futuro
		//Em prol de avisar ao quarto jogador que ele nao pode entrar na partida, tem que ser aceito a sua conexao antes
		//Por isso o while anterior tem que rodar ao mesmo tempo que esse while embaixo
		while(true)
		{
			int i=0;
			try
			{
				for(;;)//Aqui tem que ter que fazer mao.contar() da mao do jogador i, caso seja 21, sair desse for
				{
					for (int j = 0; j < usuarios.size(); j++) // vai passar p
					{
						Comunicado comn;
						do
						{
							comn = usuarios.get(j).espiar();
						}
						while(!(comn instanceof PedidoDeRodada));

						comn = usuarios.get(j).envie();
					}
				}
			}
			catch(Exception e){}
		}
	}
}
