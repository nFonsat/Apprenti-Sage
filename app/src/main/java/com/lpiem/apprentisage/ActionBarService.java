/******************************************************************************************************************
*
* Apprenti-Sage Android
*
* ActionBarService.java
* 
* Michael Breton - Clément Bretin
* LP IEM - 2014
*
*******************************************************************************************************************/

package com.lpiem.apprentisage;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;

public class ActionBarService
{
	public static void initActionBar(final Activity context, ActionBar actionBar, String titre)
	{
		View actionBarView = context.getLayoutInflater().inflate(R.layout.custom_actionbar, null);
		Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/ComicRelief.ttf");
		
		TextView txtTitre= (TextView) actionBarView.findViewById(R.id.actionbar_txt_titre);
		txtTitre.setText(titre);
		txtTitre.setTypeface(font);
		
		Button btnBack = (Button) actionBarView.findViewById(R.id.actionbar_btn_back);
		btnBack.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				context.finish();
			}
		});

		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setCustomView(actionBarView);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
	}
}