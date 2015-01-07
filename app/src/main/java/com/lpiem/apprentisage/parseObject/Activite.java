/**
 * Created by iem on 07/01/15.
 */

package com.lpiem.apprentisage.parseObject;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.ArrayList;

@ParseClassName("Activite")
public class Activite extends ParseObject {
    public Activite(){ }

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

    public ArrayList<Serie> getSeries() {
        return (ArrayList<Serie>) get("series");
    }

    public void setSeries(ArrayList<Activite> series) {
        put("series", series);
    }
}
