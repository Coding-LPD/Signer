package com.scnu.zhou.signer.elss.widget.cell;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scnu.zhou.signer.R;

/**
 * Created by zhou on 16/9/6.
 */
public class TitleTextCell extends LinearLayout {

    private String title;
    private String text;
    private boolean divider = false;

    private TextView tv_title, tv_text;
    private View v_bottomline;

    public TitleTextCell(Context context) {
        super(context);
        inflate(context, R.layout.layout_cell_titletext, this);
    }

    public TitleTextCell(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleTextCell(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        inflate(context, R.layout.layout_cell_titletext, this);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TitleTextCell);
        title = a.getString(R.styleable.TitleTextCell_cell_title);
        text = a.getString(R.styleable.TitleTextCell_cell_text);
        divider = a.getBoolean(R.styleable.TitleTextCell_cell_isdivider, false);
        a.recycle();
        init();
    }

    private void init(){

        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(title);

        tv_text = (TextView) findViewById(R.id.tv_text);
        tv_text.setText(text);

        v_bottomline = findViewById(R.id.v_bottomline);
        if (divider){
            v_bottomline.setVisibility(View.VISIBLE);
        }
    }

    public void setTitle(String title){

        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(title);
    }

    public void setText(String text){

        tv_text = (TextView) findViewById(R.id.tv_text);
        tv_text.setText(text);
    }

    public void setDivider(boolean isShow){

        v_bottomline = findViewById(R.id.v_bottomline);
        if (isShow){
            v_bottomline.setVisibility(View.VISIBLE);
        }
        else{
            v_bottomline.setVisibility(View.GONE);
        }
    }
}
