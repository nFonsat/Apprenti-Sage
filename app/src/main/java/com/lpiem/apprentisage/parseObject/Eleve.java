/**
 * Created by iem on 07/01/15.
 */

package com.lpiem.apprentisage.parseObject;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.ArrayList;

@ParseClassName("Eleve")
public class Eleve  extends ParseObject {
    private int mAvatar;

    public Eleve(){ }

    public Eleve(int avatar){
        mAvatar = avatar;
    }

    public String getNom() {
        return getString("nom");
    }

    public void setNom(String nom) {
        put("nom", nom);
    }

    public String getPrenom() {
        return getString("prenom");
    }

    public void setPrenom(String prenom) {
        put("prenom", prenom);
    }

    public String getUsername() {
        return getString("username");
    }

    public void setUsername(String username) {
        put("username", username);
    }

    public ArrayList<Resultat> getResultats() {
        return (ArrayList<Resultat>) get("resultats");
    }

    public void setResultats(ArrayList<Resultat> resultats) {
        put("resultats", resultats);
    }

    public int getAvatar(){
        return mAvatar;
    }

    public void setAvatar(int avatar){
        mAvatar = avatar;
    }
}
