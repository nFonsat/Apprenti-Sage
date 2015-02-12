/**
 * Created by Nicolas on 10/01/2015.
 */
package com.lpiem.apprentisage.async;

import android.content.Context;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.lpiem.apprentisage.Utils.Consts;
import com.lpiem.apprentisage.Utils.JsonUtils;

import com.lpiem.apprentisage.database.DAO.ClasseDAO;
import com.lpiem.apprentisage.database.DAO.EleveDAO;
import com.lpiem.apprentisage.database.DAO.EnseignantDAO;
import com.lpiem.apprentisage.database.DAO.ExerciceDAO;
import com.lpiem.apprentisage.database.DAO.ResultatDAO;
import com.lpiem.apprentisage.database.DAO.SerieDAO;

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

public class SplashTask extends AsyncTask<Void, Void, String[]>{
    private static final String CLASS_TAG = Consts.TAG_APPLICATION +  " : " + SplashTask.class.getSimpleName();

    private Context mContext;

    private EnseignantDAO mEnseignantDAO;
    private ClasseDAO mClasseDAO;
    private EleveDAO mEleveDAO;
    private SerieDAO mSerieDAO;
    private ExerciceDAO mExerciceDAO;

    private Handler mUIHandler;
    private Message msg;

    public SplashTask(Context context, Handler handler) {
        super();
        mUIHandler = handler;
        mContext = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        mEnseignantDAO = new EnseignantDAO(mContext);
        mClasseDAO = new ClasseDAO(mContext);
        mEleveDAO = new EleveDAO(mContext);
        mSerieDAO = new SerieDAO(mContext);
        mExerciceDAO = new ExerciceDAO(mContext);

        msg = Message.obtain();

        //mExerciceDAO.removeAllExercice();
        //mSerieDAO.removeAllSerie();
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
        if(Integer.valueOf(responses[0]) != 200){
            msg.what = Consts.SPLASHTASK_ERROR;
            msg.arg1 = Integer.valueOf(responses[0]);
            mUIHandler.sendMessage(msg);
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
                        ResultatDAO mResultatDAO = new ResultatDAO(mContext, unEleve);
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
            Log.e(CLASS_TAG + " : JSon", jsonException.getMessage());
        } catch (SQLiteException sqliteException) {
            Log.e(CLASS_TAG + " : SqLite", sqliteException.getMessage());
        } catch (Exception exception) {
            Log.e(CLASS_TAG + " : General", exception.getMessage());
        }

        msg.what = Consts.SPLASHTASK_FINISH;
        mUIHandler.sendMessage(msg);
    }
}