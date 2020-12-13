package commons;

import commons.Comunicado;

public class Carta extends Comunicado {
	private String simbolo;
	private String nome;
	private int pontos;

	//Construtor n�o necessitando de valida��o, pois ser� usado internamente e n�o pello usu�rio
	public Carta(String nome, String  simbolo){
		this.simbolo = simbolo;
		this.nome = nome;

		if (nome.length() == 1)
			pontos = Integer.parseInt(nome);
		else if (nome.equals("AIS"))
			pontos = 1;
		else
			pontos = 10;
	}

	public int getPontos() {
		return pontos;
	}

	public String getNome() {
		return nome;
	}

	@Override
	public String toString() {
		return nome + "-" + simbolo;
	}

	@Override
	public boolean equals(Object o) //TODO Verificar equals - Carta
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Carta carta = (Carta) o;

		if (pontos != carta.pontos) return false;
		if (!simbolo.equals(carta.simbolo)) return false;
		return nome.equals(carta.nome);
	}

	@Override
	public int hashCode() //TODO Verificar hashCode - Carta
	{
		int result = simbolo.hashCode();
		result = 31 * result + nome.hashCode();
		result = 31 * result + pontos;
		return result;
	}
}
