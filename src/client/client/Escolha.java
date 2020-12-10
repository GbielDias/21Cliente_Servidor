package client;


import java.io.Serializable;

public class Escolha implements Serializable, Cloneable {
    private char opcao;

    public Escolha(char opcao)
    {
        this.opcao = opcao;
    }

    public char getOpcao() {
        return opcao;
    }

    public void setOpcao(char opcao) {
        this.opcao = opcao;
    }

    @Override
    public String toString()
    {
        return ""+this.opcao;
    }
}
