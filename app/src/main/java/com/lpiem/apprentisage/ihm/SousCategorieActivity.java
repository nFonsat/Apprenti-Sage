/******************************************************************************************************************
*
* Apprenti-Sage Android
*
* SousCategorieActivity.java
* 
* Michael Breton - Cl�ment Bretin
* LP IEM - 2014
*
*******************************************************************************************************************/

package com.lpiem.apprentisage.ihm;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.lpiem.apprentisage.ActionBarService;
import com.lpiem.apprentisage.R;
import com.lpiem.apprentisage.adapter.SubCatAdapter;
import com.lpiem.apprentisage.data.App;
import com.lpiem.apprentisage.model.Categorie;

import java.util.ArrayList;
import java.util.List;

public class SousCategorieActivity extends SherlockActivity
{
	private ListView listCategorie;
    private Button btnRetour;
    private TextView titreMatiere;
    private List<Categorie> listCat;

    private App mApplication;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sub_categorie_list);

        btnRetour = (Button)findViewById(R.id.retour);
        titreMatiere = (TextView)findViewById(R.id.titreMatiere);
		listCategorie = (ListView) findViewById(R.id.sub_categorie_list);

        listCat = new ArrayList<>();

        mApplication = App.getInstance();
        titreMatiere.setText(mApplication.getCurrentMatiere().getNom());
        titreMatiere.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/Craie.ttf"));
        listCat = mApplication.getCurrentMatiere().getSubCategorie();

		listCategorie.setAdapter(new SubCatAdapter(this));

        btnRetour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                mApplication.setCurrentMatiere(null);
            }
        });

        listCategorie.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mApplication.setCurrentActivite(listCat.get(position));
                Intent i = new Intent(SousCategorieActivity.this, SerieActivity.class);
                startActivity(i);
            }
        });

		ActionBarService.initActionBar(this, getSupportActionBar(), getString(R.string.cat_titre));
	}
}