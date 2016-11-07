package com.scnu.zhou.signer.ui.activity.chat;

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
 * Created by zhou on 16/11/7.
 */
public class ChatActivity extends BaseSlideActivity {

    @Bind(R.id.tv_title) TextView tv_title;
    @Bind(R.id.ll_return) LinearLayout ll_return;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chat);

        ButterKnife.bind(this);

        initView();
    }


    public void initView(){

        tv_title.setText("聊天室");
        ll_return.setVisibility(View.VISIBLE);
    }


    // 返回上一页面
    @OnClick(R.id.ll_return)
    public void back(){
        finish();
    }
}
