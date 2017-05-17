package app.bluetappresumido.Model;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;

import app.bluetappresumido.R;

public class CheckBoxAdapter extends BaseAdapter{
    private List<Aluno> listaAlunos;
    private Activity activity;

    public CheckBoxAdapter(Activity activity, List<Aluno> listaAlunos){
        this.activity = activity;
        this.listaAlunos = listaAlunos;
    }

    @Override
    public int getCount() {
        return listaAlunos.size();
    }

    @Override
    public Object getItem(int position) {
        return listaAlunos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = activity.getLayoutInflater().inflate(R.layout.item_chamada_manual, null);

        Aluno aluno = listaAlunos.get(position);
        final CheckBox checkBox = (CheckBox) view.findViewById(R.id.cb_chamada_manual);
        TextView nome = (TextView) view.findViewById(R.id.tv_chamada_manual);
        nome.setText(aluno.getNome());

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Aluno aluno = (Aluno) checkBox.getTag();
                aluno.setSelected(buttonView.isChecked());
            }
        });
        checkBox.setTag(listaAlunos.get(position));
        checkBox.setChecked(listaAlunos.get(position).isSelected());

        return view;
    }
}
