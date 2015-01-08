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

import com.lpiem.apprentisage.Consts;
import com.lpiem.apprentisage.R;
import com.lpiem.apprentisage.Shared;
import com.lpiem.apprentisage.ihm.CalculActivity;
import com.lpiem.apprentisage.ihm.CompterActivity;
import com.lpiem.apprentisage.ihm.ConjugaisonActivity;
import com.lpiem.apprentisage.ihm.DicteeActivity;
import com.lpiem.apprentisage.ihm.LectureActivity;
import com.lpiem.apprentisage.ihm.RangerActivity;
import com.lpiem.apprentisage.model.Categorie;

public class SubCatAdapter extends BaseAdapter
{
	private Activity context;
	
	public SubCatAdapter(Activity context)
	{
		this.context = context;
	}
	
	@Override
	public int getCount()
	{
		if(Shared.getInstance().getCurrentCategorie() != null && Shared.getInstance().getCurrentCategorie().getSubCategorie() != null)
			return Shared.getInstance().getCurrentCategorie().getSubCategorie().size();
		
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
		Categorie categorie = Shared.getInstance().getCurrentCategorie().getSubCategorie().get(position);
		View view = context.getLayoutInflater().inflate(R.layout.sub_categorie_item, null);
		
		TextView txtTitre = (TextView) view.findViewById(R.id.sub_categorie_item_txt_nom);
		txtTitre.setText(categorie.getNom());
		txtTitre.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/ComicRelief.ttf"));
		txtTitre.setBackgroundColor(Shared.getInstance().getCurrentCategorie().getColor());
		
		TextView txtSerie = (TextView) view.findViewById(R.id.sub_categorie_item_txt_success);
		
		if(categorie.getSerieReussi() != 0)
		{
			txtSerie.setText(context.getString(R.string.success_serie) + " " + categorie.getSerieReussi());
		}
		else
		{
			txtSerie.setText("");
		}
		
		Button itemBtn = (Button) view.findViewById(R.id.sub_categorie_item_btn_cat);
		itemBtn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Shared.getInstance().setCurrentSubCategorie(Shared.getInstance().getCurrentCategorie().getSubCategorie().get(position));
				
				if(Shared.getInstance().getCurrentSubCategorie().getMode().equals(Consts.MODE_RANGER))
				{
					Intent intent = new Intent(context, RangerActivity.class);
					context.startActivity(intent);
				}
				else if (Shared.getInstance().getCurrentSubCategorie().getMode().equals(Consts.MODE_COMPTER)){
					Intent i = new Intent(context, CompterActivity.class);
					context.startActivity(i);
				}
				else if (Shared.getInstance().getCurrentSubCategorie().getMode().equals(Consts.MODE_LECTURE)){
					Intent i = new Intent(context, LectureActivity.class);
					context.startActivity(i);
				}
				else if (Shared.getInstance().getCurrentSubCategorie().getMode().equals(Consts.MODE_CALCUL)){
					Intent i = new Intent(context, CalculActivity.class);
					context.startActivity(i);
				}
				else if (Shared.getInstance().getCurrentSubCategorie().getMode().equals(Consts.MODE_CONJUGAISON)){
					Intent i = new Intent(context, ConjugaisonActivity.class);
					context.startActivity(i);
				}
				else if (Shared.getInstance().getCurrentSubCategorie().getMode().equals(Consts.MODE_DICTEE)){
					Intent i = new Intent(context, DicteeActivity.class);
					context.startActivity(i);
				}
			}
		});
		
		return view;
	}
}