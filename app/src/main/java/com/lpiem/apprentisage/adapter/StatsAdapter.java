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
import com.lpiem.apprentisage.database.DAO.ResultatDAO;
import com.lpiem.apprentisage.metier.Resultat;
import com.lpiem.apprentisage.model.Categorie;

import java.util.ArrayList;

public class StatsAdapter extends BaseAdapter{
	private Activity mActivity;

    private App mApplication;
    private ResultatDAO mResultatDAO;
	
	public StatsAdapter(Activity activity){
        mActivity = activity;
        mApplication = App.getInstance();
        mResultatDAO = new ResultatDAO(mActivity.getApplicationContext(), mApplication.getCurrentEleve());
	}
	
	@Override
	public int getCount(){
        return mApplication.getCurrentMatieres(mActivity).size();
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
		Categorie matiere = mApplication.getCurrentMatieres(mActivity).get(position);
		
		if(convertView == null){
			convertView = mActivity.getLayoutInflater().inflate(R.layout.item_stats, null);
		}
		
		//convertView.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
		
		TextView txtCategorie = (TextView) convertView.findViewById(R.id.stats_txt_categorie);
        TextView txtPourcentage = (TextView) convertView.findViewById(R.id.stats_txt_pourcentage);

        ArrayList<Resultat> resultats = mResultatDAO.getResultatsByMatiere(matiere.getNom());
        int pourcentage = ((resultats.size() * 100) / matiere.getSubCategorie().size());
        matiere.setPourcentage(pourcentage);

        txtCategorie.setText(matiere.getNom());
        txtPourcentage.setText(matiere.getPourcentage() + " %");

        ProgressBar progress = (ProgressBar) convertView.findViewById(R.id.stats_progress);
        progress.setProgress(matiere.getPourcentage());

        txtCategorie.setTypeface(Typeface.createFromAsset(mActivity.getAssets(), "fonts/ComicRelief.ttf"));
        txtPourcentage.setTypeface(Typeface.createFromAsset(mActivity.getAssets(), "fonts/ComicRelief.ttf"));

        if(matiere.getPourcentage() < 30)
			progress.setProgressDrawable(mActivity.getResources().getDrawable(R.drawable.progress_craie_rouge));
		else if(matiere.getPourcentage() >= 30 && matiere.getPourcentage() < 60)
			progress.setProgressDrawable(mActivity.getResources().getDrawable(R.drawable.progress_craie_jaune));
		else if(matiere.getPourcentage() >= 60)
			progress.setProgressDrawable(mActivity.getResources().getDrawable(R.drawable.progress_craie_vert));
		
		return convertView;
	}
}