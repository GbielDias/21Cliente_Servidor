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
        Comunicado com = null;
        for(;;)
        {
            try {
                com = servidor.espiar();
            }catch(Exception e){}
            try
			{
                if(com instanceof ComunicadoDeDesligamento)
                {
                    System.out.println("\nO servidor vai ser desligado agora;");
                    System.err.println("volte mais tarde!\n");
                    System.exit(0);
                }
                else if(com instanceof ComunicadoDeVitoria)
                {
                    System.out.println("Você venceu a partida");
                   // servidor.envie();
                    if(servidor.espiar() instanceof ComunicadoDeFimDeJogo)
                    {
                        fimDeJogo();
                    }


                    Comunicado comunicado = null;
                    do
                    {
                        try {
                            comunicado = servidor.espiar();
                        }
                        catch(Exception e){}
                    }
                    while(!(comunicado instanceof ComunicadoDeDesligamento || comunicado instanceof ComunicadoDeRestart));


                    if(comunicado instanceof ComunicadoDeRestart)
                    {
                        servidor.envie();
                        comunicado=null;

                        System.out.println("O jogo vai ser reiniciado");

                        //Fazer algo aqui para reiniciar a partida, ainda não sei o que;
                        


                    }

                }
                else if(com instanceof ComunicadoDeDerrota)
                {
                    System.out.println("Você perdeu :( Alguém já venceu a partida");
                   // servidor.envie();
                    if(servidor.espiar() instanceof ComunicadoDeFimDeJogo)
                    {
                        fimDeJogo();
                    }


                    Comunicado comunicado = null;
                    do
                    {
                        try {
                            comunicado = servidor.espiar();
                        }
                        catch(Exception e){}
                    }
                    while(!(comunicado instanceof ComunicadoDeDesligamento || comunicado instanceof ComunicadoDeRestart));


                    if(comunicado instanceof ComunicadoDeRestart)
                    {
                        servidor.envie();
                        comunicado=null;

                        /*

                            Fazer algo aqui, ainda não sei o que;
                        */


                    }
                }
			}
			catch (Exception erro)
			{}
        }
    }

    private void fimDeJogo()
    {
        try {
            System.out.println("Você quer jogar novamente, ou encerrar a partida");
            System.out.println("R. Jogar novamente");
            System.out.println("E. Encerrar servidor");
            String opcao;
            do {
                opcao = (Teclado.getUmString().toLowerCase());
            }
            while (!(opcao.equals("r") || opcao.equals("e")));

            switch (opcao)
            {
                case "r": servidor.receba(new ComunicadoDeRestart()); break;
                case "e": servidor.receba(new ComunicadoDeDesligamento());
            }
        }
        catch(Exception e){}
    }
}
