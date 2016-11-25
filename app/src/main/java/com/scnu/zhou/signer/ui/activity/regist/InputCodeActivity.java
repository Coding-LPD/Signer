package com.scnu.zhou.signer.ui.activity.regist;

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
import com.scnu.zhou.signer.component.bean.http.ResultResponse;
import com.scnu.zhou.signer.presenter.regist.ISmsPresenter;
import com.scnu.zhou.signer.presenter.regist.SmsPresenter;
import com.scnu.zhou.signer.ui.activity.base.BaseSlideActivity;
import com.scnu.zhou.signer.ui.activity.user.settings.UpdatePasswordActivity;
import com.scnu.zhou.signer.ui.widget.toast.ToastView;
import com.scnu.zhou.signer.view.regist.ISmsView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * Created by zhou on 16/9/3.
 */
public class InputCodeActivity extends BaseSlideActivity implements ISmsView{

    @Bind(R.id.ll_return) LinearLayout ll_return;
    @Bind(R.id.tv_title) TextView tv_title;

    @Bind(R.id.tv_resend) TextView tv_resend;
    private int second = 60;

    @Bind(R.id.et_smscode) EditText et_smscode;
    @Bind(R.id.btn_next) Button btn_next;

    private String phone;
    private String smsCode;

    private ISmsPresenter smsPresenter;

    private String state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_input_code);

        ButterKnife.bind(this);

        initView();
        initData();
        MyHandler.postDelayed(MyRunnable, 1000);

        // 发送短信验证码
        //smsPresenter.sendSmsCode(phone);
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
    }

    @Override
    public void initData() {

        phone = getIntent().getStringExtra("phone");
        state = getIntent().getStringExtra("state");

        smsPresenter = new SmsPresenter(this);
    }


    // 发送短信验证码成功
    @Override
    public void onSendSmsSuccess(ResultResponse<String> response) {

        Log.e("smsId", response.getData());
    }


    // 发送短信验证码失败
    @Override
    public void onSendSmsError(Throwable e) {

        Log.e("send sms error", e.toString());
    }


    // 验证短信验证码成功
    @Override
    public void onVerifySmsSuccess(ResultResponse<String> response) {

        dismissLoadingDialog();
        if (response.getCode().equals("200")){

            if (state.equals("regist")) {      // 注册
                Intent intent = new Intent(InputCodeActivity.this, InputPasswordActivity.class);
                intent.putExtra("phone", phone);
                startActivity(intent);
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
            else if (state.equals("update")){    // 修改密码
                Intent intent = new Intent(InputCodeActivity.this, UpdatePasswordActivity.class);
                intent.putExtra("phone", phone);
                startActivity(intent);
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
            }
        }
        else{
            String data = response.getMsg();
            ToastView toastView = new ToastView(InputCodeActivity.this, data);
            toastView.setGravity(Gravity.CENTER, 0, 0);
            toastView.show();
        }
    }


    // 验证短信验证码失败
    @Override
    public void onVerifySmsError(Throwable e) {

        dismissLoadingDialog();
        Log.e("verify sms error", e.toString());
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
        showLoadingDialog("验证中");
        //smsPresenter.verifySmsCode(phone, smsCode);

        if (state.equals("regist")) {      // 注册
            Intent intent = new Intent(InputCodeActivity.this, InputPasswordActivity.class);
            intent.putExtra("phone", phone);
            startActivity(intent);
            overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
        }
        else if (state.equals("update")){    // 修改密码
            Intent intent = new Intent(InputCodeActivity.this, UpdatePasswordActivity.class);
            intent.putExtra("phone", phone);
            startActivity(intent);
            overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
        }
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
}
