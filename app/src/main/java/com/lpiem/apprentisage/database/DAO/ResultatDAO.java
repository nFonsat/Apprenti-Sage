/**
 * Created by iem on 13/01/15.
 */
package com.lpiem.apprentisage.database.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.lpiem.apprentisage.database.ConfigDB;
import com.lpiem.apprentisage.database.DataBaseAccess;
import com.lpiem.apprentisage.metier.Eleve;
import com.lpiem.apprentisage.metier.Exercice;
import com.lpiem.apprentisage.metier.Resultat;
import com.lpiem.apprentisage.metier.Serie;
import com.lpiem.apprentisage.metier.TypeResultat;

import java.util.ArrayList;

public class ResultatDAO extends DataBaseAccess {
    Eleve mEleve;

    public ResultatDAO(Context context, Eleve eleve){
        super(context);
        mEleve = eleve;
    }

    public long ajouter(Resultat resultat){
        EleveDAO mEleveDAO = new EleveDAO(mContext);
        long idEleve = mEleveDAO.eleveIsDataBase(mEleve);
        if(!idIsConforme(idEleve)){
            return idEleve;
        }

        ContentValues resultatValue = new ContentValues();

        resultatValue.put(ConfigDB.TABLE_RESULTAT_COL_NAME, resultat.getNom());
        resultatValue.put(ConfigDB.TABLE_RESULTAT_COL_TYPE, resultat.getType());
        resultatValue.put(ConfigDB.TABLE_RESULTAT_COL_NOTE, resultat.getNote());
        resultatValue.put(ConfigDB.TABLE_RESULTAT_COL_ID_ELEVE, idEleve);
        resultatValue.put(ConfigDB.TABLE_RESULTAT_COL_ID_CORRESPONDANT, resultat.geIdTableCorrespondant());

        long idComparaisonResultat = resultatIsDataBase(resultat);
        if(idIsConforme(idComparaisonResultat)) {
            resultat.setId(idComparaisonResultat);
            return updateDataInDatabase(ConfigDB.TABLE_RESULTAT, resultatValue, idComparaisonResultat);
        }

        return savingDataInDatabase(ConfigDB.TABLE_RESULTAT, resultatValue);
    }

    public ArrayList<Resultat> getResultatsByEleve() {
        EleveDAO eleveDAO = new EleveDAO(mContext);
        mEleve.setId(eleveDAO.eleveIsDataBase(mEleve));

        String sqlQuery =
                "SELECT * FROM " + ConfigDB.TABLE_RESULTAT  +
                        " WHERE " + ConfigDB.TABLE_RESULTAT + "." + ConfigDB.TABLE_RESULTAT_COL_ID_ELEVE + " = '" + mEleve.getId() + "'";

        Cursor cursor = sqlRequest(sqlQuery);

        ArrayList<Resultat> resultats = new ArrayList<>();
        if((cursor.getCount() > 0) && (cursor.moveToFirst())){
            do {
                Resultat resultat = Cursor2Resultat(cursor);
                resultats.add(resultat);
            }while (cursor.moveToNext());
        }

        closeDataBase();
        return resultats;
    }

    public Resultat getResultatByExercice(Exercice exercice) {
        EleveDAO eleveDAO = new EleveDAO(mContext);
        mEleve.setId(eleveDAO.eleveIsDataBase(mEleve));

        String sqlQuery =
                "SELECT * FROM " + ConfigDB.TABLE_RESULTAT  +
                        " WHERE " + ConfigDB.TABLE_RESULTAT_COL_ID_ELEVE + " = '" + mEleve.getId() + "'" +
                        " AND " + ConfigDB.TABLE_RESULTAT_COL_TYPE + " = '" + TypeResultat.RESULTAT_EXERCICE.getType() + "'" +
                        " AND " + ConfigDB.TABLE_RESULTAT_COL_ID_CORRESPONDANT + " = '" + exercice.getId() + "'" ;

        Cursor cursor = sqlRequest(sqlQuery);

        Resultat resultat = null;
        Log.d("getResultatByExercice : cursor.getCount()", String.valueOf(cursor.getCount()));
        if((cursor.getCount() == 1) && (cursor.moveToFirst())){
            resultat = Cursor2Resultat(cursor);
        }

        closeDataBase();
        return resultat;
    }

