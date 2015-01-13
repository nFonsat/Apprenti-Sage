/**
 * Created by iem on 07/01/15.
 */

package com.lpiem.apprentisage.metier;

import java.util.ArrayList;


public class Enseignant extends BaseEntity {
    private String mAvatar;
    private String mNom;
    private String mPrenom;
    private String mEmail;
    private String mUsername;
    private ArrayList<Classe> mClasses;

    public Enseignant() {
        super();
        mClasses = new ArrayList<>();
    }

    public String getAvatar(){
        return mAvatar;
    }

    public void setAvatar(String avatar){
        mAvatar = avatar;
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

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String username) {
        mEmail = username;
    }

    public String getUsername(){
        return mUsername;
    }

    public void setUsername(String username){
        mUsername =  username;
    }

    public ArrayList<Classe> getClasses() {
        return mClasses;
    }

    public void setClasses(ArrayList<Classe> classes) {
        mClasses = classes;
    }
}
