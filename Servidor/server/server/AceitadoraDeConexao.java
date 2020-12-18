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
			if (isComecou && usuarios.size()==1)
				isComecou=false;

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

	@Override
	public String toString ()
	{
		String ret ="";

		ret = "Servidor: " + servidor + "\nPartida iniciada: " + isComecou + "\n Dealer:" + dealer+ "\n Gerenciadora Da Rodada:" + gerenciadoraDeRodada;

		return ret;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;

		if (o == null) return false;

		if (this.getClass() != o.getClass()) return false;

		AceitadoraDeConexao act = (AceitadoraDeConexao) o;

		if (!this.servidor.equals(act.servidor)) return false;

		if (!this.gerenciadoraDeRodada.equals(act.gerenciadoraDeRodada)) return false;

		if(!this.isComecou.equals(act.isComecou)) return false;

		if (!this.dealer.equals(act.dealer)) return false;

		if (this.usuarios.size() != act.usuarios.size()) return false;

		for (int i = 0; i < this.usuarios.size() ; i++)
			if (!this.usuarios.get(i).equals(act.usuarios.get(i)))
				return false;

			return true;
	}

	@Override
	public int hashCode()
	{
		int ret = 456;

		ret = ret * 7 + this.dealer.hashCode();
		ret = ret * 7 + this.isComecou.hashCode();
		ret = ret * 7 + this.gerenciadoraDeRodada.hashCode();
		ret = ret * 7 + this.servidor.hashCode();

		for (int i = 0; i < usuarios.size(); i++){
			ret = ret * 7 + usuarios.get(i).hashCode();
		}

		return Math.abs(ret);
	}
}
