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
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockActivity;
import com.lpiem.apprentisage.Utils.ActionBarService;
import com.lpiem.apprentisage.Utils.Consts;
import com.lpiem.apprentisage.R;
import com.lpiem.apprentisage.adapter.SerieAdapter;
import com.lpiem.apprentisage.applicatif.App;
import com.lpiem.apprentisage.applicatif.ResultatApp;
import com.lpiem.apprentisage.fragment.AudioTextFragment;
import com.lpiem.apprentisage.fragment.CompterFragment;
import com.lpiem.apprentisage.fragment.FragmentAccess;
import com.lpiem.apprentisage.fragment.TextFragment;
import com.lpiem.apprentisage.metier.Eleve;
import com.lpiem.apprentisage.metier.Exercice;
import com.lpiem.apprentisage.metier.Serie;
import com.lpiem.apprentisage.metier.TypeExercice;
import com.lpiem.apprentisage.metier.Categorie;

import java.io.IOException;

public class SerieActivity extends SherlockActivity implements FragmentAccess {
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
        Button mValiderBtn = (Button)findViewById(R.id.exercice_btn_ok);
        Fragment fragment = null;

        if (exercice == null) {
            recommencerSerie(mCurrentSerie);
            return null;
        }

        if(exercice.getType().equalsIgnoreCase(TypeExercice.FragmentText.value())) {
            fragment = new TextFragment();
            ((TextFragment) fragment).setParameter(mValiderBtn, exercice);
        } else if(exercice.getType().equalsIgnoreCase(TypeExercice.FragmentAudio.value())) {
            fragment = new AudioTextFragment();
            ((AudioTextFragment) fragment).setParameter(mValiderBtn, exercice);
        } else if(exercice.getType().equalsIgnoreCase(TypeExercice.FragmentCompter.value())) {
            fragment = new CompterFragment();
            ((CompterFragment) fragment).setParameter(mValiderBtn, exercice);
        }

        mFragmentTransaction = getFragmentManager().beginTransaction();
        mFragmentTransaction.replace(R.id.fragment_media_exercice, fragment).commit();
        return fragment;
    }

    public void backToListActivite(View view){
        finish();
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
                mResultatApplication.updateResultatSerie(mContext, serie);
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

    @Override
    public void exerciceSuccess() throws IOException {
        MediaPlayer player = new MediaPlayer();

        AssetFileDescriptor mAssetFileDescriptor = getAssets().openFd("sons/success.mp3");
        player.setDataSource(mAssetFileDescriptor.getFileDescriptor(), mAssetFileDescriptor.getStartOffset(), mAssetFileDescriptor.getLength());

        player.prepare();
        player.start();
    }

    @Override
    public void exerciceError(String laReponse) {
        AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setTitle("Dommage tu n'as pas la bonne réponse");
        dialog.setMessage("La bonne réponse était " + laReponse);
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.ok), new AlertDialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public void serieIsFinished() {
        mSerieAdapter.notifyDataSetChanged();
        mFragmentTransaction = getFragmentManager().beginTransaction();
        mFragmentTransaction.remove(mFragment).commit();
    }

    @Override
    public Exercice changeExercice(){
        Exercice nextExercice;
        nextExercice = mCurrentSerie.nextExercice(mCurrentEleve, mContext); //Peut retourner null
        if((mFragment != null)){
            mFragmentTransaction = getFragmentManager().beginTransaction();
            mFragmentTransaction.remove(mFragment).commit();
        }
        mFragment = switchFragment(nextExercice);
        return nextExercice;
    }
}