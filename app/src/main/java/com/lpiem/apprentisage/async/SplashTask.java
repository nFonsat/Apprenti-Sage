/**
 * Created by Nicolas on 10/01/2015.
 */
package com.lpiem.apprentisage.async;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.lpiem.apprentisage.Consts;

import com.lpiem.apprentisage.Utils.JsonUtils;

import com.lpiem.apprentisage.database.DAO.ClasseDAO;
import com.lpiem.apprentisage.database.DAO.EleveDAO;
import com.lpiem.apprentisage.database.DAO.EnseignantDAO;
import com.lpiem.apprentisage.database.DAO.SerieDAO;

import com.lpiem.apprentisage.ihm.AccueilActivity;

import com.lpiem.apprentisage.metier.Classe;
import com.lpiem.apprentisage.metier.Eleve;
import com.lpiem.apprentisage.metier.Enseignant;
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
    private SerieDAO mSerieDAO;


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
    }

    @Override
    protected String[] doInBackground(Void... params) {
        RestApiCall api = new RestApiCall(ConfigNetwork.URL_LOCALHOST_NICOLAS_LIST_ENSEIGNANT);

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
            return;
        }

        try {
            JSONArray enseignantList = new JSONArray(responses[1]);

            for (int i = 0; i < enseignantList.length(); i++) {
                JSONObject unEnseignantJson = enseignantList.getJSONObject(i);

                Enseignant enseignant = JsonUtils.jsonToEnseignant(unEnseignantJson);
                mEnseignantDAO.ajouter(enseignant);

                ArrayList<Classe> classes = enseignant.getClasses();
                for (Classe classe : classes){
                    mClasseDAO.ajouter(classe, enseignant);

                    ArrayList<Eleve> eleves = classe.getEleves();
                    for (Eleve eleve : eleves){
                        mEleveDAO.ajouter(eleve, classe);
                    }
                }

                JSONArray serieList = unEnseignantJson.getJSONArray("series");

                for (int j = 0; j < serieList.length(); j++){
                    JSONObject uneSerieJson = serieList.getJSONObject(j);

                    Serie serie = JsonUtils.jsonToSerie(uneSerieJson);
                    mSerieDAO.ajouter(serie, enseignant);
                }
            }
        } catch (JSONException t) {
            Log.e(Consts.TAG_APPLICATION + " : Api Call JSON Error ", t.getMessage());
        }

        ArrayList<Enseignant> enseignantsTest = mEnseignantDAO.getEnseignants();
        Log.d(Consts.TAG_APPLICATION + " : enseignantsTest ", enseignantsTest.toString());

        for(Enseignant enseignant : enseignantsTest){
            ArrayList<Serie> seriesByProf = mSerieDAO.getSeriesByProf(enseignant);
            Log.d(Consts.TAG_APPLICATION + " : SerieByprof ", seriesByProf.toString());
        }

        Intent i = new Intent(mActity, AccueilActivity.class);
        mActity.startActivity(i);
        mActity.finish();
    }
}