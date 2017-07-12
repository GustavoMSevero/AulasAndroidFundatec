package br.org.fundatec.ormapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class UpdateActivity extends AppCompatActivity {

    private EditText mEdtName;
    private EditText mEdtBirth;
    private EditText mEdtGender;
    private Person person;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mEdtName = (EditText)findViewById(R.id.edtName);
        mEdtBirth = (EditText)findViewById(R.id.edtBirth);
        mEdtGender = (EditText)findViewById(R.id.edtGender);

        if ( getIntent().hasExtra("id") ) {
            Long id = getIntent().getLongExtra("id", 0L);
            person = Person.findById( Person.class, id);
            mEdtName.setText( person.getName() );
            mEdtBirth.setText( person.getBirth() );
            mEdtGender.setText( person.getGender() );
        }
    }


    public void update() {

        person.setName( mEdtName.getText().toString() );
        person.setGender( mEdtGender.getText().toString() );
        person.setBirth( mEdtBirth.getText().toString() );
        person.save();
        finish();

    }

    public void delete(View view) {

        person.delete();
        finish();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_update, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_update) {
            update();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
