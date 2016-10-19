package com.scnu.zhou.signer.widget.edit;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.scnu.zhou.signer.R;

/**
 * Created by zhou on 16/9/2.
 */
public class WhiteClearableEditText extends LinearLayout{

    private String text;
    private String hint;
    private Drawable clearDrawable = null;    // 清除按钮
    private boolean password;   //输入格式

    private EditText et_text;
    private Button btn_clear;

    public WhiteClearableEditText(Context context) {
        super(context);

        inflate(context, R.layout.layout_whiteclearable_edittext, this);

        init();
    }

    public WhiteClearableEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WhiteClearableEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        inflate(context, R.layout.layout_whiteclearable_edittext, this);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.WhiteClearableEditText);
        text = a.getString(R.styleable.WhiteClearableEditText_input_text);
        hint = a.getString(R.styleable.WhiteClearableEditText_input_hint);
        clearDrawable = a.getDrawable(R.styleable.WhiteClearableEditText_clearIcon);
        password = a.getBoolean(R.styleable.WhiteClearableEditText_ispassword,false);

        a.recycle();
        init();
    }


    private void init(){


        et_text = (EditText) findViewById(R.id.et_text);
        et_text.setText(text);
        et_text.setHint(hint);
        if (password){
            et_text.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }


        btn_clear = (Button) findViewById(R.id.btn_clear);
        if (clearDrawable != null){
            //btn_clear.setVisibility(View.VISIBLE);
            btn_clear.setBackground(clearDrawable);
            btn_clear.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    et_text.setText("");
                }
            });

            et_text.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    if (!TextUtils.isEmpty(et_text.getText().toString())){
                        btn_clear.setVisibility(View.VISIBLE);
                    }
                    else{
                        btn_clear.setVisibility(View.GONE);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
    }

    public String getText(){
        return et_text.getText().toString();
    }

    public void setText(String text){
        et_text.setText(text);
    }

    public String getHint(){
        return et_text.getHint().toString();
    }

    public void setHint(String hint){
        et_text.setHint(hint);
    }

    /**
     * 设置监听器
     */
    public void addTextChangedListener(TextWatcher watcher){
        et_text.addTextChangedListener(watcher);
    }
}
