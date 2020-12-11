package client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import commons.*;

public class Aplicacao {

    public static final String HOST_PADRAO = "localhost";
    public static final int PORTA_PADRAO = 3333;

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

        try {
            conexao = instanciarConexao(args);
            transmissor = instanciarTransmissor(conexao);
            receptor = instanciarReceptor(conexao);
            servidor = instanciarServidor(conexao, receptor, transmissor);
            tratadoraDeDesligamento = instanciarTratadora(servidor);
        } catch (Exception err) {
            System.err.println(err.getMessage());
            System.err.println("Indique o servidor e a porta corretos!\n");
            return;
        }

        //Aguarde os usuarios entrarem
        //Jogo começa aqui
        tratadoraDeDesligamento.start();

        /*Comunicado comunicado = null;
		do
		{
			System.out.println("Aguarde os jogares entrarem na partida");
			try
			{
				comunicado = (Comunicado) servidor.espiar();
			}
			catch(Exception err)
			{
			}
		}
		while (!(comunicado instanceof ComunicadoDeAguarde)); */

        Comunicado comn = null;
        do {
            try {
                comn = (Comunicado) servidor.espiar();
            } catch (Exception err) {
                System.err.println(err.getMessage() + " Erro ao espiar");
            }
        }
        while (!(comn instanceof MaoDoJogador));

        comn = null;

        try {
            maoDoJogador = (MaoDoJogador) servidor.envie();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        System.out.println(maoDoJogador);
        String opcao = "";

        do
        {
//          AQUI COMECA A RODADA DO JOGADOR(A)
            try
            {

                System.out.println("Opções:");
                System.out.println("C. Comprar do baralho e descartar");
                System.out.println("D. Comprar a ultima descartada e descartar");
                System.out.println("S. Sair da partida\n");
                System.out.print("> ");

                opcao = Teclado.getUmString().toUpperCase();

                if (!(opcao.equals("C") || !opcao.equals("D") || !opcao.equals("S"))) {
                    throw new Exception("Opcao Inválida");
                }

                //Opcao "C" funcionando corretamente
                if (opcao.equals("C"))
                {
                    //Servidor recebe informacao da mao e do pedido do cliente
                    servidor.receba(new Pedido(maoDoJogador, opcao));

                    do
                    {
//                      O cliente espera a resposta do servidor com sua mao nova
                        comn = (Comunicado) servidor.espiar();

                    }while (!(comn instanceof MaoDoJogador));


//                  O servidor finalmente envia a mao do jogador e esse é passado pro objeto maoDoJogador
                    maoDoJogador = (MaoDoJogador) servidor.envie();



                    //Sou obrigado a mandar ao servidor um nome de uma carta que exista na mao do jogador.
                    //So saio da repeticao quando tiver a carta com o nome correspondente
                    do
                    {
                        System.out.println(maoDoJogador);
                        System.out.print("Escolha o nome (tudo antes do hifen) de uma carta para ser descartada: ");

                        opcao = Teclado.getUmString().toUpperCase();
                        System.out.println("Jogador contem a carta "+opcao + ": " + maoDoJogador.contemCarta(opcao));
//                      Vai verificar caso o jogador tem essa carta que ele(a) escreveu

                    }
                    while (!maoDoJogador.contemCarta(opcao));
//                  Caso tenha a carta descrevida, sai do do-while.


//                  O servidor recebe a mao do jogador e a opcao, que no caso eh o nome da carta
                    servidor.receba(new Pedido(maoDoJogador, opcao));

                    do
                    {
//                      O usuario espera o servidor enviar a sua mao de volta
                        comn = (Comunicado) servidor.espiar();
                    }
                    while (!(comn instanceof MaoDoJogador));

//                  Usuario recebe sua mao de volta e ve sua mao logo apos
                    maoDoJogador = (MaoDoJogador) servidor.envie();
                    System.out.println(maoDoJogador);


                }
                else if (opcao.equals("D"))
                {
                    servidor.receba(new Pedido(maoDoJogador, opcao));

                    //quando escolher a "D", ele ta travando aqui mesmo que voce tenha uma Carta no Descarte
                    do
                    {
                        comn = (Comunicado) servidor.espiar();
                    }
                    //Nao consegui entender o motivo da expressao booleana abaixo ser sempre verdadeira
                    while (!(comn instanceof MaoDoJogador));
                    maoDoJogador = (MaoDoJogador) servidor.envie();

                    System.out.println(maoDoJogador);

                    //Aqui eu trato quando o que veio do Servidor for um Pedido, que no caso nao ha carta descartada ainda
//                    comn = servidor.envie();
//                    if(comn instanceof Pedido)
//                    {
//                        System.out.println("Nao ha nenhuma Carta descartada ainda");
//                        continue;
//                    }

//                  O jogador recebe a mão nova dele

                    //Sou obrigado a mandar ao servidor um nome de uma carta que exista na mao do jogador.
                    //So saio da repeticao quando tiver a carta com o nome correspondete
                    do {
                        System.out.print("Escolha o nome (tudo antes do hifen) uma carta para ser descartada: ");


                        opcao = Teclado.getUmString().toUpperCase();
                        System.out.println(opcao + " " + maoDoJogador.contemCarta(opcao));


                    } while (!maoDoJogador.contemCarta(opcao));

                    servidor.receba(new Pedido(maoDoJogador, opcao));

                    do {
                        comn = (Comunicado) servidor.espiar();
                    }
                    while (!(comn instanceof MaoDoJogador));

                    maoDoJogador = (MaoDoJogador) servidor.envie();
                    System.out.println(maoDoJogador);
                }
            }
            catch (Exception erro)
            {
                System.err.println("Opção inválida");
            }

//          AQUI ACABA A RODADA DO JOGADOR(A)

        }
        while (!opcao.equals("S"));

        try {
            servidor.receba(new PedidoParaSair());
        } catch (Exception erro) {
        }
    }

