package server;

import commons.*;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class GerenciadoraDeRodada {

    ArrayList<Parceiro> usuarios;
    Socket conexao;
    int j=-1;
    //Tem que ter conexao com o usuario;


    public GerenciadoraDeRodada(ArrayList<Parceiro> usuarios) throws Exception {
        if (usuarios == null)
            throw new Exception("Usuario passado pelo parametro eh nulo");


        this.usuarios = usuarios;
    }

    public void proximoJogador()
    {
        j++;
        if(j >= usuarios.size())
            j=0;
    }

    public boolean pode(Parceiro usuario)
    {
        if(j > -1)
            return usuario == usuarios.get(j);
        else
            return false;
    }

}