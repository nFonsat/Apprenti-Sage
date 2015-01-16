/******************************************************************************************************************
*
* Apprenti-Sage Android
*
* SubCatAdapater.java
* 
* Michael Breton - Clement Bretin
* LP IEM - 2014
*
*******************************************************************************************************************/

package com.lpiem.apprentisage.adapter;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lpiem.apprentisage.R;
import com.lpiem.apprentisage.applicatif.App;
import com.lpiem.apprentisage.model.Categorie;

public class SubCatAdapter extends BaseAdapter
{
	private Activity context;
    private App mApplication;
    private Categorie mCurrentCategorie;
	
	public SubCatAdapter(Activity context)
	{
		this.context = context;
        mApplication = App.getInstance();
        mCurrentCategorie = mApplication.getCurrentMatiere();
	}
	
	@Override
	public int getCount()
	{
		if(mCurrentCategorie != null && mCurrentCategorie.getSubCategorie() != null)
			return mCurrentCategorie.getSubCategorie().size();
		
		return 0;
	}

	@Override
	public Object getItem(int position)
	{
		return null;
	}

	@Override
	public long getItemId(int position)
	{
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent)
	{
		Categorie categorie = mCurrentCategorie.getSubCategorie().get(position);

		View view = context.getLayoutInflater().inflate(R.layout.serie_item, null);

        LinearLayout linearIdActivity = (LinearLayout) view.findViewById(R.id.linearIdActivity);
        linearIdActivity.setBackgroundColor(mCurrentCategorie.getColor());
		
		TextView txtTitre = (TextView) view.findViewById(R.id.serie_txt_nom);
		txtTitre.setText(categorie.getNom());
		txtTitre.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/ComicRelief.ttf"));
        txtTitre.setTextColor(view.getResources().getColor(R.color.white));
		
		TextView noteSerie = (TextView) view.findViewById(R.id.serie_txt_note);
        noteSerie.setText(String.valueOf(categorie.getPourcentage())+" %  ");
        noteSerie.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/ComicRelief.ttf"));
        noteSerie.setTextColor(view.getResources().getColor(R.color.white));
        // recuperer la note de la serie

		return view;
	}
}