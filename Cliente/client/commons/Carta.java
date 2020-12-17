package commons;


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
	public boolean equals(Object o)
	{
		if (this == o) return true;

		if (o == null || getClass() != o.getClass()) return false;

		Carta carta = (Carta) o;

		if (this.pontos != carta.pontos) return false;

		if (!this.simbolo.equals(carta.simbolo)) return false;

		return nome.equals(carta.nome);
	}

	@Override
	public int hashCode()
	{
		int result = 255;

		result = 11 * result + this.simbolo.hashCode();
		result = 31 * result + this.nome.hashCode();
		result = 31 * result + this.pontos;

		return Math.abs(result);
	}
}
