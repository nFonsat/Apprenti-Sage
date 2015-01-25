/******************************************************************************************************************
*
* Apprenti-Sage Android
*
* ProfilActivity.java
* 
* Michael Breton - Cl�ment Bretin
* LP IEM - 2014
*
*******************************************************************************************************************/

package com.lpiem.apprentisage.ihm;

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
import com.lpiem.apprentisage.R;
import com.lpiem.apprentisage.UIService;
import com.lpiem.apprentisage.adapter.StatsAdapter;
import com.lpiem.apprentisage.applicatif.App;
import com.lpiem.apprentisage.applicatif.ResultatApp;
import com.lpiem.apprentisage.metier.Classe;
import com.lpiem.apprentisage.metier.Eleve;
import com.lpiem.apprentisage.model.Categorie;

public class ProfilActivity extends SherlockActivity{

    private App mApplication;
    private ResultatApp mResultatApp;

	private TextView nomTxt;
	private TextView prenomTxt;
    private TextView classeTxt;

	private TextView pourcentageTxt;
	private ImageView avatarView;
	private LinearLayout categoriesLayout;

    private Eleve eleve ;
    private Classe classe;
	
	private ListView listStats;
	
	private StatsAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        mApplication = App.getInstance();
        mResultatApp = ResultatApp.getInstance();

        eleve = mApplication.getCurrentEleve();
        classe = mApplication.getCurrentClasse();

		listStats = (ListView) findViewById(R.id.profil_list_stats);
		adapter = new StatsAdapter(this);
		listStats.setAdapter(adapter);

		nomTxt = (TextView) findViewById(R.id.nom_txt);
		prenomTxt = (TextView) findViewById(R.id.prenom_txt);
        classeTxt = (TextView) findViewById(R.id.classe_txt);

		pourcentageTxt = (TextView) findViewById(R.id.pourcentage_txt);
		avatarView = (ImageView) findViewById(R.id.avatar_view);
		categoriesLayout = (LinearLayout) findViewById(R.id.categorie_layout);
		
		nomTxt.setText("Nom: " + eleve.getNom());
		prenomTxt.setText("Prenom: " + eleve.getPrenom());
        classeTxt.setText("Classe: " + classe.getNom());
		
		nomTxt.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/ComicRelief.ttf"));
		prenomTxt.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/ComicRelief.ttf"));
        classeTxt.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/ComicRelief.ttf"));
		pourcentageTxt.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/ComicRelief.ttf"));

        avatarView.setImageResource(R.drawable.avatar_3);

		initMatiereList();
		
		ActionBarService.initActionBar(this, this.getSupportActionBar(), getString(R.string.profil_titre));
	}

    public void backInHomePage(View view){
        mApplication.setCurrentEleve(null);
        finish();
    }
	
	@Override
	protected void onResume() {
		super.onResume();
        mResultatApp.calculateResultMatieres(this);
		adapter.notifyDataSetChanged();
	}
	
	private void initMatiereList()
	{
		for(final Categorie matiere : mApplication.getCurrentMatieres(this))
		{
			View view = getLayoutInflater().inflate(R.layout.categorie_item,null);
			
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,0,1);
			params.setMargins(0, 0, 0, UIService.getPx(this, 5));
			view.setLayoutParams(params);
			
			TextView txtNom = (TextView) view.findViewById(R.id.categorie_item_txt_nom);
			txtNom.setBackgroundColor(matiere.getColor());
			txtNom.setText(matiere.getNom());
			
			Button btnCat = (Button) view.findViewById(R.id.categorie_item_btn_cat);
			btnCat.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					mApplication.setCurrentMatiere(matiere);
                    Intent intent = new Intent(ProfilActivity.this, SousCategorieActivity.class);
                    startActivity(intent);
				}
			});
			
			categoriesLayout.addView(view);
		}
	}
}
