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
import com.lpiem.apprentisage.metier.Eleve;
import com.lpiem.apprentisage.metier.Resultat;

import java.util.ArrayList;

public class ResultatDAO extends DataBaseAccess {
    public ResultatDAO(Context context){
        super(context);

    }

    public long ajouter(Resultat resultat, Eleve eleve){
        EleveDAO mEleveDAO = new EleveDAO(mContext);
        long idEleve = mEleveDAO.eleveIsDataBase(eleve);
        if(idEleve <= 0){
            Log.d(Consts.TAG_APPLICATION + " : insertResultat : Eleve : idEleve ", String.valueOf(idEleve));
            return idEleve;
        }

        ContentValues resultatValue = new ContentValues();
        resultatValue.put(ConfigDB.TABLE_RESULTAT_COL_NAME, resultat.getNom());
        resultatValue.put(ConfigDB.TABLE_RESULTAT_COL_TYPE, resultat.getType());
        resultatValue.put(ConfigDB.TABLE_RESULTAT_COL_NOTE, resultat.getNote());
        resultatValue.put(ConfigDB.TABLE_RESULTAT_COL_ID_ELEVE, idEleve);

        long idComparaisonResultat = resultatIsDataBase(resultat, idEleve);
        if(idComparaisonResultat != -1 ) {
            Log.d(Consts.TAG_APPLICATION + " : insertResultat : Resulat : idComparaisonResultat ", String.valueOf(idComparaisonResultat));
            return mDataBase.update(ConfigDB.TABLE_RESULTAT, resultatValue, ConfigDB.TABLE_RESULTAT_COL_ID  + " LIKE ?", new String[] {String.valueOf(idComparaisonResultat)});
        }

        openDbWrite();
        return mDataBase.insert(ConfigDB.TABLE_RESULTAT, null, resultatValue);
    }

    public ArrayList<Resultat> getResultatsByEleve(Eleve eleve) {
        EleveDAO eleveDAO = new EleveDAO(mContext);
        long idEleve = eleveDAO.eleveIsDataBase(eleve);
        String sqlQuery =
                "SELECT * FROM " + ConfigDB.TABLE_RESULTAT  +
                        " WHERE " + ConfigDB.TABLE_RESULTAT + "." + ConfigDB.TABLE_RESULTAT_COL_ID_ELEVE + " = '" + idEleve + "'";

        openDbRead();
        Cursor cursor = mDataBase.rawQuery(sqlQuery, null);

        ArrayList<Resultat> resultats = new ArrayList<>();
        if((cursor.getCount() > 0) && (cursor.moveToFirst())){
            do {
                Resultat resultat = Cursor2Resultat(cursor);
                resultats.add(resultat);
            }while (cursor.isAfterLast());
        }

        closeDataBase();
        return resultats;
    }

    public Resultat Cursor2Resultat(Cursor cursor) {
        Resultat resultat = new Resultat();

        resultat.setNom(cursor.getString(cursor.getColumnIndex(ConfigDB.TABLE_RESULTAT_COL_NAME)));
        resultat.setType(cursor.getString(cursor.getColumnIndex(ConfigDB.TABLE_RESULTAT_COL_TYPE)));
        resultat.setNote(cursor.getInt(cursor.getColumnIndex(ConfigDB.TABLE_RESULTAT_COL_NOTE)));

        return resultat;
    }

    public long resultatIsDataBase(Resultat resultat, long idEleve) {
        long trouver = -1;

        String sqlQuery =
                "SELECT * FROM " + ConfigDB.TABLE_RESULTAT +
                        " WHERE " + ConfigDB.TABLE_RESULTAT_COL_NAME + " = '" + resultat.getNom() + "'" +
                        " AND " + ConfigDB.TABLE_RESULTAT_COL_TYPE + " = '" + resultat.getType()  + "'" +
                        " AND " + ConfigDB.TABLE_RESULTAT_COL_ID_ELEVE + " = '" + idEleve + "'";

        openDbRead();
        Cursor cursor = mDataBase.rawQuery(sqlQuery, null);


        if((cursor.getCount() > 0) && (cursor.moveToFirst())){
            trouver = cursor.getLong(cursor.getColumnIndex(ConfigDB.TABLE_RESULTAT_COL_ID));
        }

        closeDataBase();
        return trouver;
    }
}
