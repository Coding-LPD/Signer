package com.scnu.zhou.signer.ui.widget.edit;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scnu.zhou.signer.R;

/**
 * Created by zhou on 16/10/21.
 */
public class SixNumEditText extends LinearLayout {

    private ImageView point01, point02, point03, point04, point05, point06;
    private TextView num01, num02, num03, num04, num05, num06;

    private int pos = 1;
    private String number = "";

    private OnCompleteInputListener listener;

    public SixNumEditText(Context context) {
        super(context);

        inflate(context, R.layout.layout_sixnum_edittext, this);

        init();
    }

    public SixNumEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SixNumEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        inflate(context, R.layout.layout_sixnum_edittext, this);

        init();
    }

    private void init(){

        point01 = (ImageView) findViewById(R.id.point01);
        point02 = (ImageView) findViewById(R.id.point02);
        point03 = (ImageView) findViewById(R.id.point03);
        point04 = (ImageView) findViewById(R.id.point04);
        point05 = (ImageView) findViewById(R.id.point05);
        point06 = (ImageView) findViewById(R.id.point06);

        num01 = (TextView) findViewById(R.id.num01);
        num02 = (TextView) findViewById(R.id.num02);
        num03 = (TextView) findViewById(R.id.num03);
        num04 = (TextView) findViewById(R.id.num04);
        num05 = (TextView) findViewById(R.id.num05);
        num06 = (TextView) findViewById(R.id.num06);
    }

    public void input(int num){

        if (num/10 > 1){   // 非个位数
            return;
        }

        if (pos == 7){    // 位数已满
            return;
        }

        switch (pos){
            case 1:
                point01.setVisibility(GONE);
                num01.setText(num + "");
                break;
            case 2:
                point02.setVisibility(GONE);
                num02.setText(num + "");
                break;
            case 3:
                point03.setVisibility(GONE);
                num03.setText(num + "");
                break;
            case 4:
                point04.setVisibility(GONE);
                num04.setText(num + "");
                break;
            case 5:
                point05.setVisibility(GONE);
                num05.setText(num + "");
                break;
            case 6:
                point06.setVisibility(GONE);
                num06.setText(num + "");
                break;
        }

        pos++;

        number = number + num;

        if (pos == 7){
            listener.onCompleteInput();
        }
    }


    public void backspace(){

        if (pos == 1) return;

        switch (pos){
            case 2:
                point01.setVisibility(VISIBLE);
                num01.setText("");
                break;
            case 3:
                point02.setVisibility(VISIBLE);
                num02.setText("");
                break;
            case 4:
                point03.setVisibility(VISIBLE);
                num03.setText("");
                break;
            case 5:
                point04.setVisibility(VISIBLE);
                num04.setText("");
                break;
            case 6:
                point05.setVisibility(VISIBLE);
                num05.setText("");
                break;
            case 7:
                point06.setVisibility(VISIBLE);
                num06.setText("");
                break;
        }

        pos--;

        number = number.substring(0, number.length() - 1);
    }

    public String getNumber(){
        return number;
    }

    public void setOnCompleteInputListener(OnCompleteInputListener listener){

        this.listener = listener;
    }

    public interface OnCompleteInputListener{

        void onCompleteInput();

    }
}
