package com.scnu.zhou.signer.ui.widget.segment;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scnu.zhou.signer.R;

/**
 * Created by zhou on 16/10/22.
 */
public class TwoStageSegment extends LinearLayout {

    private String left_title = "";
    private String right_title = "";

    private TextView tv_left_title, tv_right_title;
    private LinearLayout ll_segement;

    private final int STATE_LEFT = 0x001;
    private final int STATE_RIGHT = 0x002;

    private int state = STATE_LEFT;

    private OnSelectListener listener;

    public TwoStageSegment(Context context) {
        super(context);

        inflate(context, R.layout.segment_notice, this);

        init();
    }

    public TwoStageSegment(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TwoStageSegment(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        inflate(context, R.layout.segment_notice, this);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TwoStageSegment);
        left_title = a.getString(R.styleable.TwoStageSegment_left_title);
        right_title = a.getString(R.styleable.TwoStageSegment_right_title);

        a.recycle();

        init();
    }

    private void init(){

        tv_left_title = (TextView) findViewById(R.id.tv_left_title);
        tv_right_title = (TextView) findViewById(R.id.tv_right_title);

        tv_left_title.setText(left_title);
        tv_right_title.setText(right_title);

        ll_segement = (LinearLayout) findViewById(R.id.ll_segement);
        ll_segement.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (state == STATE_LEFT){
                    state = STATE_RIGHT;
                    if (listener != null){
                        listener.onSelectRight();
                    }

                    ll_segement.setBackgroundResource(R.drawable.segment_right);
                    tv_left_title.setTextColor(Color.parseColor("#666666"));
                    tv_right_title.setTextColor(Color.parseColor("#ffffff"));
                }
                else{
                    state = STATE_LEFT;
                    if (listener != null){
                        listener.onSelectLeft();
                    }

                    ll_segement.setBackgroundResource(R.drawable.segment_left);
                    tv_left_title.setTextColor(Color.parseColor("#ffffff"));
                    tv_right_title.setTextColor(Color.parseColor("#666666"));
                }
            }
        });
    }

    public void setLeftTitle(String title){

        left_title = title;
    }

    public void setRightTitle(String title){

        right_title = title;
    }

    public void setOnSelectListener(OnSelectListener listener){

        this.listener = listener;
    }

    public interface OnSelectListener{

        void onSelectLeft();
        void onSelectRight();
    }
}
