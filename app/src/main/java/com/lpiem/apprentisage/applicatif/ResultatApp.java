/**
 * Created by Nicolas on 25/01/2015.
 */
package com.lpiem.apprentisage.applicatif;

import android.content.Context;
import android.util.Log;

import com.lpiem.apprentisage.Consts;
import com.lpiem.apprentisage.database.DAO.ResultatDAO;
import com.lpiem.apprentisage.metier.Exercice;
import com.lpiem.apprentisage.metier.Resultat;
import com.lpiem.apprentisage.metier.Serie;
import com.lpiem.apprentisage.metier.TypeResultat;
import com.lpiem.apprentisage.model.Categorie;

import java.util.ArrayList;

public class ResultatApp {
    public static final String LOG = Consts.TAG_APPLICATION + " : " + ResultatApp.class.getSimpleName();

    private static ResultatApp mResultatApplication;

    private App mApplication = App.getInstance();
    private ResultatDAO mResultatDAO;

    public static ResultatApp getInstance(){
        if(mResultatApplication == null){
            mResultatApplication = new ResultatApp();
        }
        return mResultatApplication;
    }

    //Calcule les resultats de toutes les matieres
    public ArrayList<Resultat> calculateResultMatieres(Context context){
        mApplication = App.getInstance();
        ArrayList<Resultat> resultats = new ArrayList<>();

        for (Categorie matiere : mApplication.getCurrentMatieres(context)){
            resultats.add(calculateResultMatiere(context, matiere));
        }

        return resultats;
    }

    //Calculer le resultat d'une matiere par rapport à toutes ces activités
    public Resultat calculateResultMatiere(Context context, Categorie matiere){
        mApplication = App.getInstance();
        mResultatDAO = new ResultatDAO(context, mApplication.getCurrentEleve());
        ArrayList<Resultat> resultats = mResultatDAO.getResultatsByMatiere(matiere.getNom());

        int nombreTotalDeSerie = getNumberSerieByMatiere(context, matiere);
        int nombreDeResultatTrouve = getNumberResultatByMatiere(context,matiere);
        matiere.setPourcentage((nombreDeResultatTrouve * 100) / nombreTotalDeSerie);

        Resultat resultatMatiere = new Resultat();
        resultatMatiere.setNom(matiere.getNom());
        resultatMatiere.setType(TypeResultat.RESULTAT_MATIERE.getType());

        int noteActivite = 0;
        for (Resultat resultat : resultats){
            noteActivite += resultat.getNote();
        }
        resultatMatiere.setNote(noteActivite);

        long id = mResultatDAO.ajouter(resultatMatiere);
        resultatMatiere.setId(id);

        return resultatMatiere;
    }

    //Calculer le resultat d'une activité par rapport à toutes ces séries
    public Resultat calculateResultActivite(Context context, Categorie activite){
        mApplication = App.getInstance();
        mResultatDAO = new ResultatDAO(context, mApplication.getCurrentEleve());
        ArrayList<Resultat> resultats = mResultatDAO.getResultatsByActivite(activite.getNom());
        activite.setPourcentage((resultats.size()*100)/activite.getSerieList().size());

        Resultat resultatActivite = new Resultat();
        resultatActivite.setNom(mApplication.getCurrentMatiere().getNom() + ":" + activite.getNom());
        resultatActivite.setType(TypeResultat.RESULTAT_ACTIVITE.getType());

        int noteActivite = 0;
        for (Resultat resultat : resultats){
            noteActivite += resultat.getNote();
        }
        resultatActivite.setNote(noteActivite);

        long id = mResultatDAO.ajouter(resultatActivite);
        resultatActivite.setId(id);

        return resultatActivite;
    }

    //Calculer le resultat d'une série par rapport à toutes ces exercices
    public Resultat calculateResultSerie(Context context, Serie serie){
        mApplication = App.getInstance();
        Resultat resultatSerie = new Resultat();
        resultatSerie.setType(TypeResultat.RESULTAT_SERIE.getType());

        int noteSerie = 0;
        resultatSerie.setNom(mApplication.getCurrentActivite().getNom());
        resultatSerie.setIdTableCorrespondant(serie.getId());

        mResultatDAO = new ResultatDAO(context, mApplication.getCurrentEleve());
        for (Resultat unResultat : mResultatDAO.getResultatsBySerie(serie)){
            noteSerie += unResultat.getNote();
        }

        resultatSerie.setNote(noteSerie);
        mResultatDAO.ajouter(resultatSerie);

        return resultatSerie;
    }

    //Calculer le resultat d'un exercice;
    public Resultat calculateResultExercice(Context context, Exercice exercice, String maReponse){
        mApplication = App.getInstance();
        Resultat resultatExercice = new Resultat();
        resultatExercice.setType(TypeResultat.RESULTAT_EXERCICE.getType());


        resultatExercice.setNom(String.valueOf(mApplication.getCurrentSerie().getId()));
        resultatExercice.setIdTableCorrespondant(exercice.getId());

        ArrayList<String> lesReponses = exercice.getResponses();

        int i = 0;
        boolean reponseCorrecte = false;
        while((i < lesReponses.size()) && (!reponseCorrecte)){
            String uneReponse = lesReponses.get(i);
            if(uneReponse.equalsIgnoreCase(maReponse)){
                resultatExercice.setNote(1);
                reponseCorrecte = true;
            }
            i++;
        }

        mResultatDAO = new ResultatDAO(context, mApplication.getCurrentEleve());
        long id = mResultatDAO.ajouter(resultatExercice);
        resultatExercice.setId(id);

        return resultatExercice;
    }

    //Calculer le nombre de serie que comporte une matiere
    public int getNumberSerieByMatiere(Context context, Categorie matiere){
        mApplication = App.getInstance();
        int numberSerie =0;
        for (Serie uneSerie : mApplication.getSeries(context)){
            if(uneSerie.getMatiere().equalsIgnoreCase(matiere.getNom()) && uneSerie.getExercices().size() > 0)
                numberSerie++;
        }
        return numberSerie;
    }

    //Calculer le nombre de resultat que comporte une matiere
    public int getNumberResultatByMatiere(Context context, Categorie matiere){
        mApplication = App.getInstance();
        int numberResultat = 0;
        for (String activite : mApplication.getActiviteByMatiere(context, matiere.getNom())){
            for (Resultat unResultat : mResultatDAO.getResultatsByActivite(activite)){
                numberResultat++;
            }
        }

        return numberResultat;
    }

    public void updateResultatSerie(Context context, Serie serie){
        mApplication = App.getInstance();
        mResultatDAO = new ResultatDAO(context, mApplication.getCurrentEleve());

        //Suppression des resultats des exercices de la serie
        for (Resultat resultatExercice : mResultatDAO.getResultatsBySerie(serie)) {
            mResultatDAO.supprimer(resultatExercice);
        }

        //Suppression du resultat de la serie
        for (Resultat resultatSerie : mResultatDAO.getResultatsByActivite(mApplication.getCurrentActivite().getNom())){
            if (resultatSerie.getIdTableCorrespondant() == serie.getId()){
                mResultatDAO.supprimer(resultatSerie);
            }
        }
    }
}