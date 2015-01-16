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
import com.lpiem.apprentisage.Consts;
import com.lpiem.apprentisage.R;
import com.lpiem.apprentisage.adapter.SerieAdapter;
import com.lpiem.apprentisage.applicatif.App;
import com.lpiem.apprentisage.database.DAO.ResultatDAO;
import com.lpiem.apprentisage.fragment.AudioFragment;
import com.lpiem.apprentisage.fragment.TextFragment;
import com.lpiem.apprentisage.metier.Eleve;
import com.lpiem.apprentisage.metier.Exercice;
import com.lpiem.apprentisage.metier.Resultat;
import com.lpiem.apprentisage.metier.Serie;
import com.lpiem.apprentisage.metier.TypeResultat;
import com.lpiem.apprentisage.model.Categorie;

import java.util.ArrayList;


public class SerieActivity extends SherlockActivity {
    public static final String LOG = Consts.TAG_APPLICATION + " : " + SerieActivity.class.getSimpleName();

    private SerieAdapter mSerieAdapter;
    private ListView mListSeries;

    private FragmentTransaction mFragmentTransaction;
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

        mListSeries.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
                mCurrentSerie = mActivite.getSerieList().get(position);
                mCurrentExercice = mCurrentSerie.nextExercice(mCurrentEleve, mContext); //Peut retourner null
                switchFragment();
            }
        });

        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        ActionBarService.initActionBar(this, this.getSupportActionBar(), mActivite.getNom());
    }

    private void switchFragment() {
        if(mCurrentExercice == null && mFragment == null){
            return;
        }

        if (mCurrentExercice == null) {
            mFragmentTransaction = getFragmentManager().beginTransaction();
            mFragmentTransaction.remove(mFragment).commit();
            mFragment = null;
            return;
        }

        switch (mCurrentExercice.getType()){
            case "text":
                mFragment = new TextFragment();
                ((TextFragment) mFragment).setParameter(mCurrentExercice);
                break;
            case "audio":
                mFragment = new AudioFragment();
                ((AudioFragment) mFragment).setParameter(mCurrentExercice);
                break;
            case "audio-text":
                break;
        }

        mFragmentTransaction = getFragmentManager().beginTransaction();
        mFragmentTransaction.replace(R.id.fragment_media_exercice, mFragment).commit();
    }

    public void back(View view){
        finish();
    }

    public void validerReponse(View view){
        if(mCurrentSerie == null || mCurrentExercice == null){
            return;
        }

        String maReponse = ((TextFragment) mFragment).getResponse();
        ArrayList<String> lesReponses = mCurrentExercice.getResponses();

        boolean reponseCorrecte = false;
        int i = 0;

        mResultat = new Resultat();
        mResultat.setNom(String.valueOf(mCurrentSerie.getId()));
        mResultat.setType(TypeResultat.RESULTAT_EXERCICE.getType());
        mResultat.setIdTableCorrespondant(mCurrentExercice.getId());

        while((i < lesReponses.size()) && (!reponseCorrecte)){
            String uneReponse = lesReponses.get(i);
            if(uneReponse.equalsIgnoreCase(maReponse)){
                Log.d(LOG, "Tu as la bonne reponse");
                mResultat.setNote(1);
                reponseCorrecte = true;
            }
            i++;
        }

        ResultatDAO mResultatDAO = new ResultatDAO(mContext, mCurrentEleve);
        mResultatDAO.ajouter(mResultat);

        ArrayList<Resultat> resultatsSerie = mResultatDAO.getResultatsBySerie(mCurrentSerie);
        if (resultatsSerie.size() == mCurrentSerie.getExercices().size()){
            int noteSerie = 0;
            Log.d(LOG, "Tu as terminer la série");
            Resultat resultatSerie = new Resultat();
            resultatSerie.setNom(mCurrentSerie.getNom());
            resultatSerie.setType(TypeResultat.RESULTAT_SERIE.getType());
            resultatSerie.setIdTableCorrespondant(mCurrentSerie.getId());
            for (Resultat unResultat : resultatsSerie){
                noteSerie += unResultat.getNote();
            }
            resultatSerie.setNote(noteSerie);
            mResultatDAO.ajouter(resultatSerie);

            mSerieAdapter.setNote(noteSerie, mCurrentSerie);

            mFragmentTransaction = getFragmentManager().beginTransaction();
            mFragmentTransaction.remove(mFragment).commit();
            mFragment = null;
            return;
        }

        mCurrentExercice = mCurrentSerie.nextExercice(mCurrentEleve, mContext); //Peut retourner null
        switchFragment();
    }
}