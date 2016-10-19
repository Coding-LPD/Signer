package com.scnu.zhou.signer.elss.widget.cell;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scnu.zhou.signer.R;

/**
 * Created by zhou on 16/9/6.
 */
public class IconTitleCell extends LinearLayout {

    private String title;
    private Drawable icon = null;
    private boolean divider = false;

    private TextView tv_title;
    private ImageView iv_icon;
    private View v_bottomline;

    public IconTitleCell(Context context) {
        super(context);
        inflate(context, R.layout.layout_cell_icontext, this);
    }

    public IconTitleCell(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IconTitleCell(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        inflate(context, R.layout.layout_cell_icontext, this);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.IconTitleCell);
        title = a.getString(R.styleable.IconTitleCell_text);
        icon = a.getDrawable(R.styleable.IconTitleCell_icon);
        divider = a.getBoolean(R.styleable.IconTitleCell_isdivider, false);
        a.recycle();
        init();
    }

    private void init(){

        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(title);

        iv_icon = (ImageView) findViewById(R.id.iv_icon);
        if (icon != null) {
            iv_icon.setVisibility(VISIBLE);
            iv_icon.setImageDrawable(icon);
        }

        v_bottomline = findViewById(R.id.v_bottomline);
        if (divider){
            v_bottomline.setVisibility(View.VISIBLE);
        }
    }

    public void setTitle(String title){

        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(title);
    }

    public void setIconDrawable(Drawable icon){

        iv_icon = (ImageView) findViewById(R.id.iv_icon);
        iv_icon.setVisibility(VISIBLE);
        iv_icon.setImageDrawable(icon);
    }

    public void setIconResource(int res){

        iv_icon = (ImageView) findViewById(R.id.iv_icon);
        iv_icon.setVisibility(VISIBLE);
        iv_icon.setImageResource(res);
    }

    public void setDivider(boolean isShow){

        if (isShow){
            v_bottomline.setVisibility(View.VISIBLE);
        }
    }
}
