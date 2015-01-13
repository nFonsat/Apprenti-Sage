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
import com.lpiem.apprentisage.jsonObject.Classe;
import com.lpiem.apprentisage.jsonObject.Eleve;

import java.util.ArrayList;

public class EleveDAO extends DataBaseAccess {

    public EleveDAO(Context context){
        super(context);
    }

    public long ajouter(Eleve eleve, Classe classe){
        ClasseDAO mClasseDAO = new ClasseDAO(mContext);
        long idComparaisonEleve = eleveIsDataBase(eleve);
        if(idComparaisonEleve != -1 ){
            Log.d(Consts.TAG_APPLICATION + " : insertEleve : Eleve : idComparaisonEleve ", String.valueOf(idComparaisonEleve));
            return idComparaisonEleve;
        }

        long idClasse = mClasseDAO.classeIsDataBase(classe);
        if(idClasse <= 0){
            Log.d(Consts.TAG_APPLICATION + " : insertEleve : Classe : idClasse ", String.valueOf(idClasse));
            return idClasse;
        }

        ContentValues eleveValue = new ContentValues();
        eleveValue.put(ConfigDB.TABLE_ELEVE_COL_NAME, eleve.getNom());
        eleveValue.put(ConfigDB.TABLE_ELEVE_COL_PRENOM, eleve.getPrenom());
        eleveValue.put(ConfigDB.TABLE_ELEVE_COL_USERNAME, eleve.getUsername());
        eleveValue.put(ConfigDB.TABLE_ELEVE_COL_AVATAR, eleve.getAvatar());
        eleveValue.put(ConfigDB.TABLE_ELEVE_COL_ID_CLASSE, idClasse);

        openDbWrite();
        return mDataBase.insert(ConfigDB.TABLE_ELEVE, null, eleveValue);
    }

    public ArrayList<Eleve> getElevesByClasse(Classe classe){
        ClasseDAO mClasseDAO = new ClasseDAO(mContext);
        long idClasse = mClasseDAO.classeIsDataBase(classe);
        String sqlQuery =
                "SELECT * FROM " + ConfigDB.TABLE_ELEVE  +
                        " WHERE " + ConfigDB.TABLE_ELEVE + "." + ConfigDB.TABLE_ELEVE_COL_ID_CLASSE + " = '" + idClasse + "'";

        openDbRead();
        Cursor cursor = mDataBase.rawQuery(sqlQuery, null);

        ArrayList<Eleve> eleves = new ArrayList<>();
        if((cursor.getCount() > 0) && (cursor.moveToFirst())){
            do {
                Eleve eleve = Cursor2Eleve(cursor);
                eleves.add(eleve);
            }while (cursor.isAfterLast());
        }

        closeDataBase();
        return eleves;
    }

    private Eleve Cursor2Eleve(Cursor cursor) {
        Eleve eleve = new Eleve();

        eleve.setNom(cursor.getString(cursor.getColumnIndex(ConfigDB.TABLE_ELEVE_COL_NAME)));
        eleve.setPrenom(cursor.getString(cursor.getColumnIndex(ConfigDB.TABLE_ELEVE_COL_PRENOM)));
        eleve.setUsername(cursor.getString(cursor.getColumnIndex(ConfigDB.TABLE_ELEVE_COL_USERNAME)));
        eleve.setAvatar(cursor.getString(cursor.getColumnIndex(ConfigDB.TABLE_ELEVE_COL_AVATAR)));

        return eleve;
    }

    public long eleveIsDataBase(Eleve eleve) {
        long trouver = -1;

        String sqlQuery =
                "SELECT * FROM " + ConfigDB.TABLE_ELEVE +
                        " WHERE " + ConfigDB.TABLE_ELEVE_COL_USERNAME + " = '" + eleve.getUsername() + "'" +
                        " AND " + ConfigDB.TABLE_ELEVE_COL_NAME + " = '" + eleve.getNom()  + "'" +
                        " AND " + ConfigDB.TABLE_ELEVE_COL_PRENOM + " = '" + eleve.getPrenom() + "'";

        openDbRead();
        Cursor cursor = mDataBase.rawQuery(sqlQuery, null);


        if((cursor.getCount() > 0) && (cursor.moveToFirst())){
            trouver = cursor.getLong(cursor.getColumnIndex(ConfigDB.TABLE_ELEVE_COL_ID));
        }

        closeDataBase();
        return trouver;
    }
}
