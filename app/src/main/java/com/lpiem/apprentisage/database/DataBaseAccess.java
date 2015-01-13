/**
 * Created by Nicolas on 11/01/2015.
 */
package com.lpiem.apprentisage.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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

    public long savingDataInDatabase(String table, ContentValues value){
        openDbWrite();
        long rowId = mDataBase.insert(table, null, value);
        closeDataBase();
        return rowId;
    }

    public long updateDataInDatabase(String table, ContentValues value, String selection, String[] selectionArgs){
        openDbWrite();
        long rowId = mDataBase.update(table, value, selection, selectionArgs);
        closeDataBase();
        return rowId;
    }

    public long deleteDataInDatabase(String table, String selection, String[] selectionArgs){
        openDbWrite();
        long rowId = mDataBase.delete(table, selection, selectionArgs);
        closeDataBase();
        return rowId;
    }

    public Cursor sqlRequest(String sqlQuery){
        openDbRead();
        return mDataBase.rawQuery(sqlQuery, null);
    }

    public long idInDataBase(String sqlQuery, String column){
        openDbRead();
        long value = -1;
        Cursor cursor = sqlRequest(sqlQuery);
        if((cursor.getCount() > 0) && (cursor.moveToFirst())){
            value = cursor.getLong(cursor.getColumnIndex(column));
        }
        closeDataBase();
        return value;
    }

    public boolean idIsConforme(long id){
        return (id > 0) ? true : false;
    }
}
