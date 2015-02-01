/**
 * Created by iem on 15/01/15.
 */
package com.lpiem.apprentisage.metier;

public enum TypeExercice {
    FragmentText("text"),
    FragmentAudio("audio"),
    FragmentCompter("compter"),;

    String mType;

    TypeExercice(String type){
        mType = type;
    }

    public String value() {
        return mType;
    }
}