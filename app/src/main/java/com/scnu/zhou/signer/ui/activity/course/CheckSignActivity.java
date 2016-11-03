package com.scnu.zhou.signer.ui.activity.course;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scnu.zhou.signer.R;
import com.scnu.zhou.signer.component.bean.http.ResultResponse;
import com.scnu.zhou.signer.component.bean.main.SignBean;
import com.scnu.zhou.signer.component.cache.UserCache;
import com.scnu.zhou.signer.component.util.http.ResponseCode;
import com.scnu.zhou.signer.presenter.home.CoursePresenter;
import com.scnu.zhou.signer.ui.activity.base.BaseSlideActivity;
import com.scnu.zhou.signer.ui.widget.calendar.NoteCalendar;
import com.scnu.zhou.signer.ui.widget.toast.ToastView;
import com.scnu.zhou.signer.view.home.ICheckSignView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zhou on 16/10/26.
 */
public class CheckSignActivity extends BaseSlideActivity implements ICheckSignView{

    @Bind(R.id.tv_title) TextView tv_title;
    @Bind(R.id.ll_return) LinearLayout ll_return;

    @Bind(R.id.nc_calendar) NoteCalendar nc_calendar;

    @Bind(R.id.tv_signed) TextView tv_signed;
    @Bind(R.id.tv_unsigned) TextView tv_unsigned;

    private String title;
    private String courseId;

    private Map<String, Boolean> note01;
    private Map<String, Boolean> note02;

    private CoursePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_check_sign);

        ButterKnife.bind(this);

        initView();
        initData();
    }


    @Override
    public void initView(){

        ll_return.setVisibility(View.VISIBLE);

        showLoadingDialog("加载中");
    }


    @Override
    public void initData(){

        title = getIntent().getStringExtra("title");
        tv_title.setText(title);

        courseId = getIntent().getStringExtra("courseId");

        note01 = new HashMap<>();
        note02 = new HashMap<>();

        presenter = new CoursePresenter(this);
        presenter.getSignDetail(courseId, UserCache.getInstance().getId(this));
    }


    /**
     * implement for get data
     */
    @Override
    public void onGetSignDetailSuccess(ResultResponse<List<SignBean>> response) {

        dismissLoadingDialog();
        if (response.getCode().equals("200")) {

            List<SignBean> result = response.getData();
            Log.e("result", result.size() + "");
            for (SignBean bean: result){

                if (bean.isTag()){
                    Log.e("note01", bean.getTime());
                    note01.put(bean.getTime(), true);
                }
                else{
                    Log.e("note02", bean.getTime());
                    note02.put(bean.getTime(), true);
                }
            }

            nc_calendar.setNote01(note01);
            nc_calendar.setNote02(note02);

            tv_signed.setText("已签到" + note01.size() + "次");
            tv_unsigned.setText("未签到" + note02.size() + "次");
        }
        else{

            ToastView toastView = new ToastView(CheckSignActivity.this,
                    ResponseCode.getInstance().getMessage(response.getCode()));
            toastView.setGravity(Gravity.CENTER, 0, 0);
            toastView.show();
        }
    }


    @Override
    public void onGetSignDetailError(Throwable e) {

        dismissLoadingDialog();
        ToastView toastView = new ToastView(CheckSignActivity.this, "请检查您的网络连接");
        toastView.setGravity(Gravity.CENTER, 0, 0);
        toastView.show();
    }


    // 返回上一页面
    @OnClick(R.id.ll_return)
    public void back(){
        finish();
    }
}
