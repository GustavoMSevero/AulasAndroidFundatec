package br.org.fundatec.poatransporteapp;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ItinerarioPojo {

@SerializedName("idlinha")
@Expose
public String id;
@SerializedName("codigo")
@Expose
public String codigo;
@SerializedName("nome")
@Expose
public String nome;

    public String toString(){
        String dadosItinerario = "id: "+id+"\nCódigo: "+codigo+"\nNome: "+nome;
        return dadosItinerario;
    }
}