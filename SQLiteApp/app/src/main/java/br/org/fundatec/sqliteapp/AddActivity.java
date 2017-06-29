package br.org.fundatec.sqliteapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class AddActivity extends AppCompatActivity {

    private EditText mEdtName;
    private EditText mEdtBirth;
    private EditText mEdtGender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        mEdtName = (EditText)findViewById(R.id.edtName);
        mEdtBirth = (EditText)findViewById(R.id.edtBirth);
        mEdtGender = (EditText)findViewById(R.id.edtGender);
    }

    public void save(View view) {

        BDLite database = new BDLite(this);
        Person person = new Person(0L,
                mEdtName.getText().toString(),
                mEdtGender.getText().toString(),
                mEdtBirth.getText().toString());

        database.insertPerson( person );
        finish();

    }
}
