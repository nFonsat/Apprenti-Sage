/**
 * Created by iem on 07/01/15.
 */
package com.lpiem.apprentisage.metier;

public class Resultat extends BaseEntity {
    private String mType;
    private String mNom;
    private int mNote;

    public Resultat(){
        super();
    }

    public String getNom() {
        return mNom;
    }

    public void setNom(String nom) {
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
}
