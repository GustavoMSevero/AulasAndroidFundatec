package app.bluetappresumido.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import app.bluetappresumido.Model.Aluno;
import app.bluetappresumido.Model.BancoDeDados;
import app.bluetappresumido.Model.Turma;
import app.bluetappresumido.R;

public class AdicionarTurma extends AppCompatActivity{
    private static String nomeTurma = "", nomeArquivoCsv = "", cargaHoraria; // nomeTurma = nomeTabela
    private static final String PATH = Environment.getExternalStorageDirectory().toString() + "/BlueApp/";
    private static final String PATHCSV = Environment.getExternalStorageDirectory().toString() + "/Download/";

    private EditText etNomeTurma, etNomeArquivoCsv, etCargaHoraria;
    private Button btCriarTurma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_turma);
    }

    @Override
    protected void onStart(){
        super.onStart();
        inserirTurma();
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

    //METODO RESPONSÁVEL PELA CRIAÇÃO DE UMA NOVA TURMA.
    public void inserirTurma() {
        setContentView(R.layout.activity_adicionar_turma);

        //SETA ELEMENTOS DO LAYOUT.
        etNomeTurma = (EditText) findViewById(R.id.et_nome_turma);
        etNomeArquivoCsv = (EditText) findViewById(R.id.et_nome_arquivo_csv);
        etCargaHoraria = (EditText) findViewById(R.id.et_carga_horaria_total);

        //CLIQUE NO BOTÃO "CRIAR TURMA"
        btCriarTurma = (Button) findViewById(R.id.bt_criar_turma);
        btCriarTurma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nomeTurma = etNomeTurma.getText().toString();
                nomeArquivoCsv = etNomeArquivoCsv.getText().toString();
                cargaHoraria = etCargaHoraria.getText().toString();

                //VERIFICA SE TODOS OS CAMPOS DO FORMULÁRIO FORAM PREENCHIDOS.
                if (nomeTurma.length() > 0 && nomeArquivoCsv.length() > 0 && etCargaHoraria.length() > 0) {
                    //VERIFICA SE O NOME DA TURMA COMEÇA POR UM ALGARISMO.
                    if (!Character.isDigit(nomeTurma.charAt(0))) {
                        //VERIFICA SE O NOME DO ARQUIVO CONTEM A EXTENSÃO .CSV.
                        if (!nomeArquivoCsv.contains(".csv")) {
                            nomeArquivoCsv = nomeArquivoCsv + ".csv";
                        }
                        File arquivo = new File(PATHCSV + nomeArquivoCsv);

                        //VERIFICA SE O ARQUIVO .CSV EXISTE.
                        if (arquivo.exists()) {
                            BancoDeDados bd = new BancoDeDados(AdicionarTurma.this);
                            ArrayList<String> nomesTurma = bd.getNomesTurmas();

                            //VERIFICA SE JÁ EXISTE UMA TURMA COM ESSE NOME.
                            if (!nomesTurma.contains(nomeTurma)) {
                                Turma turma = new Turma(nomeTurma, Integer.parseInt(cargaHoraria));
                                bd.inserirTurmaBD(turma, "Turmas");
                                bd.criarTabelaAluno(nomeTurma);
                                bd.criarTabelaRegistro(nomeTurma);

                                //LÊ ARQUIVO .CSV E ARMAZENA AS INFORMAÇÕES NO BANCO DE DADOS.
                                try {
                                    //LÊ O CABEÇALHO DO ARQUIVO .CSV
                                    BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(arquivo), "ISO-8859-1"));
                                    String cabecalho = reader.readLine();
                                    String dadosCabecalho[] = cabecalho.split(",");
                                    int posNome = 0, posMatricula = 1, posCurso = 2, posEmail = 3;

                                    //FAZ A CORRESPONDENCIA DE CADA COLUNA DO ARQUIVO COM OS ATRIBUTOS DO ALUNO.
                                    for (int i = 0; i < dadosCabecalho.length; i++) {
                                        if (dadosCabecalho[i].equals("nome"))      { posNome = i;      }
                                        if (dadosCabecalho[i].equals("matrícula")) { posMatricula = i; }
                                        if (dadosCabecalho[i].equals("curso"))     { posCurso = i;     }
                                        if (dadosCabecalho[i].equals("e-mail"))    { posEmail = i;     }
                                    }

                                    //LÊ CADA LINHA DO ARQUIVO.
                                    while (reader.ready()) {
                                        String linha = reader.readLine();
                                        String dados[] = linha.split(",");
                                        Aluno aluno = new Aluno(dados[posNome], Integer.parseInt(dados[posMatricula]), "", "", dados[posCurso], dados[posEmail]);
                                        bd.inserirAlunoBD(aluno, nomeTurma);
                                    }
                                    reader.close();
                                    bd.close();
                                }catch (Exception e) {
                                    e.printStackTrace();
                                }

                                Toast.makeText(getApplicationContext(), "Turma criada com sucesso!", Toast.LENGTH_LONG).show();
                                finish();
                            } else {
                                final AlertDialog.Builder builder = new AlertDialog.Builder(AdicionarTurma.this);
                                builder.setTitle("Atenção");
                                builder.setMessage("Esta turma já existe");

                                //BOTÃO "OK".
                                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();
                                    }
                                });

                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                        } else {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(AdicionarTurma.this);
                            builder.setTitle("Atenção");
                            builder.setMessage("Arquivo .csv não encontrado");

                            //BOTÃO "OK".
                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            });

                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    } else {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(AdicionarTurma.this);
                        builder.setTitle("Atenção");
                        builder.setMessage("O nome da turma não pode começar por um algarismo");

                        //BOTÃO "OK".
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });

                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(AdicionarTurma.this);
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
