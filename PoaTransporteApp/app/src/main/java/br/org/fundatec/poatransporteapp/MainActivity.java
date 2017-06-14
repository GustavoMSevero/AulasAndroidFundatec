package br.org.fundatec.poatransporteapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        lerTransportePortoAlegre();

        ListView list = (ListView) findViewById(R.id.lista);

        //Abre a view do item selecionado
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TransportePortoAlegre linha = ((ArrayAdapter<TransportePortoAlegre>)parent.getAdapter()).getItem( position );
                //Toast.makeText(MainActivity.this, linha.nome,Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                intent.putExtra("LINHAID", linha.id);
                startActivity( intent );
            }
        });

    }

    private void lerTransportePortoAlegre(){
        final ArrayList<TransportePortoAlegre> tpa = new ArrayList<>();
        RequestQueue vq = Volley.newRequestQueue(this);
        GsonRequest<TransportePortoAlegre[]> request = new GsonRequest<>("http://www.poatransporte.com.br/php/facades/process.php?"+
                "a=nc&p=%&t=o", TransportePortoAlegre[].class, null, new Response.Listener<TransportePortoAlegre[]>() {
            @Override
            public void onResponse(TransportePortoAlegre[] response) {
                Log.i("TRANSPORTEPORTOALEGRE", response.toString());
                for (int i = 0; i < response.length; i++){
                    TransportePortoAlegre temp = new TransportePortoAlegre();
                    temp.id = response[i].id;
                    temp.codigo = response[i].codigo;
                    temp.nome = response[i].nome;
                    tpa.add(temp);
                }

                //Adapter carrega os dados na tela
                ArrayAdapter<TransportePortoAlegre> adapter = new ArrayAdapter<TransportePortoAlegre>(MainActivity.this, android.R.layout.simple_expandable_list_item_1, tpa);

                list = ((ListView)findViewById(R.id.lista));
                list.setAdapter(adapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TRANSPORTEPORTOALEGRE", error.getMessage());
            }
        });

        vq.add(request);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
