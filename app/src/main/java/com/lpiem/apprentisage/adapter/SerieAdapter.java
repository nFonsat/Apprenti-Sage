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
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lpiem.apprentisage.R;
import com.lpiem.apprentisage.data.App;

import com.lpiem.apprentisage.metier.Serie;

public class SerieAdapter extends BaseAdapter {
    private TextView mTxtNom;
    private TextView mTxtNote;

	private int selectedIndex = -1;
	private Activity mContext;

    private App mApplication;
	
	public SerieAdapter(Activity context){
		this.mContext = context;
        mApplication = App.getInstance();
	}
	
	@Override
	public int getCount()
	{
        return mApplication.getCurrentActivite().getSerieList().size();
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
		Serie serie = mApplication.getCurrentActivite().getSerieList().get(position);
		View view = mContext.getLayoutInflater().inflate(R.layout.serie_item, null);

		mTxtNom = (TextView) view.findViewById(R.id.serie_txt_nom);
        mTxtNom.setText(serie.getNom());
        mTxtNom.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "fonts/ComicRelief.ttf"));
		
		mTxtNote = (TextView) view.findViewById(R.id.serie_txt_note);
        mTxtNote.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "fonts/ComicRelief.ttf"));
		
		if(selectedIndex == position)
			view.setBackgroundColor(mApplication.getCurrentActivite().getColor());
		
		//if(serie.isFinished())
		//	txtNote.setText(serie.getNote() + "/10");
		
		return view;
	}

    public void setNote(int note, Serie serie){
        int remetreADix = ((note * 10)/serie.getExercices().size());
        mTxtNote.setText(remetreADix + "/" + 10);
    }
	
	public void setCurrentIndex(int position)
	{
		selectedIndex = position;
	}
}