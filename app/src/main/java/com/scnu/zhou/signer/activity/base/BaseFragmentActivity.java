package com.scnu.zhou.signer.activity.base;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;
import android.view.WindowManager;

import com.scnu.zhou.signer.R;
import com.scnu.zhou.signer.util.statusbar.StatusBarUtil;
import com.scnu.zhou.signer.util.statusbar.SystemBarTintManager;
import com.scnu.zhou.signer.widget.dialog.LoadingDialog;

/**
 * Created by zhou on 16/9/2.
 */
public abstract class BaseFragmentActivity extends FragmentActivity {

    private LoadingDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.colorStatusBar);  //通知栏所需颜色
        }

        StatusBarUtil.setStatusBarDarkMode(true,this);

        dialog = new LoadingDialog(this);
    }

    public abstract void initView();

    public abstract void initData();

    public abstract void loadData();

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
}
