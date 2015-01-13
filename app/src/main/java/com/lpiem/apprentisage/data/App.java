/**
 * Created by Nicolas on 13/01/2015.
 */
package com.lpiem.apprentisage.data;

import com.lpiem.apprentisage.metier.Classe;
import com.lpiem.apprentisage.metier.Eleve;
import com.lpiem.apprentisage.metier.Enseignant;

public class App {
    private static App mApplication;

    private Enseignant mCurrentEnseignant = new Enseignant();
    private Classe mCurrentClasse = new Classe();
    private Eleve mCurrentEleve = new Eleve();

    public static App getInstance(){
        if(mApplication == null){
            mApplication = new App();
        }
        return mApplication;
    }

    public Enseignant getCurrentEnseignant(){
        return mCurrentEnseignant;
    }

    public Classe getCurrentClasse(){
        return mCurrentClasse;
    }

    public Eleve getCurrentEleve(){
        return mCurrentEleve;
    }

    public void setCurrentEnseignant(Enseignant enseignant){
        mCurrentEnseignant = enseignant;
    }

    public void setCurrentClasse(Classe classe){
        mCurrentClasse = classe;
    }

    public void setCurrentEleve(Eleve eleve){
        mCurrentEleve = eleve;
    }
}
