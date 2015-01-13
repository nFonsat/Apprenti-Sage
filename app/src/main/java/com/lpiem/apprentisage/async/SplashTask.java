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
import com.lpiem.apprentisage.jsonObject.Eleve;
import com.lpiem.apprentisage.jsonObject.Enseignant;
import com.lpiem.apprentisage.jsonObject.Serie;
import com.lpiem.apprentisage.network.ConfigNetwork;
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
            Log.d(Consts.TAG_APPLICATION + " : Api Call JSON Array : List Enseignants", enseignantList.toString());

            for (int i = 0; i < enseignantList.length(); i++) {
                JSONObject unEnseignantJson = enseignantList.getJSONObject(i);

                Enseignant enseignant = JsonUtils.jsonToEnseignant(unEnseignantJson);


                long idEnseignant = dbAccess.insertEnseignant(enseignant);
                Log.d(Consts.TAG_APPLICATION + " : Api Call Enseigant : idEnseignant " + i, String.valueOf(idEnseignant));

                ArrayList<Classe> classes = enseignant.getClasses();
                for (Classe c : classes){
                    long idClasse = dbAccess.insertClasse(c, enseignant);
                    Log.d(Consts.TAG_APPLICATION + " : Api Call Classe : idClasse ", String.valueOf(idClasse));

                    ArrayList<Eleve> eleves = c.getEleves();
                    for (Eleve e : eleves){
                        long idEleve = dbAccess.insertEleve(e, c);
                        Log.d(Consts.TAG_APPLICATION + " : Api Call Eleve : idEleve ", String.valueOf(idEleve));
                    }
                }

                JSONArray serieList = unEnseignantJson.getJSONArray("series");

                for (int j = 0; j < serieList.length(); j++){
                    JSONObject uneSerieJson = serieList.getJSONObject(j);

                    Serie serie = JsonUtils.jsonToSerie(uneSerieJson);
                }
            }
        } catch (JSONException t) {
            Log.e(Consts.TAG_APPLICATION + " : Api Call JSON Error ", t.getMessage());
        }

        ArrayList<Enseignant> enseignantsTest = dbAccess.getEnseignants();
        Log.d(Consts.TAG_APPLICATION + " : enseignantsTest ", enseignantsTest.toString());
        Log.d(Consts.TAG_APPLICATION + " : enseignantsTest ", enseignantsTest.get(0).getClasses().toString());

        Intent i = new Intent(mActity, AccueilActivity.class);
        mActity.startActivity(i);
        mActity.finish();
    }
}