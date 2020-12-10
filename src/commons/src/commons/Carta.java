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
		else if (nome == "Ais")
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
	
	public String ToString() {
		return nome + " | " + simbolo;
	}
}
