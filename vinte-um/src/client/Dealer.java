package client;

import java.util.ArrayList;

import client.models.Carta;

// Aqui vai ter todos os relacionamentos entre mao e baralho
public class Dealer{
	MaoDoJogador maoDoJogador;
	Baralho baralho;
	Carta descarte = null;
	int vezesJogadas = 0;

	// Retirar os valores do baralho e colocar na maoDoJogador
	public Dealer() {
		baralho = new Baralho(); // 1-10

		ArrayList<Carta> caralho = new ArrayList<>();

		for (int i = 0; i < 3; i++)
			caralho.add(baralho.comprarUmaCarta());

		maoDoJogador = new MaoDoJogador(caralho);
	}

	public void comprar() {
		Carta carta = baralho.comprarUmaCarta();

		maoDoJogador.getMao().add(carta);

		descartar();
	}

	private void descartar() {
		int opcao;
		while (true) {
			try {
				// 1, 4, 6
				System.out.println(maoDoJogador); 
				vezesJogadas++;

				System.out.println("Qual carta você deseja descartar?");
				
				opcao = Teclado.getUmInt();
				switch (opcao) {
				case 1:
				case 2:
				case 3:
				case 4:
					descarte = maoDoJogador.getMao().get(opcao);
					maoDoJogador.removerNumero(opcao);
				}

				break;
			} catch (Exception err) {
				System.out.println("Carta inválida");
			}
		}
	}

	public void comprarDescarte() throws Exception {
		if (descarte == null)
			throw new Exception("NÃo tem nenhuma carta no monte de descarte");

		maoDoJogador.getMao().add(descarte);
		descartar();
	}

	public int contar() {
		return maoDoJogador.contar();
	}

}
