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

package com.lpiem.apprentisage.ihm;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.lpiem.apprentisage.R;

public class ActionBarService
{
	public static void initActionBar(final Activity activity, ActionBar actionBar, String titre)
	{
		View actionBarView = activity.getLayoutInflater().inflate(R.layout.custom_actionbar, null);
		Typeface font = Typeface.createFromAsset(activity.getAssets(), "fonts/ComicRelief.ttf");
		
		TextView txtTitre= (TextView) actionBarView.findViewById(R.id.actionbar_txt_titre);
		txtTitre.setText(titre);
		txtTitre.setTypeface(font);
		
		Button btnBack = (Button) actionBarView.findViewById(R.id.actionbar_btn_back);
		btnBack.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				activity.finish();
			}
		});

		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setCustomView(actionBarView);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
	}
}