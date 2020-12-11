package server;

import commons.*;

import java.util.Locale;

// Aqui vai ter todos os relacionamentos entre mao e baralho
public class Dealer {
	private Baralho baralho;
	private Carta descarte = null;

	// Retirar os valores do baralho e colocar na maoDoJogador
	public Dealer() {
		baralho = new Baralho(); // 1-10

	}

//	public void comprar(MaoDoJogador mao) {
//		mao.getMao().add(baralho.comprarUmaCarta());
//		descartar(mao);
//	}

	public MaoDoJogador comprar(MaoDoJogador mao) {
		if(descarte == null){
			System.out.println("Voce vai recebe uma carta do Baralho");
			mao.getMao().add(baralho.comprarUmaCarta());

		} else {
			System.out.println("Voce vai escolher o ultimo descarte: " + descarte +
					". Ou vai querer comprar do baralho\nD - Ultimo Descarte\nB - Baralh");

			String opcao = Teclado.getUmString().toUpperCase();

			if(opcao.equals("D")) {
				mao.getMao().add(descarte);
			}
			else {
				mao.getMao().add(baralho.comprarUmaCarta());
			}

		}
		return mao;
	}

	private void descartar(MaoDoJogador mao) {
		System.out.println(mao);
		System.out.println("Qual carta voce deseja descartar?");

		String opcao;
		while (true) {
			try {
				// 1, 4, 6


				opcao = Teclado.getUmString();

				descarte = mao.getCarta(opcao);
				mao.removerCarta(opcao);

			} catch (Exception err) {
				System.out.println("Carta invalida");
			}
		}
	}

	public void comprarEDescarte(MaoDoJogador mao) throws Exception {
		if (descarte == null)
			throw new Exception("Nao tem nenhuma carta no monte de descarte");

		mao.getMao().add(descarte);
		descartar(mao);
	}

	public int contar(MaoDoJogador mao) {
		return mao.contar();
	}

	public Baralho getBaralho() {
		return this.baralho;
	}

}
