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
import com.scnu.zhou.signer.component.bean.main.CourseDetail;
import com.scnu.zhou.signer.component.bean.sign.Signer;
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
     * @param week
     * @param session
     */
    @Override
    public void onGetCourseDetailSuccess(CourseDetail response, String week, String session) {

        dismissLoadingDialog();

        tv_title.setText(response.getCourse().getName());
        tv_name.setText(response.getCourse().getName());
        tv_location.setText(response.getCourse().getLocation());
        tv_teacher.setText(response.getCourse().getTeacherName());

        tv_week.setText(week);
        tv_session.setText(session);

        signers = response.getRecords();
        adapter = new SignerAdapter(this, signers);
        gv_signer.setAdapter(adapter);

        tv_check_sign.setText("共有" + response.getSignNum() + "次签到");
    }


    /**
     * 获取信息失败
     * @param msg
     */
    @Override
    public void onGetCourseDetailError(String msg) {

        dismissLoadingDialog();
        ToastView toastView = new ToastView(CourseDetailActivity.this, msg);
        toastView.setGravity(Gravity.CENTER, 0, 0);
        toastView.show();
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
