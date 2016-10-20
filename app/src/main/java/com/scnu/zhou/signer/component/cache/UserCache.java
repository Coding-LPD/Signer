package com.scnu.zhou.signer.component.cache;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by zhou on 2016/9/9.
 */
public class UserCache {

    public static String getPhone(Context context){

        SharedPreferences sharedPreferences = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        return sharedPreferences.getString("phone", "");
    }

    public static String getPassword(Context context){

        SharedPreferences sharedPreferences = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        return sharedPreferences.getString("password", "");
    }

    public static void setName(Context context, String name){

        SharedPreferences sharedPreferences = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name", name);
        editor.commit();
    }

    public static void setAvatar(Context context, String avatar){

        SharedPreferences sharedPreferences = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("avatar", avatar);
        editor.commit();
    }

    public static String getName(Context context){

        SharedPreferences sharedPreferences = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        return sharedPreferences.getString("name", "");
    }

    public static String getAvatar(Context context){

        SharedPreferences sharedPreferences = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        return sharedPreferences.getString("avatar", "");
    }


    public static void login(Context context, String phone, String password){
        SharedPreferences sharedPreferences = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("phone", phone);
        editor.putString("password", password);
        editor.commit();
    }


    public static void logout(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name", "");
        editor.putString("password", "");
        editor.commit();
    }
}
