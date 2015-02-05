package com.lpiem.apprentisage.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lpiem.apprentisage.R;
import com.lpiem.apprentisage.metier.Enseignant;

import java.util.List;

/**
 * Created by iem on 12/01/15.
 */
public class EnseignantAdapter extends BaseAdapter {

    private Context context;
    private List<Enseignant> enseignantsList;
    private LayoutInflater inflater;

    public EnseignantAdapter(List<Enseignant> enseignants, Context context) {

        this.context = context;
        this.enseignantsList = enseignants ;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return enseignantsList.size();
    }

    @Override
    public Object getItem(int position) {
        return enseignantsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_spinnerlog, null);
            holder = new ViewHolder();
            holder.nomTxt = (TextView) convertView.findViewById(R.id.nom_txt);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Enseignant enseignant = enseignantsList.get(position);

        holder.nomTxt.setText(enseignant.getPrenom() + " " + enseignant.getNom());
        holder.nomTxt.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/ComicRelief.ttf"));

        return convertView;
    }

    private class ViewHolder {
        public TextView nomTxt;
    }
}
