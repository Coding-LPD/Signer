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
public class FeedbackActivity extends BaseSlideActivity{

    @Bind(R.id.ll_return) LinearLayout ll_return;
    @Bind(R.id.tv_title) TextView tv_title;
    @Bind(R.id.tv_right) TextView tv_right;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_feedback);

        ButterKnife.bind(this);
        initView();
    }

    public void initView() {

        ll_return.setVisibility(View.VISIBLE);
        tv_title.setText("意见反馈");
        tv_right.setText("发送");
    }


    // 返回上一页面
    @OnClick(R.id.ll_return)
    public void back(){
        finish();
    }


    // 提交反馈
    @OnClick(R.id.tv_right)
    public void postFeedBack(){

    }
}
