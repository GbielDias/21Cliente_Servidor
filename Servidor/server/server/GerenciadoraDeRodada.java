package server;

import commons.*;


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

    public boolean pode(Parceiro usuario) throws Exception
    {
        try {
            synchronized (usuarios) {

                return usuario == usuarios.get(j);

            }
        }
        catch (Exception e)
        {
            throw new Exception("Usuário retirado");
        }
    }

    public ArrayList<Parceiro> getUsuarios() // TODO getUsuario - GerenciadoraDeRodada (necessario??)
    {
        return usuarios;
    }

    public int getJ() {
        return j;
    }

    public void setJ(int j) {
        this.j = j;
    }

    @Override
    public String toString() //TODO toString - GerenciadoraDeRodada
    {
        return "GerenciadoraDeRodada{" +
                "usuarios=" + usuarios +
                ", conexao=" + conexao +
                ", j=" + j +
                '}';
    }

    @Override
    public boolean equals(Object obj) //TODO Equal - GerenciadoraDeRodada
    {

        return false;
    }

    @Override
    public int hashCode() //TODO hashCode - GerenciadoraDeRodada
    {

        return 0;
    }

}