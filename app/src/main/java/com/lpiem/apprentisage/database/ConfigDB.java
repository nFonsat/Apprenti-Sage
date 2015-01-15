/**
 * Created by Nicolas on 11/01/2015.
 */
package com.lpiem.apprentisage.database;

public class ConfigDB {
    public static final String DATABASE_NAME = "apprenti_sage.db";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_ENSEIGNANT = "enseignant";
    public static final String TABLE_ENSEIGNANT_CLASSE = "classe";
    public static final String TABLE_CLASSE = "enseignant_classe";
    public static final String TABLE_ELEVE = "eleve";
    public static final String TABLE_RESULTAT = "resultat";
    public static final String TABLE_SERIE = "serie";
    public static final String TABLE_EXERCICE = "exercice";


    public static final String TABLE_ENSEIGNANT_COL_ID = "id_enseignant";
    public static final String TABLE_ENSEIGNANT_COL_NAME = "nom";
    public static final String TABLE_ENSEIGNANT_COL_PRENOM = "prenom";
    public static final String TABLE_ENSEIGNANT_COL_AVATAR = "avatar";
    public static final String TABLE_ENSEIGNANT_COL_EMAIL = "email";
    public static final String TABLE_ENSEIGNANT_COL_USERNAME = "username";
    public static final String CREATE_SCHEMA_ENSEIGNANT = "CREATE TABLE " + TABLE_ENSEIGNANT + "(" +
            TABLE_ENSEIGNANT_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE," +
            TABLE_ENSEIGNANT_COL_NAME + " TEXT," +
            TABLE_ENSEIGNANT_COL_PRENOM + " TEXT," +
            TABLE_ENSEIGNANT_COL_AVATAR + " TEXT," +
            TABLE_ENSEIGNANT_COL_EMAIL + " TEXT," +
            TABLE_ENSEIGNANT_COL_USERNAME + " TEXT UNIQUE)";


    public static final String TABLE_CLASSE_COL_ID = "id_classe";
    public static final String TABLE_CLASSE_COL_NAME = "nom";
    public static final String TABLE_CLASSE_COL_LEVEL = "niveau";
    public static final String TABLE_CLASSE_COL_YEAR = "annee";
    public static final String CREATE_SCHEMA_CLASSE = "CREATE TABLE " + TABLE_CLASSE + "(" +
            TABLE_CLASSE_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, " +
            TABLE_CLASSE_COL_NAME + " TEXT, " +
            TABLE_CLASSE_COL_LEVEL + " TEXT, " +
            TABLE_CLASSE_COL_YEAR + " INTEGER)";


    public static final String TABLE_ENSEIGNANT_CLASSE_COL_ID_ENSEIGNANT = "id_enseigant";
    public static final String TABLE_ENSEIGNANT_CLASSE_COL_ID_CLASSE = "id_classe";
    public static final String CREATE_SCHEMA_ENSEIGNANT_CLASSE = "CREATE TABLE " + TABLE_ENSEIGNANT_CLASSE + "(" +
            TABLE_ENSEIGNANT_CLASSE_COL_ID_ENSEIGNANT + " INTEGER, " +
            TABLE_ENSEIGNANT_CLASSE_COL_ID_CLASSE + " INTEGER, " +
            "FOREIGN KEY(" + TABLE_ENSEIGNANT_CLASSE_COL_ID_ENSEIGNANT + ") REFERENCES " + TABLE_ENSEIGNANT + "(" + TABLE_ENSEIGNANT_CLASSE_COL_ID_ENSEIGNANT + ") ON DELETE CASCADE, " +
            "FOREIGN KEY(" + TABLE_ENSEIGNANT_CLASSE_COL_ID_CLASSE + ") REFERENCES " + TABLE_CLASSE + "(" + TABLE_ENSEIGNANT_CLASSE_COL_ID_CLASSE + ") ON DELETE CASCADE)";


    public static final String TABLE_ELEVE_COL_ID = "id_eleve";
    public static final String TABLE_ELEVE_COL_NAME = "nom";
    public static final String TABLE_ELEVE_COL_PRENOM = "prenom";
    public static final String TABLE_ELEVE_COL_AVATAR = "avatar";
    public static final String TABLE_ELEVE_COL_USERNAME = "username";
    public static final String TABLE_ELEVE_COL_ID_CLASSE = "id_classe";
    public static final String CREATE_SCHEMA_ELEVE = "CREATE TABLE " + TABLE_ELEVE + "(" +
            TABLE_ELEVE_COL_ID + " INTEGER PRIMARY KEY  AUTOINCREMENT UNIQUE , " +
            TABLE_ELEVE_COL_NAME + " TEXT, " +
            TABLE_ELEVE_COL_PRENOM + " TEXT, " +
            TABLE_ELEVE_COL_AVATAR + " TEXT, " +
            TABLE_ELEVE_COL_USERNAME + " TEXT NOT NULL  UNIQUE, " +
            TABLE_ELEVE_COL_ID_CLASSE + " INTEGER, " +
            "FOREIGN KEY(" + TABLE_ELEVE_COL_ID_CLASSE + ") REFERENCES " + TABLE_CLASSE + "(" + TABLE_ELEVE_COL_ID_CLASSE + ") ON DELETE CASCADE)";


