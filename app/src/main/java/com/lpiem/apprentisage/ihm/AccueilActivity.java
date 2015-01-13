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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.lpiem.apprentisage.R;
import com.lpiem.apprentisage.Shared;
import com.lpiem.apprentisage.adapter.ListeClasseAdapter;
import com.lpiem.apprentisage.adapter.ListeEnseignantAdapter;
import com.lpiem.apprentisage.adapter.ProfilAdapter;
import com.lpiem.apprentisage.database.DataBaseAccess;
import com.lpiem.apprentisage.jsonObject.Classe;
import com.lpiem.apprentisage.jsonObject.Enseignant;

import java.util.ArrayList;
import java.util.List;

public class AccueilActivity extends SherlockActivity{

	private Context context;
	private ProfilAdapter adapterEleve;
    private ListeClasseAdapter adapterClasse;
    private ListeEnseignantAdapter adapterEneignant;
	private ListView listViewEleve;
	private TextView txtTitre;
    private Spinner listeEnseignantSpinner;
    private List<Enseignant> listeEnseignant;
    private Spinner listeClasseSpinner;
    private List<Classe> listeClasse;


    private DataBaseAccess dataBaseAccess;


    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.accueil2);

        context = this;

        dataBaseAccess = new DataBaseAccess(this);
		
		txtTitre = (TextView) findViewById(R.id.title_txt);
		txtTitre.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Craie.ttf"));

        listViewEleve = (ListView) findViewById(R.id.listViewEleve);
        listeEnseignantSpinner = (Spinner) findViewById(R.id.listeEnseignant);
        listeEnseignant = new ArrayList<Enseignant>(dataBaseAccess.getEnseignants());
        listeClasseSpinner = (Spinner) findViewById(R.id.listeClasse);

        adapterEneignant = new ListeEnseignantAdapter(listeEnseignant, context);
        listeEnseignantSpinner.setAdapter(adapterEneignant);

        listeEnseignantSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {


            @Override
            public void onItemSelected(AdapterView<?> arg0, View view, int position, long id) {
                // recup le prof clické
                listeClasse = new ArrayList<Classe>(listeEnseignant.get(position).getClasses());
                // lancer l'adapter pour la classe
                adapterClasse = new ListeClasseAdapter(listeClasse, context);
                listeClasseSpinner.setAdapter(adapterClasse);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }


        });

//        adapterClasse = new ListeClasseAdapter(dataBaseAccess.getClass(), context);

        adapterEleve = new ProfilAdapter(Shared.getInstance().getListProfils(), context);
        listViewEleve.setAdapter(adapterEleve);


        listViewEleve.setOnItemClickListener(new OnItemClickListener() {

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
		adapterEleve.notifyDataSetChanged();
	}
}
