/**
 * Created by Nicolas on 13/01/2015.
 */
package com.lpiem.apprentisage.data;

import android.content.Context;

import com.lpiem.apprentisage.database.DAO.SerieDAO;
import com.lpiem.apprentisage.metier.Classe;
import com.lpiem.apprentisage.metier.Eleve;
import com.lpiem.apprentisage.metier.Enseignant;
import com.lpiem.apprentisage.metier.Serie;

import java.util.ArrayList;
import java.util.HashSet;

public class App {
    private static App mApplication;

    private Enseignant mCurrentEnseignant = new Enseignant();
    private Classe mCurrentClasse = new Classe();
    private Eleve mCurrentEleve = new Eleve();
    private ArrayList<Serie> mCurrentSeries;
    private String mCurrentMatiere;
    private String mCurrentActivite;

    public static App getInstance(){
        if(mApplication == null){
            mApplication = new App();
        }
        return mApplication;
    }

    public Enseignant getCurrentEnseignant(){
        return mCurrentEnseignant;
    }

    public Classe getCurrentClasse(){
        return mCurrentClasse;
    }

    public Eleve getCurrentEleve(){
        return mCurrentEleve;
    }

    public void setCurrentEnseignant(Enseignant enseignant){
        mCurrentEnseignant = enseignant;
    }

    public void setCurrentClasse(Classe classe){
        mCurrentClasse = classe;
    }

    public void setCurrentEleve(Eleve eleve){
        mCurrentEleve = eleve;
    }

    public ArrayList<Serie> getSeries(Context context){
        if(mCurrentSeries == null || mCurrentSeries.size() == 0){
            SerieDAO serieDAO = new SerieDAO(context);
            mCurrentSeries = serieDAO.getSeriesByClasse(mCurrentClasse, mCurrentEnseignant);
        }
        return mCurrentSeries;
    }

    public String getCurrentMatiere(){
        return mCurrentMatiere;
    }

    public String getCurrentActivite(){
        return mCurrentActivite;
    }

    public void setCurrentMatiere(String matiere){
        mCurrentMatiere = matiere;
    }

    public void setCurrentActivite(String activite){
        mCurrentActivite = activite;
    }

    public ArrayList<String> getMatieres(Context context){
        ArrayList<Serie> series = getSeries(context);

        HashSet<String> set = new HashSet<>();
        for (Serie uneSerie : series){
            set.add(uneSerie.getMatiere());
        }

        return new ArrayList<>(set);
    }

    public ArrayList<String> getActiviteByMatiere(Context context){
        ArrayList<Serie> series = getSeries(context);

        HashSet<String> set = new HashSet<>();
        for (Serie uneSerie : series){
            if(uneSerie.getMatiere().equalsIgnoreCase(mCurrentActivite))
                set.add(uneSerie.getActivite());
        }

        return new ArrayList<>(set);
    }
}
