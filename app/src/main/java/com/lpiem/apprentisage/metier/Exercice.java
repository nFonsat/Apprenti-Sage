/**
 * Created by iem on 07/01/15.
 */
package com.lpiem.apprentisage.metier;


import java.util.ArrayList;

public class Exercice {
    private String mEnonce;
    private String mType;
    private String mMedia;
    private ArrayList<String> mResponses;

    public Exercice(String enonce, String type, String media, ArrayList<String> responses){
        mEnonce = enonce;
        mType = type;
        mMedia = media;
        mResponses = responses;
    }

    public Exercice() {

    }

    public String getEnonce(){
        return mEnonce;
    }

    public void setEnonce(String enonce){
        mEnonce = enonce;
    }

    public String getType(){
        return mType;
    }

    public void setType(String type){
        mType = type;
    }

    public String getMedia(){
        return mMedia;
    }

    public void setMedia(String media){
        mMedia = media;
    }

    public ArrayList<String> getResponses() {
        return mResponses;
    }

    public void setResponses(ArrayList<String> responses) {
        mResponses = responses;
    }
}