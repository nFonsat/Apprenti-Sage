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
import com.lpiem.apprentisage.metier.Serie;

import java.util.ArrayList;

public class SerieDAO extends DataBaseAccess {

    public SerieDAO(Context context){
        super(context);
    }

    public long ajouter(Serie serie, Enseignant enseignant){
        EnseignantDAO mEnseignantDAO = new EnseignantDAO(mContext);
        long idEnseignant = mEnseignantDAO.enseignantIsDataBase(enseignant);
        if(!idIsConforme(idEnseignant)){
            return -1;
        }

        long idComparaison = serieIsDataBase(serie, idEnseignant);
        if(idIsConforme(idComparaison) ){
            serie.setId(idComparaison);
            return idComparaison;
        }

        ContentValues serieValue = new ContentValues();
        serieValue.put(ConfigDB.TABLE_SERIE_COL_NAME, serie.getNom());
        serieValue.put(ConfigDB.TABLE_SERIE_COL_DESC, serie.getDescription());
        serieValue.put(ConfigDB.TABLE_SERIE_COL_DIFF, serie.getDifficulte());
        serieValue.put(ConfigDB.TABLE_SERIE_COL_LEVEL, serie.getNiveau());
        serieValue.put(ConfigDB.TABLE_SERIE_COL_ACTIVITE, serie.getActivite());
        serieValue.put(ConfigDB.TABLE_SERIE_COL_MATIERE, serie.getMatiere());
        serieValue.put(ConfigDB.TABLE_SERIE_COL_IS_PUBLIC, serie.isPublic());
        serieValue.put(ConfigDB.TABLE_SERIE_COL_ID_ENSEIGNANT, idEnseignant);

        return savingDataInDatabase(ConfigDB.TABLE_SERIE, serieValue);
    }

    public ArrayList<Serie> getSeriesByClasse(Classe classe, Enseignant enseignant){
        String sqlQuery =
                "SELECT * FROM " + ConfigDB.TABLE_SERIE  +
                        " WHERE " + ConfigDB.TABLE_SERIE_COL_LEVEL + " = '" + classe.getNiveau() + "'" +
                        " AND " + ConfigDB.TABLE_SERIE_COL_ID_ENSEIGNANT  + " = '" + enseignant.getId() + "'";

        Cursor cursor = sqlRequest(sqlQuery);

        ArrayList<Serie> series = new ArrayList<>();
        ExerciceDAO mExerciceDAO = new ExerciceDAO(mContext);
        if((cursor.getCount() > 0) && (cursor.moveToFirst())){
            do {
                Serie serie = Cursor2Serie(cursor);
                serie.setExercices(mExerciceDAO.getExercicesBySeries(serie, enseignant));
                series.add(serie);
            }while (cursor.moveToNext());
        }

        closeDataBase();
        return series;
    }

    public Serie Cursor2Serie(Cursor cursor) {
        Serie serie = new Serie();

        serie.setId(cursor.getLong(cursor.getColumnIndex(ConfigDB.TABLE_SERIE_COL_ID)));
        serie.setNom(cursor.getString(cursor.getColumnIndex(ConfigDB.TABLE_SERIE_COL_NAME)));
        serie.setDescription(cursor.getString(cursor.getColumnIndex(ConfigDB.TABLE_SERIE_COL_DESC)));
        serie.setDifficulte(cursor.getInt(cursor.getColumnIndex(ConfigDB.TABLE_SERIE_COL_DIFF)));
        serie.setNiveau(cursor.getString(cursor.getColumnIndex(ConfigDB.TABLE_SERIE_COL_LEVEL)));
        serie.setActivite(cursor.getString(cursor.getColumnIndex(ConfigDB.TABLE_SERIE_COL_ACTIVITE)));
        serie.setMatiere(cursor.getString(cursor.getColumnIndex(ConfigDB.TABLE_SERIE_COL_MATIERE)));
        serie.setPublic(Boolean.valueOf(cursor.getString(cursor.getColumnIndex(ConfigDB.TABLE_SERIE_COL_IS_PUBLIC))));

        return serie;
    }

    public long serieIsDataBase(Serie serie, long idEnseignant) {
        String sqlQuery =
                "SELECT * FROM " + ConfigDB.TABLE_SERIE +
                        " WHERE " + ConfigDB.TABLE_SERIE_COL_NAME + " = '" + serie.getNom() + "'" +
                        " AND " + ConfigDB.TABLE_SERIE_COL_DIFF + " = '" + serie.getDifficulte()  + "'" +
                        " AND " + ConfigDB.TABLE_SERIE_COL_LEVEL + " = '" + serie.getNiveau()  + "'" +
                        " AND " + ConfigDB.TABLE_SERIE_COL_ACTIVITE + " = '" + serie.getActivite()  + "'" +
                        " AND " + ConfigDB.TABLE_SERIE_COL_MATIERE + " = '" + serie.getMatiere()  + "'" +
                        " AND " + ConfigDB.TABLE_SERIE_COL_ID_ENSEIGNANT + " = '" + idEnseignant + "'";

        return idInDataBase(sqlQuery, ConfigDB.TABLE_SERIE_COL_ID);
    }
}
