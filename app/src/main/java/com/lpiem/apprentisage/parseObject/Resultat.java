/**
 * Created by iem on 07/01/15.
 */

package com.lpiem.apprentisage.parseObject;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Resultat")
public class Resultat extends ParseObject {
    public Resultat(){ }

    public String getNom(){
        return getString("nom");
    }

    public void setNom(String nom){
        put("nom", nom);
    }

    public String getType(){
        return getString("type_resultat");
    }

    public void setType(String type){
        put("type_resultat", type);
    }

    public int getNote(){
        return getInt("note");
    }

    public void setNote(int note){
        put("note", note);
    }
}
