package com.scnu.zhou.signer.ui.activity.course;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scnu.zhou.signer.R;
import com.scnu.zhou.signer.component.adapter.gridview.SignerAdapter;
import com.scnu.zhou.signer.component.bean.http.ResultResponse;
import com.scnu.zhou.signer.component.bean.main.CourseDetail;
import com.scnu.zhou.signer.component.bean.sign.Signer;
import com.scnu.zhou.signer.component.util.http.ResponseCode;
import com.scnu.zhou.signer.presenter.home.CoursePresenter;
import com.scnu.zhou.signer.presenter.home.ICoursePresenter;
import com.scnu.zhou.signer.ui.activity.base.BaseSlideActivity;
import com.scnu.zhou.signer.ui.widget.toast.ToastView;
import com.scnu.zhou.signer.view.home.ICourseView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zhou on 16/10/26.
 */
public class CourseDetailActivity extends BaseSlideActivity implements ICourseView{

    @Bind(R.id.tv_title) TextView tv_title;
    @Bind(R.id.ll_return) LinearLayout ll_return;

    @Bind(R.id.tv_name) TextView tv_name;
    @Bind(R.id.tv_week) TextView tv_week;
    @Bind(R.id.tv_session) TextView tv_session;
    @Bind(R.id.tv_location) TextView tv_location;
    @Bind(R.id.tv_teacher) TextView tv_teacher;

    @Bind(R.id.gv_signer) GridView gv_signer;

    @Bind(R.id.tv_check_sign) TextView tv_check_sign;

    private List<Signer> signers;
    private SignerAdapter adapter;

    private String title;
    private String courseId;

    private ICoursePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_course_detail);

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

        presenter = new CoursePresenter(this);
        presenter.getCourseDetail(courseId);
    }


    /**
     * 获取信息成功
     * @param response
     */
    @Override
    public void onGetCourseDetailSuccess(ResultResponse<CourseDetail> response) {

        dismissLoadingDialog();
        if (response.getCode().equals("200")) {

            CourseDetail result = response.getData();

            if (result != null) {

                tv_title.setText(result.getCourse().getName());
                tv_name.setText(result.getCourse().getName());
                tv_location.setText(result.getCourse().getLocation());
                tv_teacher.setText(result.getCourse().getTeacherName());

                //Log.e("teacher", result.getCourse().getTeacherName());

                //Log.e("data", result.getCourse().getTime());
                //String time = "星期一 1节-3节,星期四 5节-8节";
                String time = result.getCourse().getTime();
                String[] sessions = time.split(",");   // 按逗号分开

                String week = "";
                String session = "";
                for (String se : sessions) {
                    String[] s = se.split("\\s+");    // 按空格分开
                    if (week.equals("")) week += s[0];
                    else week += ";" + s[0];
                    if (session.equals("")) session += s[1];
                    else session += ";" + s[1];
                }

                tv_week.setText(week);
                tv_session.setText(session);

                signers = result.getRecords();
                adapter = new SignerAdapter(this, signers);
                gv_signer.setAdapter(adapter);

                tv_check_sign.setText("共有" + result.getSignNum() + "次签到");
            }
        }
        else{

            ToastView toastView = new ToastView(CourseDetailActivity.this,
                    ResponseCode.getInstance().getMessage(response.getCode()));
            toastView.setGravity(Gravity.CENTER, 0, 0);
            toastView.show();
        }
    }


    /**
     * 获取信息失败
     * @param e
     */
    @Override
    public void onGetCourseDetailError(Throwable e) {

        dismissLoadingDialog();
        ToastView toastView = new ToastView(CourseDetailActivity.this, "请检查您的网络连接");
        toastView.setGravity(Gravity.CENTER, 0, 0);
        toastView.show();
    }


    // 返回上一页面
    @OnClick(R.id.ll_return)
    public void back(){
        finish();
    }


    // 查看所有签到
    @OnClick(R.id.tv_check_sign)
    public void checkSign(){

        Intent intent = new Intent(this, CheckSignActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("courseId", courseId);
        startActivity(intent);
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }
}
