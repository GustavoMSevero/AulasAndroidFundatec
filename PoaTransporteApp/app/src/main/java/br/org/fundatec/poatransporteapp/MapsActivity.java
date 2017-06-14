package br.org.fundatec.poatransporteapp;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONException;
import org.json.JSONObject;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private RequestQueue mVolleyQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mVolleyQueue = Volley.newRequestQueue(this);


    }

    private void getItinerario(String id){
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                "http://www.poatransporte.com.br/php/facades/process.php?a=il&p=" + id ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Itinerario itinerario =  parse( response );

                        PolylineOptions polyline = new PolylineOptions();

                        for (Itinerario.LatLng latlng : itinerario.positions){
                            LatLng marker = new LatLng(latlng.lat, latlng.lng);
                            //mMap.addMarker(new MarkerOptions().position(marker).title("Centro Histórico Porto Alegre"));
                            polyline.add(marker);
                        }
                        mMap.addPolyline(polyline);
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


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng portoAlegre = new LatLng(-30.0277, -51.2287);
        mMap.addMarker(new MarkerOptions().position(portoAlegre).title("Centro Histórico Porto Alegre"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(portoAlegre, 12));

        if ( getIntent().hasExtra("LINHAID") ){
            String id = getIntent().getStringExtra("LINHAID");
            getItinerario( id );

        }

    }
}
