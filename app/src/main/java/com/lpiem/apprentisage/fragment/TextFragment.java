/**
 * Created by Nicolas on 14/01/2015.
 */
package com.lpiem.apprentisage.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.lpiem.apprentisage.Consts;
import com.lpiem.apprentisage.R;
import com.lpiem.apprentisage.applicatif.App;
import com.lpiem.apprentisage.applicatif.ResultatApp;
import com.lpiem.apprentisage.database.DAO.ResultatDAO;
import com.lpiem.apprentisage.metier.Exercice;
import com.lpiem.apprentisage.metier.Resultat;
import com.lpiem.apprentisage.metier.Serie;

import java.io.IOException;
import java.util.ArrayList;

public class TextFragment extends Fragment {
    public static final String LOG = Consts.TAG_APPLICATION + " : " + TextFragment.class.getSimpleName();
    public FragmentAccess mFragmentAccess;

    private EditText mReponse;
    private TextView mEnonce;
    private Button mPlayEnonce;
    private Button mValiderReponse;

    private Exercice mCurrentExercice;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exercice_text, container, false);

        mEnonce = (TextView) view.findViewById(R.id.enonce_exercice_txt);
        mPlayEnonce = (Button) view.findViewById(R.id.play_exercice_btn);
        mReponse = (EditText) view.findViewById(R.id.reponse_exercice_txt);

        if(mCurrentExercice != null){
            mEnonce.setText(mCurrentExercice.getEnonce());
        }

        mPlayEnonce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCurrentExercice != null){
                    //TextToSpeech
                }
            }
        });

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mFragmentAccess = (FragmentAccess) activity;
        } catch (ClassCastException e) {
            String error = activity.toString() + " must implement FragmentAccess";
            Log.e(LOG + " : Error", error);
            throw new ClassCastException(error);
        }

    }

    public void setParameter(Exercice exercice, Button validerReponse){
        mCurrentExercice = exercice;
        mValiderReponse = validerReponse;
        mValiderReponse.setOnClickListener(new ActionValiderReponse());
    }

    private class ActionValiderReponse implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Log.d(LOG,"Appuie sur le bouton valider");
            App mApplication = App.getInstance();
            ResultatApp mResultatApplication = ResultatApp.getInstance();
            Serie mCurrentSerie = mApplication.getCurrentSerie();
            if(mCurrentSerie == null || mCurrentExercice == null){
                return;
            }

            String maReponse = mReponse.getText().toString();
            Resultat resultatExercice = mResultatApplication.calculateResultExercice(v.getContext(), mCurrentExercice, maReponse);


            if(resultatExercice.getNote() == 1){
                try {
                    mFragmentAccess.exerciceSuccess();
                } catch (IOException ioError) {
                    Log.e(LOG + " : io error", ioError.getMessage());
                } catch (Exception error) {
                    Log.e(LOG + " : error", error.getMessage());
                }
            } else {
                mFragmentAccess.exerciceError(mCurrentExercice.getResponses().get(0));
            }

            ResultatDAO mResultatDAO = new ResultatDAO(v.getContext(), mApplication.getCurrentEleve());
            ArrayList<Resultat> resultatsSerie = mResultatDAO.getResultatsBySerie(mCurrentSerie);
            if (resultatsSerie.size() == mCurrentSerie.getExercices().size()){
                mResultatApplication.calculateResultSerie(v.getContext(), mCurrentSerie);
                mFragmentAccess.serieIsFinished();
                return;
            }

            mCurrentExercice = mFragmentAccess.changeExercice();
        }
    }
}