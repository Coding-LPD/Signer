package com.scnu.zhou.signer.ui.activity.user.settings;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
public class PrivateActivity extends BaseSlideActivity{

    @Bind(R.id.ll_return) LinearLayout ll_return;
    @Bind(R.id.tv_title) TextView tv_title;

    @Bind(R.id.btn_radio01) Button btn_radio01;
    @Bind(R.id.btn_radio02) Button btn_radio02;
    @Bind(R.id.btn_radio03) Button btn_radio03;
    @Bind(R.id.btn_radio04) Button btn_radio04;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_private);

        ButterKnife.bind(this);
        initView();
    }

    public void initView() {

        ll_return.setVisibility(View.VISIBLE);
        tv_title.setText("隐私设置");
    }


    // 返回上一页面
    @OnClick(R.id.ll_return)
    public void back(){
        finish();
    }


    // 点击radio01
    @OnClick(R.id.btn_radio01)
    public void clickRadio01(){
        if (btn_radio01.isSelected()){
            btn_radio01.setSelected(false);
        }
        else{
            btn_radio01.setSelected(true);
        }
    }


    // 点击radio02
    @OnClick(R.id.btn_radio02)
    public void clickRadio02(){
        if (btn_radio02.isSelected()){
            btn_radio02.setSelected(false);
        }
        else{
            btn_radio02.setSelected(true);
        }
    }


    // 点击radio03
    @OnClick(R.id.btn_radio03)
    public void clickRadio03(){
        if (btn_radio03.isSelected()){
            btn_radio03.setSelected(false);
        }
        else{
            btn_radio03.setSelected(true);
        }
    }


    // 点击radio04
    @OnClick(R.id.btn_radio04)
    public void clickRadio04(){
        if (btn_radio04.isSelected()){
            btn_radio04.setSelected(false);
        }
        else{
            btn_radio04.setSelected(true);
        }
    }
}
