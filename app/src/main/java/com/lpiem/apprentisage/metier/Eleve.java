/**
 * Created by iem on 07/01/15.
 */

package com.lpiem.apprentisage.metier;

import java.io.Serializable;
import java.util.ArrayList;

public class Eleve extends BaseEntity {
    private String mAvatar;
    private String mNom;
    private String mPrenom;
    private String mUsername;
    private ArrayList<Resultat> mResultats;

    public Eleve(){
        super();
    }

    public String getNom() {
        return mNom;
    }

    public void setNom(String nom) {
        mNom = nom;
    }

    public String getPrenom() {
        return mPrenom;
    }

    public void setPrenom(String prenom) {
        mPrenom = prenom;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        mUsername = username;
    }

    public ArrayList<Resultat> getResultats() {
        return mResultats;
    }

    public void setResultats(ArrayList<Resultat> resultats) {
        mResultats =  resultats;
    }

    public String getAvatar(){
        return mAvatar;
    }

    public void setAvatar(String avatar){
        mAvatar = avatar;
    }
}