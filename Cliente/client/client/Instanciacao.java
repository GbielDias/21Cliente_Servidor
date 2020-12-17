package client;

import commons.Parceiro;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Instanciacao
{


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
        } catch (IOException err)
        {
            throw new Exception("O servidor está cheio");
        }
        catch (Exception err)
        {
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
        } catch (IOException err) {
            System.err.println("O servidor está cheio, volte mais tarde !");
            System.exit(0);
        }  catch (Exception err) {
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

    public static TratadoraDeComunicado instanciarTratadora(Parceiro servidor) throws Exception {
        TratadoraDeComunicado ret = null;
        try {
            ret = new TratadoraDeComunicado(servidor);
        } catch (Exception err) {
            throw new Exception("Ocorreu um erro na instanciação da \"tratadoraDeDesligamento\"");
        }
        return ret;
    }

}
