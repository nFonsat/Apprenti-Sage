/**
 * Created by Nicolas on 13/01/2015.
 */
package com.lpiem.apprentisage.applicatif;

import android.content.Context;

import com.lpiem.apprentisage.database.DAO.SerieDAO;

import com.lpiem.apprentisage.metier.Classe;
import com.lpiem.apprentisage.metier.Eleve;
import com.lpiem.apprentisage.metier.Enseignant;
import com.lpiem.apprentisage.metier.Serie;

import com.lpiem.apprentisage.model.Categorie;

import java.util.ArrayList;
import java.util.HashSet;

public class App {
    private static App mApplication;

    private Enseignant mCurrentEnseignant = null;
    private Classe mCurrentClasse = null;
    private Eleve mCurrentEleve = null;

    private Categorie mLastMatiereSelected = null;
    private Categorie mLastActiviteSelected = null;

    private ArrayList<Categorie> mCurrentMatieres = new ArrayList<>();
    private ArrayList<Serie> mCurrentSeries = new ArrayList<>();


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
        mCurrentSeries = new ArrayList<>();
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

    public Categorie getCurrentMatiere(){
        return mLastMatiereSelected;
    }

    public void setCurrentMatiere(Categorie categorie){
        mLastMatiereSelected = categorie;
    }

    public Categorie getCurrentActivite(){
        return mLastActiviteSelected;
    }

    public void setCurrentActivite(Categorie categorie){
        mLastActiviteSelected = categorie;
    }

    public ArrayList<Categorie> getCurrentMatieres(Context context){
        if(mCurrentMatieres.size() == 0){
            generateMatiere(context);
        }
        return mCurrentMatieres;
    }

    private ArrayList<Categorie> generateMatiere(Context context){
        ArrayList<Serie> series = getSeries(context);
        mCurrentMatieres = new ArrayList<>();

        for (String matiere : getListMatieres(context)){
            Categorie matiereCategorie = new Categorie(matiere);
            for(String activite : getActiviteByMatiere(context, matiere)){
                Categorie activiteCategorie = new Categorie(activite);

                for (Serie serie : series){
                    if(serie.getActivite().equalsIgnoreCase(activite)  && serie.getExercices().size() > 0){
                        activiteCategorie.getSerieList().add(serie);
                    }
                }

                matiereCategorie.getSubCategorie().add(activiteCategorie);
            }
            mCurrentMatieres.add(matiereCategorie);
        }

        return mCurrentMatieres;
    }

    public ArrayList<String> getListMatieres(Context context){
        ArrayList<Serie> series = getSeries(context);

        HashSet<String> set = new HashSet<>();
        for (Serie uneSerie : series){
            if(uneSerie.getExercices().size() > 0){
                set.add(uneSerie.getMatiere());
            }
        }

        return new ArrayList<>(set);
    }

    public ArrayList<String> getActiviteByMatiere(Context context, String matiere){
        ArrayList<Serie> series = getSeries(context);

        if(matiere == null){
            matiere = mLastMatiereSelected.getNom();
        }

        HashSet<String> set = new HashSet<>();
        for (Serie uneSerie : series){
            if(uneSerie.getMatiere().equalsIgnoreCase(matiere) && uneSerie.getExercices().size() > 0)
                set.add(uneSerie.getActivite());
        }

        return new ArrayList<>(set);
    }
}
