package com.scnu.zhou.signer.elss.util.statusbar;

import android.app.Activity;
import android.view.Window;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by zhou on 16/9/3.
 */
public class StatusBarUtil {

    // 针对MIUI系统，当状态栏颜色为白色时，将状态栏文字颜色改成黑色
    public static void setStatusBarDarkMode(boolean darkmode, Activity activity) {
        Class<? extends Window> clazz = activity.getWindow().getClass();
        try {
            int darkModeFlag = 0;
            Class<?> layoutParams = Class
                    .forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams
                    .getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class,
                    int.class);
            extraFlagField.invoke(activity.getWindow(), darkmode ? darkModeFlag
                    : 0, darkModeFlag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
