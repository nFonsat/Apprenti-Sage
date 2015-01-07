/******************************************************************************************************************
*
* Apprenti-Sage Android
*
* CreerProfilActivity.java
* 
* Michael Breton - Clément Bretin
* LP IEM - 2014
*
*******************************************************************************************************************/

package com.lpiem.apprentisage.ihm;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.lpiem.apprentisage.ActionBarService;
import com.lpiem.apprentisage.Consts;
import com.lpiem.apprentisage.R;
import com.lpiem.apprentisage.Shared;
import com.lpiem.apprentisage.model.Profil;

public class CreerProfilActivity extends SherlockActivity{
	
	private Context context;
	
	private Button validerBtn;
	private EditText nomEditTxt;
	private EditText prenomEditTxt;
	private Spinner classeSpinner;
	private ImageView avatarView;
	private LinearLayout layoutAvatar;
	private ImageView avatar1;
	private ImageView avatar2;
	private ImageView avatar3;
	
	private TextView avatarTxt;
	private TextView nomTxt;
	private TextView prenomTxt;
	private TextView classeTxt;
	
	private String classe = "CP";
	
	private int currentAvatar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_creer_profil);
		
		context = this;
		
		validerBtn = (Button) findViewById(R.id.valider_btn);
		nomEditTxt = (EditText) findViewById(R.id.nom_edittxt);
		prenomEditTxt = (EditText) findViewById(R.id.prenom_edittxt);
		classeSpinner = (Spinner) findViewById(R.id.classe_spinner);
		avatarView = (ImageView) findViewById(R.id.avatar_view);
		layoutAvatar = (LinearLayout) findViewById(R.id.avatar_layout);
		layoutAvatar.setVisibility(View.GONE);
		avatar1 = (ImageView) findViewById(R.id.avatar_view_1);
		avatar2 = (ImageView) findViewById(R.id.avatar_view_2);
		avatar3 = (ImageView) findViewById(R.id.avatar_view_3);
		
		avatarTxt = (TextView) findViewById(R.id.avatar_txt);
		nomTxt = (TextView) findViewById(R.id.nom_txt);
		prenomTxt = (TextView) findViewById(R.id.prenom_txt);
		classeTxt = (TextView) findViewById(R.id.classe_txt);
		
		avatarTxt.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/ComicRelief.ttf"));
		nomTxt.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/ComicRelief.ttf"));
		prenomTxt.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/ComicRelief.ttf"));
		classeTxt.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/ComicRelief.ttf"));
		
		List<String> listClasse = new ArrayList<String>();
		listClasse.add("CP");
		listClasse.add("CE1");
		
		ArrayAdapter<String> dataAdapterClasse = new ArrayAdapter<String> (this, R.layout.item_spinner, listClasse);
		
		dataAdapterClasse.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		classeSpinner.setAdapter(dataAdapterClasse);
		classeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
				
				if(pos == 0){
					classe = Consts.CLASSE_CP;
				}else{
					classe = Consts.CLASSE_CE1;
				}	
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
		});
		
		validerBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!nomEditTxt.getText().toString().equals("") && nomEditTxt.getText() != null
				   && !prenomEditTxt.getText().toString().equals("") && prenomEditTxt.getText() != null){
					
					Profil profil = new Profil(nomEditTxt.getText().toString(), prenomEditTxt.getText().toString(), classe, currentAvatar);
					Shared.getInstance().getListProfils().add(profil);
					Shared.getInstance().setCurrentProfil(profil);
					
					Intent i = new Intent(context, ProfilActivity.class);
					startActivity(i);
					finish();
					
					Shared.getInstance().saveProfilList();
					
					Shared.getInstance().loadStats(Shared.getInstance().getListProfils().get(Shared.getInstance().getListProfils().size() - 1));
				}
			}
		});
		
		avatarView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				layoutAvatar.setVisibility(View.VISIBLE);
				
			}
		});
		
		avatar1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				layoutAvatar.setVisibility(View.GONE);
				currentAvatar = 0;
				avatarView.setImageResource(R.drawable.avatar_1);
				
			}
		});
		avatar2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				layoutAvatar.setVisibility(View.GONE);
				currentAvatar = 1;
				avatarView.setImageResource(R.drawable.avatar_2);
				
			}
		});
		avatar3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				layoutAvatar.setVisibility(View.GONE);
				currentAvatar = 2;
				avatarView.setImageResource(R.drawable.avatar_3);
				
			}
		});
		
		ActionBarService.initActionBar(this, this.getSupportActionBar(), getString(R.string.new_profil_titre));
	}
}