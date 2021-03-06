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
import com.lpiem.apprentisage.adapter.ClasseAdapter;
import com.lpiem.apprentisage.adapter.EleveAdapter;
import com.lpiem.apprentisage.adapter.EnseignantAdapter;
import com.lpiem.apprentisage.applicatif.App;
import com.lpiem.apprentisage.database.DAO.EnseignantDAO;
import com.lpiem.apprentisage.metier.Classe;
import com.lpiem.apprentisage.metier.Eleve;
import com.lpiem.apprentisage.metier.Enseignant;

import java.util.ArrayList;
import java.util.List;

public class AccueilActivity extends SherlockActivity{
    private Context mContext;

    public App mApplication;

    private TextView txtTitre;
    private Spinner listeClasseSpinner;
    private Spinner listeEnseignantSpinner;
    private ListView listViewEleve;

    private List<Enseignant> listeEnseignant;
    private List<Classe> listeClasse;
    private List<Eleve> listeEleve;

    private EnseignantAdapter adapterEnseignant;
    private ClasseAdapter adapterClasse;
    private EleveAdapter adapterEleve;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_accueil);

        mContext = this;

        mApplication = App.getInstance();

        EnseignantDAO enseignantDAO = new EnseignantDAO(this);

		txtTitre = (TextView) findViewById(R.id.title_txt);
		txtTitre.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Craie.ttf"));

        listViewEleve = (ListView) findViewById(R.id.listViewEleve);
        listeEnseignantSpinner = (Spinner) findViewById(R.id.listeEnseignant);
        listeClasseSpinner = (Spinner) findViewById(R.id.listeClasse);

        listeEnseignantSpinner.setAdapter(null);
        listeClasseSpinner.setAdapter(null);
        listViewEleve.setAdapter(null);

        listeEnseignant = new ArrayList<>(enseignantDAO.getEnseignants());
        adapterEnseignant = new EnseignantAdapter(listeEnseignant, mContext);
        listeEnseignantSpinner.setAdapter(adapterEnseignant);


        listeEnseignantSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View view, int position, long id) {
                Enseignant enseignantSelected = listeEnseignant.get(position);
                mApplication.setCurrentEnseignant(enseignantSelected);

                listeClasse = enseignantSelected.getClasses();
                adapterClasse = new ClasseAdapter(listeClasse, mContext);
                listeClasseSpinner.setAdapter(adapterClasse);
                listViewEleve.setAdapter(null);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }

        });

        listeClasseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Classe classeSelected = listeClasse.get(position);
                mApplication.setCurrentClasse(classeSelected);

                listeEleve = classeSelected.getEleves();
                adapterEleve = new EleveAdapter(listeEleve, mContext);
                listViewEleve.setAdapter(adapterEleve);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        listViewEleve.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
                Eleve eleveSelected = listeEleve.get(position);
                mApplication.setCurrentEleve(eleveSelected);

                Intent i = new Intent(mContext, ProfilActivity.class);
                startActivity(i);
            }
        });

	}

    @Override
    protected void onResume() {
        super.onResume();
        adapterEnseignant.notifyDataSetChanged();
    }
}
