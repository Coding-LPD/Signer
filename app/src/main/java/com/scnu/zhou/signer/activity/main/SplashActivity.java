package com.scnu.zhou.signer.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import com.scnu.zhou.signer.R;
import com.scnu.zhou.signer.activity.base.BaseActivity;
import com.scnu.zhou.signer.activity.login.LoginActivity;
import com.scnu.zhou.signer.elss.engine.LoginEngine;
import com.scnu.zhou.signer.elss.util.image.ImageLoaderUtil;

/**
 * Created by zhou on 16/9/2.
 */
public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        initData();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                turnToPage();
            }
        }, 2000);
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

        ImageLoaderUtil.getInstance().initImageLoader(this);
    }

    @Override
    public void loadData() {

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
