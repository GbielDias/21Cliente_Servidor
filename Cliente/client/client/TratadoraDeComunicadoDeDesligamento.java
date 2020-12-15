package client;
import commons.*;

import java.util.Locale;

public class TratadoraDeComunicadoDeDesligamento extends Thread
{
    private Parceiro servidor;

    public TratadoraDeComunicadoDeDesligamento(Parceiro servidor) throws Exception
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
                comunicado = servidor.espiar();
            }catch(Exception e){}
            try
			{
                if(comunicado instanceof ComunicadoDeDesligamento)
                {
                    System.out.println("\nO servidor vai ser desligado agora;");
                    System.err.println("volte mais tarde!\n");
                    servidor.encerrar();
                    System.exit(0);
                }
                else if(comunicado instanceof ComunicadoDeVitoria)
                {
                    System.out.println("Você venceu a partida");

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
                else if(comunicado instanceof ComunicadoDeDerrota)
                {
                    System.out.println("Você perdeu :( Alguém já venceu a partida");
                    do
                    {
                        if(servidor.espiar() instanceof ComunicadoDeFimDeJogo)
                        {
                            fimDeJogo();
                        }




                        try
                        {
                            comunicado = servidor.envie();
                        }
                        catch(Exception e){}
                    }
                    while(!(comunicado instanceof ComunicadoDeDesligamento || comunicado instanceof ComunicadoDeRestart));


                    if(comunicado instanceof ComunicadoDeRestart)
                    {
                        servidor.envie();
                        comunicado=null;

                        System.out.println("restart01");


                    }
                }
			}
			catch (Exception erro)
			{}
        }
    }

    private void fimDeJogo()
    {
        try
        {
            servidor.envie();
            System.out.println("Você quer jogar novamente, ou encerrar a partida");
            System.out.println("R. Jogar novamente");
            System.out.println("E. Encerrar servidor");
            String opcao;
            do
            {
                opcao = (Teclado.getUmString().toLowerCase());
            }
            while (!(opcao.equals("r") || opcao.equals("e")));

            switch (opcao)
            {
                case "r": servidor.receba(new ComunicadoDeRestart()); break;
                case "e": servidor.receba(new ComunicadoDeDesligamento()); break;
            }
        }
        catch(Exception e){}
    }
}
