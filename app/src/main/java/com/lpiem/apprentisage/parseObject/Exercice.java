/**
 * Created by iem on 07/01/15.
 */
package com.lpiem.apprentisage.parseObject;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.ArrayList;

@ParseClassName("Exercice")
public class Exercice extends ParseObject{
    public Exercice(){ }

    public String getEnonce(){
        return getString("enonce");
    }

    public void setEnonce(String enonce){
        put("enonce", enonce);
    }

    public String getType(){
        return getString("type_exercice");
    }

    public void setType(String type){
        put("type_exercice", type);
    }

    public String getMedia(){
        return getString("media");
    }

    public void setMedia(String media){
        put("media", media);
    }

    public ArrayList<String> getReponses() {
        return (ArrayList<String>) get("reponses");
    }

    public void setReponses(ArrayList<String> exercice) {
        put("exercice", exercice);
    }
}
