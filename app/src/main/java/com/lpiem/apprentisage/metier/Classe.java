/**
 * Created by iem on 07/01/15.
 */
package com.lpiem.apprentisage.metier;

import java.util.ArrayList;

public class Classe extends BaseEntity {
    private String mNom;
    private String mNiveau;
    private int mAnnee;
    private ArrayList<Eleve> mEleves;

    public Classe() {
        super();
        mEleves = new ArrayList<>();
    }

    public String getNom() {
        return mNom;
    }

    public void setNom(String nom) {
        this.mNom = nom;
    }

    public String getNiveau() {
        return mNiveau;
    }

    public void setNiveau(String niveau) {
        this.mNiveau = niveau;
    }

    public int getAnnee() {
        return mAnnee;
    }

    public void setAnnee(int annee) {
        this.mAnnee = annee;
    }

    public ArrayList<Eleve> getEleves() {
        return mEleves;
    }

    public void setEleves(ArrayList<Eleve> eleves) {
        this.mEleves = eleves;
    }
}