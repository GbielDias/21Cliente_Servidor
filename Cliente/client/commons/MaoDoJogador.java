package commons;

import java.util.ArrayList;

public class MaoDoJogador extends Comunicado implements Cloneable
{
    private ArrayList<Carta> mao = new ArrayList<>();

    public MaoDoJogador(Baralho baralho) {

        for (int i = 0; i < 3; i++)
            mao.add(baralho.comprarUmaCarta());

    }

    public MaoDoJogador(MaoDoJogador modelo) throws Exception {
        if (modelo == null)
            throw new Exception("Modelo inválido");

        this.mao = new ArrayList<>();

        this.mao.addAll(modelo.mao);
    }

    public Object clone ()
    {
        MaoDoJogador ret = null;

        try {
            ret = new MaoDoJogador(this);
            }
        catch (Exception e){ }
        return ret;
    }

    public ArrayList<Carta> getMao() {
        return mao;
    }

    //Verifica se contem a carta com o nome passado
    public boolean contemCarta(String nome) {
        for (int i = 0; i < mao.size(); i++) {
            if (mao.get(i).toString().equals(nome))
                return true;
        }

        return false;
    }

    public void removerCarta(String nome){

        for (int i = 0; i < 4; i++) {
            if (mao.get(i).toString().equals(nome)) {
                mao.remove(i);
                return;
            }
        }
    }

    public Carta getCarta(String nomeCarta) throws Exception {
        if (nomeCarta == null)
            throw new Exception("Nome de carta inválido");

        for (int i = 0; i < 4; i++) {
            if (mao.get(i).toString().equals(nomeCarta)) {
                return mao.get(i);
            }
        }
        throw new Exception("Carta não encontrada");
    }

    public int contar() {
        int soma = 0;

        for (int i = 0; i < mao.size(); i++) {
            soma = soma + mao.get(i).getPontos();
        }

        return soma;
    }

    public ArrayList<Carta> resetarMao(Baralho baralho){
        for (int i = 0; i < mao.size(); i++){
            mao.remove(i);
        }

        for (int i = 0; i < 3; i++){
            mao.add(baralho.comprarUmaCarta());
        }

        return mao;
    }

    @Override
    public String toString() {
        String str = "[";

        for (int i = 0; i < mao.size(); i++) {
            str += mao.get(i).toString() + ", ";
        }

        return str.substring(0, str.length() - 2) + "] = " + contar();
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MaoDoJogador mdj = (MaoDoJogador) o;

        for (int i=0; i< this.mao.size();i++)
            if (!this.mao.get(i).equals(mdj.mao.get(i)))
                return false;

       return true;
    }

    @Override
    public int hashCode()
    {
       int ret = 255;

       for (Carta crt : mao)
        ret = 13 * ret + crt.hashCode();

       return Math.abs(ret);
    }
}
