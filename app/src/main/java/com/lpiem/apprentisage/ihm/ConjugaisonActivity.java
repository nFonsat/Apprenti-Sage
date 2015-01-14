/******************************************************************************************************************
*
* Apprenti-Sage Android
*
* ConjugaisonActivity.java
* 
* Michael Breton - Cl�ment Bretin
* LP IEM - 2014
*
*******************************************************************************************************************/

package com.lpiem.apprentisage.ihm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.AssetFileDescriptor;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.lpiem.apprentisage.ActionBarService;
import com.lpiem.apprentisage.Consts;
import com.lpiem.apprentisage.R;
import com.lpiem.apprentisage.Shared;
import com.lpiem.apprentisage.adapter.SerieAdapter;
import com.lpiem.apprentisage.model.Serie;

public class ConjugaisonActivity extends SherlockActivity
{
	private TextView txtInfos;
	private TextView txtProposition;
	private EditText editReponse;
	private Button btnValider;
	private ListView listSeries;
	
	private int score = 0;
	private int currentPhraseIndex = 0;
	private List<String> listReponse;
	private List<String> listSujet;
	private Serie currentSerie;
	private SerieAdapter serieAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_conjugaison);
		
		serieAdapter = new SerieAdapter(this);
		
		listSujet = new ArrayList<String>();
		listSujet.add("Je");
		listSujet.add("Tu");
		listSujet.add("Il/Elle/On");
		listSujet.add("Nous");
		listSujet.add("Vous");
		listSujet.add("Ils/Elles");
		
		listSeries = (ListView) findViewById(R.id.conjugaison_list_series);
		listSeries.setAdapter(serieAdapter);
		listSeries.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3)
			{
				score = 0;
				currentPhraseIndex = 0;
				
				txtProposition.setVisibility(View.VISIBLE);
				editReponse.setVisibility(View.VISIBLE);
				btnValider.setVisibility(View.VISIBLE);
				
				//currentSerie = Shared.getInstance().getCurrentSubCategorie().getSerieList().get(position);
				listReponse = currentSerie.getReponses();
				txtInfos.setText("Conjugue le verbe " + currentSerie.getNom() + " au " + Shared.getInstance().getCurrentSubCategorie().getNom() + ".");
				
				serieAdapter.setCurrentIndex(position);
				serieAdapter.notifyDataSetChanged();
				
				notifyDatasetChanged();
			}
		});
		
		txtInfos = (TextView) findViewById(R.id.conjugaison_txt_infos);		
		txtProposition = (TextView) findViewById(R.id.conjugaison_txt_proposition);
		editReponse = (EditText) findViewById(R.id.conjugaison_txt_reponse);
		
		txtInfos.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/ComicRelief.ttf"));	
		txtProposition.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/ComicRelief.ttf"));
		
		btnValider = (Button) findViewById(R.id.conjugaison_btn_ok);
		btnValider.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				String currentReponse = listReponse.get(currentPhraseIndex);
				String userReponse = editReponse.getText().toString();

				// Reponse vrai //
				if(currentReponse.equals(userReponse))
				{
					score++;
					showMessageBox("Bravo !\n"+"Ton score est de "+score+"/"+(currentPhraseIndex+1));
				}
				// Reponse fausse //
				else
				{
					showMessageBox("Tu as fait une erreur, la r�ponse �tait : " + currentReponse);
				}

				currentPhraseIndex++;
				
				// Si il reste des exercices dans la serie
				if(currentPhraseIndex < listReponse.size())
				{
					notifyDatasetChanged();
					txtInfos.setText("Score : "+score+"/"+(currentPhraseIndex));
				}
				// Sinon, on a fini la serie
				else
				{
					try
					{
						AssetFileDescriptor afd = getAssets().openFd("sons/success.mp3");
						
						MediaPlayer player = new MediaPlayer();
						player.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
						player.prepare();
						player.start();
					}
					catch(IOException e)
					{
						e.printStackTrace();
					}
					
					txtInfos.setText(getString(R.string.choose_serie));
					
					txtProposition.setVisibility(View.GONE);
					editReponse.setVisibility(View.GONE);
					btnValider.setVisibility(View.GONE);
					
					// Actualise le score de la serie en cours
					int noteSerie = (int)(((double)score/currentPhraseIndex)*10);
					if(noteSerie > currentSerie.getNote()){
						currentSerie.setNote(noteSerie);
						Shared.getInstance().generatePoucentage();
						Shared.getInstance().saveStats();
					}
					
					serieAdapter.notifyDataSetChanged();
				}
			}
		});
		
		txtProposition.setVisibility(View.GONE);
		editReponse.setVisibility(View.GONE);
		btnValider.setVisibility(View.GONE);
		
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		ActionBarService.initActionBar(this, getSupportActionBar(), Consts.MODE_CONJUGAISON);
	}
	
	private void notifyDatasetChanged()
	{
		editReponse.setText("");
		txtProposition.setText(listSujet.get(currentPhraseIndex));
	}
	
	private void showMessageBox(String msg)
	{
		AlertDialog dialog = new AlertDialog.Builder(this).create();
		dialog.setMessage(msg);
		dialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.ok), new AlertDialog.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
			}
		});
		
		dialog.show();
	}
}