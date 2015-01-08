/**
 * Created by iem on 07/01/15.
 */

package com.lpiem.apprentisage.parseObject;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.ArrayList;

@ParseClassName("Classe")
public class Classe extends ParseObject {
    public Classe(){ }

    public String getNom() {
        return getString("nom");
    }

    public void setNom(String nom) {
        put("nom", nom);
    }

    public int getNiveau() {
        return getInt("niveau");
    }

    public void setNiveau(String niveau) {
        put("niveau", niveau);
    }

    public ArrayList<Matiere> getMatieres() {
        return (ArrayList<Matiere>) get("matieres");
    }

    public void setMatieres(ArrayList<Matiere> matieres) {
        put("matieres", matieres);
    }

    public ArrayList<Eleve> getEleves() {
        return (ArrayList<Eleve>) get("eleves");
    }

    public void setEleves(ArrayList<Eleve> eleves) {
        put("eleves", eleves);
    }

    public ArrayList<Enseignant> getEnseignant() {
        return (ArrayList<Enseignant>) get("enseignants");
    }

    public void setEnseignant(ArrayList<Enseignant> enseignants) {
        put("enseignants", enseignants);
    }
}
