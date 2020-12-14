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
                    System.exit(0);
                }
                else if(com instanceof ComunicadoDeDerrota)
                {
                    System.out.println("Você perdeu :( Alguém já venceu a partida");
                }
			}
			catch (Exception erro)
			{}
        }
    }
}
