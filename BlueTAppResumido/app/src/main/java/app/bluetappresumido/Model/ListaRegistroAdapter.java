package app.bluetappresumido.Model;


import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import app.bluetappresumido.R;

public class ListaRegistroAdapter extends BaseAdapter{
    private final Activity activity;
    private List<Registro> listaRegistro;

    public ListaRegistroAdapter(Activity activity, List<Registro> listaRegistro){
        this.activity = activity;
        this.listaRegistro = listaRegistro;
    }

    @Override
    public int getCount() { return listaRegistro.size(); }

    @Override
    public long getItemId(int position) { return position; }

    @Override
    public Object getItem(int position) { return listaRegistro.get(position); }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = activity.getLayoutInflater().inflate(R.layout.item_registro_de_chamadas, null);
        Registro registro = listaRegistro.get(position);

        TextView tvData = (TextView) view.findViewById(R.id.tv_registro_de_chamadas_data);
        tvData.setText(registro.getData());
        TextView tvPresecas = (TextView) view.findViewById(R.id.tv_registro_de_chamadas_presencas);
        tvPresecas.setText(String.valueOf(registro.getPresencas()));
        TextView tvEmenta = (TextView) view.findViewById(R.id.tv_registro_de_chamadas_ementa);
        tvEmenta.setText(registro.getEmenta());

        return view;
    }
}
