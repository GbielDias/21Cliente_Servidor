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
        for(;;)
        {
			try
			{
				if (this.servidor.espiar() instanceof ComunicadoDeDesligamento /*|| this.servidor.espiar() instanceof ComunicadoDeVencedor */)
				{
				    if(this.servidor.espiar() instanceof ComunicadoDeDesligamento)
				    {
                        System.out.println("\nO servidor vai ser desligado agora;");
                        System.err.println("volte mais tarde!\n");
                        System.exit(0);
                    }
				    else
                    {
                        System.out.println("Um jogador ganhou a partida");
                        System.exit(0);
                    }
				}
			}
			catch (Exception erro)
			{}
        }
    }
}
