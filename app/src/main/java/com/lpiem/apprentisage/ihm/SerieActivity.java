package com.lpiem.apprentisage.ihm;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
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
import com.lpiem.apprentisage.applicatif.ResultatApp;
import com.lpiem.apprentisage.database.DAO.ResultatDAO;
import com.lpiem.apprentisage.fragment.AudioFragment;
import com.lpiem.apprentisage.fragment.TextFragment;
import com.lpiem.apprentisage.metier.Eleve;
import com.lpiem.apprentisage.metier.Exercice;
import com.lpiem.apprentisage.metier.Resultat;
import com.lpiem.apprentisage.metier.Serie;
import com.lpiem.apprentisage.model.Categorie;

import java.io.IOException;
import java.util.ArrayList;


public class SerieActivity extends SherlockActivity {
    public static final String LOG = Consts.TAG_APPLICATION + " : " + SerieActivity.class.getSimpleName();

    private FragmentTransaction mFragmentTransaction;
    private Fragment mFragment;
    private Context mContext;

    private SerieAdapter mSerieAdapter;

    private App mApplication;
    private ResultatApp mResultatApplication;

    private Categorie mActivite;
    private Serie mCurrentSerie;
    private Exercice mCurrentExercice;

    private Eleve mCurrentEleve;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acivity_exercice);

        mContext = getApplicationContext();

        mApplication = App.getInstance();
        mResultatApplication = ResultatApp.getInstance();

        mResultatApplication = ResultatApp.getInstance();
        mCurrentEleve = mApplication.getCurrentEleve();
        mActivite = mApplication.getCurrentActivite();

        mSerieAdapter = new SerieAdapter(this);
        ListView mListSeries = (ListView)findViewById(R.id.list_series);
        mListSeries.setAdapter(mSerieAdapter);

        mListSeries.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
                mApplication.setCurrentSerie(mActivite.getSerieList().get(position));
                mCurrentSerie = mApplication.getCurrentSerie();
                mCurrentExercice = changeExercice();
            }
        });

        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        ActionBarService.initActionBar(this, this.getSupportActionBar(), mActivite.getNom());
    }

    private Fragment switchFragment(Exercice exercice) {
        Fragment fragment = null;

        if (exercice == null) {
            recommencerSerie(mCurrentSerie);
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

    public void backToListActivite(View view){
        finish();
    }

    public void validerReponse(View view){
        if(mCurrentSerie == null || mCurrentExercice == null){
            return;
        }

        String maReponse = ((TextFragment) mFragment).getResponse();
        Resultat resultatExercice = mResultatApplication.calculateResultExercice(this, mCurrentExercice, maReponse);


        if(resultatExercice.getNote() == 1){
            try {
                success();
            } catch (IOException ioError) {
                Log.e(LOG + " : io error", ioError.getMessage());
            } catch (Exception error) {
                Log.e(LOG + " : error", error.getMessage());
            }
        } else {
            error(mCurrentExercice.getResponses().get(0));
        }

        ResultatDAO mResultatDAO = new ResultatDAO(mContext, mCurrentEleve);
        ArrayList<Resultat> resultatsSerie = mResultatDAO.getResultatsBySerie(mCurrentSerie);
        if (resultatsSerie.size() == mCurrentSerie.getExercices().size()){
            mResultatApplication.calculateResultSerie(this, mCurrentSerie);

            mSerieAdapter.notifyDataSetChanged();
            mFragmentTransaction = getFragmentManager().beginTransaction();
            mFragmentTransaction.remove(mFragment).commit();
            return;
        }

        mCurrentExercice = changeExercice();
    }

    public void success() throws IOException {
        MediaPlayer player = new MediaPlayer();

        AssetFileDescriptor mAssetFileDescriptor = getAssets().openFd("sons/success.mp3");
        player.setDataSource(mAssetFileDescriptor.getFileDescriptor(), mAssetFileDescriptor.getStartOffset(), mAssetFileDescriptor.getLength());

        player.prepare();
        player.start();
    }

    public void error(String response)
    {
        AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setTitle("Dommage tu n'as pas la bonne réponse");
        dialog.setMessage("La bonne réponse était " + response);
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.ok), new AlertDialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void recommencerSerie(final Serie serie){
        AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setMessage("Veux tu recommencer cette série ?");

        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NON", new AlertDialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES", new AlertDialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ResultatDAO resultatDAO = new ResultatDAO(mContext, mCurrentEleve);
                for(Resultat unResultatASupprimer : resultatDAO.getResultatsBySerie(serie)){
                    resultatDAO.supprimer(unResultatASupprimer);
                }
                mCurrentExercice = changeExercice();
                mSerieAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSerieAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mResultatApplication.calculateResultActivite(this, mApplication.getCurrentActivite());
    }
}