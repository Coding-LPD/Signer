package com.scnu.zhou.signer.ui.activity.course;

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
 * Created by zhou on 16/10/26.
 */
public class CourseDetailActivity extends BaseSlideActivity {

    @Bind(R.id.tv_title) TextView tv_title;
    @Bind(R.id.ll_return) LinearLayout ll_return;

    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_course_detail);

        ButterKnife.bind(this);

        initView();
        initData();
    }


    public void initView(){

        tv_title.setText("软件工程");
        ll_return.setVisibility(View.VISIBLE);
    }


    public void initData(){

        title = getIntent().getStringExtra("title");
        tv_title.setText(title);
    }


    // 返回上一页面
    @OnClick(R.id.ll_return)
    public void back(){
        finish();
    }
}
