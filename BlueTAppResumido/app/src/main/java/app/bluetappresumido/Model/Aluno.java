package app.bluetappresumido.Model;

public class Aluno {

    // Declaracao das variaveis
    private String nome, frequencia, bluetoothAddress, curso, email;
    private int matricula;
    private boolean selected;

    // Construtores
    public Aluno() { }

    public Aluno(String nome, int matricula, String frequencia, String bluetoothAddress, String curso, String email) {
        this.nome = nome;
        this.matricula = matricula;
        this.frequencia = frequencia;
        this.bluetoothAddress = bluetoothAddress;
        this.curso = curso;
        this.email = email;
    }

    // Metodos getters/setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getMatricula() {
        return matricula;
    }

    public void setMatricula(int matricula) {
        this.matricula = matricula;
    }

    public String getFrequencia() {
        return frequencia;
    }

    public void setFrequencia(String frequencia) {
        this.frequencia = frequencia;
    }

    public String getBluetoothAddress() {
        return bluetoothAddress;
    }

    public void setBluetoothAddress(String bluetoothAddress) { this.bluetoothAddress = bluetoothAddress; }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isSelected(){ return selected; }

    public void setSelected(boolean selected){ this.selected = selected; }

}

