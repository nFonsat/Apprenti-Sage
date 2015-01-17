package com.lpiem.apprentisage.ihm;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
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

    private FragmentTransaction mFragmentTransaction;
    private Fragment mFragment;
    private Context mContext;

    private SerieAdapter mSerieAdapter;

    private Categorie mActivite;
    private Serie mCurrentSerie;
    private Exercice mCurrentExercice;

    private Eleve mCurrentEleve;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acivity_exercice);

        mContext = getApplicationContext();

        App mApplication = App.getInstance();
        mCurrentEleve = mApplication.getCurrentEleve();
        mActivite = mApplication.getCurrentActivite();

        mSerieAdapter = new SerieAdapter(this);
        ListView mListSeries = (ListView)findViewById(R.id.list_series);
        mListSeries.setAdapter(mSerieAdapter);

        mListSeries.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
                mCurrentSerie = mActivite.getSerieList().get(position);
                mCurrentExercice = changeExercice();
            }
        });

        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        ActionBarService.initActionBar(this, this.getSupportActionBar(), mActivite.getNom());
    }

    private Fragment switchFragment(Exercice exercice) {
        Fragment fragment = null;

        if (exercice == null) {
            return null;
        }

        switch (exercice.getType()){
            case "text":
                fragment = new TextFragment();
                ((TextFragment) fragment).setParameter(exercice);
                break;
            case "audio":
                fragment = new AudioFragment();
                ((AudioFragment) fragment).setParameter(exercice);
                break;
            case "audio-text":
                break;
        }

        mFragmentTransaction = getFragmentManager().beginTransaction();
        mFragmentTransaction.replace(R.id.fragment_media_exercice, fragment).commit();
        return fragment;
    }

    private Exercice changeExercice(){
        Exercice nextExercice;
        nextExercice = mCurrentSerie.nextExercice(mCurrentEleve, mContext); //Peut retourner null
        if((mFragment != null)){
            mFragmentTransaction = getFragmentManager().beginTransaction();
            mFragmentTransaction.remove(mFragment).commit();
        }
        mFragment = switchFragment(nextExercice);
        return nextExercice;
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

        Resultat resultatExercice = new Resultat();
        resultatExercice.setNom(String.valueOf(mCurrentSerie.getId()));
        resultatExercice.setType(TypeResultat.RESULTAT_EXERCICE.getType());
        resultatExercice.setIdTableCorrespondant(mCurrentExercice.getId());

        int i = 0;
        boolean reponseCorrecte = false;
        while((i < lesReponses.size()) && (!reponseCorrecte)){
            String uneReponse = lesReponses.get(i);
            if(uneReponse.equalsIgnoreCase(maReponse)){
                resultatExercice.setNote(1);
                reponseCorrecte = true;
            }
            i++;
        }

        ResultatDAO mResultatDAO = new ResultatDAO(mContext, mCurrentEleve);
        long id = mResultatDAO.ajouter(resultatExercice);
        resultatExercice.setId(id);

        ArrayList<Resultat> resultatsSerie = mResultatDAO.getResultatsBySerie(mCurrentSerie);
        if (resultatsSerie.size() == mCurrentSerie.getExercices().size()){
            int noteSerie = 0;
            Resultat resultatSerie = new Resultat();
            resultatSerie.setNom(mCurrentSerie.getNom());
            resultatSerie.setType(TypeResultat.RESULTAT_SERIE.getType());
            resultatSerie.setIdTableCorrespondant(mCurrentSerie.getId());
            for (Resultat unResultat : resultatsSerie){
                noteSerie += unResultat.getNote();
            }
            resultatSerie.setNote(noteSerie);
            mResultatDAO.ajouter(resultatSerie);

            mSerieAdapter.notifyDataSetChanged();
            mFragmentTransaction = getFragmentManager().beginTransaction();
            mFragmentTransaction.remove(mFragment).commit();
            return;
        }

        mCurrentExercice = changeExercice();
    }
}