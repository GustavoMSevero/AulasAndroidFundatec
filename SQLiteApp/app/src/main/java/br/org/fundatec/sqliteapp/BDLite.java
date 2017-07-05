package br.org.fundatec.sqliteapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by tecnico on 28/06/2017.
 */

public class BDLite extends SQLiteOpenHelper {

    static private Integer version = 1;

    public BDLite(Context context) {
        super(context, "bdlite.db", null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL = "create table person " +
                "( id INTEGER PRIMARY KEY, name TEXT, gender TEXT, birth TEXT );";
        db.execSQL( SQL );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public Person insertPerson( Person person){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", person.getName());
        cv.put("gender", person.getGender());
        cv.put("birth", person.getBirth());

        Long id = db.insert("person", null, cv);
        person.setId( id );
        return person;

    }

    public ArrayList<Person> getAllPerson(){

        ArrayList<Person> people = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id, name, gender, birth FROM person", null);

        while ( cursor.moveToNext() ){
            Person p = new Person();
            p.setId(cursor.getLong(0));
            p.setName(cursor.getString(1));
            p.setGender(cursor.getString(2));
            p.setBirth(cursor.getString(3));
            people.add( p );
        }

        return people;

    }

    public Person get( Long id ){

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id, name, gender, birth FROM person " +
                "WHERE id = ?", new String[]{ id.toString() });
        Person p = new Person();
        if (cursor.moveToNext()){
            p.setId(cursor.getLong(0));
            p.setName(cursor.getString(1));
            p.setGender(cursor.getString(2));
            p.setBirth(cursor.getString(3));
        }
        return p;

    }

    public  void delete( Long id){

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM person WHERE id = ? ", new String[]{ id.toString() });

    }

    public Person updatePerson( Person person){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", person.getName());
        cv.put("gender", person.getGender());
        cv.put("birth", person.getBirth());

        db.update("person", cv, "id = ?", new String[]{ person.getId().toString() });

        return person;

    }
}
