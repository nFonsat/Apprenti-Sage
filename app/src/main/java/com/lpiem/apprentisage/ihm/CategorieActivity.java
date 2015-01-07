/******************************************************************************************************************
*
* Apprenti-Sage Android
*
* CategorieActivity.java
* 
* Michael Breton - Clément Bretin
* LP IEM - 2014
*
*******************************************************************************************************************/

package com.lpiem.apprentisage.ihm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.lpiem.apprentisage.ActionBarService;
import com.lpiem.apprentisage.R;
import com.lpiem.apprentisage.Shared;
import com.lpiem.apprentisage.UIService;
import com.lpiem.apprentisage.model.Categorie;

public class CategorieActivity extends SherlockActivity
{
	private LinearLayout listCategorie;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.categorie_list);
		
		listCategorie = (LinearLayout) findViewById(R.id.categorie_list);

		initCategorieList();
		ActionBarService.initActionBar(this, getSupportActionBar(), getString(R.string.cat_titre));
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
					Intent intent = new Intent(CategorieActivity.this,SousCategorieActivity.class);
					startActivity(intent);
				}
			});
			
			listCategorie.addView(view);
		}
	}
}