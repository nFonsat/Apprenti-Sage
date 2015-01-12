/**
 * Created by Nicolas on 11/01/2015.
 */
package com.lpiem.apprentisage.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.lpiem.apprentisage.Consts;
import com.lpiem.apprentisage.jsonObject.Classe;
import com.lpiem.apprentisage.jsonObject.Eleve;
import com.lpiem.apprentisage.jsonObject.Enseignant;

import java.util.ArrayList;

public class DataBaseAccess {
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDataBase;

    public DataBaseAccess(Context context){
        mDBHelper = new DatabaseHelper(context);
    }

    public long insertEnseignant(Enseignant enseignant) {
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

        openDbWrite();
        return mDataBase.insert(ConfigDB.TABLE_ENSEIGNANT, null, contentValues);
    }

    public long insertClasse(Classe classe, Enseignant enseignant) {
        long idComparaison = classeIsDataBase(classe);
        if(idComparaison != -1 ){
            Log.d(Consts.TAG_APPLICATION + " : insertClasse : Classe : idComparaion ", String.valueOf(idComparaison));
            return idComparaison;
        }

        ContentValues classeValues = new ContentValues();
        classeValues.put(ConfigDB.TABLE_CLASSE_COL_NAME, classe.getNom());
        classeValues.put(ConfigDB.TABLE_CLASSE_COL_LEVEL, classe.getNiveau());
        classeValues.put(ConfigDB.TABLE_CLASSE_COL_YEAR, classe.getAnnee());

        openDbWrite();
        long idClasse = mDataBase.insert(ConfigDB.TABLE_CLASSE, null, classeValues);

        long idEnseignant = enseignantIsDataBase(enseignant);
        createManyToManyEnseignantClasse(idClasse, idEnseignant);


        return idClasse;
    }

    private long createManyToManyEnseignantClasse(long idClasse, long idEnseignant){
        ContentValues manyToMany = new ContentValues();
        manyToMany.put(ConfigDB.TABLE_ENSEIGNANT_CLASSE_COL_ID_CLASSE, idClasse);
        manyToMany.put(ConfigDB.TABLE_ENSEIGNANT_CLASSE_COL_ID_ENSEIGNANT, idEnseignant);

        openDbWrite();
        return mDataBase.insert(ConfigDB.TABLE_ENSEIGNANT_CLASSE, null, manyToMany);
    }

