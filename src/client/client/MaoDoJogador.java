package client;

import java.util.ArrayList;
import client.models.Carta;
import commons.Comunicado;

public class MaoDoJogador extends Comunicado {
	private ArrayList<Carta> mao = new ArrayList<>();

	public MaoDoJogador(ArrayList<Carta> valores) {
		for (int i = 0; i < 3; i++)
			mao.add(valores.get(i));

	}

	public ArrayList<Carta> getMao() {
		return mao;
	}

	public void removerNumero(int num) throws Exception {
		if (num < 0 || num > 10)
			throw new Exception("Carta inv�lida");

		mao.remove(num);
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
