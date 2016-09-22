package com.scnu.zhou.signer.activity.sign;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scnu.zhou.signer.R;
import com.scnu.zhou.signer.activity.base.BaseSlideActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zhou on 2016/9/22.
 */
public class ConfirmSignActivity extends BaseSlideActivity{

    @Bind(R.id.tv_title) TextView tv_title;
    @Bind(R.id.ll_return) LinearLayout ll_return;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_confirm_sign);

        ButterKnife.bind(this);
        initView();
    }

    @Override
    public void initView() {

        tv_title.setText("课程详情");
        ll_return.setVisibility(View.VISIBLE);
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
}