    public long insertEleve(Eleve eleve, Classe classe){
        long idComparaisonEleve = eleveIsDataBase(eleve);
        if(idComparaisonEleve != -1 ){
            Log.d(Consts.TAG_APPLICATION + " : insertEleve : Eleve : idComparaisonEleve ", String.valueOf(idComparaisonEleve));
            return idComparaisonEleve;
        }

        long idClasse = classeIsDataBase(classe);
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

    public ArrayList<Enseignant> getEnseignants() {
        String sqlQuery = "SELECT * FROM " + ConfigDB.TABLE_ENSEIGNANT;

        openDbRead();
        Cursor cursor = mDataBase.rawQuery(sqlQuery, null);

        ArrayList<Enseignant> enseignants = new ArrayList<>();
        if((cursor.getCount() > 0) && (cursor.moveToFirst())){
            do {
                Enseignant enseignant = Cursor2Enseignant(cursor);
                enseignant.setClasses(getClassesByProf(enseignant));
                enseignants.add(enseignant);
            }while (cursor.isAfterLast());
        }

        closeDataBase();
        return enseignants;
    }

    public Enseignant getEnseignantByUsername(String username) {
        String sqlQuery =
                "SELECT * FROM " + ConfigDB.TABLE_ENSEIGNANT +
                        " WHERE " + ConfigDB.TABLE_ENSEIGNANT_COL_USERNAME + " = " + username;

        openDbRead();
        Cursor cursor = mDataBase.rawQuery(sqlQuery, null);

        if(cursor.getCount() > 1){
            return null;
        }

        Enseignant enseignant = new Enseignant();
        if((cursor.getCount() == 1) && (cursor.moveToFirst())){
            while (cursor.isAfterLast()){
                enseignant = Cursor2Enseignant(cursor);
                enseignant.setClasses(getClassesByProf(enseignant));
            }
        }

        mDataBase.close();
        return enseignant;
    }

    public ArrayList<Classe> getClassesByProf(Enseignant enseignant){
        long idEnseignant = enseignantIsDataBase(enseignant);
        String sqlQuery =
                "SELECT * FROM " + ConfigDB.TABLE_CLASSE  +
                        " JOIN " + ConfigDB.TABLE_ENSEIGNANT_CLASSE +
                        " ON " + ConfigDB.TABLE_ENSEIGNANT_CLASSE + "." + ConfigDB.TABLE_ENSEIGNANT_CLASSE_COL_ID_ENSEIGNANT + " = '" + idEnseignant + "'";


        openDbRead();
        Cursor cursor = mDataBase.rawQuery(sqlQuery, null);

        ArrayList<Classe> classes = new ArrayList<>();
        if((cursor.getCount() > 0) && (cursor.moveToFirst())){
            do {
                Classe classe = Cursor2Classe(cursor);
                classe.setEleves(getElevesByClasse(classe));
                classes.add(classe);
            }while (cursor.isAfterLast());
        }

        closeDataBase();
        return classes;
    }

    public ArrayList<Eleve> getElevesByClasse(Classe classe){
        long idClasse = classeIsDataBase(classe);
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

    private Enseignant Cursor2Enseignant(Cursor cursor) {
        Enseignant enseignant = new Enseignant();

        enseignant.setNom(cursor.getString(cursor.getColumnIndex(ConfigDB.TABLE_ENSEIGNANT_COL_NAME)));
        enseignant.setPrenom(cursor.getString(cursor.getColumnIndex(ConfigDB.TABLE_ENSEIGNANT_COL_PRENOM)));
        enseignant.setUsername(cursor.getString(cursor.getColumnIndex(ConfigDB.TABLE_ENSEIGNANT_COL_USERNAME)));
        enseignant.setAvatar(cursor.getString(cursor.getColumnIndex(ConfigDB.TABLE_ENSEIGNANT_COL_AVATAR)));
        enseignant.setEmail(cursor.getString(cursor.getColumnIndex(ConfigDB.TABLE_ENSEIGNANT_COL_EMAIL)));

        return enseignant;
    }

    private Classe Cursor2Classe(Cursor cursor) {
        Classe classe = new Classe();

        classe.setNom(cursor.getString(cursor.getColumnIndex(ConfigDB.TABLE_CLASSE_COL_NAME)));
        classe.setNiveau(cursor.getString(cursor.getColumnIndex(ConfigDB.TABLE_CLASSE_COL_LEVEL)));
        classe.setAnnee(cursor.getInt(cursor.getColumnIndex(ConfigDB.TABLE_CLASSE_COL_YEAR)));

        return classe;
    }

    private Eleve Cursor2Eleve(Cursor cursor) {
        Eleve eleve = new Eleve();

        eleve.setNom(cursor.getString(cursor.getColumnIndex(ConfigDB.TABLE_ELEVE_COL_NAME)));
        eleve.setPrenom(cursor.getString(cursor.getColumnIndex(ConfigDB.TABLE_ELEVE_COL_PRENOM)));
        eleve.setUsername(cursor.getString(cursor.getColumnIndex(ConfigDB.TABLE_ELEVE_COL_USERNAME)));
        eleve.setAvatar(cursor.getString(cursor.getColumnIndex(ConfigDB.TABLE_ELEVE_COL_AVATAR)));

        return eleve;
    }

    public long enseignantIsDataBase(Enseignant enseignant){
        openDbRead();

        long trouver = -1;

        String sqlQuery =
                "SELECT * FROM " + ConfigDB.TABLE_ENSEIGNANT +
                        " WHERE " + ConfigDB.TABLE_ENSEIGNANT_COL_USERNAME + " = '" + enseignant.getUsername() + "'" +
                        " AND " + ConfigDB.TABLE_ENSEIGNANT_COL_NAME + " = '" + enseignant.getNom()  + "'" +
                        " AND " + ConfigDB.TABLE_ENSEIGNANT_COL_PRENOM + " = '" + enseignant.getPrenom() + "'";

        Cursor cursor = mDataBase.rawQuery(sqlQuery, null);


        if((cursor.getCount() > 0) && (cursor.moveToFirst())){
            trouver = cursor.getLong(cursor.getColumnIndex(ConfigDB.TABLE_ENSEIGNANT_COL_ID));
        }

        return trouver;
    }

    public long classeIsDataBase(Classe classe){
        openDbRead();

        long trouver = -1;

        String sqlQuery =
                "SELECT * FROM " + ConfigDB.TABLE_CLASSE +
                        " WHERE " + ConfigDB.TABLE_CLASSE_COL_NAME + " = '" + classe.getNom() + "'" +
                        " AND " + ConfigDB.TABLE_CLASSE_COL_LEVEL + " = '" + classe.getNiveau()  + "'" +
                        " AND " + ConfigDB.TABLE_CLASSE_COL_YEAR + " = '" + classe.getAnnee() + "'";

        Cursor cursor = mDataBase.rawQuery(sqlQuery, null);


        if((cursor.getCount() > 0) && (cursor.moveToFirst())){
            trouver = cursor.getLong(cursor.getColumnIndex(ConfigDB.TABLE_CLASSE_COL_ID));
        }

        closeDataBase();
        return trouver;
    }

    public long eleveIsDataBase(Eleve eleve) {
        openDbRead();

        long trouver = -1;

        String sqlQuery =
                "SELECT * FROM " + ConfigDB.TABLE_ELEVE +
                        " WHERE " + ConfigDB.TABLE_ELEVE_COL_USERNAME + " = '" + eleve.getUsername() + "'" +
                        " AND " + ConfigDB.TABLE_ELEVE_COL_NAME + " = '" + eleve.getNom()  + "'" +
                        " AND " + ConfigDB.TABLE_ELEVE_COL_PRENOM + " = '" + eleve.getPrenom() + "'";

        Cursor cursor = mDataBase.rawQuery(sqlQuery, null);


        if((cursor.getCount() > 0) && (cursor.moveToFirst())){
            trouver = cursor.getLong(cursor.getColumnIndex(ConfigDB.TABLE_ELEVE_COL_ID));
        }

        closeDataBase();
        return trouver;
    }

    private void closeDataBase(){
        if(mDataBase != null && mDataBase.isOpen())
            mDataBase.close();
    }

    private void openDbRead(){
        if(mDataBase == null || !mDataBase.isReadOnly()){
            mDataBase = mDBHelper.getReadableDatabase();
        }
    }

    private void openDbWrite(){
        if(mDataBase == null || !mDataBase.isOpen()){
            mDataBase = mDBHelper.getWritableDatabase();
        }
    }
}
