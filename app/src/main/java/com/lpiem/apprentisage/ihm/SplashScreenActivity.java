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

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.lpiem.apprentisage.R;
import com.lpiem.apprentisage.Utils.Consts;
import com.lpiem.apprentisage.async.SplashTask;

public class SplashScreenActivity extends SherlockActivity {
    private static final String CLASS_TAG = Consts.TAG_APPLICATION +  " : " + SplashScreenActivity.class.getSimpleName();

    Handler mAsyncHandler = new Handler(){
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            switch (msg.what) {
                case Consts.SPLASHTASK_FINISH:
                    goToHome();
                    break;
                case Consts.SPLASHTASK_ERROR:
                    Log.e(CLASS_TAG + " : Response URL", String.valueOf(msg.arg1));
                    Toast.makeText(getApplicationContext(), getString(R.string.error_network), Toast.LENGTH_LONG).show();
                    goToHome();
                    break;
            }
        }
    };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_splashscreen);

        TextView txtTitre = (TextView) findViewById(R.id.splash_txt_titre);
        txtTitre.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Craie.ttf"));

        new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
                ConnectivityManager mConnectivityManager =
                        (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo activeNetwork = mConnectivityManager.getActiveNetworkInfo();
                boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
                boolean isWiFi = (activeNetwork != null) && (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI);

                if(isConnected || isWiFi){
                    new SplashTask(getApplicationContext(), mAsyncHandler).execute();
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.error_network), Toast.LENGTH_LONG).show();
                    goToHome();
                }
			}
		}, 3000);

		setVolumeControlStream(AudioManager.STREAM_MUSIC);
	}

    public void goToHome(){
        Intent i = new Intent(this, AccueilActivity.class);
        startActivity(i);
        finish();
    }
}
