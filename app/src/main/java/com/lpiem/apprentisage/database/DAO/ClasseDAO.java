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
import com.lpiem.apprentisage.metier.Enseignant;

import java.util.ArrayList;

public class ClasseDAO extends DataBaseAccess {

    public ClasseDAO(Context context){
        super(context);
    }

    public long ajouter(Classe classe, Enseignant enseignant) {
        long idComparaison = classeIsDataBase(classe);
        if(idIsConforme(idComparaison)){
            classe.setId(idComparaison);
            return idComparaison;
        }

        long idEnseignant = enseignant.getId();
        if(!idIsConforme(idEnseignant)){
            EnseignantDAO mEnseignantDAO = new EnseignantDAO(mContext);
            idEnseignant = mEnseignantDAO.enseignantIsDataBase(enseignant);
            if (!idIsConforme(idEnseignant))
                return -1;
        }

        ContentValues classeValue = new ContentValues();

        classeValue.put(ConfigDB.TABLE_CLASSE_COL_NAME, classe.getNom());
        classeValue.put(ConfigDB.TABLE_CLASSE_COL_LEVEL, classe.getNiveau());
        classeValue.put(ConfigDB.TABLE_CLASSE_COL_YEAR, classe.getAnnee());

        long idClasse = savingDataInDatabase(ConfigDB.TABLE_CLASSE, classeValue);
        createManyToManyEnseignantClasse(idClasse, idEnseignant);

        return idClasse;
    }

    private long createManyToManyEnseignantClasse(long idClasse, long idEnseignant){
        ContentValues manyToMany = new ContentValues();

        manyToMany.put(ConfigDB.TABLE_ENSEIGNANT_CLASSE_COL_ID_CLASSE, idClasse);
        manyToMany.put(ConfigDB.TABLE_ENSEIGNANT_CLASSE_COL_ID_ENSEIGNANT, idEnseignant);

        return savingDataInDatabase(ConfigDB.TABLE_ENSEIGNANT_CLASSE, manyToMany);
    }

    public ArrayList<Classe> getClassesByProf(Enseignant enseignant){
        long idEnseignant = enseignant.getId();

        if(!idIsConforme(idEnseignant)){
            EnseignantDAO mEnseignantDAO = new EnseignantDAO(mContext);
            idEnseignant = mEnseignantDAO.enseignantIsDataBase(enseignant);
            if(!idIsConforme(idEnseignant))
                return null;
        }

        String sqlQuery =
                "SELECT DISTINCT * FROM " + ConfigDB.TABLE_CLASSE  +
                        " JOIN " + ConfigDB.TABLE_ENSEIGNANT_CLASSE +
                        " ON " + ConfigDB.TABLE_ENSEIGNANT_CLASSE + "." + ConfigDB.TABLE_ENSEIGNANT_CLASSE_COL_ID_ENSEIGNANT + " = '" + idEnseignant + "'" +
                        " GROUP BY " + ConfigDB.TABLE_CLASSE + "."  + ConfigDB.TABLE_CLASSE_COL_ID;

        Cursor cursor = sqlRequest(sqlQuery);

        ArrayList<Classe> classes = new ArrayList<>();
        if((cursor.getCount() > 0) && (cursor.moveToFirst())){
            EleveDAO mEleveDAO = new EleveDAO(mContext);
            do {
                Classe classe = Cursor2Classe(cursor);
                classe.setEleves(mEleveDAO.getElevesByClasse(classe));
                classes.add(classe);
            }while (cursor.moveToNext());
        }

        closeDataBase();
        return classes;
    }

    private Classe Cursor2Classe(Cursor cursor) {
        Classe classe = new Classe();

        classe.setId(cursor.getLong(cursor.getColumnIndex(ConfigDB.TABLE_CLASSE_COL_ID)));
        classe.setNom(cursor.getString(cursor.getColumnIndex(ConfigDB.TABLE_CLASSE_COL_NAME)));
        classe.setNiveau(cursor.getString(cursor.getColumnIndex(ConfigDB.TABLE_CLASSE_COL_LEVEL)));
        classe.setAnnee(cursor.getInt(cursor.getColumnIndex(ConfigDB.TABLE_CLASSE_COL_YEAR)));

        return classe;
    }

    public long classeIsDataBase(Classe classe){
        String sqlQuery =
                "SELECT * FROM " + ConfigDB.TABLE_CLASSE +
                        " WHERE " + ConfigDB.TABLE_CLASSE_COL_NAME + " = '" + classe.getNom() + "'" +
                        " AND " + ConfigDB.TABLE_CLASSE_COL_LEVEL + " = '" + classe.getNiveau()  + "'" +
                        " AND " + ConfigDB.TABLE_CLASSE_COL_YEAR + " = '" + classe.getAnnee() + "'";

        return idInDataBase(sqlQuery, ConfigDB.TABLE_CLASSE_COL_ID);
    }
}
