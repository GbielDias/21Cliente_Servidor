package client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import temp.continhas.Cliente.Parceiro;

public class Aplicacao {
	private static String HOST = "localhost";
	private static int PORTA = 3333;

	public static void main(String[] args) {
		if (args.length > 2) {
			System.err.println("Uso esperado: java Cliente [HOST [PORTA]]\n");
			return;
		}

//		Socket conexao = null;
//		try {
//			String host = HOST;
//			int porta = PORTA;
//
//			if (args.length > 0)
//				host = args[0];
//
//			if (args.length == 2)
//				porta = Integer.parseInt(args[1]);
//
//			conexao = new Socket(host, porta);
//		} catch (Exception erro) {
//			System.err.println("Indique o servidor e a porta corretos!\n");
//			return;
//		}
//
//		ObjectOutputStream transmissor = null;
//		try {
//			transmissor = new ObjectOutputStream(conexao.getOutputStream());
//		} catch (Exception erro) {
//			System.err.println("Indique o servidor e a porta corretos!\n");
//			return;
//		}
//
//		ObjectInputStream receptor = null;
//		try {
//			receptor = new ObjectInputStream(conexao.getInputStream());
//		} catch (Exception erro) {
//			System.err.println("Indique o servidor e a porta corretos!\n");
//			return;
//		}
//
//		Parceiro servidor = null;
//		try {
//			servidor = new Parceiro(conexao, receptor, transmissor);
//		} catch (Exception erro) {
//			System.err.println("Indique o servidor e a porta corretos!\n");
//			return;
//		}

		Dealer dealer = new Dealer();

		System.out.println(dealer.baralho + "\n");

		for (;;) {
			if (dealer.contar() != 21) {

				System.out.println(dealer.maoDoJogador.toString());
				System.out.println("Suas Opções: ");
				System.out.println("1. Comprar uma carta e descartar um.");
				System.out.println("2. Comprar a última carta do descarte e descartar um.");

				if (dealer.descarte != null)
					System.out.println("Último descarte:" + dealer.descarte);

				try {
					char opcao = Teclado.getUmChar();

					switch (opcao) {
					case '1':
						dealer.comprar();

					case '2':
						dealer.comprarDescarte();

					default:
						System.out.println("Opção inválida");
						continue;

					}

				} catch (Exception err) {
					System.out.println(err.getMessage());
				}
			} else {
				System.out.println("Você ganhou, fez 21 com as cartas: " + dealer.maoDoJogador);
				System.out.println("Quantidade de cartas compradas:" + dealer.vezesJogadas);
				break;
			}

		}

	}
}
