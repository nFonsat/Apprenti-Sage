/******************************************************************************************************************
*
* Apprenti-Sage Android
*
* DicteeActivity.java
* 
* Michael Breton - Clément Bretin
* LP IEM - 2014
*
*******************************************************************************************************************/

package com.lpiem.apprentisage.ihm;


import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.AssetFileDescriptor;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
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
import com.lpiem.apprentisage.R;
import com.lpiem.apprentisage.Shared;
import com.lpiem.apprentisage.adapter.SerieAdapter;
import com.lpiem.apprentisage.model.Serie;

public class DicteeActivity extends SherlockActivity{
	
	private Context context;
	
	private Button playBtn;
	private Button validerBtn;
	private EditText reponseEditTxt;
	private ListView serieListView;
	private TextView noteTxt;
	private TextView infoTxt;
	
	private String currentQuestion;
	private int nbrQuestion;
	private int nbrReponses = 0;
	private int nbrBonnesReponses = 0;
	
	private Serie currentSerie;
	
	private SerieAdapter adapter;
	
	private ArrayList<String> serieReponseList;
	
	private MediaPlayer player;
	
	private AssetFileDescriptor afd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		context = this;
		
		setContentView(R.layout.activity_dictee);
		
		serieReponseList = new ArrayList<String>();
		player = new MediaPlayer();
		afd = null;
		
		playBtn = (Button) findViewById(R.id.play_btn);
		serieListView = (ListView) findViewById(R.id.list);
		validerBtn = (Button) findViewById(R.id.valider_btn);
		reponseEditTxt = (EditText) findViewById(R.id.reponse_edittxt);
		noteTxt = (TextView) findViewById(R.id.note_txt);
		infoTxt = (TextView) findViewById(R.id.dictee_txt_infos);
		
		playBtn.setVisibility(View.GONE);
		validerBtn.setVisibility(View.GONE);
		reponseEditTxt.setVisibility(View.GONE);
		noteTxt.setVisibility(View.GONE);
		
		noteTxt.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/ComicRelief.ttf"));
		
		adapter = new SerieAdapter((Activity) context);
		serieListView.setAdapter(adapter);
		
		validerBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(!reponseEditTxt.getText().toString().equals("") && reponseEditTxt.getText() != null){
					
					nbrReponses++;
					
					if(reponseEditTxt.getText().toString().toLowerCase().equals(currentQuestion.toLowerCase())){
						
						nbrBonnesReponses++;
						showMessageBox("Bravo! Ton score est de " + nbrBonnesReponses + "/" + nbrReponses);
					
					}else{
						
						showMessageBox( "Tu as trouvé " + reponseEditTxt.getText().toString() + " alors que la réponse était " + currentQuestion + " Ton score est de " + nbrBonnesReponses + "/" + nbrReponses);
					}
					
					noteTxt.setText("Score : " + nbrBonnesReponses + "/" + nbrReponses);
					
					if(nbrReponses <= nbrQuestion - 1){
						currentQuestion = serieReponseList.get(nbrReponses);
						try {
							afd = context.getAssets().openFd("sons/" + currentQuestion +".ogg");
							player.reset();
							player.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
							player.prepare();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					
					if(nbrReponses >= nbrQuestion){
						try
						{
							afd = context.getAssets().openFd("sons/success.mp3");
							MediaPlayer mPlayer = new MediaPlayer();
							mPlayer.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
							mPlayer.prepare();
							mPlayer.start();
						}
						catch(IOException e)
						{
							e.printStackTrace();
						}
						
						
						int noteSerie = (int)(((double)nbrBonnesReponses/nbrReponses)*10);
						if(noteSerie > currentSerie.getNote()){
							currentSerie.setNote(noteSerie);
							Shared.getInstance().generatePoucentage();
							Shared.getInstance().saveStats();
						}
							
						adapter.notifyDataSetChanged();
						resetSerie();
						disableView();

					}
					
					reponseEditTxt.setText("");
					
				}
			}
		});
		
		serieListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
				player.reset();
				infoTxt.setVisibility(View.GONE);
				enableView();
				noteTxt.setText("");
				nbrReponses = 0;
				nbrBonnesReponses = 0;
				reponseEditTxt.setText("");
				currentSerie = Shared.getInstance().getCurrentSubCategorie().getSerieList().get(position);
				serieReponseList = (ArrayList<String>) currentSerie.getReponses();
				currentQuestion = serieReponseList.get(0);
				nbrQuestion = serieReponseList.size();
				
				adapter.setCurrentIndex(position);
				adapter.notifyDataSetChanged();
				
				try {
					afd = getAssets().openFd("sons/" + currentQuestion +".ogg");
					player.reset();
					player.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
					player.prepare();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		playBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				player.start();

			}
		});

		player.setOnCompletionListener(new OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer mp) {

			}
		});
		
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		ActionBarService.initActionBar(this, getSupportActionBar(), "Dictée");
	}
	
	private void resetSerie(){
		currentQuestion = serieReponseList.get(0);
		
		try {
			afd = getAssets().openFd("sons/" + currentQuestion +".ogg");
			player.reset();
			player.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
			player.prepare();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void disableView(){
		playBtn.setVisibility(View.GONE);
		validerBtn.setVisibility(View.GONE);
		reponseEditTxt.setVisibility(View.GONE);
		noteTxt.setVisibility(View.GONE);
	}
	
	private void enableView(){
		playBtn.setVisibility(View.VISIBLE);
		validerBtn.setVisibility(View.VISIBLE);
		reponseEditTxt.setVisibility(View.VISIBLE);
		noteTxt.setVisibility(View.VISIBLE);
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
