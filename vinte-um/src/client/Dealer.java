package client;

import java.util.ArrayList;

public class Dealer // Aqui vai ter todos os relacionamentos entre mao e baralho
{
    Integer descarte = null;
    MaoDoJogador maoDoJogador;
    Baralho baralho;
    int vezesJogadas = 0;


    public Dealer() // Retirar os valores do baralho e colocar na maoDoJogador
    {
        baralho = new Baralho(); // 1-10

        ArrayList<Integer> valores = new ArrayList<Integer>();

        for(int i = 0; i < 3; i++)
            valores.add(baralho.comprarUmaCarta());

        maoDoJogador = new MaoDoJogador(valores);
    }


    public void comprar()
    {
       int valor = baralho.comprarUmaCarta();

       maoDoJogador.getMao().add(valor);

       descartar();
    }

    private void descartar()
    {
        int opcao;
        while(true)
        {
            try {
                System.out.println(maoDoJogador); // 1, 4, 6
                vezesJogadas++;
                System.out.println("Qual carta você deseja descartar?");
                opcao = Teclado.getUmInt();
                switch (opcao) {
                    case 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 -> {
                        maoDoJogador.removerNumero(opcao);
                        descarte = opcao;
                    }
                }

                break;
            }
            catch (Exception err)
            {
                System.out.println("Carta inválida");
            }
        }
    }

    public void comprarDescarte() throws Exception
    {
        if(descarte == null)
            throw new Exception("Não tem nenhuma carta no monte de descarte");


        maoDoJogador.getMao().add(descarte);
        descartar();
    }

    public int contar()
    {
        return maoDoJogador.contar();
    }




}
