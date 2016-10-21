package com.scnu.zhou.signer.ui.activity.sign;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scnu.zhou.signer.R;
import com.scnu.zhou.signer.component.adapter.SignerAdapter;
import com.scnu.zhou.signer.component.bean.http.ResultResponse;
import com.scnu.zhou.signer.component.bean.sign.ScanResult;
import com.scnu.zhou.signer.component.bean.sign.Signer;
import com.scnu.zhou.signer.component.util.http.ResponseCodeUtil;
import com.scnu.zhou.signer.presenter.sign.ISignPresenter;
import com.scnu.zhou.signer.presenter.sign.SignPresenter;
import com.scnu.zhou.signer.ui.activity.base.BaseSlideActivity;
import com.scnu.zhou.signer.ui.widget.toast.ToastView;
import com.scnu.zhou.signer.view.sign.ISignView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zhou on 2016/9/22.
 */
public class ConfirmSignActivity extends BaseSlideActivity implements ISignView{

    @Bind(R.id.tv_title) TextView tv_title;
    @Bind(R.id.ll_return) LinearLayout ll_return;

    @Bind(R.id.tv_week) TextView tv_week;
    @Bind(R.id.tv_session) TextView tv_session;
    @Bind(R.id.tv_location) TextView tv_location;
    @Bind(R.id.tv_teacher) TextView tv_teacher;

    @Bind(R.id.gv_signer) GridView gv_signer;

    private List<Signer> signers;
    private SignerAdapter adapter;

    private String code;
    private ISignPresenter signPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_confirm_sign);

        ButterKnife.bind(this);
        initView();
        initData();
    }


    @Override
    public void initView() {

        tv_title.setText("课程详情");
        ll_return.setVisibility(View.VISIBLE);

        showLoadingDialog("加载中");
    }


    @Override
    public void initData() {

        code = getIntent().getStringExtra("code");
        signers = new ArrayList<>();

        signPresenter = new SignPresenter(this);
        signPresenter.getScanResult(code);

        /*
        Signer signer = new Signer();
        signer.setAvatar("http://file.bmob.cn/M03/08/D3/oYYBAFb8qjyAXEu7AAE6_OuqxNA225.jpg");
        signer.setName("haha");

        signers.add(signer);
        signers.add(signer);
        signers.add(signer);
        signers.add(signer);
        signers.add(signer);
        signers.add(signer);

        adapter = new SignerAdapter(this, signers);
        gv_signer.setAdapter(adapter);*/
    }


    @Override
    public void onGetScanResultSuccess(ResultResponse<ScanResult> response) {

        if (response.getCode().equals("200")) {

            ScanResult result = response.getData();

            tv_title.setText(result.getCourse().getName());
            tv_location.setText(result.getCourse().getLocation());
            tv_teacher.setText(result.getCourse().getTeacherName());

            signers = result.getRecords();
            adapter = new SignerAdapter(this, signers);
            gv_signer.setAdapter(adapter);
        }
        else{

            dismissLoadingDialog();
            ToastView toastView = new ToastView(ConfirmSignActivity.this,
                    ResponseCodeUtil.getMessage(response.getCode()));
            toastView.setGravity(Gravity.CENTER, 0, 0);
            toastView.show();
        }
    }


    @Override
    public void onGetScanResultError(Throwable e) {

        dismissLoadingDialog();
        ToastView toastView = new ToastView(ConfirmSignActivity.this, "请检查您的网络连接");
        toastView.setGravity(Gravity.CENTER, 0, 0);
        toastView.show();
    }


    // 返回上一页面
    @OnClick(R.id.ll_return)
    public void back(){
        finish();
    }
}
