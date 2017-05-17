package app.bluetappresumido.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import app.bluetappresumido.Model.Aluno;
import app.bluetappresumido.Model.BancoDeDados;
import app.bluetappresumido.R;

public class AdicionarAluno extends AppCompatActivity{
    //DECLARAÇÃO DE VARIÁVEIS.
    private static Intent intent;
    private static Aluno aluno;
    private static String nomeTurma = "";

    EditText etNome, etMatricula, etCurso, etEmail;
    Button btInserirAluno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent = getIntent();
    }

    @Override
    protected void onStart(){
        super.onStart();
        inserirAluno();
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

    //MÉTODO RESPONSÁVEL POR ADICIONAR UM ALUNO.
    public void inserirAluno() {
        setContentView(R.layout.activity_adicionar_aluno);

        //SETA VARIÁVEIS DE LAYOUT.
        etNome = (EditText) findViewById(R.id.et_nome_aluno);
        etMatricula = (EditText) findViewById(R.id.et_matricula_aluno);
        etCurso = (EditText) findViewById(R.id.et_curso_aluno);
        etEmail = (EditText) findViewById(R.id.et_email_aluno);
        btInserirAluno = (Button) findViewById(R.id.bt_inserir_aluno);
        aluno = new Aluno();

        //CLIQUE NO BOTÃO INSERIR ALUNO.
        btInserirAluno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //VERIFICA SE TODOS OS CAMPOS DO FORMULÁRIO FORAM PREENCHIDOS.
                if(etNome.getText().length() > 0 && etMatricula.getText().length() > 0 && etCurso.getText().length() > 0 && etEmail.getText().length() > 0) {
                    nomeTurma = intent.getExtras().getString("nomeTurma");
                    BancoDeDados bd = new BancoDeDados(AdicionarAluno.this);

                    //VERIFICA SE O ALUNO JÁ EXISTE.
                    if(!bd.isAlunoBD(etMatricula.getText().toString(), nomeTurma)) {
                        //RETORNA UMA LISTA DE ALUNOS PARA CALCULAR A FREQUENCIA CASO O ALUNO ESTEJA SENDO INSERIDO APÓS TEREM SIDO REALIZADAS CHAMADAS.
                        List<Aluno> lista = bd.listarAlunoBD(nomeTurma);
                        int count = lista.get(0).getFrequencia().length(); //VERIFICA UTILIZANDO O PRIMEIRO ALUNO DA LISTA.
                        String frequencia = "";

                        //COMPLETA AS FREQUÊNCIAS DO ALUNO COM FALTAS.
                        for(int i=0; i< count; i++){
                            frequencia = frequencia + "A";
                        }

                        aluno.setNome(etNome.getText().toString());
                        aluno.setMatricula(Integer.parseInt(etMatricula.getText().toString()));
                        aluno.setFrequencia(frequencia);
                        aluno.setBluetoothAddress("");
                        aluno.setCurso(etCurso.getText().toString());
                        aluno.setEmail(etEmail.getText().toString());

                        //INSERE O ALUNO NO BD.
                        bd.inserirAlunoBD(aluno, nomeTurma);

                        Toast.makeText(getApplicationContext(), "Aluno inserido com sucesso!", Toast.LENGTH_LONG).show();
                        bd.close();
                        finish();
                    }
                    else {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(AdicionarAluno.this);
                        builder.setTitle("Atenção");
                        builder.setMessage("Este aluno já existe");

                        //BOTÃO "OK".
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });

                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                }
                else{
                    final AlertDialog.Builder builder = new AlertDialog.Builder(AdicionarAluno.this);
                    builder.setTitle("Atenção");
                    builder.setMessage("Por favor, preencha todos os campos");

                    //BOTÃO "OK".
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
    }
}
