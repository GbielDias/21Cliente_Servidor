package client;
import commons.*;

import java.util.Locale;

public class TratadoraDeComunicado extends Thread
{
    private Parceiro servidor;

    public TratadoraDeComunicado(Parceiro servidor) throws Exception
    {
        if (servidor==null)
            throw new Exception ("Porta invalida");

        this.servidor = servidor;
    }

    public void run ()
    {
        Comunicado comunicado = null;
        for(;;)
        {
            try {
                comunicado = servidor.espiar(); // Permissao
            }catch(Exception e){}
            try
			{
                if(comunicado instanceof ComunicadoDeDesligamento)
                {
                    System.err.println("\nO servidor vai ser desligado agora..");
                    System.err.println("Volte mais tarde!\n");
                    servidor.encerrar();
                    System.exit(0);
                } else if (comunicado instanceof ComunicadoDeFimDeJogo){
                    System.err.println("\nA partida foi encerrada...");
                    servidor.encerrar();
                    System.exit(0);

                }else if(comunicado instanceof ComunicadoDeVitoria)
                {
                    System.out.println("Você venceu a partida");
                    System.out.println("\nAguarde o Dono do Jogo");

                    servidor.envie();

                    if(servidor.espiar() instanceof ComunicadoDeRestart)
                    {
                        servidor.envie();

                        String opcao;
                        do{
                            System.out.print("Deseja reiniciar a partida? (s/n) ");
                            opcao = Teclado.getUmString().toLowerCase();

                        }while (!opcao.equals("s") && !opcao.equals("n"));

                        if(opcao.equals("s"))
                            servidor.receba(new Pedido(null, "REINICIAR"));
                        else
                            servidor.receba(new Pedido(null, "DESLIGAR"));


                    }

                }
                else if(comunicado instanceof ComunicadoDeDerrota)
                {
                    System.out.println("\nVocê perdeu :( Alguém já venceu a partida");

                    System.out.println("\nAguarde o Dono do Jogo");

                    servidor.envie();

                    if(servidor.espiar() instanceof ComunicadoDeRestart)
                    {
                        System.out.println("Chegou o restart");
                        servidor.envie();

                        String opcao;
                        do{
                            System.out.print("Deseja reiniciar a partida? (s/n) ");
                            opcao = Teclado.getUmString().toLowerCase();

                        }while (!opcao.equals("s") && !opcao.equals("n"));

                        if(opcao.equals("s"))
                            servidor.receba(new Pedido(null, "REINICIAR"));
                        else
                            servidor.receba(new Pedido(null, "DESLIGAR"));


                    }
                }
                else if(servidor.espiar() instanceof ComunicadoDeReinicio)
                {
                    servidor.envie();
                    System.out.println("A partida foi reiniciada. Bom jogo!!!");
                }
			}
			catch (Exception erro)
			{}
        }
    }


    @Override
    public String toString()
    {
        return "Servidor: "+ servidor;
    }

    public boolean equals (Object o){
        if (this == o) return true;

        if (o == null) return false;

        if (this.getClass() != o.getClass()) return false;

        TratadoraDeComunicado tdc = (TratadoraDeComunicado) o;

        return this.servidor.equals(tdc.servidor);
    }

    public int hashCode()
    {
        int ret = 258;

        ret = ret * 11 + servidor.hashCode();

        return Math.abs(ret);
    }
}
