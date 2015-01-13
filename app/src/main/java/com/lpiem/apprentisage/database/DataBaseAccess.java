package com.lpiem.apprentisage.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.lpiem.apprentisage.Consts;
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
        Long id;

        ContentValues contentValues = new ContentValues();
        contentValues.put(ConfigDB.TABLE_ENSEIGNANT_COL_NAME, enseignant.getNom());
        contentValues.put(ConfigDB.TABLE_ENSEIGNANT_COL_PRENOM, enseignant.getPrenom());
        contentValues.put(ConfigDB.TABLE_ENSEIGNANT_COL_USERNAME, enseignant.getUsername());
        contentValues.put(ConfigDB.TABLE_ENSEIGNANT_COL_EMAIL, enseignant.getEmail());
        contentValues.put(ConfigDB.TABLE_ENSEIGNANT_COL_AVATAR, enseignant.getAvatar());

        id = mDataBase.insert(ConfigDB.TABLE_ENSEIGNANT, null, contentValues);
        mDataBase.close();


        Log.d(Consts.TAG_APPLICATION + " : DatabaseAccess : element id ", String.valueOf(id));

        return id;
    }

    public ArrayList<Enseignant> getEnseignants() {
        mDataBase = mDBHelper.getReadableDatabase();
        ArrayList<Enseignant> enseignants = new ArrayList<>();

        String sqlQuery = "SELECT * FROM " + ConfigDB.TABLE_ENSEIGNANT;

        Cursor cursor = mDataBase.rawQuery(sqlQuery, null);

        Log.d(Consts.TAG_APPLICATION + " : DatabaseAccess : element cursor ", String.valueOf(cursor.getCount()));
        if((cursor.getCount() > 0) && (cursor.moveToFirst())){
            Log.d(Consts.TAG_APPLICATION + " : DatabaseAccess : ", "In if((cursor.getCount() > 0) && (cursor.moveToFirst()))");
            do {
                Log.d(Consts.TAG_APPLICATION + " : DatabaseAccess : ", "In while (cursor.isAfterLast())");
                Enseignant enseignant = Cursor2Enseignant(cursor);
                Log.d(Consts.TAG_APPLICATION + " : DatabaseAccess : dbAccess : list Enseignant ", enseignant.toString());
                enseignants.add(enseignant);
            }while (cursor.isAfterLast());
        }

        mDataBase.close();
        
        return enseignants;
    }



    public Enseignant getEnseignant(String info) {
        mDataBase = mDBHelper.getReadableDatabase();
        Enseignant enseignant = new Enseignant();

        String sqlQuery =
                "SELECT * FROM " + ConfigDB.TABLE_ENSEIGNANT +
                        " WHERE " + ConfigDB.TABLE_ENSEIGNANT_COL_USERNAME + " = " + info;

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
}
