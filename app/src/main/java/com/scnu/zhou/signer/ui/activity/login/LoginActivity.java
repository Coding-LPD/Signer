package com.scnu.zhou.signer.ui.activity.login;

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
import com.scnu.zhou.signer.component.engine.LoginEngine;
import com.scnu.zhou.signer.component.util.encrypt.RSAEncryptUtil;
import com.scnu.zhou.signer.component.util.http.ResponseCodeUtil;
import com.scnu.zhou.signer.component.util.image.ImageLoaderUtil;
import com.scnu.zhou.signer.presenter.login.ILoginPresenter;
import com.scnu.zhou.signer.presenter.login.LoginPresenter;
import com.scnu.zhou.signer.ui.activity.base.BaseActivity;
import com.scnu.zhou.signer.ui.activity.main.MainActivity;
import com.scnu.zhou.signer.ui.activity.regist.InputPhoneActivity;
import com.scnu.zhou.signer.ui.widget.edit.TextClearableEditText;
import com.scnu.zhou.signer.ui.widget.image.CircleImageView;
import com.scnu.zhou.signer.ui.widget.toast.ToastView;
import com.scnu.zhou.signer.view.login.ILoginView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zhou on 16/9/3.
 */
public class LoginActivity extends BaseActivity implements ILoginView, TextWatcher{

    @Bind(R.id.tv_title) TextView tv_title;
    @Bind(R.id.tv_right) TextView tv_right;
    @Bind(R.id.v_bottomline) View v_bottomline;

    @Bind(R.id.ll_profile) LinearLayout ll_profile;
    @Bind(R.id.civ_user_header) CircleImageView civ_user_header;
    @Bind(R.id.tv_user_phone) TextView tv_user_phone;

    @Bind(R.id.et_user) TextClearableEditText et_user;
    @Bind(R.id.et_password) TextClearableEditText et_password;
    @Bind(R.id.btn_login) Button btn_login;

    private static LoginActivity instance;

    private String publicKey;    // 加密公钥

    private boolean isCache = false;   // 记录之前是否有登录过
    private String user_phone = "";

    private ILoginPresenter loginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);
        instance = this;
        initView();
        initData();
    }

    @Override
    public void initView() {

        tv_title.setText("Signer登录");
        v_bottomline.setVisibility(View.GONE);

        if (!TextUtils.isEmpty(UserCache.getPhone(this))){

            isCache = true;
            user_phone = UserCache.getPhone(this);

            ll_profile.setVisibility(View.VISIBLE);
            et_user.setVisibility(View.GONE);

            ImageLoaderUtil.getInstance().displayHeaderImage(civ_user_header, UserCache.getAvatar(this));
            tv_user_phone.setText(UserCache.getPhone(this));

            tv_right.setText("切换账号");
        }

        et_user.addTextChangedListener(this);
        et_password.addTextChangedListener(this);
    }

    @Override
    public void initData() {

        loginPresenter = new LoginPresenter(this);
    }


    // 前往注册
    @OnClick(R.id.tv_regist)
    public void regist(){

        Intent intent = new Intent(this, InputPhoneActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }


    // 登录动作
    @OnClick(R.id.btn_login)
    public void login(){

        showLoadingDialog("登录中");
        loginPresenter.GetPublicKey();
    }


    // 账号切换
    @OnClick(R.id.tv_right)
    public void switchUser(){

        isCache = false;

        ll_profile.setVisibility(View.GONE);
        et_user.setVisibility(View.VISIBLE);
        tv_right.setVisibility(View.GONE);
    }


    public static LoginActivity getInstance(){
        return instance;
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        if (!isCache) {    // 需要同时输入账号和密码
            if (!TextUtils.isEmpty(et_user.getText()) && !TextUtils.isEmpty(et_password.getText())) {
                btn_login.setEnabled(true);
            } else {
                btn_login.setEnabled(false);
            }
        }
        else{   // 只需要输入密码即可
            if (!TextUtils.isEmpty(et_password.getText())) {
                btn_login.setEnabled(true);
            } else {
                btn_login.setEnabled(false);
            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }


    // 获取公钥成功
    @Override
    public void onGetPublicKeySuccess(ResultResponse<String> response) {

        if (response.getCode().equals("200")) {

            publicKey = response.getData();
            String key = RSAEncryptUtil.encryptData(et_password.getText().toString(), publicKey);

            if (isCache){
                loginPresenter.login(user_phone, key);
            }else {
                loginPresenter.login(et_user.getText(), key);
            }
        }
        else{

            dismissLoadingDialog();
            ToastView toastView = new ToastView(LoginActivity.this,
                    ResponseCodeUtil.getMessage(response.getCode()));
            toastView.setGravity(Gravity.CENTER, 0, 0);
            toastView.show();
        }
    }


    // 获取公钥失败
    @Override
    public void onGetPublicKeyError(Throwable e) {

        Log.e("error", "获取公钥： " + e.toString());

        dismissLoadingDialog();
        ToastView toastView = new ToastView(LoginActivity.this, "请检查您的网络连接");
        toastView.setGravity(Gravity.CENTER, 0, 0);
        toastView.show();
    }


    // 登录成功
    @Override
    public void onPostLoginSuccess(ResultResponse<User> response) {

        if (response.getCode().equals("200")){

            dismissLoadingDialog();

            // 保存登录信息
            LoginEngine engine = new LoginEngine(LoginActivity.this);
            if (isCache){
                engine.login(user_phone, et_password.getText());
            }
            else {
                engine.login(et_user.getText(), et_password.getText());
            }

            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            finish();
        }
        else{
            dismissLoadingDialog();
            String msg = response.getMsg();
            ToastView toastView = new ToastView(LoginActivity.this, msg);
            toastView.setGravity(Gravity.CENTER, 0, 0);
            toastView.show();
        }
    }


    // 登录失败
    @Override
    public void onPostLoginError(Throwable e) {

        Log.e("error", e.toString());

        dismissLoadingDialog();
        ToastView toastView = new ToastView(LoginActivity.this, "请检查您的网络连接");
        toastView.setGravity(Gravity.CENTER, 0, 0);
        toastView.show();
    }
}