    public ArrayList<Resultat> getResultatsBySerie(Serie serie) {
        EleveDAO eleveDAO = new EleveDAO(mContext);
        mEleve.setId(eleveDAO.eleveIsDataBase(mEleve));

        String sqlQuery =
                "SELECT * FROM " + ConfigDB.TABLE_RESULTAT  +
                        " WHERE " + ConfigDB.TABLE_RESULTAT_COL_ID_ELEVE + " = '" + mEleve.getId() + "'" +
                        " AND " + ConfigDB.TABLE_RESULTAT_COL_NAME + " = '" + serie.getActivite() + "'" +
                        " AND " + ConfigDB.TABLE_RESULTAT_COL_TYPE + " = '" + TypeResultat.RESULTAT_SERIE.getType() + "'" +
                        " AND " + ConfigDB.TABLE_RESULTAT_COL_ID_CORRESPONDANT + " = '" + serie.getId() + "'";

        Cursor cursor = sqlRequest(sqlQuery);

        ArrayList<Resultat> resultats = new ArrayList<>();
        Log.d("getResultatsBySerie : cursor.getCount()", String.valueOf(cursor.getCount()));
        if((cursor.getCount() > 0) && (cursor.moveToFirst())){
            do {
                Resultat resultat = Cursor2Resultat(cursor);
                resultats.add(resultat);
            }while (cursor.moveToNext());
        }

        closeDataBase();
        return resultats;
    }

    public ArrayList<Resultat> getResultatsByActivite(String activite) {
        EleveDAO eleveDAO = new EleveDAO(mContext);
        mEleve.setId(eleveDAO.eleveIsDataBase(mEleve));

        String sqlQuery =
                "SELECT * FROM " + ConfigDB.TABLE_RESULTAT  +
                        " WHERE " + ConfigDB.TABLE_RESULTAT_COL_ID_ELEVE + " = '" + mEleve.getId() + "'" +
                        " AND " + ConfigDB.TABLE_RESULTAT_COL_TYPE + " = '" + TypeResultat.RESULTAT_ACTIVITE.getType() + "'" +
                        " AND " + ConfigDB.TABLE_RESULTAT_COL_NAME + " = '" + activite + "'" ;

        Cursor cursor = sqlRequest(sqlQuery);

        ArrayList<Resultat> resultats = new ArrayList<>();
        Log.d("getResultatsByActivite : cursor.getCount()", String.valueOf(cursor.getCount()));
        if((cursor.getCount() > 0) && (cursor.moveToFirst())){
            do {
                Resultat resultat = Cursor2Resultat(cursor);
                resultats.add(resultat);
            }while (cursor.moveToNext());
        }

        closeDataBase();
        return resultats;
    }

    public ArrayList<Resultat> getResultatsByMatiere(Serie serie) {
        EleveDAO eleveDAO = new EleveDAO(mContext);
        mEleve.setId(eleveDAO.eleveIsDataBase(mEleve));
        String sqlQuery =
                "SELECT * FROM " + ConfigDB.TABLE_RESULTAT  +
                        " WHERE " + ConfigDB.TABLE_RESULTAT_COL_ID_ELEVE + " = '" + mEleve.getId() + "'" +
                        " AND " + ConfigDB.TABLE_RESULTAT_COL_TYPE + " = '" + TypeResultat.RESULTAT_MATIERE.getType() + "'" +
                        " AND " + ConfigDB.TABLE_RESULTAT_COL_NAME + " = '" + serie.getMatiere() + "'" ;

        Cursor cursor = sqlRequest(sqlQuery);

        ArrayList<Resultat> resultats = new ArrayList<>();
        Log.d("getResultatsByMatiere : cursor.getCount()", String.valueOf(cursor.getCount()));
        if((cursor.getCount() > 0) && (cursor.moveToFirst())){
            do {
                Resultat resultat = Cursor2Resultat(cursor);
                resultats.add(resultat);
            }while (cursor.moveToNext());
        }

        closeDataBase();
        return resultats;
    }

    public Resultat Cursor2Resultat(Cursor cursor) {
        Resultat resultat = new Resultat();

        resultat.setNom(cursor.getString(cursor.getColumnIndex(ConfigDB.TABLE_RESULTAT_COL_NAME)));
        resultat.setType(cursor.getString(cursor.getColumnIndex(ConfigDB.TABLE_RESULTAT_COL_TYPE)));
        resultat.setIdTableCorrespondant(cursor.getInt(cursor.getColumnIndex(ConfigDB.TABLE_RESULTAT_COL_ID_CORRESPONDANT)));
        resultat.setNote(cursor.getInt(cursor.getColumnIndex(ConfigDB.TABLE_RESULTAT_COL_NOTE)));

        return resultat;
    }

    public long resultatIsDataBase(Resultat resultat) {
        String sqlQuery =
                "SELECT * FROM " + ConfigDB.TABLE_RESULTAT +
                        " WHERE " + ConfigDB.TABLE_RESULTAT_COL_ID_ELEVE + " = '" + mEleve.getId() + "'" +
                        " AND " + ConfigDB.TABLE_RESULTAT_COL_TYPE + " = '" + resultat.getType()  + "'" +
                        " AND " + ConfigDB.TABLE_RESULTAT_COL_NAME + " = '" + resultat.getNom() + "'";

        return idInDataBase(sqlQuery, ConfigDB.TABLE_RESULTAT_COL_ID);
    }
}
