package com.scnu.zhou.signer.ui.activity.sign;

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
 * Created by zhou on 16/10/23.
 */
public class SignSuccessActivity extends BaseSlideActivity {

    @Bind(R.id.tv_title) TextView tv_title;
    @Bind(R.id.ll_return) LinearLayout ll_return;

    @Bind(R.id.tv_distance) TextView tv_distance;
    @Bind(R.id.tv_battery) TextView tv_battery;

    private int distance;
    private int battery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_success);

        ButterKnife.bind(this);
        initData();
        initView();
    }


    public void initView() {

        tv_title.setText("课程详情");
        ll_return.setVisibility(View.VISIBLE);

        tv_distance.setText(distance + " m");
        tv_battery.setText(battery + " %");
    }

    public void initData(){

        distance = getIntent().getIntExtra("distance", 0);
        battery = getIntent().getIntExtra("battery", 0);
    }

    // 返回上一页面
    @OnClick(R.id.ll_return)
    public void back(){
        finish();
    }
}
