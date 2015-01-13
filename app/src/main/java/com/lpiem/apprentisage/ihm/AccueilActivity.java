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
import com.lpiem.apprentisage.adapter.ListeClasseAdapter;
import com.lpiem.apprentisage.adapter.ListeEleveAdapter;
import com.lpiem.apprentisage.adapter.ListeEnseignantAdapter;
import com.lpiem.apprentisage.database.DAO.EnseignantDAO;
import com.lpiem.apprentisage.metier.Classe;
import com.lpiem.apprentisage.metier.Eleve;
import com.lpiem.apprentisage.metier.Enseignant;

import java.util.ArrayList;
import java.util.List;

public class AccueilActivity extends SherlockActivity{

	private Context context;
	private ListeEleveAdapter adapterEleve;
    private ListeClasseAdapter adapterClasse;
    private ListeEnseignantAdapter adapterEneignant;
	private TextView txtTitre;
    private Spinner listeEnseignantSpinner;
    private List<Enseignant> listeEnseignant;
    private Spinner listeClasseSpinner;
    private List<Classe> listeClasse;
    private ListView listViewEleve;
    private List<Eleve> listeEleve;


    private EnseignantDAO enseignantDAO;


    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.accueil2);

        context = this;

        enseignantDAO = new EnseignantDAO(this);

		
		txtTitre = (TextView) findViewById(R.id.title_txt);
		txtTitre.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Craie.ttf"));

        listViewEleve = (ListView) findViewById(R.id.listViewEleve);
        listeEnseignantSpinner = (Spinner) findViewById(R.id.listeEnseignant);
        listeClasseSpinner = (Spinner) findViewById(R.id.listeClasse);

        listeEnseignant = new ArrayList<Enseignant>(enseignantDAO.getEnseignants());
        adapterEneignant = new ListeEnseignantAdapter(listeEnseignant, context);
        listeEnseignantSpinner.setAdapter(adapterEneignant);

        listeEnseignantSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View view, int position, long id) {

                listeClasse = new ArrayList<Classe>(listeEnseignant.get(position).getClasses());
                adapterClasse = new ListeClasseAdapter(listeClasse, context);
                listeClasseSpinner.setAdapter(adapterClasse);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }

        });

        listeClasseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                listeEleve = new ArrayList<Eleve>(listeClasse.get(position).getEleves());
                adapterEleve = new ListeEleveAdapter(listeEleve, context);
                listViewEleve.setAdapter(adapterEleve);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        listViewEleve.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {

                Eleve eleveCurrent = listeEleve.get(position);

                Intent i = new Intent(context, ProfilActivity.class);
                i.putExtra("eleveCurrent", eleveCurrent);
                startActivity(i);
            }
        });

	}

}
