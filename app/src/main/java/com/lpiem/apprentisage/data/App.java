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
import com.lpiem.apprentisage.model.Categorie;

import java.util.ArrayList;
import java.util.HashSet;

public class App {
    private static App mApplication;

    private Enseignant mCurrentEnseignant = new Enseignant();
    private Classe mCurrentClasse = new Classe();
    private Eleve mCurrentEleve = new Eleve();

    private ArrayList<Categorie> mCurrentCategories = new ArrayList<>();
    private ArrayList<Serie> mCurrentSeries = new ArrayList<>();

    private Categorie mCurrentCategorie;

    public static App getInstance(){
        if(mApplication == null){
            mApplication = new App();
        }
        return mApplication;
    }

    public void clear(){
        mCurrentEnseignant = null;
        mCurrentClasse = null;
        mCurrentEleve = null;
        mCurrentCategorie = null;

        mCurrentSeries = new ArrayList<>();
        mCurrentCategories = new ArrayList<>();
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

    public Categorie getCurrentCategorie(){
        return mCurrentCategorie;
    }

    public void setCurrentCategorie(Categorie categorie){
        mCurrentCategorie = categorie;
    }

    public ArrayList<Categorie> getCurrentCategories(){
        return mCurrentCategories;
    }

    public void setCurrentCategories(ArrayList<Categorie> categories){
        mCurrentCategories = categories;
    }

    public ArrayList<String> getMatieres(Context context){
        ArrayList<Serie> series = getSeries(context);

        HashSet<String> set = new HashSet<>();
        for (Serie uneSerie : series){
            set.add(uneSerie.getMatiere());
        }

        return new ArrayList<>(set);
    }

    public ArrayList<String> getActiviteByMatiere(Context context, String matiere){
        ArrayList<Serie> series = getSeries(context);

        if(matiere == null){
            matiere.equalsIgnoreCase(mCurrentCategorie.getNom());
        }

        HashSet<String> set = new HashSet<>();
        for (Serie uneSerie : series){
            if(uneSerie.getMatiere().equalsIgnoreCase(matiere))
                set.add(uneSerie.getActivite());
        }

        return new ArrayList<>(set);
    }

    public ArrayList<Categorie> generateCategorie(Context context){
        mCurrentSeries = getSeries(context);

        for (String matiere : getMatieres(context)){
            Categorie matiereCategorie = new Categorie(matiere);
            for(String activite : getActiviteByMatiere(context, matiere)){
                Categorie activiteCategorie = new Categorie(activite);

                for (Serie serie : mCurrentSeries){
                    if(serie.getActivite().equalsIgnoreCase(activite)){
                        activiteCategorie.getSerieList().add(serie);
                    }
                }

                matiereCategorie.getSubCategorie().add(activiteCategorie);
            }
            mCurrentCategories.add(matiereCategorie);
        }

        return mCurrentCategories;
    }
}