    public static Socket instanciarConexao(String[] args) throws Exception {
        Socket ret = null;
        try {
            String host = Aplicacao.HOST_PADRAO;
            int porta = Aplicacao.PORTA_PADRAO;

            if (args.length > 0)
                host = args[1];
            if (args.length == 2)
                porta = Integer.parseInt(args[1]);

            ret = new Socket(host, porta);
        } catch (Exception err) {
            throw new Exception("Ocorreu um erro na instanciação de \"conexao\".");
        }

        return ret;
    }

    public static ObjectOutputStream instanciarTransmissor(Socket conexao) throws Exception {
        ObjectOutputStream ret = null;
        try {
            ret = new ObjectOutputStream(conexao.getOutputStream());
        } catch (Exception err) {
            throw new Exception("Ocorreu um erro na instanciação do \"transmissor\".");
        }

        return ret;
    }

    public static ObjectInputStream instanciarReceptor(Socket conexao) throws Exception {
        ObjectInputStream ret = null;
        try {
            ret = new ObjectInputStream(conexao.getInputStream());
        } catch (Exception err) {
            throw new Exception("Ocorreu um erro na instanciação do \"receptor\".");
        }

        return ret;
    }

    public static Parceiro instanciarServidor(Socket conexao, ObjectInputStream receptor, ObjectOutputStream transmissor) throws Exception {
        Parceiro ret = null;
        try {
            ret = new Parceiro(conexao, receptor, transmissor);
        } catch (Exception err) {
            throw new Exception("Ocorreu um erro na instanciação do \"servidor\".");
        }

        return ret;
    }

    public static TratadoraDeComunicadoDeDesligamento instanciarTratadora(Parceiro servidor) throws Exception {
        TratadoraDeComunicadoDeDesligamento ret = null;
        try {
            ret = new TratadoraDeComunicadoDeDesligamento(servidor);
        } catch (Exception err) {
            throw new Exception("Ocorreu um erro na instanciação da \"tratadoraDeDesligamento\"");
        }
        return ret;
    }


}