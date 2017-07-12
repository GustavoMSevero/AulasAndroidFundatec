package br.org.fundatec.ormapp;

import com.orm.SugarRecord;

/**
 * Created by tecnico on 28/06/2017.
 */

public class Person extends SugarRecord<Person> {

    private Long id;
    private String name;
    private String gender;
    private String birth;

    public Person(Long id, String name, String gender, String birth) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.birth = birth;
    }

    public Person() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    @Override
    public String toString() {
        return name + " nascido em " + birth;
    }
}
