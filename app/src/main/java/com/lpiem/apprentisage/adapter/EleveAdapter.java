package com.lpiem.apprentisage.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lpiem.apprentisage.R;
import com.lpiem.apprentisage.metier.Eleve;

import java.util.List;

/**
 * Created by iem on 13/01/15.
 */
public class EleveAdapter extends BaseAdapter {

    private Context context;
    private List<Eleve> eleveListe;
    private LayoutInflater inflater;

    public EleveAdapter(List<Eleve> listeEleve, Context context) {
        this.eleveListe = listeEleve;
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return eleveListe.size();
    }

    @Override
    public Object getItem(int position) {
        return eleveListe.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_spinner, null);
            holder = new ViewHolder();
            holder.nomTxt = (TextView) convertView.findViewById(R.id.nom_txt);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Eleve eleve = eleveListe.get(position);

        holder.nomTxt.setText(eleve.getNom() + " " + eleve.getPrenom());
        holder.nomTxt.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/ComicRelief.ttf"));

        return convertView;
    }

    private class ViewHolder {
        public TextView nomTxt;
    }
}
