package app.bluetappresumido.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.List;

import app.bluetappresumido.Model.Aluno;
import app.bluetappresumido.Model.BancoDeDados;
import app.bluetappresumido.Model.ListaAlunoAdapter;
import app.bluetappresumido.R;

public class InformacoesAluno extends AppCompatActivity{
    // Declaracao das variaveis
    private static Intent intent;
    private static String nomeTurma;

    private ListView listView;
    private ListaAlunoAdapter informacoesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacoes_aluno);
        listView = (ListView) findViewById(R.id.listview_informacoes_alunos);
        intent = getIntent();
        nomeTurma = intent.getExtras().getString("nomeTurma");
        exibirAlunos();
    }

    // Metodo responsavel pela exibicao das informacoes dos alunos na listView
    public void exibirAlunos(){
        BancoDeDados bd = new BancoDeDados(InformacoesAluno.this);
        List<Aluno> listaAlunos = bd.listarAlunoBD(nomeTurma);
        int cargaHoraria = bd.getCargaHoraria(nomeTurma);
        bd.close();

        this.informacoesAdapter = new ListaAlunoAdapter(this, listaAlunos, cargaHoraria);
        listView.setAdapter(informacoesAdapter);
    }
}
