package com.scnu.zhou.signer.activity.sign;

import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scnu.zhou.signer.R;
import com.scnu.zhou.signer.activity.base.BaseSlideActivity;
import com.scnu.zhou.signer.elss.adapter.SignerAdapter;
import com.scnu.zhou.signer.elss.bean.signer.Signer;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zhou on 2016/9/22.
 */
public class ConfirmSignActivity extends BaseSlideActivity{

    @Bind(R.id.tv_title) TextView tv_title;
    @Bind(R.id.ll_return) LinearLayout ll_return;

    @Bind(R.id.tv_week) TextView tv_week;
    @Bind(R.id.tv_session) TextView tv_session;
    @Bind(R.id.tv_location) TextView tv_location;
    @Bind(R.id.tv_teacher) TextView tv_teacher;

    @Bind(R.id.gv_signer) GridView gv_signer;

    private List<Signer> signers;
    private SignerAdapter adapter;

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
    }

    @Override
    public void initData() {

        signers = new ArrayList<>();

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
        gv_signer.setAdapter(adapter);
    }

    @Override
    public void loadData() {

    }


    // 返回上一页面
    @OnClick(R.id.ll_return)
    public void back(){
        finish();
    }
}
