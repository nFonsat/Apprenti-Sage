/**
 * Created by iem on 07/01/15.
 */

package com.lpiem.apprentisage.parseObject;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.ArrayList;

@ParseClassName("Matiere")
public class Matiere extends ParseObject {
    public Matiere(){ }

    public String getNom(){
        return getString("nom");
    }

    public void setNom(String nom){
        put("nom", nom);
    }

    public ArrayList<Activite> getActivites() {
        return (ArrayList<Activite>) get("activites");
    }

    public void setActivites(ArrayList<Activite> activites) {
        put("activites", activites);
    }
}
