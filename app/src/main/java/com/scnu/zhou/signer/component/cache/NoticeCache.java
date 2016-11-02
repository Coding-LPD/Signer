package com.scnu.zhou.signer.component.cache;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by zhou on 16/11/2.
 */
public class NoticeCache {

    private NoticeCache(){}

    public static NoticeCache getInstance(){

        return NoticeCacheHolder.instance;
    }

    private static class NoticeCacheHolder{

        private static final NoticeCache instance = new NoticeCache();
    }

    public int getNoticenNumber(Context context){

        SharedPreferences sharedPreferences = context.getSharedPreferences("Notice", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("number", 0);
    }

    public void setNoticenNumber(Context context, int number){

        SharedPreferences sharedPreferences = context.getSharedPreferences("Notice", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("number", number);
        editor.commit();
    }
}
