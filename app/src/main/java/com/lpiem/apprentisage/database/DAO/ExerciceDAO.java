/**
 * Created by iem on 13/01/15.
 */
package com.lpiem.apprentisage.database.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.lpiem.apprentisage.Consts;
import com.lpiem.apprentisage.database.ConfigDB;
import com.lpiem.apprentisage.database.DataBaseAccess;
import com.lpiem.apprentisage.metier.Enseignant;
import com.lpiem.apprentisage.metier.Exercice;
import com.lpiem.apprentisage.metier.Serie;

import java.util.ArrayList;


public class ExerciceDAO extends DataBaseAccess {
    public ExerciceDAO(Context context){
        super(context);
    }

    public long ajouter(Exercice exercice, Serie serie, Enseignant enseignant){
        EnseignantDAO mEnseignantDAO = new EnseignantDAO(mContext);
        long idEnseignant = mEnseignantDAO.enseignantIsDataBase(enseignant);
        if(idEnseignant <= 0){
            Log.d(Consts.TAG_APPLICATION + " : idEnseignant ", String.valueOf(idEnseignant));
            return -1;
        }

        SerieDAO mSerieDAO = new SerieDAO(mContext);
        long idSerie = mSerieDAO.serieIsDataBase(serie, idEnseignant);
        if(idSerie <= 0){
            Log.d(Consts.TAG_APPLICATION + " : idSerie ", String.valueOf(idSerie));
            return -1;
        }

        long idComparaison = exerciceIsDataBase(exercice, idEnseignant);
        if(idComparaison != -1 ){
            Log.d(Consts.TAG_APPLICATION + " : Exercice : idComparaison ", String.valueOf(idComparaison));
            return idComparaison;
        }

        ContentValues exerciceValue = new ContentValues();

        exerciceValue.put(ConfigDB.TABLE_EXERCICE_COL_ENONCE, exercice.getEnonce());
        exerciceValue.put(ConfigDB.TABLE_EXERCICE_COL_MEDIA, exercice.getMedia());
        exerciceValue.put(ConfigDB.TABLE_EXERCICE_COL_TYPE, exercice.getType());
        exerciceValue.put(ConfigDB.TABLE_EXERCICE_COL_RESPONSES, exercice.getResponses());
        exerciceValue.put(ConfigDB.TABLE_EXERCICE_COL_ID_SERIE, idSerie);

        return savingDataInDatabase(ConfigDB.TABLE_ELEVE, exerciceValue);
    }

    public ArrayList<Exercice> getExercicesBySeries(Serie serie, Enseignant enseignant){
        EnseignantDAO mEnseignantDAO = new EnseignantDAO(mContext);
        long idEnseignant = mEnseignantDAO.enseignantIsDataBase(enseignant);
        if(idEnseignant <= 0){
            Log.d(Consts.TAG_APPLICATION + " : idEnseignant ", String.valueOf(idEnseignant));
            return null;
        }

        SerieDAO mSerieDAO = new SerieDAO(mContext);
        long idSerie = mSerieDAO.serieIsDataBase(serie, idEnseignant);
        String sqlQuery =
                "SELECT * FROM " + ConfigDB.TABLE_EXERCICE  +
                        " WHERE " + ConfigDB.TABLE_EXERCICE + "." + ConfigDB.TABLE_EXERCICE_COL_ID_SERIE + " = '" + idSerie + "'";


        Cursor cursor = sqlRequest(sqlQuery);

        ArrayList<Exercice> exercices = new ArrayList<>();
        if((cursor.getCount() > 0) && (cursor.moveToFirst())){
            do {
                Exercice exercice = Cursor2Exercice(cursor);
                exercices.add(exercice);
            }while (cursor.moveToNext());
        }

        closeDataBase();
        return exercices;
    }

    public Exercice Cursor2Exercice(Cursor cursor) {
        Exercice exercice = new Exercice();

        exercice.setEnonce(cursor.getString(cursor.getColumnIndex(ConfigDB.TABLE_EXERCICE_COL_ENONCE)));
        exercice.setMedia(cursor.getString(cursor.getColumnIndex(ConfigDB.TABLE_EXERCICE_COL_MEDIA)));
        exercice.setType(cursor.getString(cursor.getColumnIndex(ConfigDB.TABLE_EXERCICE_COL_TYPE)));

        String responseString = cursor.getString(cursor.getColumnIndex(ConfigDB.TABLE_EXERCICE_COL_RESPONSES));
        ArrayList<String> responses = new ArrayList<>();

        String[] tabString = responseString.split(";");
        for (int i = 0; i < tabString.length; i++){
            responses.add(tabString[i]);
        }

        exercice.setResponses(responses);
        return exercice;
    }

    public long exerciceIsDataBase(Exercice exercice, long serie) {
        String sqlQuery =
                "SELECT * FROM " + ConfigDB.TABLE_EXERCICE +
                        " WHERE " + ConfigDB.TABLE_EXERCICE_COL_ENONCE + " = '" + exercice.getEnonce() + "'" +
                        " AND " + ConfigDB.TABLE_EXERCICE_COL_TYPE + " = '" + exercice.getType()  + "'" +
                        " AND " + ConfigDB.TABLE_EXERCICE_COL_ID_SERIE + " = '" + serie + "'";

        return idInDataBase(sqlQuery, ConfigDB.TABLE_EXERCICE_COL_ID);
    }
}
