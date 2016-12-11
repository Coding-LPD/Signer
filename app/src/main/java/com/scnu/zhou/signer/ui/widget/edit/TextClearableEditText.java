package com.scnu.zhou.signer.ui.widget.edit;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
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
import android.widget.TextView;

import com.scnu.zhou.signer.R;

/**
 * Created by zhou on 16/9/2.
 */
public class TextClearableEditText extends LinearLayout{

    private String title;
    private String text;
    private String hint;
    private int lineColor = Color.parseColor("#dddddd");    // 设置底部线条默认颜色
    private boolean singleline = true;   // 是否单行输入
    private Drawable clearDrawable = null;    // 清除按钮
    private boolean password;   //输入格式

    private TextView tv_title;
    private EditText et_text;
    private Button btn_clear;
    private View v_line;

    public TextClearableEditText(Context context) {
        super(context);

        inflate(context, R.layout.layout_textclearable_edittext, this);

        init();
    }

    public TextClearableEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextClearableEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        inflate(context, R.layout.layout_textclearable_edittext, this);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TextClearableEditText);
        title = a.getString(R.styleable.TextClearableEditText_title);
        text = a.getString(R.styleable.TextClearableEditText_txt);
        hint = a.getString(R.styleable.TextClearableEditText_hint);
        lineColor = a.getColor(R.styleable.TextClearableEditText_lineColor, Color.parseColor("#dddddd"));
        singleline = a.getBoolean(R.styleable.TextClearableEditText_singleline, true);
        clearDrawable = a.getDrawable(R.styleable.TextClearableEditText_clearDrawable);
        password = a.getBoolean(R.styleable.TextClearableEditText_password,false);

        a.recycle();
        init();
    }


    private void init(){

        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(title);

        et_text = (EditText) findViewById(R.id.et_text);
        et_text.setText(text);
        et_text.setHint(hint);
        et_text.setSingleLine(singleline);
        if (password){
            et_text.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }

        v_line = findViewById(R.id.v_line);
        v_line.setBackgroundColor(lineColor);

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

    /**
     * 将光标移动到末尾
     */
    public void setSelectionAtEnd(){

        et_text.setSelection(et_text.getText().length());

    }
}
