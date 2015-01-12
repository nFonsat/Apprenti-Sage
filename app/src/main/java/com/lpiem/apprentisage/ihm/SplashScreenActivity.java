/******************************************************************************************************************
*
* Apprenti-Sage Android
*
* SplashScreenActivity.java
* 
* Michael Breton - Cl�ment Bretin
* LP IEM - 2014
*
*******************************************************************************************************************/

package com.lpiem.apprentisage.ihm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.lpiem.apprentisage.R;
import com.lpiem.apprentisage.Shared;
import com.lpiem.apprentisage.async.SplashTask;
import com.lpiem.apprentisage.model.Categorie;
import com.lpiem.apprentisage.model.Serie;

public class SplashScreenActivity extends SherlockActivity{
    Activity currentActivity = this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_splashscreen);

		TextView txtTitre = (TextView) findViewById(R.id.splash_txt_titre);
		txtTitre.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Craie.ttf"));
		
		Shared.getInstance().getCategorieListCp().clear();
		Shared.getInstance().getCategorieListCe1().clear();
		
		loadJson("cp.json");
		loadJson("ce1.json");

		Shared.getInstance().loadProfilList();
		
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
                new SplashTask(currentActivity).execute();
			}
		}, 3000);
		
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
	}
	
	private String getFileContent(String fileName){
		StringBuilder mLine = new StringBuilder();
		try {
		    BufferedReader reader = new BufferedReader(new InputStreamReader(getAssets().open("datas/" + fileName), "UTF-8")); 
		    String str;
		    
		    while ((str = reader.readLine()) != null) {
		       mLine.append(str); 
		    }

		    reader.close();
		} catch (IOException e) {
		    e.printStackTrace();
		}

		return mLine.toString();
	}
	
	private void loadJson(String fileName)
	{
		int[] colors = {
				getResources().getColor(R.color.cat_blue),
				getResources().getColor(R.color.cat_green),
				getResources().getColor(R.color.cat_orange),
				getResources().getColor(R.color.cat_red),
				getResources().getColor(R.color.cat_violet)
		};
		
		String jsonCP = getFileContent(fileName);
		
		try {
			JSONArray categorieArray = new JSONArray(jsonCP);
			for (int i = 0; i < categorieArray.length(); i ++){
				JSONObject catJson = categorieArray.getJSONObject(i);
				
				// Initialisation categorie
				Categorie cat = new Categorie();
				cat.setNom(catJson.optString("name"));
				cat.setColor(colors[i]);
				
				// Initialisation sous categorie
				JSONArray subArray = catJson.optJSONArray("sub");
				for(int j = 0; j < subArray.length(); j++)
				{
					JSONObject subJson = subArray.getJSONObject(j);
					Categorie subCat = new Categorie();
					
					subCat.setNom(subJson.optString("name"));
					subCat.setMode(subJson.optString("mode"));
					
					// Initialisation liste series
					List<Serie> listSerie = new ArrayList<Serie>();
					JSONArray serieArray = subJson.optJSONArray("series");
					
					for(int x = 0; x < serieArray.length(); x++)
					{
						JSONObject serieJson = serieArray.optJSONObject(x);
						JSONArray reponseArray = serieJson.optJSONArray("content");
						List<String> reponses = new ArrayList<String>();
						
						Serie serie = new Serie();
						serie.setNom(serieJson.optString("name"));
						
						for(int y = 0; y < reponseArray.length(); y++)
							reponses.add(reponseArray.getString(y));
						
						serie.setReponses(reponses);
						listSerie.add(serie);
					}
					
					subCat.setSerieList(listSerie);
					cat.getSubCategorie().add(subCat);
				}
				
				if(fileName.equals("cp.json"))
					Shared.getInstance().getCategorieListCp().add(cat);
				else if(fileName.equals("ce1.json"))
					Shared.getInstance().getCategorieListCe1().add(cat);
			}
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
	}
}
