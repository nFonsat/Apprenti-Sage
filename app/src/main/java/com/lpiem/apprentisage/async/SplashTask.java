/**
 * Created by Nicolas on 10/01/2015.
 */
package com.lpiem.apprentisage.async;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.util.Log;

import com.lpiem.apprentisage.Consts;

import com.lpiem.apprentisage.Utils.JsonUtils;

import com.lpiem.apprentisage.database.DAO.ClasseDAO;
import com.lpiem.apprentisage.database.DAO.EleveDAO;
import com.lpiem.apprentisage.database.DAO.EnseignantDAO;
import com.lpiem.apprentisage.database.DAO.ExerciceDAO;
import com.lpiem.apprentisage.database.DAO.ResultatDAO;
import com.lpiem.apprentisage.database.DAO.SerieDAO;

import com.lpiem.apprentisage.ihm.AccueilActivity;

import com.lpiem.apprentisage.metier.Classe;
import com.lpiem.apprentisage.metier.Eleve;
import com.lpiem.apprentisage.metier.Enseignant;
import com.lpiem.apprentisage.metier.Exercice;
import com.lpiem.apprentisage.metier.Resultat;
import com.lpiem.apprentisage.metier.Serie;

import com.lpiem.apprentisage.network.ConfigNetwork;
import com.lpiem.apprentisage.network.RestApiCall;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SplashTask extends AsyncTask<Void, Void, String[]>{
    private Activity mActity;

    private EnseignantDAO mEnseignantDAO;
    private ClasseDAO mClasseDAO;
    private EleveDAO mEleveDAO;
    private ResultatDAO mResultatDAO;
    private SerieDAO mSerieDAO;
    private ExerciceDAO mExerciceDAO;

    private static final String CLASS_TAG = Consts.TAG_APPLICATION +  " : SplashTask";


    public SplashTask(Activity activity) {
        super();
        mActity = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        Context context = mActity.getApplicationContext();
        mEnseignantDAO = new EnseignantDAO(context);
        mClasseDAO = new ClasseDAO(context);
        mEleveDAO = new EleveDAO(context);
        mSerieDAO = new SerieDAO(context);
        mExerciceDAO = new ExerciceDAO(context);
    }

    @Override
    protected String[] doInBackground(Void... params) {
        RestApiCall api = new RestApiCall(ConfigNetwork.URL_LOCALHOST_IEM_LIST_ENSEIGNANT);

        api.executeRequest(RestApiCall.RestApiCallMethod.GET);
        String[] responses = new String[2];

        responses[0] = String.valueOf(api.getResponseCode());
        responses[1] = api.getResponse();

        return responses;
    }

    @Override
    protected void onPostExecute(String[] responses) {
        super.onPostExecute(responses);

        Log.d(Consts.TAG_APPLICATION + " : API Call Response", responses[0]);

        if(Integer.valueOf(responses[0]) != 200){
            goToHome();
            return;
        }

        try {
            JSONArray enseignantsJson = new JSONArray(responses[1]);
            for (int i = 0; i < enseignantsJson.length(); i++) {
                JSONObject unEnseignantJson = enseignantsJson.getJSONObject(i);

                Enseignant unEnseignant = JsonUtils.jsonToEnseignant(unEnseignantJson);
                mEnseignantDAO.ajouter(unEnseignant);

                for (Classe uneClasse : unEnseignant.getClasses()){
                    mClasseDAO.ajouter(uneClasse, unEnseignant);

                    for (Eleve unEleve : uneClasse.getEleves()){
                        mEleveDAO.ajouter(unEleve, uneClasse);
                        mResultatDAO = new ResultatDAO(mActity, unEleve);
                        for (Resultat unResultat : unEleve.getResultats()){
                            mResultatDAO.ajouter(unResultat);
                        }
                    }
                }

                JSONArray seriesJson = unEnseignantJson.getJSONArray("series");
                for (int j = 0; j < seriesJson.length(); j++){
                    JSONObject uneSerieJson = seriesJson.getJSONObject(j);

                    Serie uneSerie = JsonUtils.jsonToSerie(uneSerieJson);
                    mSerieDAO.ajouter(uneSerie, unEnseignant);
                    for (Exercice unExercice : uneSerie.getExercices()){
                        mExerciceDAO.ajouter(unExercice,uneSerie, unEnseignant);
                    }
                }
            }
        } catch (JSONException jsonException) {
            Log.e(CLASS_TAG + " : Error JSon", jsonException.getMessage());
        } catch (SQLiteException sqliteException) {
            Log.e(CLASS_TAG + " : Error SqLite", sqliteException.getMessage());
        } catch (Exception exception) {
            Log.e(CLASS_TAG + " : Error General", exception.getMessage());
        }

        goToHome();
    }

    public void goToHome(){
        Intent i = new Intent(mActity, AccueilActivity.class);
        mActity.startActivity(i);
        mActity.finish();
    }
}