/**
 * Created by Nicolas on 14/01/2015.
 */
package com.lpiem.apprentisage.fragment;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.lpiem.apprentisage.Utils.Consts;
import com.lpiem.apprentisage.R;
import com.lpiem.apprentisage.applicatif.App;
import com.lpiem.apprentisage.applicatif.ResultatApp;
import com.lpiem.apprentisage.database.DAO.ResultatDAO;
import com.lpiem.apprentisage.metier.Exercice;
import com.lpiem.apprentisage.metier.Resultat;
import com.lpiem.apprentisage.metier.Serie;

import java.io.IOException;
import java.util.ArrayList;

public class AudioTextFragment extends CoreExerciceFragment {
    public static final String LOG = Consts.TAG_APPLICATION + " : " + AudioTextFragment.class.getSimpleName();

    private EditText mReponse;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exercice_audio_text, container, false);

        mReponse = (EditText) view.findViewById(R.id.reponse_exercice_txt);
        Button mPlayEnonce = (Button) view.findViewById(R.id.lecture_btn_player);
        mPlayEnonce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCurrentExercice != null){
                    /*TextToSpeech*/
                    speaker(mCurrentExercice.getEnonce());
                }
            }
        });

        return view;
    }

    public void setParameter(Button btnValider, Exercice exercice){
        super.setParameter(exercice);
        btnValider.setOnClickListener(new ActionValiderReponse());
    }

    private class ActionValiderReponse implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String maReponse = mReponse.getText().toString();
            if (maReponse.isEmpty() || maReponse == null){
                return;
            }

            App mApplication = App.getInstance();
            ResultatApp mResultatApplication = ResultatApp.getInstance();
            Serie mCurrentSerie = mApplication.getCurrentSerie();
            if(mCurrentSerie == null || mCurrentExercice == null){
                return;
            }

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