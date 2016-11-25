package com.scnu.zhou.signer.ui.activity.user.settings;

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
import com.scnu.zhou.signer.component.bean.user.User;
import com.scnu.zhou.signer.component.cache.UserCache;
import com.scnu.zhou.signer.component.util.activity.ActivityManager;
import com.scnu.zhou.signer.component.util.activity.ActivityUtil;
import com.scnu.zhou.signer.component.util.encrypt.RSAEncryptUtil;
import com.scnu.zhou.signer.component.util.http.ResponseCode;
import com.scnu.zhou.signer.presenter.user.IUserPresenter;
import com.scnu.zhou.signer.presenter.user.UserPresenter;
import com.scnu.zhou.signer.ui.activity.base.BaseSlideActivity;
import com.scnu.zhou.signer.ui.activity.login.LoginActivity;
import com.scnu.zhou.signer.ui.widget.edit.TextClearableEditText;
import com.scnu.zhou.signer.ui.widget.toast.ToastView;
import com.scnu.zhou.signer.view.user.IUserPasswordView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zhou on 16/11/21.
 */
public class UpdatePasswordActivity extends BaseSlideActivity implements IUserPasswordView, TextWatcher{

    @Bind(R.id.ll_return) LinearLayout ll_return;
    @Bind(R.id.tv_title) TextView tv_title;

    @Bind(R.id.et_password) TextClearableEditText et_password;
    @Bind(R.id.et_confirm) TextClearableEditText et_confirm;
    @Bind(R.id.btn_update) Button btn_update;

    private IUserPresenter presenter;

    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_update_password);

        ButterKnife.bind(this);
        initView();
        initData();
    }


    @Override
    public void initView(){

        ll_return.setVisibility(View.VISIBLE);
        tv_title.setText("请填写新密码");

        et_password.addTextChangedListener(this);
        et_confirm.addTextChangedListener(this);
    }

    @Override
    public void initData() {

        presenter = new UserPresenter(this);
        phone = getIntent().getStringExtra("phone");
    }


    // 获取公钥成功
    @Override
    public void onGetPublicKeySuccess(ResultResponse<String> response) {

        //Log.e("data", response.getData());

        if (response.getCode().equals("200")) {

            String publicKey = response.getData();

            String key = RSAEncryptUtil.encryptData(et_password.getText().toString(), publicKey);

            presenter.updateUserPassword(phone, key);
        }
        else{

            dismissLoadingDialog();
            ToastView toastView = new ToastView(UpdatePasswordActivity.this,
                    ResponseCode.getInstance().getMessage(response.getCode()));
            toastView.setGravity(Gravity.CENTER, 0, 0);
            toastView.show();
        }
    }

    @Override
    public void onGetPublicKeyError(Throwable e) {

        Log.e("get key error", e.toString());

        dismissLoadingDialog();
        ToastView toastView = new ToastView(UpdatePasswordActivity.this, "请检查您的网络连接");
        toastView.setGravity(Gravity.CENTER, 0, 0);
        toastView.show();
    }

    @Override
    public void onUpdatePasswordSuccess(ResultResponse<User> response) {

        dismissLoadingDialog();

        if (response.getCode().equals("200")){

            ToastView toastView = new ToastView(UpdatePasswordActivity.this, "修改密码成功");
            toastView.setGravity(Gravity.CENTER, 0, 0);
            toastView.show();

            if (!ActivityUtil.isExsitActivity(this, LoginActivity.class)) {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }

            // 清除缓存
            UserCache.getInstance().logout(UpdatePasswordActivity.this);

            ActivityManager.getScreenManager().popAllActivityExceptOne(LoginActivity.class);
        }
        else{
            String data = response.getMsg();
            ToastView toastView = new ToastView(UpdatePasswordActivity.this, data);
            toastView.setGravity(Gravity.CENTER, 0, 0);
            toastView.show();
        }
    }

    @Override
    public void onUpdatePasswordError(Throwable e) {

        Log.e("update password error", e.toString());

        dismissLoadingDialog();
        ToastView toastView = new ToastView(UpdatePasswordActivity.this, "请检查您的网络连接");
        toastView.setGravity(Gravity.CENTER, 0, 0);
        toastView.show();
    }


    // 返回上一页面
    @OnClick(R.id.ll_return)
    public void back(){
        finish();
    }

    // 修改 动作
    @OnClick(R.id.btn_update)
    public void update(){

        if (!et_password.getText().equals(et_confirm.getText())){

            ToastView toastView = new ToastView(this, "前后密码输入不一致");
            toastView.setGravity(Gravity.CENTER, 0, 0);
            toastView.show();
        }
        else{

            showLoadingDialog("修改中");
            presenter.getPublicKey();
        }
    }


    // implementation for TextWatcher
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        if (!TextUtils.isEmpty(et_password.getText().toString())
                && !TextUtils.isEmpty(et_confirm.getText().toString())){
            btn_update.setEnabled(true);
        }
        else{
            btn_update.setEnabled(false);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
