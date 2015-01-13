/**
 * Created by iem on 07/01/15.
 */
package com.lpiem.apprentisage.metier;


import java.util.ArrayList;

public class Exercice extends BaseEntity {
    private String mEnonce;
    private String mType;
    private String mMedia;
    private ArrayList<String> mResponses;

    public Exercice() {
        super();
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

    public String getResponses() {
        String response = "";
        for (String uneResponse : mResponses){
            response += uneResponse + ";";
        }
        return response;
    }

    public void setResponses(ArrayList<String> responses) {
        mResponses = responses;
    }
}