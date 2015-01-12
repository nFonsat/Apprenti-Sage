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
import com.lpiem.apprentisage.database.DataBaseAccess;
import com.lpiem.apprentisage.ihm.AccueilActivity;
import com.lpiem.apprentisage.jsonObject.Classe;
import com.lpiem.apprentisage.jsonObject.Enseignant;
import com.lpiem.apprentisage.jsonObject.Serie;
import com.lpiem.apprentisage.network.RestApiCall;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SplashTask extends AsyncTask<Void, Void, String[]>{
    private DataBaseAccess dbAccess;
    private Activity mActity;


    public SplashTask(Activity activity) {
        super();
        mActity = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dbAccess = new DataBaseAccess(mActity.getApplicationContext());
        Log.d(Consts.TAG_APPLICATION + " : dbAccess ", dbAccess.toString());

    }

    @Override
    protected String[] doInBackground(Void... params) {
        RestApiCall api = new RestApiCall("http://ptut.eklerni.iem/api/enseignants");

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
            Log.d(Consts.TAG_APPLICATION + " : Api Call JSON Array : List Enseignants", enseignantList.toString());

            for (int i = 0; i < enseignantList.length(); i++) {
                JSONObject unEnseignantJson = enseignantList.getJSONObject(i);
                //Log.d(Consts.TAG_APPLICATION + " : Api Call JSON Object : EnseigantJson " + i, unEnseignantJson.toString());

                Enseignant enseignant = JsonUtils.jsonToEnseignant(unEnseignantJson);
                //Log.d(Consts.TAG_APPLICATION + " : Api Call Enseigant : username " + i, enseignant.getNom() + " " + enseignant.getPrenom());


                long idEnseignant = dbAccess.insertEnseignant(enseignant);
                Log.d(Consts.TAG_APPLICATION + " : Api Call Enseigant : idEnseignant " + i, String.valueOf(idEnseignant));

                ArrayList<Classe> classes = enseignant.getClasses();
                for (Classe c : classes){
                    //Log.d(Consts.TAG_APPLICATION + " : Api Call Classe : Classe ", c.toString());
                    long idClasse = dbAccess.insertClasse(c, enseignant);
                    Log.d(Consts.TAG_APPLICATION + " : Api Call Classe : idClasse ", String.valueOf(idClasse));
                }

                JSONArray serieList = unEnseignantJson.getJSONArray("series");
                //Log.d(Consts.TAG_APPLICATION + " : Api Call JSON Array : List Series", serieList.toString());

                for (int j = 0; j < serieList.length(); j++){
                    JSONObject uneSerieJson = serieList.getJSONObject(j);
                    //Log.d(Consts.TAG_APPLICATION + " : Api Call JSON Object : SerieJson " + j, uneSerieJson.toString());

                    Serie serie = JsonUtils.jsonToSerie(uneSerieJson);
                    //Log.d(Consts.TAG_APPLICATION + " : Api Call Serie " + j, serie.getNom());
                }
            }
        } catch (JSONException t) {
            Log.e(Consts.TAG_APPLICATION + " : Api Call JSON Error ", t.getMessage());
        }

        Intent i = new Intent(mActity, AccueilActivity.class);
        mActity.startActivity(i);
        mActity.finish();
    }
}