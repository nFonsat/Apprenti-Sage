/**
 * Created by iem on 15/01/15.
 */
package com.lpiem.apprentisage.metier;

public enum TypeResultat {
    RESULTAT_EXERCICE("exercice"),
    RESULTAT_SERIE("serie"),
    RESULTAT_ACTIVITE("activite"),
    RESULTAT_MATIERE("matiere");

    private String mType;

    TypeResultat(String type){
        mType = type;
    }

    public String getType(){
        return mType;
    }

    @Override
    public String toString() {
        return mType;
    }
}
