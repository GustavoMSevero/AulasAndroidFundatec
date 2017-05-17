package app.bluetappresumido.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import app.bluetappresumido.Model.BancoDeDados;
import app.bluetappresumido.Model.ListaRegistroAdapter;
import app.bluetappresumido.Model.Registro;
import app.bluetappresumido.R;

public class RegistroDeChamadas extends AppCompatActivity{
    // Declaracao das variaveis
    private static Intent intent;
    private static String nomeTurma;

    private ListView listView;
    private ListaRegistroAdapter registroAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_de_chamadas);

        intent = getIntent();
        nomeTurma = intent.getExtras().getString("nomeTurma");

        listView = (ListView) findViewById(R.id.lv_registro_de_chamadas);
        exibirRegistros();
    }

    public void exibirRegistros(){
        BancoDeDados bd = new BancoDeDados(RegistroDeChamadas.this);
        List<Registro> listaRegistro = bd.listarRegistroBD(nomeTurma);
        bd.close();

        if(listaRegistro.size() > 0){
            this.registroAdapter = new ListaRegistroAdapter(this, listaRegistro);
            listView.setAdapter(registroAdapter);
        }
        else{
            Toast.makeText(getApplicationContext(), "Nenhum registro encontrado!", Toast.LENGTH_LONG).show();
            finish();
        }

    }
}
