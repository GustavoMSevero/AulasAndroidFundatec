package app.bluetappresumido.Model;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

import app.bluetappresumido.R;

public class ListaAlunoAdapter extends BaseAdapter {

    // Declaracao das variaveis
    private final Activity activity;
    private List<Aluno> listaAlunos;
    private int cargaHoraria;

    // Construtor
    public ListaAlunoAdapter(Activity activity, List<Aluno> listaAluno, int cargaHoraria){
        this.activity = activity;
        this.listaAlunos = listaAluno;
        this.cargaHoraria = cargaHoraria;
    }

    @Override
    public int getCount() {
        return listaAlunos.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return listaAlunos.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = activity.getLayoutInflater().inflate(R.layout.item_informacoes_aluno, null);
        Aluno aluno = listaAlunos.get(position);

        TextView nome = (TextView) view.findViewById(R.id.tv_informacoes_nome);
        nome.setText(aluno.getNome());
        TextView matricula = (TextView) view.findViewById(R.id.tv_informacoes_matricula);
        matricula.setText(String.valueOf(aluno.getMatricula()));
        TextView curso = (TextView) view.findViewById(R.id.tv_informacoes_curso);
        curso.setText(aluno.getCurso());
        TextView email = (TextView) view.findViewById(R.id.tv_informacoes_email);
        email.setText(aluno.getEmail());
        TextView faltas = (TextView) view.findViewById(R.id.tv_informacoes_faltas);
        try {
            calculaPercentualDeFaltas(view, faltas, aluno);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return view;
    }

    void calculaPercentualDeFaltas(View view, TextView faltas,  Aluno aluno) throws IOException {
        String frequencia = aluno.getFrequencia();

        int count = 0;
        for(int i=0; i<frequencia.length(); i++){
            if(frequencia.charAt(i) == 'A'){
                count++;
            }
        }
        faltas.setText(String.valueOf(count) + " (" + String.valueOf((count*100)/cargaHoraria) + "%)");
    }
}

