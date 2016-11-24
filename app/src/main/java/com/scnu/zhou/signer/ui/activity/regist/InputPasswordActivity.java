package com.scnu.zhou.signer.ui.activity.regist;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scnu.zhou.signer.R;
import com.scnu.zhou.signer.component.bean.http.ResultResponse;
import com.scnu.zhou.signer.component.bean.login.LoginResult;
import com.scnu.zhou.signer.component.cache.UserCache;
import com.scnu.zhou.signer.component.util.encrypt.RSAEncryptUtil;
import com.scnu.zhou.signer.component.util.http.ResponseCode;
import com.scnu.zhou.signer.presenter.regist.IRegistPresenter;
import com.scnu.zhou.signer.presenter.regist.RegistPresenter;
import com.scnu.zhou.signer.ui.activity.base.BaseSlideActivity;
import com.scnu.zhou.signer.ui.activity.login.LoginActivity;
import com.scnu.zhou.signer.ui.activity.main.MainActivity;
import com.scnu.zhou.signer.ui.widget.edit.TextClearableEditText;
import com.scnu.zhou.signer.ui.widget.toast.ToastView;
import com.scnu.zhou.signer.view.regist.IRegistView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zhou on 16/9/3.
 */
public class InputPasswordActivity extends BaseSlideActivity implements IRegistView, TextWatcher{

    @Bind(R.id.ll_return) LinearLayout ll_return;
    @Bind(R.id.tv_title) TextView tv_title;

    @Bind(R.id.et_password) TextClearableEditText et_password;
    @Bind(R.id.et_confirm) TextClearableEditText et_confirm;
    @Bind(R.id.btn_regist) Button btn_regist;

    private String phone;
    private String publicKey;  // 加密公钥

    private IRegistPresenter registPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_input_password);

        ButterKnife.bind(this);
        initView();
        initData();
    }

    @Override
    public void initView() {

        ll_return.setVisibility(View.VISIBLE);
        tv_title.setText("请输入登录密码");

        et_password.addTextChangedListener(this);
        et_confirm.addTextChangedListener(this);
    }

    @Override
    public void initData() {

        phone = getIntent().getStringExtra("phone");

        registPresenter = new RegistPresenter(this);
    }


    // 获取公钥成功
    @Override
    public void onGetPublicKeySuccess(ResultResponse<String> response) {

        //Log.e("data", response.getData());

        if (response.getCode().equals("200")) {

            publicKey = response.getData();

            String key = RSAEncryptUtil.encryptData(et_password.getText().toString(), publicKey);

            registPresenter.regist(phone, key);
        }
        else{

            dismissLoadingDialog();
            ToastView toastView = new ToastView(InputPasswordActivity.this,
                    ResponseCode.getInstance().getMessage(response.getCode()));
            toastView.setGravity(Gravity.CENTER, 0, 0);
            toastView.show();
        }
    }


    // 获取公钥失败
    @Override
    public void onGetPublicKeyError(Throwable e) {

        Log.e("get key error", e.toString());

        dismissLoadingDialog();
        ToastView toastView = new ToastView(InputPasswordActivity.this, "请检查您的网络连接");
        toastView.setGravity(Gravity.CENTER, 0, 0);
        toastView.show();
    }


    // 注册成功
    @Override
    public void onPostRegistSuccess(ResultResponse<LoginResult> response) {

        if (response.getCode().equals("200")){

            dismissLoadingDialog();

            ToastView toastView = new ToastView(InputPasswordActivity.this, "注册成功");
            toastView.setGravity(Gravity.CENTER, 0, 0);
            toastView.show();

            // 注册成功
            LoginActivity.getInstance().finish();
            InputPhoneActivity.getInstance().finish();
            InputCodeActivity.getInstance().finish();

            // 保存注册信息
            UserCache.getInstance().login(this, getPhone(), et_password.getText().toString(), "0");
            UserCache.getInstance().setId(this, response.getData().getPerson().get_id());

            Intent intent = new Intent(InputPasswordActivity.this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            finish();
        }
        else{
            dismissLoadingDialog();
            String msg = response.getMsg();
            ToastView toastView = new ToastView(InputPasswordActivity.this, msg);
            toastView.setGravity(Gravity.CENTER, 0, 0);
            toastView.show();
        }
    }


    // 注册失败
    @Override
    public void onPostRegistError(Throwable e) {

        Log.e("regist error", e.toString());

        dismissLoadingDialog();
        ToastView toastView = new ToastView(InputPasswordActivity.this, "请检查您的网络连接");
        toastView.setGravity(Gravity.CENTER, 0, 0);
        toastView.show();
    }


    public String getPhone() {
        return phone;
    }

    // 返回上一页面
    @OnClick(R.id.ll_return)
    public void back(){
        finish();
    }


    // 注册动作
    @OnClick(R.id.btn_regist)
    public void regist(){

        if (!et_password.getText().equals(et_confirm.getText())){

            ToastView toastView = new ToastView(this, "前后密码输入不一致");
            toastView.setGravity(Gravity.CENTER, 0, 0);
            toastView.show();
        }
        else{

            showLoadingDialog("注册中");
            registPresenter.getPublicKey();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        if (!TextUtils.isEmpty(et_password.getText().toString())
                && !TextUtils.isEmpty(et_confirm.getText().toString())){
            btn_regist.setEnabled(true);
        }
        else{
            btn_regist.setEnabled(false);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
