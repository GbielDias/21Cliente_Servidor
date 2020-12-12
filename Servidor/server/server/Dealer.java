package server;

import commons.*;

// Aqui vai ter todos os relacionamentos entre mao e baralho
public class Dealer {
	private Baralho baralho;
	private Carta descartada = null;

	// Retirar os valores do baralho e colocar na maoDoJogador
	public Dealer() {
		baralho = new Baralho(); // 1-10

	}

	public MaoDoJogador comprarBaralho(MaoDoJogador mao) {
		mao.getMao().add(baralho.comprarUmaCarta());

		return mao;
	}
	public MaoDoJogador comprarDescartada(MaoDoJogador mao) {
		mao.getMao().add(descartada);

		return mao;
	}

	public MaoDoJogador descartar(MaoDoJogador mao, String nome) throws Exception {
		descartada = mao.getCarta(nome);

		System.out.println(descartada);
		mao.removerCarta(nome);

		return mao;
	}

	public Carta getDescartada() {
		return descartada;
	}

	public Baralho getBaralho() {
		return this.baralho;
	}

}