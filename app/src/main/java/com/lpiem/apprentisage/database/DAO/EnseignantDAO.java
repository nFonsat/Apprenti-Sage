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

import java.util.ArrayList;

public class EnseignantDAO extends DataBaseAccess {

    public EnseignantDAO(Context context) {
        super(context);
    }

    public long ajouter(Enseignant enseignant) {
        long idComparaison = enseignantIsDataBase(enseignant);
        if(idComparaison != -1 ){
            Log.d(Consts.TAG_APPLICATION + " : insertEnseignant : Enseignant : idComparaion ", String.valueOf(idComparaison));
            return idComparaison;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(ConfigDB.TABLE_ENSEIGNANT_COL_NAME, enseignant.getNom());
        contentValues.put(ConfigDB.TABLE_ENSEIGNANT_COL_PRENOM, enseignant.getPrenom());
        contentValues.put(ConfigDB.TABLE_ENSEIGNANT_COL_USERNAME, enseignant.getUsername());
        contentValues.put(ConfigDB.TABLE_ENSEIGNANT_COL_EMAIL, enseignant.getEmail());
        contentValues.put(ConfigDB.TABLE_ENSEIGNANT_COL_AVATAR, enseignant.getAvatar());

        return savingDataInDatabase(ConfigDB.TABLE_ENSEIGNANT, contentValues);
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

    public Enseignant getEnseignantByUsername(String username) {
        String sqlQuery =
                "SELECT * FROM " + ConfigDB.TABLE_ENSEIGNANT +
                        " WHERE " + ConfigDB.TABLE_ENSEIGNANT_COL_USERNAME + " = " + username;

        Cursor cursor = sqlRequest(sqlQuery);

        if(cursor.getCount() > 1){
            return null;
        }

        Enseignant enseignant = new Enseignant();
        if((cursor.getCount() == 1) && (cursor.moveToFirst())){
            ClasseDAO mClasseDAO = new ClasseDAO(mContext);
            do {
                enseignant = Cursor2Enseignant(cursor);
                enseignant.setClasses(mClasseDAO.getClassesByProf(enseignant));
            }while (cursor.isAfterLast());
        }

        mDataBase.close();
        return enseignant;
    }

    private Enseignant Cursor2Enseignant(Cursor cursor) {
        Enseignant enseignant = new Enseignant();

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
