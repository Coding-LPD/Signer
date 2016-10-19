package com.scnu.zhou.signer.elss.engine;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by zhou on 16/9/5.
 */
public class RegistEngine {

    private Context context;

    public RegistEngine(Context context){
        this.context = context;
    }

    public void regist(String phone, String password){

        SharedPreferences sharedPreferences = context.getSharedPreferences("User", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("phone", phone);
        editor.putString("password", password);
        editor.commit();
    }
}
