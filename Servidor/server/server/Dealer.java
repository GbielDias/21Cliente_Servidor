package server;

import commons.*;

// Aqui vai ter todos os relacionamentos entre mao e baralho
public class Dealer {
	private Baralho baralho;
	private Carta descartada = null;

	// Retirar os valores do baralho e colocar na maoDoJogador
	public Dealer() {
		baralho = new Baralho();
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
		mao.removerCarta(nome);

		return mao;
	}

	public Carta getDescartada() {
		return descartada;
	}

	public Baralho getBaralho() {
		return this.baralho;
	}

	public void resetDealer() {
		baralho = new Baralho();
		descartada = null;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Dealer dealer = (Dealer) o;

		if (!baralho.equals(dealer.baralho)) return false;
		return descartada.equals(dealer.descartada);
	}

	@Override
	public int hashCode()
	{
		int result = 255;

		result = 11 * result + baralho.hashCode();
		result = 31 * result + descartada.hashCode();
		return result;
	}
}
