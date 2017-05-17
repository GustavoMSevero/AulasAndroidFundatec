package app.bluetappresumido.Model;

public class Registro {
    String data, ementa;
    int posicaoInicial, posicaoFinal, presencas;

    public Registro() {}

    public Registro(String data, int presencas, String ementa, int posicaoInicial, int posicaoFinal){
        this.data = data;
        this.presencas = presencas;
        this.ementa = ementa;
        this.posicaoInicial = posicaoInicial;
        this.posicaoFinal = posicaoFinal;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getPosicaoInicial() {
        return posicaoInicial;
    }

    public void setPosicaoInicial(int posicaoInicial) {
        this.posicaoInicial = posicaoInicial;
    }

    public int getPosicaoFinal() {
        return posicaoFinal;
    }

    public void setPosicaoFinal(int posicaoFinal) {
        this.posicaoFinal = posicaoFinal;
    }

    public int getPresencas() { return presencas; }

    public void setPresencas(int presencas) { this.presencas = presencas; }

    public String getEmenta() {
        return ementa;
    }

    public void setEmenta(String ementa) {
        this.ementa = ementa;
    }
}
