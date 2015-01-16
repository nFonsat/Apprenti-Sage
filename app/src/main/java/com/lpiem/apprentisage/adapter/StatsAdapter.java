/******************************************************************************************************************
*
* Apprenti-Sage Android
*
* StatsAdapter.java
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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lpiem.apprentisage.R;
import com.lpiem.apprentisage.applicatif.App;
import com.lpiem.apprentisage.model.Categorie;

public class StatsAdapter extends BaseAdapter
{
	private Activity context;
    private App mApplication;
	
	public StatsAdapter(Activity context)
	{
		this.context = context;
        mApplication = App.getInstance();
	}
	
	@Override
	public int getCount()
	{
		//return Shared.getInstance().getCurrentCategorieList().size();
        return mApplication.getCurrentMatieres(context).size();
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
	public View getView(int position, View convertView, ViewGroup parent)
	{
		Categorie categorie = mApplication.getCurrentMatieres(context).get(position);
		
		if(convertView == null)
		{
			convertView = context.getLayoutInflater().inflate(R.layout.item_stats, null);
		}
		
		//convertView.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
		
		TextView txtCategorie = (TextView) convertView.findViewById(R.id.stats_txt_categorie);
		txtCategorie.setText(categorie.getNom());
		txtCategorie.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/ComicRelief.ttf"));
		
		TextView txtPourcentage = (TextView) convertView.findViewById(R.id.stats_txt_pourcentage);
		txtPourcentage.setText(categorie.getPourcentage() + " %");
		txtPourcentage.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/ComicRelief.ttf"));
		
		ProgressBar progress = (ProgressBar) convertView.findViewById(R.id.stats_progress);
		progress.setProgress(categorie.getPourcentage());
		
		if(categorie.getPourcentage() < 30)
			progress.setProgressDrawable(context.getResources().getDrawable(R.drawable.progress_craie_rouge));
		else if(categorie.getPourcentage() >= 30 && categorie.getPourcentage() < 60)
			progress.setProgressDrawable(context.getResources().getDrawable(R.drawable.progress_craie_jaune));
		else if(categorie.getPourcentage() >= 60)
			progress.setProgressDrawable(context.getResources().getDrawable(R.drawable.progress_craie_vert));
		
		return convertView;
	}
}