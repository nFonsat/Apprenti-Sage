/**
 * Created by iem on 07/01/15.
 */
package com.lpiem.apprentisage.parseObject;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.ArrayList;


@ParseClassName("Serie")
public class Serie extends ParseObject {
    public Serie(){ }

    public String getNom(){
        return getString("nom");
    }

    public void setNom(String nom){
        put("nom", nom);
    }

    public String getDescription(){
        return getString("descripton");
    }

    public void setDescription(String descripton){
        put("descripton", descripton);
    }

    public int getDificulte(){
        return getInt("difficulte");
    }

    public void setDificulte(int difficulte){
        put("difficulte", difficulte);
    }

    public ArrayList<Exercice> getExercices() {
        return (ArrayList<Exercice>) get("exercice");
    }

    public void setExercices(ArrayList<Exercice> exercice) {
        put("exercice", exercice);
    }
}
