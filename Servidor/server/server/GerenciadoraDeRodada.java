package server;

import commons.*;


import java.net.Socket;
import java.util.ArrayList;

public class GerenciadoraDeRodada {
    private final ArrayList<Parceiro> usuarios;
    private Socket conexao;
    private int j = 0;

    public GerenciadoraDeRodada(ArrayList<Parceiro> usuarios) throws Exception {
        if (usuarios == null)
            throw new Exception("Usuario passado pelo parametro eh nulo");

        this.usuarios = usuarios;
    }

    public void proximoJogador() {
        synchronized (usuarios) {
            j++;
            if (j >= usuarios.size())
                j = 0;
        }
    }

    public boolean pode(Parceiro usuario) throws Exception {
        try {
            synchronized (usuarios) {

                return usuario == usuarios.get(j);

            }
        } catch (Exception e) {
            throw new Exception("Usuário retirado");
        }
    }

    public ArrayList<Parceiro> getUsuarios(){
        return usuarios;
    }

    public int getJ() {
        return j;
    }

    public void setJ(int j) {
        this.j = j;
    }

    @Override
    public String toString() {
        return "GerenciadoraDeRodada{" +
                "usuarios=" + usuarios +
                ", conexao=" + conexao +
                ", j=" + j +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null)
            return false;

        if(obj.getClass() != this.getClass())
            return false;

        GerenciadoraDeRodada gr = (GerenciadoraDeRodada) obj;

        if(gr.j != j)
            return false;

        if(!gr.conexao.equals(conexao)) {
            return false;
        }

        //Mantém assim porque há uma ordem para os jogadores terem sua vez
        if(gr.usuarios.size() == usuarios.size()){
            for(int i = 0; i < usuarios.size(); i++){
                if(!usuarios.get(i).equals(gr.usuarios.get(i))) {
                    return false;
                }
            }

            return true;
        }

        return false;
    }

    @Override
    public int hashCode() {
        int hash = 111;

        //Atributos e objetos mais simples
        hash = hash * 7 + Integer.valueOf(j).hashCode();
        hash = hash * 7 + conexao.hashCode();

        //Estrutura de dados
        for (int i = 0; i < usuarios.size(); i++){
            hash = hash * 7 + usuarios.get(i).hashCode();
        }

        return hash;
    }

}