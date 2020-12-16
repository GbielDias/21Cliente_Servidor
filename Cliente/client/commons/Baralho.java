package commons;

import java.util.ArrayList;

public class Baralho implements Cloneable{

	private ArrayList<Carta> baralho;


	public Baralho() {
		baralho = new ArrayList<>();

		for (int i = 0; i < PropriedadeCartas.NOMES.length; i++)
		{
			for (int j = 0; j < PropriedadeCartas.SIMBOLOS.length; j++)
			{
				baralho.add(new Carta(PropriedadeCartas.NOMES[i], PropriedadeCartas.SIMBOLOS[j]));
			}
		}
	}

	public Baralho(Baralho modelo) throws Exception
	{
		if (modelo == null)
			throw new Exception("Modelo inválido");

		this.baralho = new ArrayList<>();

		this.baralho.addAll(modelo.baralho);
	}

	public Object clone()
	{
		Baralho ret = null;
		try {
			ret = new Baralho(this);
		}catch (Exception e){}

		return ret;
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
	public int hashCode()
	{
		int ret = 12;

		for (Carta carta: baralho)
			ret = ret * 3 + carta.hashCode();

		return Math.abs(ret);
	}

	@Override
	public String toString()
	{
		String str = "[";

		for (int i = 0; i < baralho.size(); i++) {
			if(str.length() == 15) {
				str += "\n" + baralho.get(i).toString() + ", ";
			}

			str += baralho.get(i).toString() + ", ";
		}
		return str.substring(0, str.length() - 2) + "]";
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;

		if (o == null || this.getClass() != o.getClass()) return false;

		Baralho brl = (Baralho) o;

		for (int i = 0; i< this.baralho.size();i++)
			if (!this.baralho.get(i).equals(brl.baralho.get(i)))
				return false;

		return true;
	}
}
