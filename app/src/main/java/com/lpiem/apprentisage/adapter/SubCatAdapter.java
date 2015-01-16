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
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lpiem.apprentisage.Consts;
import com.lpiem.apprentisage.R;
import com.lpiem.apprentisage.applicatif.App;
import com.lpiem.apprentisage.database.DAO.ResultatDAO;
import com.lpiem.apprentisage.metier.Eleve;
import com.lpiem.apprentisage.metier.Resultat;
import com.lpiem.apprentisage.metier.Serie;
import com.lpiem.apprentisage.model.Categorie;

import java.util.ArrayList;

public class SubCatAdapter extends BaseAdapter {
    public static final String LOG = Consts.TAG_APPLICATION + " : " + SubCatAdapter.class.getSimpleName();

    private TextView mTxtNote;
	private Activity mContext;

    private App mApplication;
    private Categorie mCurrentCategorie;

    private ResultatDAO mResultatDAO;
	
	public SubCatAdapter(Activity context, Eleve eleve) {
		this.mContext = context;
        mApplication = App.getInstance();
        mCurrentCategorie = mApplication.getCurrentMatiere();
        mResultatDAO = new ResultatDAO(mContext, eleve);
	}
	
	@Override
	public int getCount() {
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
		Categorie mActivite = mCurrentCategorie.getSubCategorie().get(position);
		View view = mContext.getLayoutInflater().inflate(R.layout.serie_item, null);

        LinearLayout linearIdActivity = (LinearLayout) view.findViewById(R.id.linearIdActivity);
        linearIdActivity.setBackgroundColor(mCurrentCategorie.getColor());
		
		TextView txtTitre = (TextView) view.findViewById(R.id.serie_txt_nom);
		mTxtNote = (TextView) view.findViewById(R.id.serie_txt_note);

        int noteActivite = 0;
        ArrayList<Resultat> resultats = mResultatDAO.getResultatsByActivite(mActivite.getNom());
        Log.d(LOG + " : Nombre de resultat pour l'activite " + mActivite.getNom(), String.valueOf(resultats.size()));
        for (Resultat unResultat : resultats){
            noteActivite += unResultat.getNote();
        }


        txtTitre.setText(mActivite.getNom());
        setNote(noteActivite, mActivite);

        txtTitre.setTextColor(view.getResources().getColor(R.color.white));
        mTxtNote.setTextColor(view.getResources().getColor(R.color.white));

		txtTitre.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "fonts/ComicRelief.ttf"));
        mTxtNote.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "fonts/ComicRelief.ttf"));

		return view;
	}



    public void setNote(int note, Categorie activite) {
        int nbTotalActivite = activite.getSerieList().size();
        int remetreADix = ((note * 100) / nbTotalActivite);
        Log.d(LOG + " : Pourcentage pour l'activite " + activite.getNom(), String.valueOf(remetreADix) + " %");
        mTxtNote.setText(remetreADix + " %  ");
    }
}