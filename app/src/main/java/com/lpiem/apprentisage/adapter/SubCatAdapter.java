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

import com.lpiem.apprentisage.Consts;
import com.lpiem.apprentisage.R;
import com.lpiem.apprentisage.applicatif.App;
import com.lpiem.apprentisage.applicatif.ResultatApp;
import com.lpiem.apprentisage.model.Categorie;

public class SubCatAdapter extends BaseAdapter {
    public static final String LOG = Consts.TAG_APPLICATION + " : " + SubCatAdapter.class.getSimpleName();

	private Activity mActivity;

    private Categorie mCurrentMatiere;
    private ResultatApp mResultatApp;
	
	public SubCatAdapter(Activity activity) {
		mActivity = activity;
        mCurrentMatiere = App.getInstance().getCurrentMatiere();
        mResultatApp = ResultatApp.getInstance();
	}
	
	@Override
	public int getCount() {
		if(mCurrentMatiere != null && mCurrentMatiere.getSubCategorie() != null)
			return mCurrentMatiere.getSubCategorie().size();
		
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
		Categorie mActivite = mCurrentMatiere.getSubCategorie().get(position);
        mResultatApp.calculateResultActivite(mActivity, mActivite);

        View view = mActivity.getLayoutInflater().inflate(R.layout.serie_item, null);

        LinearLayout linearIdActivity = (LinearLayout) view.findViewById(R.id.linearIdActivity);
        linearIdActivity.setBackgroundColor(mCurrentMatiere.getColor());

        TextView txtTitre = (TextView) view.findViewById(R.id.serie_txt_nom);
        TextView mTxtNote = (TextView) view.findViewById(R.id.serie_txt_note);


        txtTitre.setText(mActivite.getNom());
        mTxtNote.setText(mActivite.getPourcentage() + " % ");

        txtTitre.setTextColor(view.getResources().getColor(R.color.white));
        mTxtNote.setTextColor(view.getResources().getColor(R.color.white));

		txtTitre.setTypeface(Typeface.createFromAsset(mActivity.getAssets(), "fonts/ComicRelief.ttf"));
        mTxtNote.setTypeface(Typeface.createFromAsset(mActivity.getAssets(), "fonts/ComicRelief.ttf"));

		return view;
	}
}