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
import com.lpiem.apprentisage.jsonObject.Enseignant;
import com.lpiem.apprentisage.jsonObject.Serie;
import com.lpiem.apprentisage.network.RestApiCall;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SplashTask extends AsyncTask<Void, Void, String>{
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
    protected String doInBackground(Void... params) {
        RestApiCall api = new RestApiCall("http://ptut.eklerni/app.php/api/enseignants");

        api.executeRequest(RestApiCall.RestApiCallMethod.GET);

        if (api.getResponseCode() != 200){
            return String.valueOf(api.getResponseCode());
        }

        return api.getResponse();
    }

    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);
        Log.d(Consts.TAG_APPLICATION + " : API Call Response", response);

        try {
            JSONArray enseignantList = new JSONArray(response);
            Log.d(Consts.TAG_APPLICATION + " : Api Call JSON Array : List Enseignants", enseignantList.toString());

            for (int i = 0; i < enseignantList.length(); i++) {
                JSONObject unEnseignantJson = enseignantList.getJSONObject(i);
                Log.d(Consts.TAG_APPLICATION + " : Api Call JSON Object : EnseigantJson " + i, unEnseignantJson.toString());

                Enseignant enseignant = JsonUtils.jsonToEnseignant(unEnseignantJson);
                Log.d(Consts.TAG_APPLICATION + " : Api Call Enseigant : username " + i, enseignant.getNom() + " " + enseignant.getPrenom());
                dbAccess.insertEnseignant(enseignant);

                ArrayList<Enseignant> enseignantInDb = dbAccess.getEnseignant();
                Log.d(Consts.TAG_APPLICATION + " : dbAccess : list Enseignant ", enseignantInDb.toString());

                JSONArray serieList = unEnseignantJson.getJSONArray("series");
                Log.d(Consts.TAG_APPLICATION + " : Api Call JSON Array : List Series", serieList.toString());

                for (int j = 0; j < serieList.length(); j++){
                    JSONObject uneSerieJson = serieList.getJSONObject(j);
                    Log.d(Consts.TAG_APPLICATION + " : Api Call JSON Object : SerieJson " + j, uneSerieJson.toString());

                    Serie serie = JsonUtils.jsonToSerie(uneSerieJson);
                    Log.d(Consts.TAG_APPLICATION + " : Api Call Serie " + j, serie.getNom());
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