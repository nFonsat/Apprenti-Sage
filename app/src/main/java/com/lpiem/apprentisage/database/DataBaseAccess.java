/**
 * Created by Nicolas on 11/01/2015.
 */
package com.lpiem.apprentisage.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DataBaseAccess {
    protected DatabaseHelper mDBHelper;
    protected SQLiteDatabase mDataBase;
    protected Context mContext;

    public DataBaseAccess(Context context){
        mContext = context;
        mDBHelper = new DatabaseHelper(mContext);
    }

    public void closeDataBase(){
        if(mDataBase != null && mDataBase.isOpen())
            mDataBase.close();
    }

    public void openDbRead(){
        if(mDataBase == null || !mDataBase.isReadOnly()){
            mDataBase = mDBHelper.getReadableDatabase();
        }
    }

    public void openDbWrite(){
        if(mDataBase == null || !mDataBase.isOpen()){
            mDataBase = mDBHelper.getWritableDatabase();
        }
    }

    public SQLiteDatabase getDataBase(){
        return mDataBase;
    }
}
