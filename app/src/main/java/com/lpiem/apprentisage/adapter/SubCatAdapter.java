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
import android.content.Intent;
import android.graphics.Typeface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.lpiem.apprentisage.R;
import com.lpiem.apprentisage.ihm.SerieActivity;
import com.lpiem.apprentisage.data.App;
import com.lpiem.apprentisage.ihm.SousCategorieActivity;
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
		
		TextView txtTitre = (TextView) view.findViewById(R.id.serie_txt_nom);
		txtTitre.setText(categorie.getNom());
		txtTitre.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/ComicRelief.ttf"));
		txtTitre.setBackgroundColor(mCurrentCategorie.getColor());
		
		TextView noteSerie = (TextView) view.findViewById(R.id.serie_txt_note);
        // recuperer la note de la serie

		return view;
	}
}