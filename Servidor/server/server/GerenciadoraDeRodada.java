package server;

import commons.*;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class GerenciadoraDeRodada extends Thread {

    ArrayList<Parceiro> usuarios;
    Socket conexao;
    //Tem que ter conexao com o usuario;


    public GerenciadoraDeRodada(ArrayList<Parceiro> usuarios, Socket conexao) throws Exception {
        if (usuarios == null)
            throw new Exception("Usuario passado pelo parametro eh nulo");
        if(conexao == null)
            throw new Exception("Conexao passado pelo parametro eh nulo");


        this.usuarios = usuarios;
        this.conexao = conexao;

    }

    public void run() {
        try
        {
            Comunicado com;
            try
            {
                ObjectOutputStream transmissor;
                ObjectInputStream receptor;

                transmissor =   new ObjectOutputStream(this.conexao.getOutputStream());
                receptor    =   new ObjectInputStream(this.conexao.getInputStream());
            }
            catch(Exception e)
            {
                return;
            }

            int j;

            for (;;)//Aqui tem que ter que fazer mao.contar() da mao do jogador i, caso seja 21, sair desse for
            {
//              for de baixo    for de cima    for de baixo
//              0 -> 1 -> 2        ---->       0 -> 1 -> 2 ...
                for (j = 0; j < usuarios.size(); j++)
                {
                    //Manda ao usuario a permissao de jogar
                    usuarios.get(j).receba(new PermissaoDeRodada());

                    //Espera o usuario retornar o fim da rodada
                    do
                    {
                         com = usuarios.get(j).espiar();
                    }
                    while (!(com instanceof ComunicadoFinalDeRodada));

                    usuarios.get(j).envie();
                    //vezDoUsuario();
                }
            }
        }
        catch (Exception e)
        {}
    }
}