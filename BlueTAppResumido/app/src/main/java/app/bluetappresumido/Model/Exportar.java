package app.bluetappresumido.Model;

import android.content.Context;
import android.os.Environment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Exportar {
    // Metodo responsavel pela exportacao de uma turma para um arquivo .csv
    public void exportarArquivoCsv(String nomeTurma, Context context) throws IOException {
        String PATH = Environment.getExternalStorageDirectory().toString() + "/BlueApp/";
        BancoDeDados bd = new BancoDeDados(context);
        List<Aluno> listaAluno = bd.listarAlunoBD(nomeTurma);

        File arquivo = new File(PATH + nomeTurma + "Frequencias.csv");
        BufferedWriter writer = new BufferedWriter(new FileWriter(arquivo, false));

        writer.write("matricula,frequencia");
        for(int i=0; i<listaAluno.size(); i++){
            writer.write("\n" + listaAluno.get(i).getMatricula() + "," + listaAluno.get(i).getFrequencia());
        }

        writer.close();
        bd.close();
    }
}
