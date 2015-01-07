/******************************************************************************************************************
*
* Apprenti-Sage Android
*
* SousCategorieActivity.java
* 
* Michael Breton - Clément Bretin
* LP IEM - 2014
*
*******************************************************************************************************************/

package com.lpiem.apprentisage.ihm;

import android.os.Bundle;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockActivity;
import com.lpiem.apprentisage.ActionBarService;
import com.lpiem.apprentisage.R;
import com.lpiem.apprentisage.adapter.SubCatAdapter;

public class SousCategorieActivity extends SherlockActivity
{
	private ListView listCategorie;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sub_categorie_list);
		
		listCategorie = (ListView) findViewById(R.id.sub_categorie_list);
		listCategorie.setAdapter(new SubCatAdapter(this));
		
		ActionBarService.initActionBar(this, getSupportActionBar(), getString(R.string.cat_titre));
	}
}