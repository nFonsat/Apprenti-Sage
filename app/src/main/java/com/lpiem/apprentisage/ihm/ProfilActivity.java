/******************************************************************************************************************
*
* Apprenti-Sage Android
*
* ProfilActivity.java
* 
* Michael Breton - Clément Bretin
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
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.lpiem.apprentisage.ActionBarService;
import com.lpiem.apprentisage.Consts;
import com.lpiem.apprentisage.R;
import com.lpiem.apprentisage.Shared;
import com.lpiem.apprentisage.UIService;
import com.lpiem.apprentisage.adapter.StatsAdapter;
import com.lpiem.apprentisage.model.Categorie;

public class ProfilActivity extends SherlockActivity{

	private TextView nomTxt;
	private TextView prenomTxt;
	private TextView classeTxt;
	private TextView pourcentageTxt;
	private ImageView avatarView;
	private LinearLayout categoriesLayout;
	
	private ListView listStats;
	
	private StatsAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_profil);

		listStats = (ListView) findViewById(R.id.profil_list_stats);
		adapter = new StatsAdapter(this);
		listStats.setAdapter(adapter);

		nomTxt = (TextView) findViewById(R.id.nom_txt);
		prenomTxt = (TextView) findViewById(R.id.prenom_txt);
		classeTxt = (TextView) findViewById(R.id.classe_txt);
		pourcentageTxt = (TextView) findViewById(R.id.pourcentage_txt);
		avatarView = (ImageView) findViewById(R.id.avatar_view);
		categoriesLayout = (LinearLayout) findViewById(R.id.categorie_layout);
		
		nomTxt.setText("Nom: " + Shared.getInstance().getCurrentProfil().getNom());
		prenomTxt.setText("Prénom: " + Shared.getInstance().getCurrentProfil().getPrenom());
		classeTxt.setText("Classe: " + Shared.getInstance().getCurrentProfil().getClasse());
		
		nomTxt.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/ComicRelief.ttf"));
		prenomTxt.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/ComicRelief.ttf"));
		classeTxt.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/ComicRelief.ttf"));
		pourcentageTxt.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/ComicRelief.ttf"));
		
		switch(Shared.getInstance().getCurrentProfil().getAvatar()){
			case 0:
				avatarView.setImageResource(R.drawable.avatar_1);
				break;
			case 1:
				avatarView.setImageResource(R.drawable.avatar_2);
				break;
			case 2:
				avatarView.setImageResource(R.drawable.avatar_3);
				break;
		}
		
		if(Shared.getInstance().getCurrentProfil().getClasse().equals(Consts.CLASSE_CP)){
			Shared.getInstance().setCurrentCategorieList(Shared.getInstance().getCategorieListCp());
		}else if (Shared.getInstance().getCurrentProfil().getClasse().equals(Consts.CLASSE_CE1)){
			Shared.getInstance().setCurrentCategorieList(Shared.getInstance().getCategorieListCe1());
		}
		
		initCategorieList();
		
		ActionBarService.initActionBar(this, this.getSupportActionBar(), getString(R.string.profil_titre));
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		adapter.notifyDataSetChanged();
	}
	
	private void initCategorieList()
	{
		for(final Categorie categorie : Shared.getInstance().getCurrentCategorieList())
		{
			View view = getLayoutInflater().inflate(R.layout.categorie_item,null);
			
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,0,1);
			params.setMargins(0, 0, 0, UIService.getPx(this, 5));
			view.setLayoutParams(params);
			
			TextView txtNom = (TextView) view.findViewById(R.id.categorie_item_txt_nom);
			txtNom.setBackgroundColor(categorie.getColor());
			txtNom.setText(categorie.getNom());
			
			Button btnCat = (Button) view.findViewById(R.id.categorie_item_btn_cat);
			btnCat.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					Shared.getInstance().setCurrentCategorie(categorie);
					Intent intent = new Intent(ProfilActivity.this,SousCategorieActivity.class);
					startActivity(intent);
				}
			});
			
			categoriesLayout.addView(view);
		}
	}
}
