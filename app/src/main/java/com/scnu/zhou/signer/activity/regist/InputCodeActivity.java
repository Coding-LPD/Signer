package com.scnu.zhou.signer.activity.regist;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scnu.zhou.signer.R;
import com.scnu.zhou.signer.activity.base.BaseSlideActivity;
import com.scnu.zhou.signer.config.SignerApi;
import com.scnu.zhou.signer.bean.http.ResultResponse;
import com.scnu.zhou.signer.util.http.RetrofitServer;
import com.scnu.zhou.signer.widget.dialog.LoadingDialog;
import com.scnu.zhou.signer.widget.toast.ToastView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by zhou on 16/9/3.
 */
public class InputCodeActivity extends BaseSlideActivity{

    @Bind(R.id.ll_return) LinearLayout ll_return;
    @Bind(R.id.tv_title) TextView tv_title;

    @Bind(R.id.tv_resend) TextView tv_resend;
    private int second = 60;

    @Bind(R.id.et_smscode) EditText et_smscode;
    @Bind(R.id.btn_next) Button btn_next;

    private String phone;
    private String smsCode;

    private LoadingDialog dialog;

    private static InputCodeActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_input_code);

        ButterKnife.bind(this);
        instance = this;

        initView();
        initData();
        MyHandler.postDelayed(MyRunnable, 1000);

        // 发送短信验证码
        // sendSmsCode();
    }

    private Handler MyHandler = new Handler();
    private Runnable MyRunnable = new Runnable() {
        @Override
        public void run() {

            second--;

            if (second > 0){
                tv_resend.setText(second + " s");
                tv_resend.setTextColor(getResources().getColor(R.color.colorThemeLight));
                MyHandler.postDelayed(this, 1000);

            }
            else{
                tv_resend.setTextColor(getResources().getColor(R.color.colorTheme));
                tv_resend.setText("重新发送");
                second = 60;
            }
        }
    };

    @Override
    public void initView() {

        ll_return.setVisibility(View.VISIBLE);
        tv_title.setText("请填写验证码");

        dialog = new LoadingDialog(this);
        dialog.setTitle("验证中");
    }

    @Override
    public void initData() {

        phone = getIntent().getStringExtra("phone");
    }

    @Override
    public void loadData() {

    }


    // 返回上一页面
    @OnClick(R.id.ll_return)
    public void back(){
        finish();
    }


    // 重新发送短信验证码
    @OnClick(R.id.tv_resend)
    public void resend(){
        tv_resend.setText("60 s");   // 重新发送
        MyHandler.postDelayed(MyRunnable, 1000);
    }


    // 下一步:验证短信
    @OnClick(R.id.btn_next)
    public void next(){
        //verifySmsCode();
        Intent intent = new Intent(InputCodeActivity.this, InputPasswordActivity.class);
        intent.putExtra("phone", phone);
        startActivity(intent);
        overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
    }


    // 编辑框文本变动监听
    @OnTextChanged(R.id.et_smscode)
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        if (!TextUtils.isEmpty(et_smscode.getText().toString())){
            btn_next.setEnabled(true);
        }
        else{
            btn_next.setEnabled(false);
        }

        smsCode = et_smscode.getText().toString();
    }

    /**
     * 获取短信验证码
     */
    private void sendSmsCode(){

        RetrofitServer.getRetrofit()
                .create(SignerApi.class)
                .sendSmsCode(phone)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResultResponse<String>>() {

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.e("error", e.toString());
                    }

                    @Override
                    public void onNext(ResultResponse<String> response) {

                        Log.e("smsId", response.getData());
                    }
                });
    }


    /**
     * 验证短信验证码
     */
    private void verifySmsCode(){

        RetrofitServer.getRetrofit()
                .create(SignerApi.class)
                .verifySmsCode(phone, smsCode)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResultResponse<String>>() {

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.e("error", e.toString());
                    }

                    @Override
                    public void onNext(ResultResponse<String> response) {

                        if (response.getCode().equals("200")){
                            Intent intent = new Intent(InputCodeActivity.this, InputPasswordActivity.class);
                            intent.putExtra("phone", phone);
                            startActivity(intent);
                            overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                        }
                        else{
                            String data = response.getMsg();
                            ToastView toastView = new ToastView(InputCodeActivity.this, data);
                            toastView.setGravity(Gravity.CENTER, 0, 0);
                            toastView.show();
                        }
                    }
                });
    }

    public static InputCodeActivity getInstance(){
        return instance;
    }
}
