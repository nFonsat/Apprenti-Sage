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
import com.lpiem.apprentisage.jsonObject.Enseignant;

import java.util.ArrayList;

public class ClasseDAO extends DataBaseAccess {

    public ClasseDAO(Context context){
        super(context);
    }

    public long ajouter(Classe classe, Enseignant enseignant) {
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

        EnseignantDAO mEnseignantDAO = new EnseignantDAO(mContext);
        long idEnseignant = mEnseignantDAO.enseignantIsDataBase(enseignant);
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

    public ArrayList<Classe> getClassesByProf(Enseignant enseignant){
        EnseignantDAO mEnseignantDAO = new EnseignantDAO(mContext);
        EleveDAO mEleveDAO = new EleveDAO(mContext);
        long idEnseignant = mEnseignantDAO.enseignantIsDataBase(enseignant);
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
                classe.setEleves(mEleveDAO.getElevesByClasse(classe));
                classes.add(classe);
            }while (cursor.isAfterLast());
        }

        closeDataBase();
        return classes;
    }

    private Classe Cursor2Classe(Cursor cursor) {
        Classe classe = new Classe();

        classe.setNom(cursor.getString(cursor.getColumnIndex(ConfigDB.TABLE_CLASSE_COL_NAME)));
        classe.setNiveau(cursor.getString(cursor.getColumnIndex(ConfigDB.TABLE_CLASSE_COL_LEVEL)));
        classe.setAnnee(cursor.getInt(cursor.getColumnIndex(ConfigDB.TABLE_CLASSE_COL_YEAR)));

        return classe;
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
}
