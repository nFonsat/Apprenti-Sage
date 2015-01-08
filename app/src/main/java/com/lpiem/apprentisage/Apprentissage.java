package com.lpiem.apprentisage;

import com.lpiem.apprentisage.parseObject.Serie;
import com.lpiem.apprentisage.parseObject.Activite;
import com.lpiem.apprentisage.parseObject.Classe;
import com.lpiem.apprentisage.parseObject.Eleve;
import com.lpiem.apprentisage.parseObject.Enseignant;
import com.lpiem.apprentisage.parseObject.Exercice;
import com.lpiem.apprentisage.parseObject.Matiere;
import com.lpiem.apprentisage.parseObject.Resultat;
import com.parse.Parse;
import com.parse.ParseObject;

/**
 * Created by iem on 08/01/15.
 */
public class Apprentissage extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Enseignant.class);
        ParseObject.registerSubclass(Classe.class);
        ParseObject.registerSubclass(Eleve.class);
        ParseObject.registerSubclass(Activite.class);
        ParseObject.registerSubclass(Exercice.class);
        ParseObject.registerSubclass(Matiere.class);
        ParseObject.registerSubclass(Resultat.class);
        ParseObject.registerSubclass(Serie.class);

        Parse.initialize(this, "yxKdFS0RmDUbYEFCkLxOyp67TBydMBftMXh8mVrm", "oJUmo9DTjnoTXiM4wCdBnS1k13JkKkiM2q9F8pCv");
    }
}
