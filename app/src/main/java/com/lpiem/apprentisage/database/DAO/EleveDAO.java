/**
 * Created by iem on 13/01/15.
 */
package com.lpiem.apprentisage.database.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.lpiem.apprentisage.database.ConfigDB;
import com.lpiem.apprentisage.database.DataBaseAccess;
import com.lpiem.apprentisage.metier.Classe;
import com.lpiem.apprentisage.metier.Eleve;

import java.util.ArrayList;

public class EleveDAO extends DataBaseAccess {

    public EleveDAO(Context context){
        super(context);
    }

    public long ajouter(Eleve eleve, Classe classe){
        long idClasse = classe.getId();
        if(!idIsConforme(idClasse)){
            ClasseDAO mClasseDAO = new ClasseDAO(mContext);
            idClasse = mClasseDAO.classeIsDataBase(classe);
            if(!idIsConforme(idClasse)){
                return -1;
            }
        }

        long idComparaisonEleve = eleveIsDataBase(eleve);
        if(idIsConforme(idComparaisonEleve)){
            eleve.setId(idComparaisonEleve);
            return idComparaisonEleve;
        }

        ContentValues eleveValue = new ContentValues();

        eleveValue.put(ConfigDB.TABLE_ELEVE_COL_NAME, eleve.getNom());
        eleveValue.put(ConfigDB.TABLE_ELEVE_COL_PRENOM, eleve.getPrenom());
        eleveValue.put(ConfigDB.TABLE_ELEVE_COL_USERNAME, eleve.getUsername());
        eleveValue.put(ConfigDB.TABLE_ELEVE_COL_AVATAR, eleve.getAvatar());
        eleveValue.put(ConfigDB.TABLE_ELEVE_COL_ID_CLASSE, idClasse);

        return savingDataInDatabase(ConfigDB.TABLE_ELEVE, eleveValue);
    }

    public ArrayList<Eleve> getElevesByClasse(Classe classe){
        ClasseDAO mClasseDAO = new ClasseDAO(mContext);
        long idClasse = mClasseDAO.classeIsDataBase(classe);
        String sqlQuery =
                "SELECT * FROM " + ConfigDB.TABLE_ELEVE  +
                        " WHERE " + ConfigDB.TABLE_ELEVE + "." + ConfigDB.TABLE_ELEVE_COL_ID_CLASSE + " = '" + idClasse + "'";


        Cursor cursor = sqlRequest(sqlQuery);

        ArrayList<Eleve> eleves = new ArrayList<>();
        if((cursor.getCount() > 0) && (cursor.moveToFirst())){
            do {
                Eleve eleve = Cursor2Eleve(cursor);
                eleves.add(eleve);
            }while (cursor.moveToNext());
        }

        closeDataBase();
        return eleves;
    }

    private Eleve Cursor2Eleve(Cursor cursor) {
        Eleve eleve = new Eleve();

        eleve.setId(cursor.getLong(cursor.getColumnIndex(ConfigDB.TABLE_ELEVE_COL_ID)));
        eleve.setNom(cursor.getString(cursor.getColumnIndex(ConfigDB.TABLE_ELEVE_COL_NAME)));
        eleve.setPrenom(cursor.getString(cursor.getColumnIndex(ConfigDB.TABLE_ELEVE_COL_PRENOM)));
        eleve.setUsername(cursor.getString(cursor.getColumnIndex(ConfigDB.TABLE_ELEVE_COL_USERNAME)));
        eleve.setAvatar(cursor.getString(cursor.getColumnIndex(ConfigDB.TABLE_ELEVE_COL_AVATAR)));

        return eleve;
    }

    public long eleveIsDataBase(Eleve eleve) {
        String sqlQuery =
                "SELECT * FROM " + ConfigDB.TABLE_ELEVE +
                        " WHERE " + ConfigDB.TABLE_ELEVE_COL_USERNAME + " = '" + eleve.getUsername() + "'" +
                        " AND " + ConfigDB.TABLE_ELEVE_COL_NAME + " = '" + eleve.getNom()  + "'" +
                        " AND " + ConfigDB.TABLE_ELEVE_COL_PRENOM + " = '" + eleve.getPrenom() + "'";

        return idInDataBase(sqlQuery, ConfigDB.TABLE_ELEVE_COL_ID);
    }
}
