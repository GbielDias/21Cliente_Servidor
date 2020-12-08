package com.company;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class MaoDoJogador
{
     private ArrayList<Integer> mao = new ArrayList<Integer>();



    public MaoDoJogador(ArrayList<Integer> valores)
    {
        for(int i = 0;i < 3; i++)
            mao.add(valores.get(i));

    }

    public ArrayList<Integer> getMao() {
        return mao;
    }

    public void removerNumero(int num) throws Exception
    {
        if(num < 0 || num > 10)
            throw new Exception("Carta inválidade");

        mao.remove(mao.indexOf(num));
    }

    public int contar()
    {
        int valor = 0;
        for(int i = 0; i < mao.size(); i++)
            valor = valor + mao.get(i);

        return valor;

    }


    @Override
    public String toString() {
        return mao.toString() + "= " + contar();
    }
}
