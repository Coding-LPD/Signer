package com.scnu.zhou.signer.ui.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import com.scnu.zhou.signer.R;
import com.scnu.zhou.signer.ui.activity.base.BaseActivity;
import com.scnu.zhou.signer.ui.activity.login.LoginActivity;
import com.scnu.zhou.signer.component.engine.LoginEngine;
import com.scnu.zhou.signer.component.util.image.ImageLoaderUtil;

/**
 * Created by zhou on 16/9/2.
 */
public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        ImageLoaderUtil.getInstance().initImageLoader(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                turnToPage();
            }
        }, 2000);
    }

    /**
     * 根据情况进行页面跳转
     */
    private void turnToPage(){

        LoginEngine engine = new LoginEngine(this);
        if (!TextUtils.isEmpty(engine.getUserPhone()) &&
                !TextUtils.isEmpty(engine.getUserPassword())){
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            finish();
        }
        else{
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            finish();
        }
    }
}
