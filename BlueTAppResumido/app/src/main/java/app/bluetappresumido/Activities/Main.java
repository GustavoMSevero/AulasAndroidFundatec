package app.bluetappresumido.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import app.bluetappresumido.Model.BancoDeDados;
import app.bluetappresumido.Model.Exportar;
import app.bluetappresumido.R;

public class Main extends AppCompatActivity {
    //VARIAVEIS DE PERMISSÃO.
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE,
                                           Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                           Manifest.permission.BLUETOOTH,
                                           Manifest.permission.BLUETOOTH_ADMIN,
                                           Manifest.permission.INTERNET};

    //DECLARAÇÃO DE VARIÁVEIS.
    private static final String PATH = Environment.getExternalStorageDirectory().toString() + "/BlueApp/";
    private String nomeTurma;
    private Button bt_inserir_turma, bt_remover_todas_turmas;

    private BancoDeDados bancoDeDados;
    static ArrayList<String> nomesTurma = new ArrayList<String>();
    static ArrayAdapter<String> adapter;
    static ListView lv_turmas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //SETA VARIÁVEIS.
        bt_inserir_turma =        (Button) findViewById(R.id.bt_inserir_turma);
        bt_remover_todas_turmas = (Button) findViewById(R.id.bt_remover_todas_turmas);

        bancoDeDados = new BancoDeDados(Main.this);
        bancoDeDados.criarTabelaTurmas();
        bancoDeDados.close();

        verifyStoragePermissions(Main.this);
        criarDiretorio();
    }

    @Override
    protected  void onStart(){
        super.onStart();
        refreshAdapter();

        //CLIQUE NO BOTÃO ADICIONAR TURMA.
        bt_inserir_turma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main.this, AdicionarTurma.class);
                startActivity(intent);
            }
        });

        //CLIQUE NO BOTÃO REMOVER TODAS AS TURMAS.
        bt_remover_todas_turmas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //GERA UM ALERTA PARA CONFIRMAR A OPERAÇÃO.
                final AlertDialog.Builder builder = new AlertDialog.Builder(Main.this);
                builder.setTitle("Confirmação de operação");
                builder.setMessage("Gostaria de remover todas as turmas?");

                //BOTÃO "SIM".
                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            BancoDeDados bd = new BancoDeDados(Main.this);
                            new RemoverTurma().removerTodasTurmas(bd);
                            adapter.clear(); // Limpa o adapter
                            bd.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        dialog.dismiss();
                    }
                });

                //BOTÃO "NÀO".
                builder.setNegativeButton("Não", null);

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        //SETA O ADAPTER PARA MOSTRAR AS TURMAS EXISTENTES.
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, nomesTurma);  //Faz a ponte entre os dados e o listview
        lv_turmas = (ListView) findViewById(R.id.lv_turmas_main);
        lv_turmas.setAdapter(adapter);
        registerForContextMenu(lv_turmas);

        //CLIQUE CURTO EM ALGUMA TURMA.
        lv_turmas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //CAPTURA O NOME DA TURMA CLICADA.
                nomeTurma = parent.getAdapter().getItem(position).toString();
                Intent intent = new Intent(Main.this, InformacoesAluno.class);
                intent.putExtra("nomeTurma", nomeTurma);
                startActivity(intent);
            }
        });

        //CLIQUE LONGO EM ALGUMA TURMA.
        lv_turmas.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //CAPTURA O NOME DA TURMA CLICADA.
                nomeTurma = parent.getAdapter().getItem(position).toString();
                return false;
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
        refreshAdapter();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        this.finish();
    }

    //MÉTODO RESPONSÁVEL PELA CRIAÇÃO DO DIRETÓRIO DO APP.
    public void criarDiretorio() {
        File pasta = new File(PATH);
        if(!pasta.exists()) { // Se o diretorio nao existir, cria o diretorio da app
            pasta.mkdirs();
        }
    }

    //MÉTODO RESPONSÁVEL POR ATUALIZAR O LISTVIEW QUE MOSTRA AS TURMAS EXISTENTES.
    public void refreshAdapter(){
        bancoDeDados = new BancoDeDados(Main.this);
        nomesTurma = bancoDeDados.getNomesTurmas();
        bancoDeDados.close();
    }

    //MÉTODO RESPONSÁVEL PELA CRIAÇÃO DO MENU APÓS UM CLIQUE LONGO SOBRE UMA TURMA.
    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, view, menuInfo);
        menu.setHeaderTitle(nomeTurma);
        getMenuInflater().inflate(R.menu.menu_de_contexto, menu);
    }

    //MÉTODO QUE GERENCIA AS OPÇÕES DO MENU DE CONTEXTO.
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            //CHAMADA POR BLUETOOTH.
            case R.id.menu_chamada_bluetooh:
                break;

            //CHAMADA MANUAL.
            case R.id.menu_chamada_manual:
                intent = new Intent(Main.this, ChamadaManual.class);
                intent.putExtra("nomeTurma",nomeTurma);
                startActivity(intent);
                break;

            //INSERIR ALUNO.
            case R.id.menu_inserir_aluno:
                intent = new Intent(Main.this, AdicionarAluno.class);
                intent.putExtra("nomeTurma", nomeTurma);
                startActivity(intent);
                break;

            //REMOVER ALUNO.
            case R.id.menu_remover_aluno:
                intent = new Intent(Main.this, RemoverAluno.class);
                intent.putExtra("nomeTurma", nomeTurma);
                startActivity(intent);
                break;

            //REMOVER TURMA.
            case R.id.menu_remover_turma:
                //CRIA UM ALERTA PARA CONFIRMAÇÃO DA OPERAÇÃO.
                final AlertDialog.Builder builder2 = new AlertDialog.Builder(Main.this);
                builder2.setTitle("Atenção");
                builder2.setMessage("Gostaria de remover esta turma?");
                //BOTÃO "OK".
                builder2.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        BancoDeDados bd = new BancoDeDados(Main.this);
                        try {
                            new RemoverTurma().removerTurma(bd, nomeTurma);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        dialog.dismiss();
                        bd.close();
                        adapter.remove(nomeTurma);
                    }
                });

                //BOTÃO "CANCELAR".
                builder2.setNegativeButton("Cancelar", null);
                AlertDialog dialog2 = builder2.create();
                dialog2.show();
                break;

            //REGISTRO DE CHAMADAS.
            case R.id.menu_ver_registro_de_chamadas:
                intent = new Intent(Main.this, RegistroDeChamadas.class);
                intent.putExtra("nomeTurma", nomeTurma);
                startActivity(intent);
                break;

            //EXPORTAR.
            case R.id.menu_exportar:
                try {
                    new Exportar().exportarArquivoCsv(nomeTurma, this.getApplicationContext());
                    Toast.makeText(getApplicationContext(), "Arquivo exportado com sucesso!", Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            default:
        }
        return super.onContextItemSelected(item);
    }

    //METÓDO RESPONSÁVEL PELA REQUISIÇÃO DE PERMISSÃO.
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case REQUEST_EXTERNAL_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                }
                else{
                    Toast.makeText(this, "O aplicativo necessita de sua permissão para funcionar corretamente!", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
