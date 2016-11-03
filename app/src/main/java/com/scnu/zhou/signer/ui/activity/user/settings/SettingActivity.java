package com.scnu.zhou.signer.ui.activity.user.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scnu.zhou.signer.R;
import com.scnu.zhou.signer.ui.activity.base.BaseSlideActivity;
import com.scnu.zhou.signer.ui.activity.login.LoginActivity;
import com.scnu.zhou.signer.ui.activity.main.MainActivity;
import com.scnu.zhou.signer.component.cache.ClearCache;
import com.scnu.zhou.signer.component.cache.UserCache;
import com.scnu.zhou.signer.ui.widget.picker.MenuPicker;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zhou on 2016/9/11.
 */
public class SettingActivity extends BaseSlideActivity{

    @Bind(R.id.ll_return) LinearLayout ll_return;
    @Bind(R.id.tv_title) TextView tv_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_setting);

        ButterKnife.bind(this);
        initView();
    }

    public void initView() {

        ll_return.setVisibility(View.VISIBLE);
        tv_title.setText("设置");
    }


    // 返回上一页面
    @OnClick(R.id.ll_return)
    public void back(){
        finish();
    }


    // 修改密码
    @OnClick(R.id.tc_password)
    public void updatePassword(){

    }


    // 隐私设置
    @OnClick(R.id.tc_private)
    public void setPrivate(){
        Intent intent = new Intent(this, PrivateActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }


    // 关于Signer
    @OnClick(R.id.tc_about)
    public void about(){
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }


    // 意见反馈
    @OnClick(R.id.tc_feedback)
    public void feedback(){
        Intent intent = new Intent(this, FeedbackActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }


    // 清理缓存
    @OnClick(R.id.tc_clear)
    public void clearCache(){
        List<String> clear = new ArrayList<>();
        clear.add("清理缓存");
        new ClearPicker().show(this, clear);
    }


    // 退出登录
    @OnClick(R.id.tc_logout)
    public void logout(){
        List<String> logout = new ArrayList<>();
        logout.add("退出登录");
        new LogoutPicker().show(this, logout);
    }


    /**
     * 清理缓存picker
     */
    private class ClearPicker extends MenuPicker{

        @Override
        public void execute() {
            ClearCache.getInstance().clear(SettingActivity.this);
            this.dismiss();
        }
    }


    /**
     * 退出登录picker
     */
    private class LogoutPicker extends MenuPicker{

        @Override
        public void execute() {

            Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);

            UserCache.getInstance().logout(SettingActivity.this);

            MainActivity.getInstance().finish();
            finish();
        }
    }
}
