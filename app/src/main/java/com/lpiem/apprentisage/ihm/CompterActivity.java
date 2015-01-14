/******************************************************************************************************************
*
* Apprenti-Sage Android
*
* CompterActivity.java
* 
* Michael Breton - Clement Bretin
* LP IEM - 2014
*
*******************************************************************************************************************/

package com.lpiem.apprentisage.ihm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
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
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.lpiem.apprentisage.ActionBarService;
import com.lpiem.apprentisage.R;
import com.lpiem.apprentisage.Shared;
import com.lpiem.apprentisage.UIService;
import com.lpiem.apprentisage.adapter.SerieAdapter;
import com.lpiem.apprentisage.model.Serie;

public class CompterActivity extends SherlockActivity{
	
	private Context context;
	
	private ListView serieListView;
	private RelativeLayout gameLayout;
	private LinearLayout countLineLayout;
	private Button validerBtn;
	private EditText reponseEditTxt;
	private TextView noteTxt;
	private TextView infoTxt;
	
	private SerieAdapter adapter;
	
	private Serie currentSerie;
	
	private int maxJetons;
	private int currentIndexJeton = 0;
	private int nbrJetons;
	private int nbrReponse = 0;
	private int nbrBonneReponse = 0;
	
	private ArrayList<FrameLayout> lineJetons;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		context = this;
		
		lineJetons = new ArrayList<FrameLayout>();
		
		setContentView(R.layout.activity_compter);
		
		serieListView = (ListView) findViewById(R.id.list);
		gameLayout = (RelativeLayout) findViewById(R.id.game_area);
		countLineLayout = (LinearLayout) findViewById(R.id.line_count_layout);
		validerBtn = (Button) findViewById(R.id.valider_btn);
		reponseEditTxt = (EditText) findViewById(R.id.reponse_edittxt);
		noteTxt = (TextView) findViewById(R.id.note_txt);
		infoTxt = (TextView) findViewById(R.id.compter_txt_infos);
		
		validerBtn.setVisibility(View.GONE);
		reponseEditTxt.setVisibility(View.GONE);
		
		noteTxt.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/ComicRelief.ttf"));
		
		validerBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (!reponseEditTxt.getText().toString().equals("") && reponseEditTxt.getText() != null){
					 
					nbrReponse++;
					
					if(reponseEditTxt.getText().toString().equals(String.valueOf(nbrJetons))){
						nbrBonneReponse++;
						showMessageBox("Bravo! Ton score est de " + nbrBonneReponse + "/" + nbrReponse);
					}else{
						showMessageBox( "Tu as trouv� " + reponseEditTxt.getText().toString() + " alors qu'il y avait " + nbrJetons + " jetons. Ton score est de " + nbrBonneReponse + "/" + nbrReponse);
					}
					
					countLineLayout.removeAllViews();
					reponseEditTxt.setText("");
					lineJetons.clear();
					currentIndexJeton = 0;
					addCountLine();
					
					initGameView();
					noteTxt.setText("Score : " + nbrBonneReponse + "/" + nbrReponse);
					
					if(nbrReponse >= 10){
						
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
						
						if(nbrBonneReponse > currentSerie.getNote()){
							currentSerie.setNote(nbrBonneReponse);
							Shared.getInstance().generatePoucentage();
							Shared.getInstance().saveStats();
							adapter.notifyDataSetChanged();
						}
						resetGame();
					}
					
				}
			}
		});
		
		adapter = new SerieAdapter((Activity) context);
		serieListView.setAdapter(adapter);
		
		//maxJetons = Integer.valueOf(Shared.getInstance().getCurrentSubCategorie().getSerieList().get(0).getReponses().get(0));
		
		serieListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
				validerBtn.setVisibility(View.VISIBLE);
				reponseEditTxt.setVisibility(View.VISIBLE);
				infoTxt.setVisibility(View.GONE);
				
				//currentSerie = Shared.getInstance().getCurrentSubCategorie().getSerieList().get(position);
				//maxJetons = Integer.valueOf(Shared.getInstance().getCurrentSubCategorie().getSerieList().get(position).getReponses().get(0));
				
				adapter.setCurrentIndex(position);
				adapter.notifyDataSetChanged();
				
				resetGame();
			}
		});
		
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		ActionBarService.initActionBar(this, getSupportActionBar(), getString(R.string.compter_titre));
	}
	
	private void resetGame(){
		noteTxt.setText("");
		reponseEditTxt.setText("");
		lineJetons.clear();
		countLineLayout.removeAllViews();
		addCountLine();
		currentIndexJeton = 0;
		nbrReponse = 0;
		nbrBonneReponse = 0;
		initGameView();
	}
	
	private void initGameView(){
		gameLayout.removeAllViews();
		
		int x = gameLayout.getWidth();
		int y = gameLayout.getHeight();
		
		Random randomJetons = new Random();
		nbrJetons = randomJetons.nextInt(maxJetons) + 1;
		
		for (int i = 0; i < nbrJetons; i++){
			
			FrameLayout button = new FrameLayout(this);
			button.setBackgroundResource(R.drawable.pomme);
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(UIService.getPx(this, 40),UIService.getPx(this, 40));
			Random random = new Random(); 
			
			int marginTop = random.nextInt(y - UIService.getPx(this, 45)) + UIService.getPx(this, 5);
			int marginLeft = random.nextInt(x - UIService.getPx(this, 45)) + UIService.getPx(this, 5);
			params.setMargins(marginLeft, marginTop, 0, 0);
			
			button.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					v.setVisibility(View.INVISIBLE);
					v.setClickable(false);
					
					lineJetons.get(currentIndexJeton).setBackgroundResource(R.drawable.pomme);
					currentIndexJeton ++;
					
					if(currentIndexJeton % 10 == 0){
						addCountLine();
					}
				}
			});
			
			gameLayout.addView(button, params);
		}
	}
	
	private void addCountLine(){
		
		LinearLayout lineLayout = new LinearLayout(context);
		LinearLayout.LayoutParams lineParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		lineParams.setMargins(UIService.getPx(this, 10), UIService.getPx(this, 5), 0, 0);
		lineLayout.setOrientation(LinearLayout.HORIZONTAL);
		
		for(int i = 0; i < 10; i ++){
			FrameLayout frameUnit = new FrameLayout(this);
			LinearLayout.LayoutParams unitParams = new LinearLayout.LayoutParams(UIService.getPx(this, 30), UIService.getPx(this, 30));
			lineLayout.addView(frameUnit, unitParams);
			
			lineJetons.add(frameUnit);
		}
		
		countLineLayout.addView(lineLayout, lineParams);
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
