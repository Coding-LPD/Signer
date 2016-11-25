package com.scnu.zhou.signer.ui.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import com.scnu.zhou.signer.R;
import com.scnu.zhou.signer.component.cache.UserCache;
import com.scnu.zhou.signer.component.util.image.ImageLoaderUtil;
import com.scnu.zhou.signer.ui.activity.base.BaseActivity;
import com.scnu.zhou.signer.ui.activity.login.LoginActivity;

/**
 * Created by zhou on 16/9/2.
 */
public class SplashActivity extends BaseActivity {

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        handler = new Handler();
        initImageLoader();
    }

    private void initImageLoader(){

        new Thread(new Runnable() {
            @Override
            public void run() {

                ImageLoaderUtil.getInstance().initImageLoader(SplashActivity.this);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        turnToPage();
                    }
                }, 2000);
            }
        }).start();
    }

    /**
     * 根据情况进行页面跳转
     */
    private void turnToPage(){

        if (!TextUtils.isEmpty(UserCache.getInstance().getPhone(this)) &&
                !TextUtils.isEmpty(UserCache.getInstance().getPassword(this))){

            if (isStudent()) {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                finish();
            }
            else {
                Intent intent = new Intent(SplashActivity.this, MainActivity02.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                finish();
            }
        }
        else{
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            finish();
        }
    }
}
