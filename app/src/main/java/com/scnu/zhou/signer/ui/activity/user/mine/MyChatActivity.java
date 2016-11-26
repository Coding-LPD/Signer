package com.scnu.zhou.signer.ui.activity.user.mine;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.scnu.zhou.signer.R;
import com.scnu.zhou.signer.component.cache.UserCache;
import com.scnu.zhou.signer.presenter.mine.IMySignerPresenter;
import com.scnu.zhou.signer.presenter.mine.MySignerPresenter;
import com.scnu.zhou.signer.ui.activity.base.BaseSlideActivity;
import com.scnu.zhou.signer.ui.widget.calendar.NoteCalendar;
import com.scnu.zhou.signer.ui.widget.toast.ToastView;
import com.scnu.zhou.signer.view.mine.IMySignerView;

import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zhou on 16/11/23.
 */
public class MyChatActivity extends BaseSlideActivity implements IMySignerView {

    @Bind(R.id.ll_return) LinearLayout ll_return;
    @Bind(R.id.tv_title) TextView tv_title;

    @Bind(R.id.nc_calendar) NoteCalendar nc_calendar;
    @Bind(R.id.tv_date) TextView tv_date;

    @Bind(R.id.lv_mychat) ListView lv_mychat;
    @Bind(R.id.pb_loading) ProgressBar pb_loading;
    @Bind(R.id.tv_tip) TextView tv_tip;

    private IMySignerPresenter presenter;

    private String studentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mychat);

        ButterKnife.bind(this);
        initData();
        initView();
    }

    @Override
    public void initView() {

        ll_return.setVisibility(View.VISIBLE);
        tv_title.setText("我的发言");

        tv_date.setText(nc_calendar.getCurrentMonth() + 1 + "月" + nc_calendar.getCurrentDay() + "日");
        presenter.getChatDaysDetail(studentId, nc_calendar.getCurrentYear() + "-" +
                (nc_calendar.getCurrentMonth() + 1) + "-" + nc_calendar.getCurrentDay());

        nc_calendar.setOnItemClickListener(new NoteCalendar.OnItemClickListener() {
            @Override
            public void onClick(int year, int month, int day) {

                tv_date.setText(month + "月" + day + "日");

                pb_loading.setVisibility(View.VISIBLE);
                lv_mychat.setVisibility(View.GONE);
                tv_tip.setVisibility(View.GONE);
                presenter.getChatDaysDetail(studentId, year + "-" + month + "-" + day);
            }
        });

        showLoadingDialog("获取信息中");
    }

    @Override
    public void initData() {

        studentId = UserCache.getInstance().getId(this);

        presenter = new MySignerPresenter(this);
        presenter.getChatDays(studentId,
                nc_calendar.getCurrentYear() + "-" + (nc_calendar.getCurrentMonth() + 1));
        Log.e("date", nc_calendar.getCurrentYear() + "-" + (nc_calendar.getCurrentMonth() + 1));
    }

    // 获得聊天日历note
    @Override
    public void onGetDays(Map<String, Boolean> note) {

        dismissLoadingDialog();
        nc_calendar.setNote01(note);
    }

    @Override
    public void onShowError(String msg) {

        dismissLoadingDialog();
        ToastView toastView = new ToastView(MyChatActivity.this, msg);
        toastView.setGravity(Gravity.CENTER, 0, 0);
        toastView.show();
    }

    @Override
    public void onShowError(Throwable e) {

        Log.e("get sign error", e.toString());

        dismissLoadingDialog();
        ToastView toastView = new ToastView(MyChatActivity.this, "请检查您的网络连接");
        toastView.setGravity(Gravity.CENTER, 0, 0);
        toastView.show();
    }

    // 获得某日期的签到详情
    @Override
    public void onGetDaysDetails(List<String> response) {

        pb_loading.setVisibility(View.GONE);

        if (response.size() == 0){
            tv_tip.setVisibility(View.VISIBLE);
            tv_tip.setText("暂无发言");
        }
        else {

            lv_mychat.setVisibility(View.VISIBLE);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    R.layout.simple_list_item, response);
            lv_mychat.setAdapter(adapter);
        }
    }

    @Override
    public void onShowDetailError(Throwable e) {

        Log.e("get sign detail error", e.toString());

        pb_loading.setVisibility(View.GONE);
        //ToastView toastView = new ToastView(MySignActivity.this, "请检查您的网络连接");
        //toastView.setGravity(Gravity.CENTER, 0, 0);
        //toastView.show();
        tv_tip.setText("获取信息失败");
    }

    @Override
    public void onShowDetailError(String msg) {

        pb_loading.setVisibility(View.GONE);
        //ToastView toastView = new ToastView(MySignActivity.this, msg);
        //toastView.setGravity(Gravity.CENTER, 0, 0);
        //toastView.show();
        tv_tip.setText("获取信息失败");
    }


    // 返回上一页面
    @OnClick(R.id.ll_return)
    public void back(){
        finish();
    }
}
