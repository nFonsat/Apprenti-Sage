/******************************************************************************************************************
*
* Apprenti-Sage Android
*
* AccueilActivity.java
* 
* Michael Breton - Cl�ment Bretin
* LP IEM - 2014
*
*******************************************************************************************************************/

package com.lpiem.apprentisage.ihm;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.lpiem.apprentisage.R;
import com.lpiem.apprentisage.Shared;
import com.lpiem.apprentisage.adapter.ProfilAdapter;

import com.parse.Parse;

public class AccueilActivity extends SherlockActivity{

	private Button creerProfilBtn;
	private Context context;
	private ProfilAdapter adapter;
	private ListView listView;
	private TextView txtTitre;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_accueil);

        Parse.initialize(this, "yxKdFS0RmDUbYEFCkLxOyp67TBydMBftMXh8mVrm", "oJUmo9DTjnoTXiM4wCdBnS1k13JkKkiM2q9F8pCv");

        context = this;
		
		txtTitre = (TextView) findViewById(R.id.title_txt);
		txtTitre.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Craie.ttf"));
		
		creerProfilBtn = (Button) findViewById(R.id.creer_profil_btn);
		listView = (ListView) findViewById(R.id.list);
		
		adapter = new ProfilAdapter(Shared.getInstance().getListProfils(), context);
		listView.setAdapter(adapter);
		
		creerProfilBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(context, CreerProfilActivity.class);
				startActivity(i);
			}
		});
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
				Shared.getInstance().setCurrentProfil(Shared.getInstance().getListProfils().get(position));
				
				Intent i = new Intent(context, ProfilActivity.class);
				startActivity(i);
				
				Shared.getInstance().loadStats(Shared.getInstance().getListProfils().get(position));
			}
		});
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		adapter.notifyDataSetChanged();
	}
}
