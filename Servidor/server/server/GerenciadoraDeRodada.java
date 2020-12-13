package server;

import commons.*;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class GerenciadoraDeRodada {

    private final ArrayList<Parceiro> usuarios;
    private Socket conexao;
    private int j=0;


    public GerenciadoraDeRodada(ArrayList<Parceiro> usuarios) throws Exception {
        if (usuarios == null)
            throw new Exception("Usuario passado pelo parametro eh nulo");


        this.usuarios = usuarios;
    }

    public void proximoJogador()
    {
        synchronized (usuarios)
        {
            j++;
            if (j >= usuarios.size())
                j = 0;
        }
    }

    public boolean pode(Parceiro usuario)
    {
        synchronized (usuarios) {
            if (j > -1)
                return usuario == usuarios.get(j);
            else
                return false;
        }
    }

}