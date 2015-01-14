package com.lpiem.apprentisage.ihm;

import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockActivity;
import com.lpiem.apprentisage.ActionBarService;
import com.lpiem.apprentisage.R;
import com.lpiem.apprentisage.adapter.SerieAdapter;
import com.lpiem.apprentisage.data.App;
import com.lpiem.apprentisage.metier.Exercice;
import com.lpiem.apprentisage.metier.Serie;
import com.lpiem.apprentisage.model.Categorie;


public class SerieActivity extends SherlockActivity {

    private SerieAdapter mSerieAdapter;
    private ListView mListSeries;

    private Serie mSerie;

    private App mApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acivity_exercice);

        mApplication = App.getInstance();
        Categorie activite = mApplication.getCurrentActivite();

        mSerieAdapter = new SerieAdapter(this);
        mListSeries = (ListView)findViewById(R.id.list_series);
        mListSeries.setAdapter(mSerieAdapter);

        mListSeries.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3)
            {
                mSerie = mApplication.getCurrentActivite().getSerieList().get(position);
                for(Exercice exercice : mSerie.getExercices()){
                    Log.d("Enoncé de l'exercice", exercice.getEnonce());
                }
            }
        });

        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        ActionBarService.initActionBar(this, this.getSupportActionBar(), activite.getNom());
    }

    public void back(View view){
        finish();
    }

    public void validerReponse(View view){
        if(mSerie == null){
            return;
        }
    }
}