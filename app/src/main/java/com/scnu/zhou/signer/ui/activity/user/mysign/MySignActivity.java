package com.scnu.zhou.signer.ui.activity.user.mysign;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scnu.zhou.signer.R;
import com.scnu.zhou.signer.ui.activity.base.BaseSlideActivity;
import com.scnu.zhou.signer.ui.widget.calendar.NoteCalendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zhou on 16/11/9.
 */
public class MySignActivity extends BaseSlideActivity {

    @Bind(R.id.ll_return) LinearLayout ll_return;
    @Bind(R.id.tv_title) TextView tv_title;

    @Bind(R.id.nc_calendar) NoteCalendar nc_calendar;
    @Bind(R.id.tv_date) TextView tv_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mysign);

        ButterKnife.bind(this);
        initView();
    }


    public void initView() {

        ll_return.setVisibility(View.VISIBLE);
        tv_title.setText("我的签到");

        tv_date.setText(nc_calendar.getCurrentMonth() + 1 + "月" + nc_calendar.getCurrentDay() + "日");
        nc_calendar.setOnItemClickListener(new NoteCalendar.OnItemClickListener() {
            @Override
            public void onClick(int year, int month, int day) {

                tv_date.setText(month + "月" + day + "日");
            }
        });
    }


    // 返回上一页面
    @OnClick(R.id.ll_return)
    public void back(){
        finish();
    }
}
