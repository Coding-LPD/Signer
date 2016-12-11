package com.scnu.zhou.signer.ui.activity.base;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.scnu.zhou.signer.R;
import com.scnu.zhou.signer.component.cache.UserCache;
import com.scnu.zhou.signer.component.util.activity.ActivityManager;
import com.scnu.zhou.signer.component.util.statusbar.StatusBarUtil;
import com.scnu.zhou.signer.component.util.statusbar.SystemBarTintManager;
import com.scnu.zhou.signer.ui.widget.dialog.LoadingDialog;

/**
 * Created by zhou on 16/9/2.
 */
public class BaseSlideActivity extends SlideBackActivity {

    private LoadingDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.colorStatusBar);  //通知栏所需颜色
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        StatusBarUtil.setStatusBarDarkMode(true, this);

        dialog = new LoadingDialog(this);

        // Activity进栈
        ActivityManager.getScreenManager().pushActivity(this);
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    /**
     * 显示加载进度条
     */
    public void showLoadingDialog(String msg){

        dialog.setTitle(msg);
        dialog.show();
    }

    /**
     * 取消加载进度条
     */
    public void dismissLoadingDialog(){

        if (dialog.isShowing()) dialog.dismiss();
    }

    /**
     * 打开或取消键盘
     */
    public void toggleInputMethod(){

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // 打开输入法
                InputMethodManager m = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }, 500);
    }

    /**
     * 是否是学生
     */
    public boolean isStudent(){

        if (UserCache.getInstance().getRole(this).equals("0")){
            return true;
        }
        else{
            return false;
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        // Activity出栈
        ActivityManager.getScreenManager().popActivity(this);
    }
}
