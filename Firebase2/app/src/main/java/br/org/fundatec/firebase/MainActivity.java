package br.org.fundatec.firebase;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    private RequestQueue mVolleyQueue;
    private ArrayList<Music> musics = new ArrayList<>();
    private ListView mListView;
    private SwipeRefreshLayout mRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mandar para a outra tela
                startActivity( new Intent( MainActivity.this, AddMusicActivity.class ));
            }
        });
        
        mVolleyQueue = Volley.newRequestQueue(this);
        mListView = (ListView)findViewById(R.id.listviewmusicas);

        lerMusicas();

        mRefresh = (SwipeRefreshLayout)findViewById(R.id.refresh);
        mRefresh.setEnabled(true);
        mRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                lerMusicas();
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Music music = ((ArrayAdapter<Music>)parent.getAdapter()).getItem( position );
                Intent intent = new Intent( MainActivity.this, AddMusicActivity.class );
                intent.putExtra("key", music.getId());
                intent.putExtra("title", music.getTitle());
                intent.putExtra("user", music.getUser());
                startActivity( intent );
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        lerMusicas();
    }

    public void lerMusicas(){
        StringRequest req = new StringRequest("https://fundatecti09.firebaseio.com/musicas.json",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject( response );
                            jsonObject.toString();

                            Iterator<?> keys = jsonObject.keys();

                            while( keys.hasNext() ) {
                                String key = (String)keys.next();
                                if ( jsonObject.get(key) instanceof JSONObject ) {
                                    String user = ((JSONObject) jsonObject.get(key)).get("user").toString();
                                    String music = ((JSONObject) jsonObject.get(key)).get("music").toString();
                                    Music music1 = new Music(user, music, key);
                                    musics.add( 0, music1 );

                                }
                            }

                            ArrayAdapter<Music> adapter = new ArrayAdapter<Music>(
                                    MainActivity.this, android.R.layout.simple_list_item_1, musics);
                            mListView.setAdapter( adapter );
                            mRefresh.setRefreshing(false);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Erro", Toast.LENGTH_SHORT).show();
            }
        });
        mVolleyQueue.add( req );
    }


}
