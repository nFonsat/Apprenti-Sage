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
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.lpiem.apprentisage.Consts;
import com.lpiem.apprentisage.R;
import com.lpiem.apprentisage.Shared;
import com.lpiem.apprentisage.adapter.ListeClasseAdapter;
import com.lpiem.apprentisage.adapter.ListeEnseignantAdapter;
import com.lpiem.apprentisage.adapter.ProfilAdapter;
import com.lpiem.apprentisage.database.DataBaseAccess;

public class AccueilActivity extends SherlockActivity{

	private Context context;
	private ProfilAdapter adapterEleve;
    private ListeClasseAdapter adapterClasse;
    private ListeEnseignantAdapter adapterEneignant;
	private ListView listViewEleve;
	private TextView txtTitre;
    private Spinner listeEnseignant;
    private Spinner listeClasse;


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
        listeEnseignant = (Spinner) findViewById(R.id.listeEnseignant);
        listeClasse = (Spinner) findViewById(R.id.listeClasse);

        adapterEneignant = new ListeEnseignantAdapter(dataBaseAccess.getEnseignants(), context);
        listeEnseignant.setAdapter(adapterEneignant);

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
