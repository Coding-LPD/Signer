package com.scnu.zhou.signer.ui.activity.sign;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scnu.zhou.signer.R;
import com.scnu.zhou.signer.ui.activity.base.BaseActivity;
import com.scnu.zhou.signer.ui.widget.edit.SixNumEditText;
import com.scnu.zhou.signer.ui.widget.keyboard.NumberKeyboard;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zhou on 16/10/21.
 */
public class InputSignActivity extends BaseActivity {

    @Bind(R.id.tv_title) TextView tv_title;
    @Bind(R.id.ll_return) LinearLayout ll_return;
    @Bind(R.id.sixNumEditText) SixNumEditText sixNumEditText;
    @Bind(R.id.numberKeyboard) NumberKeyboard numberKeyboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_input_sign);
        ButterKnife.bind(this);
        initView();
    }

    public void initView(){

        tv_title.setText("签到码");
        ll_return.setVisibility(View.VISIBLE);

        numberKeyboard.setOnInputListener(new NumberKeyboard.OnIuputListener() {
            @Override
            public void onInput(int num) {

                if (num != -1) {
                    sixNumEditText.input(num);
                }
                else{
                    sixNumEditText.backspace();
                }
            }
        });


        sixNumEditText.setOnCompleteInputListener(new SixNumEditText.OnCompleteInputListener() {
            @Override
            public void onCompleteInput() {
                // 输入完成
                String result = sixNumEditText.getNumber();
                Log.e("result", result);
                Intent intent = new Intent(InputSignActivity.this, ConfirmSignActivity.class);
                intent.putExtra("code", result);
                startActivity(intent);
                overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
                finish();
            }
        });
    }

    // 返回上一页面
    @OnClick(R.id.ll_return)
    public void back(){
        finish();
    }
}
