package client;

import java.io.Serializable;
import commons.*;

@SuppressWarnings("serial")
public class PedidoDeDescarte extends Comunicado {


    private int iCarta;
    public PedidoDeDescarte(int iCarta) throws Exception
    { //Como parâmetro seria passada a carta que iria ser descartada, a posição ou alguma referencia ao objeto

        this.iCarta = iCarta;
    }

    public int getICarta()
    {
        return this.iCarta;
    }
}
