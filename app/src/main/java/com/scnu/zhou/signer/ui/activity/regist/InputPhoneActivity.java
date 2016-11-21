package com.scnu.zhou.signer.ui.activity.regist;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scnu.zhou.signer.R;
import com.scnu.zhou.signer.ui.activity.base.BaseSlideActivity;
import com.scnu.zhou.signer.component.util.phone.PhoneUtil;
import com.scnu.zhou.signer.ui.widget.edit.TextClearableEditText;
import com.scnu.zhou.signer.ui.widget.toast.ToastView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zhou on 16/9/3.
 */
public class InputPhoneActivity extends BaseSlideActivity{

    @Bind(R.id.ll_return) LinearLayout ll_return;
    @Bind(R.id.tv_title) TextView tv_title;
    @Bind(R.id.btn_next) Button btn_next;

    @Bind(R.id.et_phone) TextClearableEditText et_phone;

    private static InputPhoneActivity instance;

    private String state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_input_phone);

        ButterKnife.bind(this);
        instance = this;
        initView();
        initData();
    }

    public void initView() {

        ll_return.setVisibility(View.VISIBLE);
        tv_title.setText("请填写手机号码");

        et_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(et_phone.getText())){
                    btn_next.setEnabled(true);
                }
                else{
                    btn_next.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void initData(){

        state = getIntent().getStringExtra("state");
    }


    // 返回上一页面
    @OnClick(R.id.ll_return)
    public void back(){
        finish();
    }


    // 下一步
    @OnClick(R.id.btn_next)
    public void next(){
        if (PhoneUtil.isMobileNO(et_phone.getText())) {
            Intent intent = new Intent(this, InputCodeActivity.class);
            intent.putExtra("phone", et_phone.getText());
            intent.putExtra("state", state);
            startActivity(intent);
            overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
        }
        else{
            ToastView toastView = new ToastView(this, "手机号码格式不正确");
            toastView.setGravity(Gravity.CENTER, 0, 0);
            toastView.show();
        }
    }

    // 返回Activity实例对象
    public static InputPhoneActivity getInstance(){
        return instance;
    }
}
