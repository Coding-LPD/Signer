package com.scnu.zhou.signer.ui.widget.keyboard;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.scnu.zhou.signer.R;

/**
 * Created by zhou on 16/10/21.
 */
public class NumberKeyboard extends LinearLayout{

    private Button button0, button1, button2, button3, button4, button5,
            button6, button7, button8, button9, button_delete;

    private OnIuputListener listener;

    public NumberKeyboard(Context context) {
        super(context);

        inflate(context, R.layout.layout_number_keyboard, this);

        init();
    }

    public NumberKeyboard(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NumberKeyboard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        inflate(context, R.layout.layout_number_keyboard, this);

        init();
    }

    private void init(){

        button0 = (Button) findViewById(R.id.button0);
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        button4 = (Button) findViewById(R.id.button4);
        button5 = (Button) findViewById(R.id.button5);
        button6 = (Button) findViewById(R.id.button6);
        button7 = (Button) findViewById(R.id.button7);
        button8 = (Button) findViewById(R.id.button8);
        button9 = (Button) findViewById(R.id.button9);
        button_delete = (Button) findViewById(R.id.button_delete);
    }

    private void setClickListeners(){

        button0.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onInput(0);
            }
        });

        button1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onInput(1);
            }
        });

        button2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onInput(2);
            }
        });

        button3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onInput(3);
            }
        });

        button4.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onInput(4);
            }
        });

        button5.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onInput(5);
            }
        });

        button6.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onInput(6);
            }
        });

        button7.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onInput(7);
            }
        });

        button8.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onInput(8);
            }
        });

        button9.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onInput(9);
            }
        });

        button_delete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onInput(-1);
            }
        });
    }

    public void setOnInputListener(OnIuputListener listener){

        this.listener = listener;
        setClickListeners();
    }

    public interface OnIuputListener{

        void onInput(int num);
    }
}
