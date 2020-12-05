package server;

import java.util.ArrayList;

import temp.server.models.Card;

public class SupervisoraDeConexao {

	ArrayList<Card> cartas = new ArrayList<>();

	public SupervisoraDeConexao() {
		//TODO as coisa da supervisora
		
		
		int cont = 0;
		
		while(cont < 53) {
			Card carta = new Card();
			if(!cartas.contains(carta))
				cartas.add(carta);
			
		}
	}
	
}
