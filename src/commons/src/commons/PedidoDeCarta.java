package commons;

import java.io.Serializable;

public class PedidoDeCarta  extends Comunicado {
    private MaoDoJogador mao;

    public PedidoDeCarta(MaoDoJogador mao){
        this.mao = mao;
    }

    public MaoDoJogador getMao(){
        return mao;
    }
}
