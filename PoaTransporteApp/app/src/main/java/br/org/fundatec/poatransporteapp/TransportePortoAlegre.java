package br.org.fundatec.poatransporteapp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TransportePortoAlegre {

@SerializedName("id")
@Expose
public String id;
@SerializedName("codigo")
@Expose
public String codigo;
@SerializedName("nome")
@Expose
public String nome;

    public String toString(){
        //String dados = "id: "+id+"\nCódigo: "+codigo+"\nNome: "+nome;
        String dados = codigo+"   "+nome;
        return dados;
    }

}