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
                mCurrentExercice = mCurrentSerie.nextExercice(mCurrentEleve, mContext); //Peut retourner null
                switchFragment();
            }
        });

        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        ActionBarService.initActionBar(this, this.getSupportActionBar(), mActivite.getNom());
    }

    private void switchFragment() {
        if(mCurrentExercice == null ){
            return;
        }

        Log.d("Type d'exercice", mCurrentExercice.getType());
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

        if(mFragment == null){
            return;
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

        Log.d("L'id la serie", String.valueOf(mCurrentSerie.getId()));
        Log.d("L'id l'exercice", String.valueOf(mCurrentExercice.getId()));

        String maReponse = ((TextFragment) mFragment).getResponse();
        Log.d("ma Reponse", maReponse);

        ArrayList<String> lesReponses = mCurrentExercice.getResponses();

        boolean reponseCorrecte = false;
        int i = 0;

        mResultat = new Resultat();
        mResultat.setNom(String.valueOf(mCurrentSerie.getId()));
        mResultat.setType(TypeResultat.RESULTAT_EXERCICE.getType());
        mResultat.setIdTableCorrespondant(mCurrentExercice.getId());

        while((i < lesReponses.size()) && (!reponseCorrecte)){
            String uneReponse = lesReponses.get(i);
            Log.d("une reponse", uneReponse);
            if(uneReponse.equalsIgnoreCase(maReponse)){
                Log.d("Bravo", "Tu as la bonne reponse");
                mResultat.setNote(1);
                reponseCorrecte = true;
            }
            i++;
        }


        Log.d("Reponse Correcte ?", String.valueOf(reponseCorrecte));
        Log.d("Resutat Obtenue", String.valueOf(mResultat.getNote()));

        ResultatDAO mResultatDAO = new ResultatDAO(mContext, mCurrentEleve);
        long id = mResultatDAO.ajouter(mResultat);
        Log.d("L'id du resultat de l'exercice", String.valueOf(id));


        ArrayList<Resultat> resultatsTest = mResultatDAO.getResultatsByEleve();
        Log.d("Nombre de resultat pour cette eleve", String.valueOf(resultatsTest.size()));

        ArrayList<Resultat> resultatsSerie = mResultatDAO.getResultatsBySerie(mCurrentSerie);
        Log.d("Nombre de resultat pour cette serie", String.valueOf(resultatsSerie.size()));

        if (resultatsSerie.size() == mCurrentSerie.getExercices().size()){
            int noteSerie = 0;
            Log.d("Bravo", "Tu as terminer la série");
            Resultat resultatSerie = new Resultat();
            resultatSerie.setNom(mCurrentSerie.getNom());
            resultatSerie.setType(TypeResultat.RESULTAT_SERIE.getType());
            resultatSerie.setIdTableCorrespondant(mCurrentSerie.getId());
            for (Resultat unResultat : resultatsSerie){
                noteSerie += unResultat.getNote();
            }
            resultatSerie.setNote(noteSerie);
            id = mResultatDAO.ajouter(resultatSerie);
            Log.d("L'id du resultat de la serie", String.valueOf(id));

            mSerieAdapter.setNote(noteSerie, mCurrentSerie);

            mFragment = null;
            getFragmentManager().popBackStack(null, getFragmentManager().POP_BACK_STACK_INCLUSIVE);
        }
    }
}