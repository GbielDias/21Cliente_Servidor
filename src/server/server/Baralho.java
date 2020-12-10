package server;

import server.models.Carta;
import server.models.PropriedadeCartas;

import java.util.ArrayList;

public class Baralho {
	ArrayList<Carta> baralho;

//	public Baralho() {
//		cartas = new ArrayList<Carta>(52);
//
//		int valor = 1;
//		for (int i = 0; i < 52; i = i + 4) // 16 -> 10 / 4- de cada numero
//		{
//
//			if (valor > 10)
//				valor = 10;
//
//			cartas.add(valor);
//			cartas.add(valor);
//			cartas.add(valor);
//			cartas.add(valor);
//
//			valor++;
//		}
//	}
	
	public Baralho() {
		baralho = new ArrayList<>();
		
		for (int i = 0; i < PropriedadeCartas.NOMES.length; i++) {
			for (int j = 0; j < PropriedadeCartas.SIMBOLOS.length; j++) {
				baralho.add(new Carta(PropriedadeCartas.NOMES[i], PropriedadeCartas.SIMBOLOS[j]));
			}
		}
	}

	public Carta comprarUmaCarta() {
		int indice = (int) (Math.random() * baralho.size()); // Escolher um indice entre 0 e o tamanho do vetor
		Carta carta = baralho.get(indice);
		baralho.remove(indice);

		return carta;
	}

	public void setCartas(ArrayList<Carta> cartas) {
		this.baralho = cartas;
	}

	public ArrayList<Carta> getCartas() {
		return baralho;
	}

	public int getTamanho() {
		return baralho.size();
	}

	@Override
	public int hashCode() {
		int ret = 12;

		for (Carta carta: baralho)
			ret = ret * 3 + carta.hashCode();

		return ret;
	}

	@Override
	public String toString() {
		String str = "[";
		
		for (int i = 0; i < baralho.size(); i++) {
			if(str.length() == 15) {
				str += "\n" + baralho.get(i).ToString() + ", ";
			}
				
			str += baralho.get(i).ToString() + ", ";
		}
		return str.substring(0, str.length() - 2) + "]";
	}
}
