/******************************************************************************************************************
*
* Apprenti-Sage Android
*
* LectureActivity.java
* 
* Michael Breton - Cl�ment Bretin
* LP IEM - 2014
*
*******************************************************************************************************************/

package com.lpiem.apprentisage.ihm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.AssetFileDescriptor;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.lpiem.apprentisage.ActionBarService;
import com.lpiem.apprentisage.Consts;
import com.lpiem.apprentisage.R;
import com.lpiem.apprentisage.Shared;
import com.lpiem.apprentisage.adapter.SerieAdapter;
import com.lpiem.apprentisage.model.Serie;

public class LectureActivity extends SherlockActivity
{
	private ListView serieList;
	private TextView txtInfos;
	private Button btnPlay;
	
	private LinearLayout layoutProposition;
	private List<String> listProposition;
	private List<String> listReponse;
	
	private int score = 0;
	private int currentPhraseIndex = 0;
	private Serie currentSerie;
	private SerieAdapter serieAdapter;
	private MediaPlayer player;
	private AssetFileDescriptor afd = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lecture);
		
		listProposition = new ArrayList<String>();
		listReponse = new ArrayList<String>();
		
		// Init serie list
		serieAdapter = new SerieAdapter(this);
		serieList = (ListView) findViewById(R.id.lecture_list_series);
		serieList.setAdapter(serieAdapter);
		serieList.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3)
			{
				score = 0;
				currentPhraseIndex = 0;
				txtInfos.setText("");
				btnPlay.setVisibility(View.VISIBLE);
				
				//currentSerie = Shared.getInstance().getCurrentSubCategorie().getSerieList().get(position);
				listProposition = new ArrayList<String>(currentSerie.getReponses());
				listReponse = new ArrayList<String>(listProposition);

				Collections.shuffle(listProposition);
				Collections.shuffle(listReponse);
				
				serieAdapter.setCurrentIndex(position);
				serieAdapter.notifyDataSetChanged();
				
				initExercice();
			}
		});
		
		player = new MediaPlayer();
		txtInfos = (TextView) findViewById(R.id.lecture_txt_infos);
		
		txtInfos.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/ComicRelief.ttf"));
		
		// Init Player
		btnPlay = (Button) findViewById(R.id.lecture_btn_player);
		btnPlay.setVisibility(View.GONE);
		btnPlay.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{

				player.start();

			}
		});
		
		player = new MediaPlayer();
		player.setOnCompletionListener(new OnCompletionListener()
		{
			@Override
			public void onCompletion(MediaPlayer mp)
			{
				
			}
		});
		
		layoutProposition = (LinearLayout) findViewById(R.id.lecture_layout_proposition);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		
		ActionBarService.initActionBar(this, getSupportActionBar(), Consts.MODE_LECTURE);
	}
	
	private void notifyDatasetChanged()
	{
		layoutProposition.removeAllViews();
		
		if(listReponse.size() == 0)
			return;
		
		loadSound(listReponse.get(currentPhraseIndex));
		
		for(final String str : listProposition)
		{
			Button btn = new Button(this);
			btn.setBackgroundResource(R.drawable.button_selector);
			btn.setTypeface(Typeface.DEFAULT_BOLD);
			btn.setText(str);
			btn.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					// Reponse Vrai
					if(str.equals(listReponse.get(currentPhraseIndex)))
					{
						score++;
						showMessageBox("Bravo !\n"+"Ton score est de "+score+"/"+(currentPhraseIndex+1));
					}
					// Reponse Fausse
					else
					{
						showMessageBox("Tu as fait une erreur, la r�ponse �tait : "+listReponse.get(currentPhraseIndex));
					}

					currentPhraseIndex++;
					
					// Si il reste des exercices dans la serie
					if(currentPhraseIndex < listReponse.size())
					{
						initExercice();
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
						
						listReponse.clear();
						listProposition.clear();
						layoutProposition.removeAllViews();
						btnPlay.setVisibility(View.GONE);
						
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
			
			layoutProposition.addView(btn, new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT,1));
		}
	}
	
	private void initExercice()
	{
		Collections.shuffle(listProposition);
		notifyDatasetChanged();
	}
	
	private void loadSound(String fileName)
	{
		try
		{
			afd = getAssets().openFd("sons/"+fileName+".ogg");
			player.reset();
			player.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
			player.prepare();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
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