package app.bluetappresumido.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import app.bluetappresumido.Model.Aluno;
import app.bluetappresumido.Model.BancoDeDados;
import app.bluetappresumido.Model.CheckBoxAdapter;
import app.bluetappresumido.Model.Registro;
import app.bluetappresumido.R;

public class ChamadaManual extends AppCompatActivity{
    //DECLARAÇÃO DE VARIÁVEIS.
    private Intent intent;
    private String nomeTurma, ementa="", dataFinal="";
    int presencas=0, cargaHorariaAula=0;
    BancoDeDados bd;

    private List<Aluno> listaAluno;
    private CheckBoxAdapter checkBoxAdapter;

    private ListView listView;
    private Button btSalvar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chamada_manual);

        //SETA VARIÁVEIS.
        intent = getIntent();
        nomeTurma = intent.getExtras().getString("nomeTurma");

        bd = new BancoDeDados(this);
        listaAluno = bd.listarAlunoBD(nomeTurma);

        criaDialog();
    }

    protected void onStart(){
        super.onStart();

        listView = (ListView) findViewById(R.id.lv_chamada_manual);
        this.checkBoxAdapter = new CheckBoxAdapter(this, listaAluno);
        listView.setAdapter(checkBoxAdapter);

        //BOTÃO "SALVAR"
        btSalvar = (Button) findViewById(R.id.bt_salvar_chamada_manual);
        btSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //VERIFICA SE JÁ FOI REALIZADA ALGUMA CHAMADA PARA ESTA TURMA HOJE.
                if (!bd.isRegistroBD(dataFinal, nomeTurma)) {
                    registraFrequencia(0);
                    criaRegistroDeChamada(0);
                    bd.close();
                    finish();
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(ChamadaManual.this);
                    builder.setTitle("Atenção");
                    builder.setMessage("Você já realizou uma chamada para esta turma hoje. O que deseja fazer?");

                    //BOTÃO "NOVA CHAMADA"
                    builder.setPositiveButton("Nova Chamada", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            registraFrequencia(0);
                            criaRegistroDeChamada(0);
                            bd.close();
                            dialog.dismiss();
                            finish();
                        }
                    });

                    //BOTÃO "SOBRESCREVER A ANTERIOR".
                    builder.setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            bd.close();
                            dialog.dismiss();
                            finish();
                        }
                    });

                    //BOTÃO "CANCELAR".
                    builder.setNegativeButton("Sobrescrever a Anterior", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            registraFrequencia(1);
                            criaRegistroDeChamada(1);
                            bd.close();
                            dialog.dismiss();
                            finish();
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        this.finish();
    }

    //FUNÇÃO RESPONSÁVEL POR CRIAR UMA JANELA QUE CAPTURA A EMENTA E A CARGA HORÁRIA EQUIVALENTE À CHAMADA.
    public void criaDialog(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(ChamadaManual.this);
        View view = getLayoutInflater().inflate(R.layout.ementa_chamada, null);
        builder.setTitle("Chamanda Manual");
        builder.setView(view);

        final EditText edEmenta = (EditText) view.findViewById(R.id.ed_ementa);
        final EditText edCargaHoraria = (EditText) view.findViewById(R.id.ed_carga_horaria);

        //BOTÃO "OK".
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                ementa = edEmenta.getText().toString();
                cargaHorariaAula = Integer.parseInt(edCargaHoraria.getText().toString());

                Calendar calendario = Calendar.getInstance();
                SimpleDateFormat dataformat = new SimpleDateFormat("dd/MM/yyyy");
                dataFinal = dataformat.format(calendario.getTime());

                dialog.dismiss();
            }
        });

        //BOTÃO "CANCELAR".
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void registraFrequencia(int tipo){
        switch (tipo){
            //NOVA CHAMADA.
            case 0:
                for(int i=0; i<listaAluno.size(); i++){
                    Aluno aluno = (Aluno) checkBoxAdapter.getItem(i);
                    String novaFrequencia = listaAluno.get(i).getFrequencia();
                    //VERIFICA O STATUS DO CHECKBOX, MARCADO OU DESMARCADO.
                    if(aluno.isSelected()){
                        int count = 0;
                        while (count<cargaHorariaAula){
                            novaFrequencia = novaFrequencia + "P";
                            count++;
                        }

                        bd.inserirFrequencia(nomeTurma, novaFrequencia, i+1);
                        presencas++;
                    }
                    else {
                        int count = 0;
                        while (count<cargaHorariaAula){
                            novaFrequencia = novaFrequencia + "A";
                            count++;
                        }
                        bd.inserirFrequencia(nomeTurma, novaFrequencia, i+1);
                    }
                }
                Toast.makeText(getApplicationContext(), "Chamada realizada com sucesso!", Toast.LENGTH_LONG).show();
                break;

            //SOBRESCREVE CHAMADA ANTERIOR.
            case 1:
                for(int i=0; i<listaAluno.size(); i++){
                    Aluno aluno = (Aluno) checkBoxAdapter.getItem(i);
                    List<Registro> listaRegistro = bd.listarRegistroBD(nomeTurma);
                    String novaFrequencia;

                    if(listaRegistro.get(listaRegistro.size()-1).getPosicaoInicial() == 0){
                        novaFrequencia = "";
                    }
                    else{
                        novaFrequencia = listaAluno.get(i).getFrequencia().substring(0, listaRegistro.get(listaRegistro.size()-1).getPosicaoInicial());
                    }

                    //VERIFICA O STATUS DO CHECKBOX, MARCADO OU DESMARCADO.
                    if(aluno.isSelected()){
                        int count = 0;
                        while (count<cargaHorariaAula){
                            novaFrequencia = novaFrequencia + "P";
                            count++;
                        }

                        bd.inserirFrequencia(nomeTurma, novaFrequencia, i+1);
                        presencas++;
                    }
                    else {
                        int count = 0;
                        while (count<cargaHorariaAula){
                            novaFrequencia = novaFrequencia + "A";
                            count++;
                        }
                        bd.inserirFrequencia(nomeTurma, novaFrequencia, i+1);
                    }
                }
                Toast.makeText(getApplicationContext(), "Chamada realizada com sucesso!", Toast.LENGTH_LONG).show();
                break;
            default:
        }

    }

    //FUNÇÃO RESPONSÁVEL POR CRIAR UM LOG DA CHAMADA REALIZADA.
    public void criaRegistroDeChamada(int tipo){
        Registro registro;
        List<Registro> listaRegistro = bd.listarRegistroBD(nomeTurma);
        int posInicial, posFinal;

        switch (tipo){
            //LOG DA NOVA CHAMADA.
            case 0:
                if(listaRegistro.size() == 0){
                    posInicial = 0;
                    posFinal = cargaHorariaAula-1;

                }
                else{
                    posInicial = listaRegistro.get(listaRegistro.size()-1).getPosicaoFinal()+1;
                    posFinal = listaRegistro.get(listaRegistro.size()-1).getPosicaoFinal() + cargaHorariaAula;
                }

                registro = new Registro(dataFinal, presencas, ementa, posInicial, posFinal);
                bd.inserirRegistroBD(registro, nomeTurma);
                break;
            //LOG DA CHAMADA SOBRESCRITA.
            case 1:
                if(listaRegistro.size() == 1){
                    posInicial = 0;
                    posFinal = cargaHorariaAula-1;
                }
                else{
                    posInicial = listaRegistro.get(listaRegistro.size()-2).getPosicaoFinal()+1;
                    posFinal = listaRegistro.get(listaRegistro.size()-2).getPosicaoFinal() + cargaHorariaAula;
                }

                registro = new Registro(dataFinal, presencas, ementa, posInicial, posFinal);
                bd.sobrescreverRegistroBD(registro, listaRegistro.size(), nomeTurma);
                break;
        }
    }
}
