package com.scnu.zhou.signer.elss.engine;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by zhou on 16/9/5.
 */
public class LoginEngine {

    private Context context;

    public LoginEngine(Context context){
        this.context = context;
    }

    public void login(String phone, String password){

        SharedPreferences sharedPreferences = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("phone", phone);
        editor.putString("password", password);
        editor.commit();
    }

    public String getUserPhone(){

        SharedPreferences sharedPreferences = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        return sharedPreferences.getString("phone", "");
    }

    public String getUserPassword(){

        SharedPreferences sharedPreferences = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        return sharedPreferences.getString("password", "");
    }
}
