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
import com.scnu.zhou.signer.component.util.encrypt.RSAEncryptUtil;
import com.scnu.zhou.signer.component.util.http.ResponseCode;
import com.scnu.zhou.signer.presenter.user.IUserPresenter;
import com.scnu.zhou.signer.presenter.user.UserPresenter;
import com.scnu.zhou.signer.ui.activity.base.BaseSlideActivity;
import com.scnu.zhou.signer.ui.activity.login.LoginActivity;
import com.scnu.zhou.signer.ui.activity.regist.InputPhoneActivity;
import com.scnu.zhou.signer.ui.widget.edit.TextClearableEditText;
import com.scnu.zhou.signer.ui.widget.toast.ToastView;
import com.scnu.zhou.signer.view.user.IUserPasswordView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zhou on 16/11/21.
 */
public class UpdatePasswordActivity2 extends BaseSlideActivity implements IUserPasswordView, TextWatcher{

    @Bind(R.id.ll_return) LinearLayout ll_return;
    @Bind(R.id.tv_title) TextView tv_title;

    @Bind(R.id.et_oldpassword) TextClearableEditText et_oldpassword;
    @Bind(R.id.et_password) TextClearableEditText et_password;
    @Bind(R.id.et_confirm) TextClearableEditText et_confirm;
    @Bind(R.id.btn_update) Button btn_update;

    private IUserPresenter presenter;

    private String phone;

    public static UpdatePasswordActivity2 instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_update_password2);

        ButterKnife.bind(this);
        initView();
        initData();

        instance = this;
    }


    @Override
    public void initView(){

        ll_return.setVisibility(View.VISIBLE);
        tv_title.setText("修改密码");

        et_oldpassword.addTextChangedListener(this);
        et_password.addTextChangedListener(this);
        et_confirm.addTextChangedListener(this);
    }

    @Override
    public void initData() {

        presenter = new UserPresenter(this);
        phone = UserCache.getInstance().getPhone(this);
    }


    public static UpdatePasswordActivity2 getInstance(){

        return instance;
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
            ToastView toastView = new ToastView(UpdatePasswordActivity2.this,
                    ResponseCode.getInstance().getMessage(response.getCode()));
            toastView.setGravity(Gravity.CENTER, 0, 0);
            toastView.show();
        }
    }

    @Override
    public void onGetPublicKeyError(Throwable e) {

        Log.e("get key error", e.toString());

        dismissLoadingDialog();
        ToastView toastView = new ToastView(UpdatePasswordActivity2.this, "请检查您的网络连接");
        toastView.setGravity(Gravity.CENTER, 0, 0);
        toastView.show();
    }

    @Override
    public void onUpdatePasswordSuccess(ResultResponse<User> response) {

        dismissLoadingDialog();

        if (response.getCode().equals("200")){

            ToastView toastView = new ToastView(UpdatePasswordActivity2.this, "修改密码成功");
            toastView.setGravity(Gravity.CENTER, 0, 0);
            toastView.show();

            // 页面跳转 
            Intent intent = new Intent(UpdatePasswordActivity2.this, LoginActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);

            // 清除缓存
            UserCache.getInstance().logout(UpdatePasswordActivity2.this);

            ActivityManager.getScreenManager().popAllActivityExceptOne(LoginActivity.class);
        }
        else{
            String data = response.getMsg();
            ToastView toastView = new ToastView(UpdatePasswordActivity2.this, data);
            toastView.setGravity(Gravity.CENTER, 0, 0);
            toastView.show();
        }
    }

    @Override
    public void onUpdatePasswordError(Throwable e) {

        Log.e("update password error", e.toString());

        dismissLoadingDialog();
        ToastView toastView = new ToastView(UpdatePasswordActivity2.this, "请检查您的网络连接");
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

        if (!et_oldpassword.getText().toString().equals(UserCache.getInstance().getPassword(this))){

            ToastView toastView = new ToastView(this, "原密码输入错误");
            toastView.setGravity(Gravity.CENTER, 0, 0);
            toastView.show();
        }
        else if (!et_password.getText().toString().equals(et_confirm.getText().toString())){

            ToastView toastView = new ToastView(this, "新密码前后输入不一致");
            toastView.setGravity(Gravity.CENTER, 0, 0);
            toastView.show();
        }
        else{

            showLoadingDialog("修改中");
            presenter.getPublicKey();
        }
    }


    // 忘记密码
    @OnClick(R.id.tv_forget)
    public void forget(){

        Intent intent = new Intent(this, InputPhoneActivity.class);
        intent.putExtra("state", "update");
        startActivity(intent);
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }


    // implementation for TextWatcher
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        if (!TextUtils.isEmpty(et_password.getText().toString())
                && !TextUtils.isEmpty(et_confirm.getText().toString())
                && !TextUtils.isEmpty(et_oldpassword.getText().toString())){
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
