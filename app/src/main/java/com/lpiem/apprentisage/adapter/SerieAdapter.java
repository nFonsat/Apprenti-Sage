/******************************************************************************************************************
*
* Apprenti-Sage Android
*
* SerieAdapter.java
* 
* Michael Breton - Clément Bretin
* LP IEM - 2014
*
*******************************************************************************************************************/

package com.lpiem.apprentisage.adapter;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lpiem.apprentisage.R;
import com.lpiem.apprentisage.Shared;
import com.lpiem.apprentisage.model.Serie;

public class SerieAdapter extends BaseAdapter
{
	private int selectedIndex = -1;
	private Activity context;
	
	public SerieAdapter(Activity context)
	{
		this.context = context;
	}
	
	@Override
	public int getCount()
	{
		return Shared.getInstance().getCurrentSubCategorie().getSerieList().size();
	}

	@Override
	public Object getItem(int arg0)
	{
		return null;
	}

	@Override
	public long getItemId(int arg0)
	{
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		Serie serie = Shared.getInstance().getCurrentSubCategorie().getSerieList().get(position);
		View view = context.getLayoutInflater().inflate(R.layout.serie_item, null);

		TextView txtNom = (TextView) view.findViewById(R.id.serie_txt_nom);
		txtNom.setText(serie.getNom());
		txtNom.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/ComicRelief.ttf"));
		
		TextView txtNote = (TextView) view.findViewById(R.id.serie_txt_note);
		txtNote.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/ComicRelief.ttf"));
		
		if(selectedIndex == position)
			view.setBackgroundColor(Shared.getInstance().getCurrentCategorie().getColor());
		
		if(serie.isFinished())
			txtNote.setText(serie.getNote() + "/10");
		
		return view;
	}
	
	public void setCurrentIndex(int position)
	{
		selectedIndex = position;
	}
}