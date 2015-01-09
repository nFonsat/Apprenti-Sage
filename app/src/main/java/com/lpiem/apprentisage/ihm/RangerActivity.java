/******************************************************************************************************************
*
* Apprenti-Sage Android
*
* RangerActivity.java
* 
* Michael Breton - Clément Bretin
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
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.lpiem.apprentisage.ActionBarService;
import com.lpiem.apprentisage.Consts;
import com.lpiem.apprentisage.R;
import com.lpiem.apprentisage.Shared;
import com.lpiem.apprentisage.UIService;
import com.lpiem.apprentisage.adapter.SerieAdapter;
import com.lpiem.apprentisage.model.Serie;

public class RangerActivity extends SherlockActivity
{
	private ListView listSeries;
	private Button btnValider;
	private LinearLayout layoutReponse;
	private LinearLayout layoutProposition;
	private TextView txtInfos;
	private SerieAdapter serieAdapter;
	
	private int score = 0;
	private int currentPhraseIndex = 0;
	private Serie currentSerie;
	private List<String> listExercice;
	private List<String> listReponse;
	private List<String> listProposition;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ranger);
		
		serieAdapter = new SerieAdapter(this);
		
		layoutReponse = (LinearLayout) findViewById(R.id.ranger_layout_reponse);
		layoutProposition = (LinearLayout) findViewById(R.id.ranger_layout_proposition);
		
		listSeries = (ListView) findViewById(R.id.ranger_list_series);
		listSeries.setAdapter(serieAdapter);
		listSeries.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3)
			{
				score = 0;
				currentPhraseIndex = 0;
				txtInfos.setText("");
				
				btnValider.setVisibility(View.VISIBLE);
				
				currentSerie = Shared.getInstance().getCurrentSubCategorie().getSerieList().get(position);
				
				if(currentSerie.getReponses().size() == 1)
				{
					try
					{
						listExercice = createNumberExercice(Integer.valueOf(currentSerie.getReponses().get(0)));
					}
					catch(Exception e)
					{
						e.printStackTrace();
						listExercice = new ArrayList<String>();
					}
				}
				else
				{
					listExercice = currentSerie.getReponses();
				}
				
				serieAdapter.setCurrentIndex(position);
				serieAdapter.notifyDataSetChanged();
				
				Collections.shuffle(listExercice);
				initExercice();
			}
		});
		
		btnValider = (Button) findViewById(R.id.ranger_btn_valider);
		btnValider.setVisibility(View.GONE);
		btnValider.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if(listExercice.isEmpty())
					return;
				
				if(listProposition.isEmpty())
				{
					String reponse = listToString(listReponse);

					// Reponse vrai //
					if(reponse.equals(listExercice.get(currentPhraseIndex)))
					{
						score++;
						showMessageBox("Bravo !\n"+"Ton score est de "+score+"/"+(currentPhraseIndex+1));
					}
					// Reponse fausse //
					else
					{
						showMessageBox("Tu as fait une erreur, la réponse était : "+listExercice.get(currentPhraseIndex));
					}

					currentPhraseIndex++;
					
					// Si il reste des exercices dans la serie
					if(currentPhraseIndex < listExercice.size())
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
						
						layoutReponse.removeAllViews();
						layoutProposition.removeAllViews();
						
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
				// Faux : Tout les mots ne sont pas en place
				else
				{
					txtInfos.setText(getString(R.string.ranger_warning_valider));
				}
			}
		});
		
		txtInfos = (TextView) findViewById(R.id.ranger_txt_infos);
		txtInfos.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/ComicRelief.ttf"));

		listReponse = new ArrayList<String>();
		listExercice = new ArrayList<String>();
		listProposition = new ArrayList<String>();
		
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		ActionBarService.initActionBar(this, getSupportActionBar(), Shared.getInstance().getCurrentSubCategorie().getNom());
	}
	
	private void initExercice()
	{
		String[] propArray = listExercice.get(currentPhraseIndex).split(" ");
		
		listReponse.clear();
		listProposition.clear();
		
		for(int i = 0; i < propArray.length; i++)
		{
			listProposition.add(propArray[i]);
			listReponse.add(Consts.CASE_VIDE);
		}
		
		Collections.shuffle(listProposition);
		notifyDatasetChanged();
	}
	
	private void notifyDatasetChanged()
	{
		int margin = UIService.getPx(this, 5);
		int padding = UIService.getPx(this, 20);
		int caseHeight = UIService.getPx(this, 100);
		
		layoutReponse.removeAllViews();
		layoutProposition.removeAllViews();

		for(int i = 0; i < listProposition.size(); i++)
		{
			TextView childProposition = new TextView(this);

			childProposition.setText(listProposition.get(i));
			childProposition.setBackgroundResource(R.drawable.ranger_shape);
			childProposition.setPadding(padding,padding,padding,padding);
			childProposition.setGravity(Gravity.CENTER);
			childProposition.setTextSize(25);
			childProposition.setTypeface(Typeface.DEFAULT_BOLD);
			
			LayoutParams paramProposition = new LayoutParams(LayoutParams.WRAP_CONTENT,caseHeight);
			paramProposition.setMargins(margin, margin, margin, margin);
			
			final int index = i;
			childProposition.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					int i = listReponse.indexOf(Consts.CASE_VIDE);
					
					if(i != -1)
						listReponse.set(i, listProposition.get(index));

					listProposition.remove(index);
					notifyDatasetChanged();
				}
			});
			
			layoutProposition.addView(childProposition, paramProposition);
		}
		
		for(int i = 0; i < listReponse.size(); i++)
		{
			TextView childReponse = new TextView(this);
			childReponse.setPadding(padding,padding,padding,padding);
			childReponse.setGravity(Gravity.CENTER);
			childReponse.setTextSize(25);
			childReponse.setTypeface(Typeface.DEFAULT_BOLD);
			
			if(listReponse.get(i).equals(Consts.CASE_VIDE))
			{
				childReponse.setText("     ");
				childReponse.setBackgroundResource(R.drawable.ranger_empty_shape);
			}
			else
			{
				childReponse.setText(listReponse.get(i));
				childReponse.setBackgroundResource(R.drawable.ranger_shape);
			}

			LayoutParams paramReponse = new LayoutParams(LayoutParams.WRAP_CONTENT,caseHeight);
			paramReponse.setMargins(margin, margin, margin, margin);
			
			final int index = i;
			childReponse.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					if(!listReponse.get(index).equals(Consts.CASE_VIDE))
					{
						listProposition.add(listReponse.get(index));
						listReponse.set(index,Consts.CASE_VIDE);
						notifyDatasetChanged();
					}
				}
			});

			layoutReponse.addView(childReponse, paramReponse);
		}
	}
	
	private String listToString(List<String> list)
	{
		StringBuilder builder = new StringBuilder();
		
		for(String str : list)
		{
			builder.append(str+" ");
		}
		
		return builder.toString().trim();
	}
	
	private String IntListToString(List<Integer> list)
	{
		StringBuilder builder = new StringBuilder();
		
		for(Integer str : list)
		{
			builder.append(str+" ");
		}
		
		return builder.toString().trim();
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
	
	private List<String> createNumberExercice(int max)
	{
		List<String> reponse = new ArrayList<String>();
		List<Integer> temp = new ArrayList<Integer>();
		
		for(int i = 0; i < 10; i++)
		{
			temp.clear();
			
			for(int j = 0; j < 6; j++)
			{
				int random = (int)(Math.random() * (max+1));
				while(temp.contains(random))
				{
					random = (int)(Math.random() * (max+1));
				}
				
				temp.add(random);
			}
			
			Collections.sort(temp);
			reponse.add(IntListToString(temp));
		}

		return reponse;
	}
}