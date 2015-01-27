/**
 * Created by Nicolas on 26/01/2015.
 */
package com.lpiem.apprentisage.fragment;

import com.lpiem.apprentisage.metier.Exercice;

import java.io.IOException;

public interface FragmentAccess {
    public void exerciceSuccess() throws IOException;
    public void exerciceError(String laReponse);
    public void serieIsFinished();
    public Exercice changeExercice();
}