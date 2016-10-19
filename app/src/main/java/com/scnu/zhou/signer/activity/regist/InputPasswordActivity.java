package com.scnu.zhou.signer.activity.regist;

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
import com.scnu.zhou.signer.activity.base.BaseSlideActivity;
import com.scnu.zhou.signer.activity.login.LoginActivity;
import com.scnu.zhou.signer.activity.main.MainActivity;
import com.scnu.zhou.signer.config.SignerApi;
import com.scnu.zhou.signer.engine.RegistEngine;
import com.scnu.zhou.signer.model.http.ResultResponse;
import com.scnu.zhou.signer.model.user.User;
import com.scnu.zhou.signer.util.encrypt.RSAEncryptUtil;
import com.scnu.zhou.signer.util.http.ResponseCodeUtil;
import com.scnu.zhou.signer.util.http.RetrofitServer;
import com.scnu.zhou.signer.view.edit.TextClearableEditText;
import com.scnu.zhou.signer.view.toast.ToastView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by zhou on 16/9/3.
 */
public class InputPasswordActivity extends BaseSlideActivity implements TextWatcher{

    @Bind(R.id.ll_return) LinearLayout ll_return;
    @Bind(R.id.tv_title) TextView tv_title;

    @Bind(R.id.et_password) TextClearableEditText et_password;
    @Bind(R.id.et_confirm) TextClearableEditText et_confirm;
    @Bind(R.id.btn_regist) Button btn_regist;

    private String phone;
    private String publicKey;  // 加密公钥

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
    }

    @Override
    public void loadData() {

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
            getPublicKey();
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


    /**
     * 获取公钥
     */
    private void getPublicKey(){

        RetrofitServer.getRetrofit()
                .create(SignerApi.class)
                .getPublicKey()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResultResponse<String>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.e("error", e.toString());

                        dismissLoadingDialog();
                        ToastView toastView = new ToastView(InputPasswordActivity.this, "请检查您的网络连接");
                        toastView.setGravity(Gravity.CENTER, 0, 0);
                        toastView.show();
                    }

                    @Override
                    public void onNext(ResultResponse<String> response) {

                        //Log.e("data", response.getData());

                        if (response.getCode().equals("200")) {

                            publicKey = response.getData();

                            String key = RSAEncryptUtil.encryptData(et_password.getText().toString(), publicKey);

                            postRegist(phone, key);
                        }
                        else{

                            dismissLoadingDialog();
                            ToastView toastView = new ToastView(InputPasswordActivity.this,
                                    ResponseCodeUtil.getMessage(response.getCode()));
                            toastView.setGravity(Gravity.CENTER, 0, 0);
                            toastView.show();
                        }
                    }
                });
    }

    /**
     * 注册
     */
    private void postRegist(String phone, String password){

        RetrofitServer.getRetrofit()
                .create(SignerApi.class)
                .regist(phone, password, "0")
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResultResponse<User>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.e("error", e.toString());

                        dismissLoadingDialog();
                        ToastView toastView = new ToastView(InputPasswordActivity.this, "请检查您的网络连接");
                        toastView.setGravity(Gravity.CENTER, 0, 0);
                        toastView.show();
                    }

                    @Override
                    public void onNext(ResultResponse<User> response) {

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
                            RegistEngine engine = new RegistEngine(InputPasswordActivity.this);
                            engine.regist(getPhone(), et_password.getText().toString());

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
                });
    }
}
