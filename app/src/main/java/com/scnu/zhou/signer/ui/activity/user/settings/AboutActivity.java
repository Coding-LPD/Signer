package com.scnu.zhou.signer.ui.activity.user.settings;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scnu.zhou.signer.R;
import com.scnu.zhou.signer.ui.activity.base.BaseSlideActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zhou on 2016/9/12.
 */
public class AboutActivity extends BaseSlideActivity{

    @Bind(R.id.ll_return) LinearLayout ll_return;
    @Bind(R.id.tv_title) TextView tv_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_about);

        ButterKnife.bind(this);
        initView();
    }

    @Override
    public void initView() {

        ll_return.setVisibility(View.VISIBLE);
        tv_title.setText("关于Signer");
    }

    @Override
    public void initData() {

    }

    @Override
    public void loadData() {

    }


    // 返回上一页面
    @OnClick(R.id.ll_return)
    public void back(){
        finish();
    }


    // 检查更新
    @OnClick(R.id.tc_update)
    public void checkUpdate(){

    }


    // 官网
    @OnClick(R.id.tc_web)
    public void checkWeb(){

    }
}
