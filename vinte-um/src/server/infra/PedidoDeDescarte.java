package server.infra;

public class PedidoDeDescarte extends Comunicado {


    private String carta;
    public PedidoDeDescarte(String carta) throws Exception
    { //Como parâmetro seria passada a carta que iria ser descartada, a posição ou alguma referencia ao objeto
        if (carta == null)
            throw new Exception("Parâmetro inválido");

        this.carta = carta;
    }

    public String getCarta()
    {
        return this.carta;
    }
}
