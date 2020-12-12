package client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


import commons.*;

public class Aplicacao {

    public static final String HOST_PADRAO = "localhost";
    public static final int PORTA_PADRAO = 35555;

    public static void main(String[] args) // Cliente
    {
        if (args.length > 2) {
            System.err.println("Uso esperado: java app [HOST[PORTA]]");
            return;
        }

        //Criando os objetos que vão ser instanciado
        Socket conexao = null;
        ObjectOutputStream transmissor = null;
        ObjectInputStream receptor = null;
        Parceiro servidor = null;
        TratadoraDeComunicadoDeDesligamento tratadoraDeDesligamento = null;
        MaoDoJogador maoDoJogador = null;

        try
        {
            conexao = Instanciacao.instanciarConexao(args);
            transmissor = Instanciacao.instanciarTransmissor(conexao);
            receptor = Instanciacao.instanciarReceptor(conexao);
            servidor = Instanciacao.instanciarServidor(conexao, receptor, transmissor);
            tratadoraDeDesligamento = Instanciacao.instanciarTratadora(servidor);
        }
        catch (Exception err)
        {
            System.err.println(err.getMessage());
            System.err.println("Verefique se o servidor está ativo.\n " +
                    "Se sim, verefique se o servidor e a porta provido estão corretos!\n");
            return;
        }



        tratadoraDeDesligamento.start();


        //Aguarde os usuarios entrarem
        System.out.println("Aguarde os jogares entrarem na partida");

        Comunicado comunicado = null;
        while (!(comunicado instanceof ComunicadoDeComecar))
		{

			try
			{
				comunicado = (Comunicado) servidor.espiar();
			}
			catch(Exception err)
			{
			    System.err.print(err.getMessage());
            }
		}

        try
        {
            comunicado = servidor.envie();
        }
        catch(Exception e)
        {
            System.err.print(e.getMessage());
        }

        System.out.println("O jogo comecou!");

        while (true)
        {
            Comunicado rodada = null;

            try {

                do
                {
                    rodada = servidor.espiar();
                }
                while (!(rodada instanceof PermissaoDeRodada));
                rodada = servidor.envie();
            }
            catch (Exception e){

            }

            while (rodada instanceof PermissaoDeRodada)
            {

                    comunicado = null;
                    do {
                        try {
                            comunicado = servidor.espiar();
                        } catch (Exception err) {
                            System.err.println(err.getMessage() + " Erro ao espiar");
                        }
                    }
                    while (!(comunicado instanceof MaoDoJogador));

                    try {
                        maoDoJogador = (MaoDoJogador) servidor.envie();
                    } catch (Exception e) {
                        System.err.println(e.getMessage());
                    }

                    System.out.println(maoDoJogador);


                    String opcao;
                    try
                    {

                        System.out.println("Opções:");
                        System.out.println("C. Comprar do baralho e descartar");
                        System.out.println("D. Comprar a ultima descartada e descartar");
                        System.out.println("S. Sair da partida");
                        System.out.print("> ");

                        opcao = Teclado.getUmString().toUpperCase();

                        if (!(opcao.equals("C") || opcao.equals("D") || opcao.equals("S")))
                            throw new Exception("Opcao Inválida");


                        //Opcao "C" funcionando corretamente
                        if (opcao.equals("C"))
                        {
                            //Servidor recebe informacao da mao e do pedido do cliente
                            servidor.receba(new Pedido(maoDoJogador, opcao));

                            do {
                                //                      O cliente espera a resposta do servidor com sua mao nova
                                comunicado = (Comunicado) servidor.espiar();

                            } while (!(comunicado instanceof MaoDoJogador));


                            //                  O servidor finalmente envia a mao do jogador e esse é passado pro objeto maoDoJogador
                            maoDoJogador = (MaoDoJogador) servidor.envie();


                            //Sou obrigado a mandar ao servidor um nome de uma carta que exista na mao do jogador.
                            //So saio da repeticao quando tiver a carta com o nome correspondente
                            do
                            {
                                System.out.println(maoDoJogador);
                                System.out.print("Escolha o nome (tudo antes do hifen) de uma carta para ser descartada: ");

                                opcao = Teclado.getUmString().toUpperCase();
                                System.out.println("Jogador contem a carta " + opcao + ": " + maoDoJogador.contemCarta(opcao));
                                //                      Vai verificar caso o jogador tem essa carta que ele(a) escreveu

                            }
                            while (!maoDoJogador.contemCarta(opcao));
                            //                  Caso tenha a carta descrevida, sai do do-while.


                            //                  O servidor recebe a mao do jogador e a opcao, que no caso eh o nome da carta
                            servidor.receba(new Pedido(maoDoJogador, opcao));

                            do
                            {

                                //O usuario espera o servidor enviar a sua mao de volta
                                comunicado = (Comunicado) servidor.espiar();
                            }
                            while (!(comunicado instanceof MaoDoJogador));

                            //                  Usuario recebe sua mao de volta e ve sua mao logo apos
                            maoDoJogador = (MaoDoJogador) servidor.envie();
                            System.out.println(maoDoJogador);


                        }
                        else if (opcao.equals("D"))
                        {
                            servidor.receba(new Pedido(maoDoJogador, opcao));

                            do {
                                comunicado = (Comunicado) servidor.espiar();
                            }

                            while (!(comunicado instanceof MaoDoJogador));
                            maoDoJogador = (MaoDoJogador) servidor.envie();


                            System.out.println(maoDoJogador);

                            do {
                                System.out.print("Escolha o nome (tudo antes do hifen) uma carta para ser descartada: ");


                                opcao = Teclado.getUmString().toUpperCase();
                                System.out.println(opcao + " " + maoDoJogador.contemCarta(opcao));


                            } while (!maoDoJogador.contemCarta(opcao));

                            servidor.receba(new Pedido(maoDoJogador, opcao));

                            do {
                                comunicado = (Comunicado) servidor.espiar();
                            }
                            while (!(comunicado instanceof MaoDoJogador));

                            maoDoJogador = (MaoDoJogador) servidor.envie();
                            System.out.println(maoDoJogador);
                        }
                        else // S
                        {
                            break;
                        }


//                      AQUI ACABA A RODADA DO JOGADOR(A)
                        rodada = null;

                    } catch (Exception erro)
                    {
                        System.err.println("Opção inválida");
                    }
            }

            try
            {
                servidor.receba(new PedidoParaSair());
            } catch (Exception erro) {}
        }

    }

}