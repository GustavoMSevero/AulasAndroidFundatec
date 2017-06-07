package br.org.fundatec.poatransporteapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static java.util.logging.Level.parse;

public class ItinerarioActivity extends AppCompatActivity {

    private ListView listIti;
    private ListView listview;
    private RequestQueue mVolleyQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itinerario);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listview = (ListView) findViewById(R.id.listaItinerario);
        mVolleyQueue = Volley.newRequestQueue(this);
        getItinerario();

//        ListView listIti = (ListView) findViewById(R.id.listaItinerario);

//        final ArrayList<ItinerarioPojo> it = new ArrayList<>();
//        RequestQueue vq = Volley.newRequestQueue(this);
//        GsonRequest<ItinerarioPojo> request = new GsonRequest<>("http://www.poatransporte.com.br/php/facades/process.php?a=il&p=5487\n" +
//                "\n", ItinerarioPojo.class, null, new Response.Listener<ItinerarioPojo>() {
//
//            public void onResponse(ItinerarioPojo response) {
//                Log.i("ITINERARIOPORTOALEGRE", response.toString());
//
//                it.add(response);
//
//                ArrayAdapter<ItinerarioPojo> adapter = new ArrayAdapter<ItinerarioPojo>(ItinerarioActivity.this, android.R.layout.simple_expandable_list_item_1, it);
//
//                listIti = ((ListView) findViewById(R.id.listaItinerario));
//                listIti.setAdapter(adapter);
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e("ITINERARIOPORTOALEGRE", error.getMessage());
//            }
//        });
//
//        vq.add(request);
    }

    private void getItinerario(){
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                "http://www.poatransporte.com.br/php/facades/process.php?a=il&p=5487" ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                       Itinerario itinerario =  parse( response );
                        ArrayAdapter<Itinerario.LatLng> adapter = new ArrayAdapter<>(
                                ItinerarioActivity.this, android.R.layout.simple_expandable_list_item_1,
                                itinerario.positions
                        );
                        listview.setAdapter(adapter);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        mVolleyQueue.add(stringRequest);
    }

    private Itinerario parse(String response) {
        Itinerario it = new Itinerario();
        try {
            JSONObject json = new JSONObject(response);
            it.codigo = json.getString("codigo");
            it.idlinha = json.getString("idlinha");
            it.nome = json.getString("nome");
            Integer perna = 0;
            // Get each numbered entry...
            while (json.has(perna.toString())) {
                String teste = json.getJSONObject(perna.toString()).toString();
                Log.i("Perna", " " + teste);
                Double lat = json.getJSONObject(perna.toString()).getDouble("lat");
                Double lng = json.getJSONObject(perna.toString()).getDouble("lng");
                it.positions.add( new Itinerario.LatLng( lat, lng ) );
                perna++;
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return it;

    }

}
