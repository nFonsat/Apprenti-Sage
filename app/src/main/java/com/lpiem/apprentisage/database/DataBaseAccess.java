package com.lpiem.apprentisage.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.lpiem.apprentisage.Consts;
import com.lpiem.apprentisage.jsonObject.Classe;
import com.lpiem.apprentisage.jsonObject.Enseignant;

import java.util.ArrayList;

/**
 * Created by Nicolas on 11/01/2015.
 */
public class DataBaseAccess {
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDataBase;

    public DataBaseAccess(Context context){
        mDBHelper = new DatabaseHelper(context);
    }

    public long insertEnseignant(Enseignant enseignant) {
        mDataBase = mDBHelper.getWritableDatabase();
        long id;

        ContentValues contentValues = new ContentValues();
        contentValues.put(ConfigDB.TABLE_ENSEIGNANT_COL_NAME, enseignant.getNom());
        contentValues.put(ConfigDB.TABLE_ENSEIGNANT_COL_PRENOM, enseignant.getPrenom());
        contentValues.put(ConfigDB.TABLE_ENSEIGNANT_COL_USERNAME, enseignant.getUsername());
        contentValues.put(ConfigDB.TABLE_ENSEIGNANT_COL_EMAIL, enseignant.getEmail());
        contentValues.put(ConfigDB.TABLE_ENSEIGNANT_COL_AVATAR, enseignant.getAvatar());

        id = mDataBase.insert(ConfigDB.TABLE_ENSEIGNANT, null, contentValues);
        closeDataBase();

        return id;
    }

    public ArrayList<Enseignant> getEnseignants() {
        mDataBase = mDBHelper.getReadableDatabase();
        ArrayList<Enseignant> enseignants = new ArrayList<>();

        String sqlQuery = "SELECT * FROM " + ConfigDB.TABLE_ENSEIGNANT;

        Cursor cursor = mDataBase.rawQuery(sqlQuery, null);

        if((cursor.getCount() > 0) && (cursor.moveToFirst())){
            do {
                Enseignant enseignant = Cursor2Enseignant(cursor);
                enseignants.add(enseignant);
            }while (cursor.isAfterLast());
        }

        closeDataBase();
        
        return enseignants;
    }

    public Enseignant getEnseignantByUsername(String username) {
        mDataBase = mDBHelper.getReadableDatabase();
        Enseignant enseignant = new Enseignant();

        String sqlQuery =
                "SELECT * FROM " + ConfigDB.TABLE_ENSEIGNANT +
                        " WHERE " + ConfigDB.TABLE_ENSEIGNANT_COL_USERNAME + " = " + username;

        Cursor cursor = mDataBase.rawQuery(sqlQuery, null);

        if(cursor.getCount() > 1){
            return null;
        }

        if((cursor.getCount() == 1) && (cursor.moveToFirst())){
            while (cursor.isAfterLast()){
                enseignant = Cursor2Enseignant(cursor);
            }
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
        long trouver = -1;
        mDataBase = mDBHelper.getReadableDatabase();

        String sqlQuery =
                "SELECT * FROM " + ConfigDB.TABLE_ENSEIGNANT +
                        " WHERE " + ConfigDB.TABLE_ENSEIGNANT_COL_USERNAME + " = '" + enseignant.getUsername() + "'" +
                        " AND " + ConfigDB.TABLE_ENSEIGNANT_COL_NAME + " = '" + enseignant.getNom()  + "'" +
                        " AND " + ConfigDB.TABLE_ENSEIGNANT_COL_PRENOM + " = '" + enseignant.getPrenom() + "'";

        Cursor cursor = mDataBase.rawQuery(sqlQuery, null);
        closeDataBase();

        if((cursor.getCount() > 0) && (cursor.moveToFirst())){
            trouver = cursor.getLong(cursor.getColumnIndex(ConfigDB.TABLE_ENSEIGNANT_COL_ID));
        }

        return trouver;
    }

    public long insertClasse(Classe classe, Enseignant enseignant) {
        mDataBase = mDBHelper.getWritableDatabase();
        long idClasse;

        ContentValues classeValues = new ContentValues();
        classeValues.put(ConfigDB.TABLE_CLASSE_COL_NAME, classe.getNom());
        classeValues.put(ConfigDB.TABLE_CLASSE_COL_LEVEL, classe.getNiveau());
        classeValues.put(ConfigDB.TABLE_CLASSE_COL_YEAR, classe.getAnnee());

        idClasse = mDataBase.insert(ConfigDB.TABLE_CLASSE, null, classeValues);

        long idEnseignant = enseignantIsDataBase(enseignant);

        ContentValues manyToMany = new ContentValues();
        manyToMany.put(ConfigDB.TABLE_ENSEIGNANT_CLASSE_COL_ID_CLASSE, idClasse);
        manyToMany.put(ConfigDB.TABLE_ENSEIGNANT_CLASSE_COL_ID_ENSEIGNANT, idEnseignant);

        mDataBase.insert(ConfigDB.TABLE_ENSEIGNANT_CLASSE, null, manyToMany);
        closeDataBase();

        return idClasse;
    }

    private void closeDataBase(){
        if(mDataBase.isOpen())
            mDataBase.close();
    }
}
