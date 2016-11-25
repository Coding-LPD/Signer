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
import com.scnu.zhou.signer.component.bean.http.ResultResponse;
import com.scnu.zhou.signer.component.bean.mine.MyChat;
import com.scnu.zhou.signer.component.cache.UserCache;
import com.scnu.zhou.signer.presenter.mine.IMySignerPresenter;
import com.scnu.zhou.signer.presenter.mine.MySignerPresenter;
import com.scnu.zhou.signer.ui.activity.base.BaseSlideActivity;
import com.scnu.zhou.signer.ui.widget.calendar.NoteCalendar;
import com.scnu.zhou.signer.ui.widget.toast.ToastView;
import com.scnu.zhou.signer.view.mine.IMyChatView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zhou on 16/11/23.
 */
public class MyChatActivity extends BaseSlideActivity implements IMyChatView {

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

    @Override
    public void onGetChatDaysSuccess(ResultResponse<List<String>> response) {

        dismissLoadingDialog();
        if (response.getCode().equals("200")){

            List<String> days = response.getData();
            Map<String, Boolean> note = new HashMap<>();
            for (String d:days){
                note.put(d, true);
            }
            nc_calendar.setNote01(note);
        }
        else{
            String data = response.getMsg();
            ToastView toastView = new ToastView(MyChatActivity.this, data);
            toastView.setGravity(Gravity.CENTER, 0, 0);
            toastView.show();
        }
    }

    @Override
    public void onGetChatDaysError(Throwable e) {

        Log.e("get sign days error", e.toString());

        dismissLoadingDialog();
        ToastView toastView = new ToastView(MyChatActivity.this, "请检查您的网络连接");
        toastView.setGravity(Gravity.CENTER, 0, 0);
        toastView.show();
    }

    @Override
    public void onGetChatDaysDetailSuccess(ResultResponse<List<MyChat>> response) {

        pb_loading.setVisibility(View.GONE);

        if (response.getCode().equals("200")){

            List<String> data = new ArrayList<>();

            if (response.getData().size() == 0){
                tv_tip.setVisibility(View.VISIBLE);
                tv_tip.setText("暂无发言");
            }
            else {

                lv_mychat.setVisibility(View.VISIBLE);
                for (MyChat chat : response.getData()) {

                    data.add(chat.getCourseName() + "  " + chat.getMsgCount() + "条");
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                        R.layout.simple_list_item, data);
                lv_mychat.setAdapter(adapter);
            }
        }
        else{
            //String data = response.getMsg();
            //ToastView toastView = new ToastView(MySignActivity.this, data);
            //toastView.setGravity(Gravity.CENTER, 0, 0);
            //toastView.show();
            tv_tip.setText("获取信息失败");
        }
    }

    @Override
    public void onGetChatDaysDetailError(Throwable e) {

        Log.e("get sign detail error", e.toString());

        pb_loading.setVisibility(View.GONE);
        //ToastView toastView = new ToastView(MySignActivity.this, "请检查您的网络连接");
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
