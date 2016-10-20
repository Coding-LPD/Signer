package com.scnu.zhou.signer.component.cache;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by zhou on 2016/9/9.
 */
public class UserCache {

    private UserCache(){}

    private static class UserCacheHolder{

        private static final UserCache instance = new UserCache();
    }

    public static final UserCache getInstance(){

        return UserCacheHolder.instance;
    }

    public String getPhone(Context context){

        SharedPreferences sharedPreferences = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        return sharedPreferences.getString("phone", "");
    }

    public String getPassword(Context context){

        SharedPreferences sharedPreferences = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        return sharedPreferences.getString("password", "");
    }

    public void setName(Context context, String name){

        SharedPreferences sharedPreferences = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name", name);
        editor.commit();
    }

    public void setAvatar(Context context, String avatar){

        SharedPreferences sharedPreferences = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("avatar", avatar);
        editor.commit();
    }

    public String getName(Context context){

        SharedPreferences sharedPreferences = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        return sharedPreferences.getString("name", "");
    }

    public String getAvatar(Context context){

        SharedPreferences sharedPreferences = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        return sharedPreferences.getString("avatar", "");
    }


    public void login(Context context, String phone, String password){
        SharedPreferences sharedPreferences = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("phone", phone);
        editor.putString("password", password);
        editor.commit();
    }


    public void logout(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name", "");
        editor.putString("password", "");
        editor.commit();
    }
}
