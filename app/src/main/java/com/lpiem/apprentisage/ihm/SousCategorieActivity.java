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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.lpiem.apprentisage.R;
import com.lpiem.apprentisage.adapter.SubCatAdapter;
import com.lpiem.apprentisage.applicatif.App;
import com.lpiem.apprentisage.metier.Categorie;

import java.util.ArrayList;
import java.util.List;

public class SousCategorieActivity extends SherlockActivity
{
    private List<Categorie> listCat;

    private App mApplication;
    private SubCatAdapter mSubCatAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sub_categorie_list);
        TextView titreMatiere = (TextView)findViewById(R.id.titreMatiere);
        ListView listCategorie = (ListView) findViewById(R.id.sub_categorie_list);

        listCat = new ArrayList<>();

        mApplication = App.getInstance();

        titreMatiere.setText(mApplication.getCurrentMatiere().getNom());
        titreMatiere.setTypeface(Typeface.createFromAsset(this.getAssets(), "fonts/Craie.ttf"));
        listCat = mApplication.getCurrentMatiere().getSubCategorie();

        mSubCatAdapter = new SubCatAdapter(this);
		listCategorie.setAdapter(mSubCatAdapter);

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

    public void backToProfile(View view){
        mApplication.setCurrentMatiere(null);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSubCatAdapter.notifyDataSetChanged();
    }
}