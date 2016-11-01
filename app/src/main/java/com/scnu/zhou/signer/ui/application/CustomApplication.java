package com.scnu.zhou.signer.ui.application;

import android.app.Application;

/**
 * Created by zhou on 16/11/1.
 */
public class CustomApplication extends Application {

    private static final String TAG = "Application";

    public static CustomApplication getInstance(){

        return CustomApplicationHolder.instance;
    }

    private static class CustomApplicationHolder{

        private static final CustomApplication instance = new CustomApplication();
    }
}
