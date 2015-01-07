/**
 * Created by iem on 07/01/15.
 */

package com.lpiem.apprentisage.parseObject;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.ArrayList;


@ParseClassName("Enseignant")
public class Enseignant extends ParseObject {
    public Enseignant(){ }

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

    public String getEmail() {
        return getString("email");
    }

    public void setEmail(String email) {
        put("email", email);
    }

    public ArrayList<Classe> getClasses() {
        return (ArrayList<Classe>) get("classes");
    }

    public void setClasses(ArrayList<Classe> classes) {
        put("classes", classes);
    }
}
