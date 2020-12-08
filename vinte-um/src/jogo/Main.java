package com.company;

public class Main {

    public static void main(String[] args)
    {
        // 4 Ás / 4 dois / 4 três / 4 quatro
        // 4 cinco / 4 seis / 4 sete / 4 oito
        // 4 nove / 4 dez / 4 Dama / 4 valete / 4 reis



        Dealer dealer = new Dealer();

        System.out.println(dealer.baralho);

        for(;;)
        {
            if(dealer.contar() != 21)
            {
                System.out.println(dealer.maoDoJogador);
                System.out.println("Suas Opções: ");
                System.out.println("1. Comprar uma carta e descartar um.");
                System.out.println("2. Comprar a última carta do descarte e descartar um.");
                if(dealer.descarte != null)
                    System.out.println("Último descarte:" + dealer.descarte);

                try {
                    char opcao = Teclado.getUmChar();

                    switch (opcao)
                    {
                        case '1' -> dealer.comprar();

                        case '2' -> dealer.comprarDescarte();
                    }


                } catch (Exception err) {
                    System.out.println(err.getMessage());
                }
            }
            else
            {
                System.out.println("Você ganhou, fez 21 com as cartas: "+dealer.maoDoJogador);
                System.out.println("Quantidade de cartas compradas:" + dealer.vezesJogadas);
                break;
            }


        }







    }
}
