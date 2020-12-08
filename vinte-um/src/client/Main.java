package client;

public class Main {

	public static void main(String[] args) {
		Dealer dealer = new Dealer();

		System.out.println(dealer.baralho);

		for (;;) {
			if (dealer.contar() != 21) {
				System.out.println(dealer.maoDoJogador);
				System.out.println("Suas Opções: ");
				System.out.println("1. Comprar uma carta e descartar um.");
				System.out.println("2. Comprar a última carta do descarte e descartar um.");
				if (dealer.descarte != null)
					System.out.println("Ãšltimo descarte:" + dealer.descarte);

				try {
					char opcao = Teclado.getUmChar();

					switch (opcao) {
					case '1' -> dealer.comprar();

					case '2' -> dealer.comprarDescarte();
					}

				} catch (Exception err) {
					System.out.println(err.getMessage());
				}
			} else {
				System.out.println("VocÃª ganhou, fez 21 com as cartas: " + dealer.maoDoJogador);
				System.out.println("Quantidade de cartas compradas:" + dealer.vezesJogadas);
				break;
			}

		}

	}
}
