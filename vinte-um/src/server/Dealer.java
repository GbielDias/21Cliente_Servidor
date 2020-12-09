package server;

import server.models.Carta;
import java.util.ArrayList;

// Aqui vai ter todos os relacionamentos entre mao e baralho
public class Dealer {
	private Baralho baralho;
	private Carta descarte = null;
	private int vezesJogadas = 0;

	// Retirar os valores do baralho e colocar na maoDoJogador
	public Dealer() {
		baralho = new Baralho(); // 1-10


	}

	public void comprar(MaoDoJogador mao) {
		 mao.getMao().add(baralho.comprarUmaCarta());
		descartar(mao);
	}

	private void descartar(MaoDoJogador mao) {

		String opcao;
		while (true) {
			try {
				// 1, 4, 6
				System.out.println(mao);
				vezesJogadas++;

				System.out.println("Qual carta voc� deseja descartar?");

				opcao = Teclado.getUmString();

					descarte = mao.getCarta(opcao);
					mao.removerCarta(opcao);

			} catch (Exception err) {
				System.out.println("Carta inv�lida");
			}
		}
	}

	public void comprarDescarte(MaoDoJogador mao) throws Exception {
		if (descarte == null)
			throw new Exception("N�o tem nenhuma carta no monte de descarte");

		mao.getMao().add(descarte);
		descartar(mao);
	}

	public int contar(MaoDoJogador mao) {
		return mao.contar();
	}

	public Baralho getBaralho(){
		return this.baralho;
	}

}
