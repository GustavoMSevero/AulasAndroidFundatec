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

import app.bluetappresumido.Model.BancoDeDados;
import app.bluetappresumido.R;

public class RemoverAluno extends AppCompatActivity{
    Intent intent;
    private static String nomeTurma = "";

    EditText etMatricula;
    Button btRemoverAluno;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remover_aluno);

        intent = getIntent();
    }

    @Override
    protected void onStart(){
        super.onStart();
        removerAluno();
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
    // Metodo responsavel pela remocao de um aluno



    public void removerAluno() {
        //SETA VARIÁVEIS DE LAYOUT.
        etMatricula = (EditText) findViewById(R.id.et_matricula_aluno);
        btRemoverAluno = (Button) findViewById(R.id.bt_remover_aluno);

        //CLIQUE NO BOTÃO "REMOVER ALUNO".
        btRemoverAluno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //VERIFICA SE O CAMPO MATRÍCULA FOI PREENCHIDO.
                if(etMatricula.getText().length() > 0) {
                    nomeTurma = intent.getExtras().getString("nomeTurma");
                    BancoDeDados bd = new BancoDeDados(RemoverAluno.this);

                    //VERIFICA SE O ALUNO EXISTE.
                    if(bd.isAlunoBD(etMatricula.getText().toString(), nomeTurma)) {
                        bd.removerAlunoBD(etMatricula.getText().toString(), nomeTurma);
                        Toast.makeText(getApplicationContext(), "Aluno removido com sucesso!", Toast.LENGTH_LONG).show();
                        bd.close();
                        finish();
                    }
                    else {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(RemoverAluno.this);
                        builder.setTitle("Atenção");
                        builder.setMessage("Aluno não encontrado");

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
                    final AlertDialog.Builder builder = new AlertDialog.Builder(RemoverAluno.this);
                    builder.setTitle("Atenção");
                    builder.setMessage("Por favor, preencha todos os campos");

                    // Botao "ok"
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
