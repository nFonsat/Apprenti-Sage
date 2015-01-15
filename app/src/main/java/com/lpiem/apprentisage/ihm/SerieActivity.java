package com.lpiem.apprentisage.ihm;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockActivity;
import com.lpiem.apprentisage.ActionBarService;
import com.lpiem.apprentisage.R;
import com.lpiem.apprentisage.adapter.SerieAdapter;
import com.lpiem.apprentisage.data.App;
import com.lpiem.apprentisage.database.DAO.ResultatDAO;
import com.lpiem.apprentisage.fragment.AudioFragment;
import com.lpiem.apprentisage.fragment.TextFragment;
import com.lpiem.apprentisage.metier.Eleve;
import com.lpiem.apprentisage.metier.Exercice;
import com.lpiem.apprentisage.metier.Resultat;
import com.lpiem.apprentisage.metier.Serie;
import com.lpiem.apprentisage.model.Categorie;


public class SerieActivity extends SherlockActivity {

    private SerieAdapter mSerieAdapter;
    private ListView mListSeries;

    private Fragment mFragment;
    private Context mContext;

    private Categorie mActivite;
    private Serie mCurrentSerie;
    private Exercice mCurrentExercice;

    private Eleve mCurrentEleve;
    private Resultat mResultat;

    private App mApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acivity_exercice);

        mContext = getApplicationContext();

        mApplication = App.getInstance();
        mCurrentEleve = mApplication.getCurrentEleve();
        mActivite = mApplication.getCurrentActivite();

        mSerieAdapter = new SerieAdapter(this);
        mListSeries = (ListView)findViewById(R.id.list_series);
        mListSeries.setAdapter(mSerieAdapter);

        mListSeries.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
                mCurrentSerie = mActivite.getSerieList().get(position);
                mCurrentExercice = mCurrentSerie.nextExercice(mCurrentEleve, mContext);
                if(mCurrentExercice == null ){
                    return;
                }

                Log.d("Enonce du prochain exercice", mCurrentExercice.getEnonce());
                switchFragment(mCurrentExercice);
            }
        });

        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        ActionBarService.initActionBar(this, this.getSupportActionBar(), mActivite.getNom());
    }

    private void switchFragment(Exercice exercice) {
        /*if(mFragment == null) {
            return;
        }*/

        Log.d("Type d'exercice", mCurrentExercice.getEnonce());
        switch (exercice.getType()){
            case "text":
                mFragment = new TextFragment();
                ((TextFragment) mFragment).setParameter(exercice);
                break;
            case "audio":
                mFragment = new AudioFragment();
                ((AudioFragment) mFragment).setParameter(exercice);
                break;
            case "audio-text":
                break;
        }

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_media_exercice, mFragment).commit();
    }

    public void back(View view){
        finish();
    }

    public void validerReponse(View view){
        if(mCurrentSerie == null || mCurrentExercice == null){
            return;
        }

        String maReponse = ((TextFragment) mFragment).getResponse();

        for(String uneReponse : mCurrentExercice.getResponses()){
            if(uneReponse.equalsIgnoreCase(maReponse)){
                Log.d("Bravo", "Tu as la bonne reponse");
                mResultat = new Resultat();

                ResultatDAO mResultatDAO = new ResultatDAO(mContext, mCurrentEleve);
                mResultatDAO.ajouter(mResultat);
            }
        }
    }
}