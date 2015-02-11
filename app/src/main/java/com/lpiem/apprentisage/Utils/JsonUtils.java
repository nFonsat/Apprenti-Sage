/**
 * Created by Nicolas on 10/01/2015.
 */
package com.lpiem.apprentisage.Utils;

import com.lpiem.apprentisage.metier.Classe;
import com.lpiem.apprentisage.metier.Eleve;
import com.lpiem.apprentisage.metier.Enseignant;
import com.lpiem.apprentisage.metier.Exercice;
import com.lpiem.apprentisage.metier.Resultat;
import com.lpiem.apprentisage.metier.Serie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonUtils {
    public static Enseignant jsonToEnseignant(JSONObject object) throws JSONException {
        Enseignant enseignant = new Enseignant();

        enseignant.setPrenom(object.getString("prenom"));

        enseignant.setNom(object.getString("nom"));

        enseignant.setUsername(object.getString("username"));

        //enseignant.setEmail(object.getString("email"));

        ArrayList<Classe> classes = new ArrayList<>();
        JSONArray classesJson = object.getJSONArray("classes");
        for (int i = 0; i < classesJson.length(); i++){
            Classe classe = jsonToClasse(classesJson.getJSONObject(i));
            classes.add(classe);
        }
        enseignant.setClasses(classes);

        return enseignant;
    }

    public static Classe jsonToClasse(JSONObject object) throws JSONException {
        Classe classe = new Classe();
        classe.setNom(object.getString("nom"));
        classe.setAnnee(object.getInt("annee"));
        classe.setNiveau(object.getString("niveau"));

        ArrayList<Eleve> eleves = new ArrayList<>();
        JSONArray elevesJson = object.getJSONArray("eleves");
        for (int i = 0; i < elevesJson.length(); i++){
            Eleve eleve = jsonToEleve(elevesJson.getJSONObject(i));
            eleves.add(eleve);
        }
        classe.setEleves(eleves);

        return classe;
    }

    public static Eleve jsonToEleve(JSONObject object) throws JSONException {
        Eleve eleve = new Eleve();
        //eleve.setAvatar(object.getInt("picture"));
        eleve.setNom(object.getString("nom"));
        eleve.setPrenom(object.getString("prenom"));
        eleve.setUsername(object.getString("username"));

        ArrayList<Resultat> resultats = new ArrayList<>();
        JSONArray resultatsJson = object.getJSONArray("resultats");
        for (int i = 0; i < resultatsJson.length(); i++){
            Resultat resultat = jsonToResultat(resultatsJson.getJSONObject(i));
            resultats.add(resultat);
        }
        eleve.setResultats(resultats);

        return eleve;
    }

    public static Resultat jsonToResultat(JSONObject object) throws JSONException {
        Resultat resultat = new Resultat();
        resultat.setNom(object.getString("nom"));
        resultat.setNote(object.getInt("note"));
        resultat.setType(object.getString("type"));
        return resultat;
    }

    public static Serie jsonToSerie(JSONObject object) throws JSONException {
        Serie serie = new Serie();
        serie.setNom(object.getString("nom"));
        //serie.setDescription(object.getString("description"));
        serie.setDifficulte(object.getInt("difficulte"));
        serie.setNiveau(object.getString("niveau"));
        serie.setPublic(object.getBoolean("public"));

        JSONObject activite = object.getJSONObject("activite");
        serie.setActivite(activite.getString("name"));

        JSONObject matiere = activite.getJSONObject("matiere");
        serie.setMatiere(matiere.getString("name"));

        ArrayList<Exercice> exercices = new ArrayList<>();
        JSONArray exercicesJson = object.getJSONArray("questions");
        for (int i = 0; i < exercicesJson.length(); i++){
            Exercice exercice = jsonToExercice(exercicesJson.getJSONObject(i));
            exercices.add(exercice);
        }
        serie.setExercices(exercices);

        return serie;
    }

    public static Exercice jsonToExercice(JSONObject object) throws JSONException {
        Exercice exercice = new Exercice();
        exercice.setEnonce(object.getString("label"));

        JSONObject media = object.getJSONObject("media");
        exercice.setType(media.getString("media"));

        //exercice.setMedia(object.getString(""));

        ArrayList<String> responses = new ArrayList<>();
        JSONArray responsesJson = object.getJSONArray("reponses");
        for (int i = 0; i < responsesJson.length(); i++){
            JSONObject objectReponse = responsesJson.getJSONObject(i);
            responses.add(objectReponse.getString("label"));
        }
        exercice.setResponses(responses);

        return exercice;
    }
}