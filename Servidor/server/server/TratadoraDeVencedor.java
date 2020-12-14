package server;

import commons.Comunicado;
import commons.Parceiro;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class TratadoraDeVencedor extends Thread
{
    private ArrayList<Parceiro> usuarios;
    private final GerenciadoraDeRodada GER;
    private Socket conexao;

    public TratadoraDeVencedor(ArrayList<Parceiro> usuarios, GerenciadoraDeRodada ger, Socket conexao) throws Exception
    {
        if(usuarios==null)
            throw new Exception("Usuarios nulos");
        if(ger == null)
            throw new Exception("gerenciadora nula");
        if(conexao == null)
            throw new Exception("conexao nula");

        this.usuarios = usuarios;
        this.GER = ger;
        this.conexao = conexao;

    }

    public void run()
    {
        ObjectInputStream receptor;
        try {
            receptor = new ObjectInputStream(conexao.getInputStream());
        }catch(Exception e)
        {
            return;
        }

        ObjectOutputStream transmissor;
        try
        {
            transmissor = new ObjectOutputStream(conexao.getOutputStream());
        }
        catch(Exception e)
        {
            try
            {
                receptor.close();
                return;
            }
            catch (Exception f)
            {}
        }




        Comunicado vencedor;

        while(true)
        {
            try {
                synchronized (usuarios)
                {
//                  if (usuarios.get(GER.getJ()).espiar() instanceof ComunicadoDeVencedor)
                    {

                        System.out.println("Um jogador ganhou");

                        for (Parceiro usuario: usuarios)
//                            usuario.receba(new ComunicadoDeVencedor());
                        return;
                    }
                }

            } catch (Exception e)
            {
                System.err.println("Erro no run da trataVence");
            }
        }

    }
}
