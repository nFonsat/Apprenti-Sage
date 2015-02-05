/**
 * Created by Nicolas on 31/01/2015.
 */
package com.lpiem.apprentisage.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.lpiem.apprentisage.Utils.Consts;
import com.lpiem.apprentisage.R;
import com.lpiem.apprentisage.Utils.UIService;
import com.lpiem.apprentisage.applicatif.App;
import com.lpiem.apprentisage.applicatif.ResultatApp;
import com.lpiem.apprentisage.database.DAO.ResultatDAO;
import com.lpiem.apprentisage.metier.Exercice;
import com.lpiem.apprentisage.metier.Resultat;
import com.lpiem.apprentisage.metier.Serie;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class CompterFragment extends CoreExerciceFragment {
    public static final String LOG = Consts.TAG_APPLICATION + " : " + CompterFragment.class.getSimpleName();

    private Context mContext;
    private LinearLayout mCountLineLayout;
    private RelativeLayout mGameLayout;
    private EditText mReponse;

    private ArrayList<FrameLayout> mLineJetons;

    private int mIndexJeton;
    private int mNbJeton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exercice_compter, container, false);

        mContext = view.getContext();
        mLineJetons = new ArrayList<>();

        mGameLayout = (RelativeLayout) view.findViewById(R.id.game_area);
        mCountLineLayout = (LinearLayout) view.findViewById(R.id.line_count_layout);
        mReponse = (EditText) view.findViewById(R.id.reponse_edittxt);

        initGame();

        return view;
    }

    public void setParameter(Button btnValider, Exercice exercice){
        super.setParameter(exercice);
        btnValider.setOnClickListener(new ActionValiderReponse());
        initGame();
    }

    private void initGame(){
        if(mCurrentExercice == null){
            return;
        }

        if(!isNumeric(mCurrentExercice.getEnonce())){
            mFragmentAccess.changeExercice();
            return;
        }

        mNbJeton = Integer.parseInt(mCurrentExercice.getEnonce());

        resetGame();
    }

    private void resetGame(){
        mReponse.setText("");
        mIndexJeton = 0;
        mLineJetons.clear();
        mCountLineLayout.removeAllViews();
        addCountLine();
        initGameView();
    }

    private void initGameView(){
        mGameLayout.removeAllViews();

        int x = mGameLayout.getWidth();
        int y = mGameLayout.getHeight();

        for (int i = 0; i < mNbJeton; i++){
            FrameLayout button = new FrameLayout(mContext);
            button.setBackgroundResource(R.drawable.pomme);

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(UIService.getPx(mContext, 40),UIService.getPx(mContext, 40));

            Random random = new Random();
            int marginTop = random.nextInt(y - UIService.getPx(mContext, 45)) + UIService.getPx(mContext, 5);
            int marginLeft = random.nextInt(x - UIService.getPx(mContext, 45)) + UIService.getPx(mContext, 5);
            params.setMargins(marginLeft, marginTop, 0, 0);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.setVisibility(View.INVISIBLE);
                    v.setClickable(false);

                    mLineJetons.get(mIndexJeton).setBackgroundResource(R.drawable.pomme);
                    mIndexJeton ++;

                    if(mIndexJeton % 10 == 0){
                        addCountLine();
                    }
                }
            });

            mGameLayout.addView(button, params);
        }
    }

    private void addCountLine(){
        LinearLayout lineLayout = new LinearLayout(mContext);
        LinearLayout.LayoutParams lineParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lineParams.setMargins(UIService.getPx(mContext, 10), UIService.getPx(mContext, 5), 0, 0);
        lineLayout.setOrientation(LinearLayout.HORIZONTAL);

        for(int i = 0; i < 10; i ++){
            FrameLayout frameUnit = new FrameLayout(mContext);
            LinearLayout.LayoutParams unitParams = new LinearLayout.LayoutParams(UIService.getPx(mContext, 30), UIService.getPx(mContext, 30));
            lineLayout.addView(frameUnit, unitParams);

            mLineJetons.add(frameUnit);
        }

        mCountLineLayout.addView(lineLayout, lineParams);
    }

    private class ActionValiderReponse implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String maReponse = mReponse.getText().toString();
            if(maReponse == null || maReponse.isEmpty()){
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
                    Log.e(LOG + " : IOError", ioError.getMessage());
                } catch (Exception error) {
                    Log.e(LOG + " : Error", error.getMessage());
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