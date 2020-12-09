package server;

import server.models.Carta;

import java.util.ArrayList;

public class MaoDoJogador {
	private ArrayList<Carta> mao = new ArrayList<>();

	public MaoDoJogador(Baralho valores) {

		for (int i = 0; i < 3; i++)
			mao.add(valores.comprarUmaCarta());

	}

	public ArrayList<Carta> getMao() {
		return mao;
	}

	public void removerCarta(String c) throws Exception {
		if(!mao.contains(c))
			throw new Exception("Carta inv�lida");

		for (int i=0; i < 4; i++)
		{
			if(mao.get(i).ToString().equals(c))
			{
				mao.remove(i);
				return;
			}
		}
	}

	public Carta getCarta (String nomeCarta) throws Exception
	{
		if (nomeCarta == null)
			throw new Exception("Nome de carta inválido");

		for (int i=0; i < 4; i++)
		{
			if(mao.get(i).ToString().equals(nomeCarta))
			{
				return mao.get(i);
			}
		}
		throw new Exception("Carta não encontrada");
	}

	public int contar() {
		int soma = 0;
		
		for (int i = 0; i < mao.size(); i++) {
			soma = soma + mao.get(i).getPontos();			
		}

		return soma;
	}

	@Override
	public String toString() {
		String str = "[";
		
		for (int i = 0; i < mao.size(); i++) {
			str += mao.get(i).ToString() + ", ";
		}
		
		return str.substring(0,  str.length() - 2)+ "] = " + contar();
	}
}
