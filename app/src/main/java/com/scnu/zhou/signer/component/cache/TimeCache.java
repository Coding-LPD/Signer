package com.scnu.zhou.signer.component.cache;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by zhou on 16/11/18.
 */
public class TimeCache {

    private TimeCache(){}

    private static class TimeCacheHolder{

        private static final TimeCache instance = new TimeCache();
    }

    public static final TimeCache getInstance(){

        return TimeCacheHolder.instance;
    }

    public String getTime(Context context, String courseId){

        SharedPreferences sharedPreferences = context.getSharedPreferences("Time", Context.MODE_PRIVATE);
        return sharedPreferences.getString(courseId, "");
    }

    public void setTime(Context context, String courseId, String time){

        SharedPreferences sharedPreferences = context.getSharedPreferences("Time", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(courseId, time);
        editor.commit();
    }
}
