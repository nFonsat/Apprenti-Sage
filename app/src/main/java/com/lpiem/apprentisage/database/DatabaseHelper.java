/**
 * Created by Nicolas on 11/01/2015.
 */
package com.lpiem.apprentisage.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.lpiem.apprentisage.Utils.Consts;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String LOG = Consts.TAG_APPLICATION + " : " + DatabaseHelper.class.getSimpleName();

    public DatabaseHelper(Context context) {
        super(context, ConfigDB.DATABASE_NAME, null, ConfigDB.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(LOG, "Creation database");
        db.execSQL(ConfigDB.CREATE_SCHEMA_ENSEIGNANT);
        db.execSQL(ConfigDB.CREATE_SCHEMA_CLASSE);
        db.execSQL(ConfigDB.CREATE_SCHEMA_ENSEIGNANT_CLASSE);
        db.execSQL(ConfigDB.CREATE_SCHEMA_ELEVE);
        db.execSQL(ConfigDB.CREATE_SCHEMA_RESULTAT);
        db.execSQL(ConfigDB.CREATE_SCHEMA_SERIE);
        db.execSQL(ConfigDB.CREATE_SCHEMA_EXERCICE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(LOG, "Upgrade database");
        db.execSQL(ConfigDB.DELETE_TABLE_EXERCICE);
        db.execSQL(ConfigDB.DELETE_TABLE_SERIE);
        db.execSQL(ConfigDB.DELETE_TABLE_RESULTAT);
        db.execSQL(ConfigDB.DELETE_TABLE_ELEVE);
        db.execSQL(ConfigDB.DELETE_TABLE_ENSEIGNANT_CLASSE);
        db.execSQL(ConfigDB.DELETE_TABLE_CLASSE);
        db.execSQL(ConfigDB.DELETE_TABLE_ENSEIGNANT);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(LOG, "Downgrade database");
        onUpgrade(db, oldVersion, newVersion);
    }
}
