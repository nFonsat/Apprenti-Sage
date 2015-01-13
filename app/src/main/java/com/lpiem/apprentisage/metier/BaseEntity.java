/**
 * Created by iem on 13/01/15.
 */
package com.lpiem.apprentisage.metier;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class BaseEntity {
    protected long mId;
    protected Date mLastUsed;

    public BaseEntity() {
        mId = -1;
        mLastUsed = new Date();
    }

    public BaseEntity(int id, Date date) {
        mId = id;
        mLastUsed = date;
    }

    public long getId(){
        return mId;
    }

    public Date getLastUsed(){
        return mLastUsed;
    }

    public void setId(long id){
        mId = id;
    }

    public void setLastUsed(Date using){
        mLastUsed = using;
    }

    public JSONObject getJson() throws JSONException{
        JSONObject jsonObject = new JSONObject();

        jsonObject.put("id", getId());
        jsonObject.put("last_used", getLastUsed());

        return jsonObject;
    }

    /*public String toString() throws JSONException{
        return getJson().toString();
    }*/
}