    public static final String TABLE_RESULTAT_COL_ID = "id_resultat";
    public static final String TABLE_RESULTAT_COL_NAME = "nom";
    public static final String TABLE_RESULTAT_COL_TYPE = "type";
    public static final String TABLE_RESULTAT_COL_NOTE = "note";
    public static final String TABLE_RESULTAT_COL_ID_CORRESPONDANT = "id_correspondant";
    public static final String TABLE_RESULTAT_COL_ID_ELEVE = "id_eleve";
    public static final String CREATE_SCHEMA_RESULTAT = "CREATE TABLE " + TABLE_RESULTAT + "(" +
            TABLE_RESULTAT_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, " +
            TABLE_RESULTAT_COL_NAME + " TEXT, " +
            TABLE_RESULTAT_COL_TYPE + " TEXT, " +
            TABLE_RESULTAT_COL_NOTE + " INTEGER, " +
            TABLE_RESULTAT_COL_ID_CORRESPONDANT + " INTEGER, " +
            TABLE_RESULTAT_COL_ID_ELEVE + " INTEGER NOT NULL  UNIQUE, " +
            "FOREIGN KEY(" + TABLE_RESULTAT_COL_ID_ELEVE + ") REFERENCES " + TABLE_ELEVE + "(" + TABLE_RESULTAT_COL_ID_ELEVE + ") ON DELETE CASCADE)";


    public static final String TABLE_SERIE_COL_ID = "id_serie";
    public static final String TABLE_SERIE_COL_NAME = "nom";
    public static final String TABLE_SERIE_COL_DESC = "description";
    public static final String TABLE_SERIE_COL_LEVEL = "niveau";
    public static final String TABLE_SERIE_COL_DIFF = "difficulte";
    public static final String TABLE_SERIE_COL_MATIERE = "matiere";
    public static final String TABLE_SERIE_COL_ACTIVITE = "activite";
    public static final String TABLE_SERIE_COL_IS_PUBLIC = "is_public";
    public static final String TABLE_SERIE_COL_ID_ENSEIGNANT = "id_enseignant";
    public static final String CREATE_SCHEMA_SERIE = "CREATE TABLE " + TABLE_SERIE + "(" +
            TABLE_SERIE_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, " +
            TABLE_SERIE_COL_NAME + " TEXT, " +
            TABLE_SERIE_COL_DESC + " TEXT, " +
            TABLE_SERIE_COL_LEVEL + " TEXT, " +
            TABLE_SERIE_COL_DIFF + " INTEGER, " +
            TABLE_SERIE_COL_MATIERE + " TEXT NOT NULL, " +
            TABLE_SERIE_COL_ACTIVITE + " TEXT NOT NULL, " +
            TABLE_SERIE_COL_IS_PUBLIC + " BOOL NOT NULL, " +
            TABLE_SERIE_COL_ID_ENSEIGNANT + " INTEGER NOT NULL, " +
            "FOREIGN KEY(" + TABLE_SERIE_COL_ID_ENSEIGNANT + ") REFERENCES " + TABLE_ENSEIGNANT + "(" + TABLE_SERIE_COL_ID_ENSEIGNANT + ") ON DELETE CASCADE)";


    public static final String TABLE_EXERCICE_COL_ID = "id_exercice";
    public static final String TABLE_EXERCICE_COL_ENONCE = "enonce";
    public static final String TABLE_EXERCICE_COL_TYPE = "type";
    public static final String TABLE_EXERCICE_COL_MEDIA = "media";
    public static final String TABLE_EXERCICE_COL_RESPONSES = "responses";
    public static final String TABLE_EXERCICE_COL_ID_SERIE = "id_serie";
    public static final String CREATE_SCHEMA_EXERCICE = "CREATE TABLE " + TABLE_EXERCICE + "(" +
            TABLE_EXERCICE_COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT UNIQUE, " +
            TABLE_EXERCICE_COL_ENONCE + " TEXT, " +
            TABLE_EXERCICE_COL_TYPE + " TEXT, " +
            TABLE_EXERCICE_COL_MEDIA + " TEXT, " +
            TABLE_EXERCICE_COL_RESPONSES + " TEXT, " +
            TABLE_EXERCICE_COL_ID_SERIE + " INTEGER NOT NULL, " +
            "FOREIGN KEY(" + TABLE_EXERCICE_COL_ID_SERIE + ") REFERENCES " + TABLE_SERIE + "(" + TABLE_EXERCICE_COL_ID_SERIE + ") ON DELETE CASCADE)";

    public static final String DELETE_TABLE_ENSEIGNANT = "DROP TABLE IF EXISTS " + TABLE_ENSEIGNANT;
    public static final String DELETE_TABLE_CLASSE = "DROP TABLE IF EXISTS " + TABLE_CLASSE;
    public static final String DELETE_TABLE_ENSEIGNANT_CLASSE = "DROP TABLE IF EXISTS " + TABLE_ENSEIGNANT_CLASSE;
    public static final String DELETE_TABLE_ELEVE = "DROP TABLE IF EXISTS " + TABLE_ELEVE;
    public static final String DELETE_TABLE_RESULTAT = "DROP TABLE IF EXISTS " + TABLE_RESULTAT;
    public static final String DELETE_TABLE_SERIE = "DROP TABLE IF EXISTS " + TABLE_SERIE;
    public static final String DELETE_TABLE_EXERCICE = "DROP TABLE IF EXISTS " + TABLE_EXERCICE;
}
