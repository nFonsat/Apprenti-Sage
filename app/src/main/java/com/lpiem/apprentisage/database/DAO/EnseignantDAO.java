/**
 * Created by iem on 13/01/15.
 */
package com.lpiem.apprentisage.database.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.lpiem.apprentisage.database.ConfigDB;
import com.lpiem.apprentisage.database.DataBaseAccess;
import com.lpiem.apprentisage.metier.Enseignant;

import java.util.ArrayList;

public class EnseignantDAO extends DataBaseAccess {

    public EnseignantDAO(Context context) {
        super(context);
    }

    public long ajouter(Enseignant enseignant) {
        long idComparaison = enseignantIsDataBase(enseignant);
        if(idIsConforme(idComparaison) ){
            enseignant.setId(idComparaison);
            return idComparaison;
        }

        ContentValues enseignantValues = new ContentValues();

        enseignantValues.put(ConfigDB.TABLE_ENSEIGNANT_COL_NAME, enseignant.getNom());
        enseignantValues.put(ConfigDB.TABLE_ENSEIGNANT_COL_PRENOM, enseignant.getPrenom());
        enseignantValues.put(ConfigDB.TABLE_ENSEIGNANT_COL_USERNAME, enseignant.getUsername());
        enseignantValues.put(ConfigDB.TABLE_ENSEIGNANT_COL_EMAIL, enseignant.getEmail());
        enseignantValues.put(ConfigDB.TABLE_ENSEIGNANT_COL_AVATAR, enseignant.getAvatar());

        return savingDataInDatabase(ConfigDB.TABLE_ENSEIGNANT, enseignantValues);
    }

    public ArrayList<Enseignant> getEnseignants() {
        String sqlQuery = "SELECT * FROM " + ConfigDB.TABLE_ENSEIGNANT;

        Cursor cursor = sqlRequest(sqlQuery);

        ArrayList<Enseignant> enseignants = new ArrayList<>();
        ClasseDAO mClasseDAO = new ClasseDAO(mContext);
        if((cursor.getCount() > 0) && (cursor.moveToFirst())){
            do {
                Enseignant enseignant = Cursor2Enseignant(cursor);
                enseignant.setClasses(mClasseDAO.getClassesByProf(enseignant));
                enseignants.add(enseignant);
            }while (cursor.moveToNext());
        }

        closeDataBase();
        return enseignants;
    }

    private Enseignant Cursor2Enseignant(Cursor cursor) {
        Enseignant enseignant = new Enseignant();

        enseignant.setId(cursor.getInt(cursor.getColumnIndex(ConfigDB.TABLE_ENSEIGNANT_COL_ID)));
        enseignant.setNom(cursor.getString(cursor.getColumnIndex(ConfigDB.TABLE_ENSEIGNANT_COL_NAME)));
        enseignant.setPrenom(cursor.getString(cursor.getColumnIndex(ConfigDB.TABLE_ENSEIGNANT_COL_PRENOM)));
        enseignant.setUsername(cursor.getString(cursor.getColumnIndex(ConfigDB.TABLE_ENSEIGNANT_COL_USERNAME)));
        enseignant.setAvatar(cursor.getString(cursor.getColumnIndex(ConfigDB.TABLE_ENSEIGNANT_COL_AVATAR)));
        enseignant.setEmail(cursor.getString(cursor.getColumnIndex(ConfigDB.TABLE_ENSEIGNANT_COL_EMAIL)));

        return enseignant;
    }

    public long enseignantIsDataBase(Enseignant enseignant){
        String sqlQuery =
                "SELECT * FROM " + ConfigDB.TABLE_ENSEIGNANT +
                        " WHERE " + ConfigDB.TABLE_ENSEIGNANT_COL_USERNAME + " = '" + enseignant.getUsername() + "'" +
                        " AND " + ConfigDB.TABLE_ENSEIGNANT_COL_NAME + " = '" + enseignant.getNom()  + "'" +
                        " AND " + ConfigDB.TABLE_ENSEIGNANT_COL_PRENOM + " = '" + enseignant.getPrenom() + "'";

        return idInDataBase(sqlQuery, ConfigDB.TABLE_ENSEIGNANT_COL_ID);
    }
}
