package app.bluetappresumido.Activities;

import android.support.v7.app.AppCompatActivity;
import java.io.IOException;
import java.util.ArrayList;

import app.bluetappresumido.Model.BancoDeDados;

public class RemoverTurma extends AppCompatActivity{
    //MÉTODO RESPONSÁVEL PELA REMOÇÃO DE UMA TURMA.
    public void removerTurma(BancoDeDados bd, String nomeTurma) throws IOException {
        // Remove a tabela (turma) do BD
        bd.removerTurmaBD(nomeTurma);
    }

    //MÉTODO RESPONSÁVEL PELA REMOÇÃO DE TODAS AS TURMAS.
    public void removerTodasTurmas(BancoDeDados bd) throws IOException {
        // Remove todas as tabelas (turmas) do BD
        ArrayList<String> tabelas = bd.getNomesTurmas();
        bd.limparBD(tabelas);
    }
}
