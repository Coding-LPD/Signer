package com.scnu.zhou.signer.ui.widget.scrollview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by zhou on 16/11/18.
 */
public class IMMListenerScrollView extends ScrollView {

    private final String TAG = "IMMListenerScrollView";
    private InputWindowListener listener;

    public IMMListenerScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //不拦截，继续分发下去
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (oldh > h) {
            Log.e(TAG, "input window show");
            listener.show();
        } else{
            Log.e(TAG, "input window hidden");
            listener.hidden();
        }
    }

    public void setListener(InputWindowListener listener) {
        this.listener = listener;
    }


    public interface InputWindowListener {
        void show();

        void hidden();
    }
}
