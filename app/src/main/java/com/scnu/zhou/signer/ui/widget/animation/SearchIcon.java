package com.scnu.zhou.signer.ui.widget.animation;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.scnu.zhou.signer.R;

/**
 * Created by zhou on 16/11/28.
 */
public class SearchIcon extends View {

    private int mWidth = 100;
    private int mHeight = 100;

    private Paint mPaint;

    private Path path, dst;
    private PathMeasure measure;
    private float length = 0;

    private int color = Color.parseColor("#FFFFFF");
    private int duration = 2000;

    public SearchIcon(Context context) {
        this(context, null);
    }

    public SearchIcon(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (attrs != null){

            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SearchIcon);
            color = a.getColor(R.styleable.SearchIcon_color, Color.parseColor("#FFFFFF"));
            duration = a.getInt(R.styleable.SearchIcon_duration, 2000);
            a.recycle();

        }

        initPaint();
        initPath();
        startAnimation();
    }


    // 初始化画笔
    private void initPaint(){

        mPaint = new Paint();
        mPaint.setColor(color);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(10);
    }


    // 初始化路径
    private void initPath(){

        int radius = ((mWidth > mHeight)?mHeight:mWidth) / 2;
        float x = (float)(radius * (0.5 - Math.cos(Math.PI / 4)));
        float y = (float)(radius * (0.5 - Math.sin(Math.PI / 4)));

        path = new Path();

        path.moveTo(0, 0);
        RectF oval1 = new RectF(-2*radius - x, -2*radius - y, -x, -y);          // 放大镜圆环
        path.addArc(oval1, 45, 359.9f);

        path.lineTo(radius/1.5f, radius/1.5f);

        dst = new Path();
        measure = new PathMeasure(path, false);
    }


    // 测量View的大小
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST){
            setMeasuredDimension(mWidth, mHeight);
        }
        else if (widthSpecMode == MeasureSpec.AT_MOST){
            setMeasuredDimension(mWidth, heightSpecSize);
        }
        else if (heightSpecMode == MeasureSpec.AT_MOST){
            setMeasuredDimension(widthSpecSize, mHeight);
        }
    }


    // 确定View的大小
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mWidth = w;
        mHeight = h;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.translate(mWidth/2, mHeight/2);

        //initPath();
        canvas.drawPath(dst, mPaint);
    }

    public void startAnimation(){

        Log.e("length", measure.getLength()+"");
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1.0f);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(){

            @Override
            public void onAnimationUpdate(ValueAnimator animator){

                float currentValue = (Float) animator.getAnimatedValue();
                measure.getSegment(length, measure.getLength() * currentValue, dst, false);

                length = measure.getLength() * currentValue;

                if (length == measure.getLength()){
                    dst.reset();
                    startAnimation();
                }
                postInvalidate();    // 根据进度值改变布局
            }
        });
        valueAnimator.setDuration(duration).start();
    }
}
