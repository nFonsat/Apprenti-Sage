/**
 * Created by iem on 07/01/15.
 */
package com.lpiem.apprentisage.metier;

public class Resultat extends BaseEntity {
    private String mNom;
    private String mType;
    private int mNote;
    private int mIdTableCorrespondant; //Id de l'exercice, de la serie, de l'activite ou de la matiere Corespondant au resultat

    public Resultat(){
        super();
    }

    public String getNom(){
        return mNom;
    }

    public void setNom(String nom){
        mNom = nom;
    }

    public String getType(){
        return mType;
    }

    public void setType(String type){
        mType = type;
    }

    public int getNote(){
        return mNote;
    }

    public void setNote(int note){
        mNote = note;
    }

    public int geIdTableCorrespondant(){
        return mIdTableCorrespondant;
    }

    public void setIdTableCorrespondant(int idCorrespondant){
        mIdTableCorrespondant = idCorrespondant;
    }
}
