package server;

import commons.*;

import java.util.ArrayList;

public class GerenciadoraDePartida extends Thread {

    ArrayList<Parceiro> usuarios;
    //Tem que ter conexao com o usuario;


    public GerenciadoraDePartida(ArrayList<Parceiro> usuarios) throws Exception {
        if (usuarios == null)
            throw new Exception("Usuario passado pelo parametro eh nulo");


        this.usuarios = usuarios;

    }

    public void run() {
        try {
            int j;

            for (;;)//Aqui tem que ter que fazer mao.contar() da mao do jogador i, caso seja 21, sair desse for
            {
//              for de baixo    for de cima    for de baixo
//              0 -> 1 -> 2        ---->       0 -> 1 -> 2 ...
                for (j = 0; j < usuarios.size(); j++)
                {
                    usuarios.get(j).receba(new PermissaoDeRodada());

                    //vezDoUsuario();
                }
            }
        }
        catch (Exception e)
        {}
    }
}