package app.bluetappresumido.Model;

public class Turma {
    private String nomeTurma;
    private int cargaHoraria;

    public Turma(){
    }

    public Turma(String nomeTurma, int cargaHoraria){
        this.nomeTurma = nomeTurma;
        this.cargaHoraria = cargaHoraria;
    }

    public void setNome(String nomeTurma){
        this.nomeTurma = nomeTurma;
    }

    public String getNome(){
        return this.nomeTurma;
    }

    public void setCargaHoraria(int cargaHoraria){
        this.cargaHoraria = cargaHoraria;
    }

    public int getCargaHoraria(){
        return this.cargaHoraria;
    }

}
