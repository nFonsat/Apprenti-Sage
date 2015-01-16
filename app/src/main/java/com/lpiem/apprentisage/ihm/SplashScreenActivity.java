/******************************************************************************************************************
*
* Apprenti-Sage Android
*
* SplashScreenActivity.java
* 
* Michael Breton - Clement Bretin
* LP IEM - 2014
*
*******************************************************************************************************************/
package com.lpiem.apprentisage.ihm;

import android.app.Activity;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.lpiem.apprentisage.R;
import com.lpiem.apprentisage.async.SplashTask;

public class SplashScreenActivity extends SherlockActivity{
    Activity currentActivity = this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_splashscreen);

        TextView txtTitre = (TextView) findViewById(R.id.splash_txt_titre);
        txtTitre.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Craie.ttf"));

        new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
                new SplashTask(currentActivity).execute();
			}
		}, 3000);

		setVolumeControlStream(AudioManager.STREAM_MUSIC);
	}
}
