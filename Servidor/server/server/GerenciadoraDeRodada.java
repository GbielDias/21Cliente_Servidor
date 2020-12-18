package server;

import commons.*;


import java.net.Socket;
import java.util.ArrayList;

public class GerenciadoraDeRodada {
    private final ArrayList<Parceiro> usuarios;
    private final ArrayList<SupervisoraDeConexao> supervisoras = new ArrayList<>();
    private int j = 0;

    public GerenciadoraDeRodada(ArrayList<Parceiro> usuarios) throws Exception {
        if (usuarios == null)
            throw new Exception("Usuario passado pelo parametro eh nulo");

        this.usuarios = usuarios;
    }

    public void proximoJogador() {
        synchronized (usuarios) {
            j++;
            if (j == usuarios.size())
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

    public void addSupervisora(SupervisoraDeConexao sup) throws Exception{
        if(sup == null)
            throw new Exception("Supervisora nula");

        supervisoras.add(sup);
    }

    public void resetarMao(Dealer dealer){
        synchronized (supervisoras){
            for (int i = 0; i < supervisoras.size(); i++){
                supervisoras.get(i).mao = new MaoDoJogador(dealer.getBaralho());
            }
        }
    }

    public ArrayList<SupervisoraDeConexao> getSupervisoras()
    {
        return supervisoras;
    }

    public void fimThreadSup()
    {
        synchronized (supervisoras){
            for (int i = 0; i < supervisoras.size(); i++){
                supervisoras.get(i).fim = false;
            }
        }
    }

    @Override
    public String toString() {
        return "GerenciadoraDeRodada{" +
                "usuarios=" + usuarios +
                ", j=" + j +
                '}';
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) return true;

        if(obj == null)
            return false;

        if(obj.getClass() != this.getClass())
            return false;

        GerenciadoraDeRodada gr = (GerenciadoraDeRodada) obj;

        if(gr.j != j)
            return false;

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

        //Estrutura de dados
        for (int i = 0; i < usuarios.size(); i++){
            hash = hash * 7 + usuarios.get(i).hashCode();
        }

        return Math.abs(hash);
    }

}