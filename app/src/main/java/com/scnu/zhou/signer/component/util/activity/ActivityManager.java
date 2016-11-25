package com.scnu.zhou.signer.component.util.activity;

import android.app.Activity;

import java.util.Stack;

/**
 * Created by zhou on 16/11/25.
 */
public class ActivityManager {

    private static Stack<Activity> activityStack;

    private ActivityManager() {
    }

    public static ActivityManager getScreenManager() {

        return ActivityManagerHolder.instance;
    }

    private static class ActivityManagerHolder{

        private static final ActivityManager instance = new ActivityManager();
    }

    //退出栈顶Activity
    public void popActivity(Activity activity) {
        if (activity != null) {
            //在从自定义集合中取出当前Activity时，也进行了Activity的关闭操作
            activity.finish();
            activityStack.remove(activity);
            activity = null;
        }
    }

    //获得当前栈顶Activity
    public Activity currentActivity() {
        Activity activity = null;
        if(!activityStack.empty())
            activity= activityStack.lastElement();
        return activity;
    }

    //将当前Activity推入栈中
    public void pushActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }


    //退出栈中所有Activity
    public void popAllActivity() {
        while (true) {
            Activity activity = currentActivity();
            if (activity == null) {
                break;
            }
            popActivity(activity);
        }
    }

    //退出栈中所有Activity除了某一个
    public void popAllActivityExceptOne(Class cls) {
        while (true) {
            Activity activity = currentActivity();
            if (activity == null) {
                break;
            }
            if (activity.getClass().equals(cls)) {
                break;
            }
            popActivity(activity);
        }
    }
}
