package client;
import commons.*;

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
                    System.exit(0);
                }
                else if(comunicado instanceof ComunicadoDeVitoria)
                {
                    System.out.println("Você venceu a partida");
                    do
                    {
                        if (servidor.espiar() instanceof ComunicadoDeFimDeJogo)
                        {
                            fimDeJogo();
                        }


                        try
                        {
                            comunicado = servidor.envie();
                        }
                        catch (Exception e) {}


                    }while (!(comunicado instanceof ComunicadoDeDesligamento || comunicado instanceof ComunicadoDeRestart));


                    if(comunicado instanceof ComunicadoDeRestart)
                    {
                        servidor.envie();
                        comunicado=null;

                        System.out.println("O jogo vai ser reiniciado");

                        //Fazer algo aqui para reiniciar a partida, ainda não sei o que;
                        


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
