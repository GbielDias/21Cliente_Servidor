package client;

import java.util.ArrayList;

public class Baralho
{
    ArrayList<Integer> cartas;




    public Baralho()
    {
        cartas = new ArrayList<Integer>(52);

        int valor = 1;
        for(int i = 0;i < 52;i=i+4) // 16 -> 10 / 4- de cada numero
        {

            if(valor > 10) valor = 10;

            cartas.add(valor);
            cartas.add(valor);
            cartas.add(valor);
            cartas.add(valor);

            valor++;
        }
    }


    public int comprarUmaCarta()
    {
        int indice = (int)(Math.random() * cartas.size()); //Escolher um indice entre 0 e o tamanho do vetor
        int valor = cartas.get(indice);
        cartas.remove(indice);

        return valor;
    }



    public void setCartas(ArrayList<Integer> cartas) {
        this.cartas = cartas;
    }



    public ArrayList<Integer> getCartas() {
        return cartas;
    }

    public int getTamanho()
    {
        return cartas.size();
    }






    @Override
    public int hashCode()
    {
        int ret = 12;

        for (Integer i : cartas)
            ret = ret * 3 + i.hashCode();

        return ret;
    }

    @Override
    public String toString()
    {
       return cartas.toString();
    }
}
