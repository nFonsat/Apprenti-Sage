/**
 * Created by iem on 07/01/15.
 */
package com.lpiem.apprentisage.metier;

import android.content.Context;
import android.util.Log;

import com.lpiem.apprentisage.database.DAO.ResultatDAO;

import java.util.ArrayList;

public class Serie extends BaseEntity {
    private String mNom;
    private String mDescription;
    private int mDifficulte;
    private String mMatiere;
    private String mActivite;
    private String mNiveau;
    private boolean mPublic;
    private ArrayList<Exercice> mExercices;

    public Serie() {
        super();
        mExercices = new ArrayList<>();
    }

    public String getNom(){
        return mNom;
    }

    public void setNom(String nom){
        mNom = nom;
    }

    public String getMatiere(){
        return mMatiere;
    }

    public void setMatiere(String matiere){
        mMatiere = matiere;
    }

    public String getActivite(){
        return mActivite;
    }

    public void setActivite(String activite){
        mActivite = activite;
    }

    public String getNiveau(){
        return mNiveau;
    }

    public void setNiveau(String niveau){
        mNiveau = niveau;
    }

    public String getDescription(){
        return mDescription;
    }

    public void setDescription(String descripton){
        mDescription = descripton;
    }

    public int getDifficulte(){
        return mDifficulte;
    }

    public void setDifficulte(int difficulte){
        mDifficulte = difficulte;
    }

    public boolean isPublic(){
        return mPublic;
    }

    public void setPublic(boolean isPublic){
        mPublic = isPublic;
    }

    public ArrayList<Exercice> getExercices() {
        return mExercices;
    }

    public void setExercices(ArrayList<Exercice> exercices) {
        mExercices = exercices;
    }

    public Exercice nextExercice(Eleve eleve, Context context) {
        Exercice nextExercice = null;

        ResultatDAO mResultatDAO = new ResultatDAO(context, eleve);
        for (Exercice exercice : mExercices){
            Log.d("Enonce de l'exercice", exercice.getEnonce());
            if(mResultatDAO.getResultatByExercice(exercice) == null)
                return exercice;
        }
        return nextExercice;
    }
}