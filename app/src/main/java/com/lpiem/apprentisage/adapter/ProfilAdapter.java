/******************************************************************************************************************
*
* Apprenti-Sage Android
*
* ProfilAdapter.java
* 
* Michael Breton - Clément Bretin
* LP IEM - 2014
*
*******************************************************************************************************************/

package com.lpiem.apprentisage.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lpiem.apprentisage.R;
import com.lpiem.apprentisage.model.Profil;

public class ProfilAdapter extends BaseAdapter{
	
	private Context context;
	private List<Profil> profilsList;
	private LayoutInflater inflater;

	public ProfilAdapter(List<Profil> profilsList, Context context){
		this.context = context;
		this.profilsList = profilsList;
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		return profilsList.size();
	}

	@Override
	public Object getItem(int position) {
		return profilsList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_profil_list, null);
			holder = new ViewHolder();
			holder.nomTxt = (TextView) convertView.findViewById(R.id.nom_txt);
			holder.classeTxt = (TextView) convertView.findViewById(R.id.classe_txt);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Profil profil = profilsList.get(position);
		
		holder.nomTxt.setText(profil.getPrenom() + " " + profil.getNom());
		holder.nomTxt.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/ComicRelief.ttf"));
		holder.classeTxt.setText(profil.getClasse());
		holder.classeTxt.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/ComicRelief.ttf"));
		
		return convertView;
	}
	
	private class ViewHolder {
		public TextView nomTxt;
		public TextView classeTxt;
	}

}
