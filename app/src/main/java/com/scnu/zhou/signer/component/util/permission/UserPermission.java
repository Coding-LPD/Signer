package com.scnu.zhou.signer.component.util.permission;

import android.content.Context;
import android.content.pm.PackageManager;

/**
 * Created by zhou on 16/11/7.
 */
public class UserPermission {

    private UserPermission(){

    }

    private static class UserPermissionHolder{

        private static UserPermission instance = new UserPermission();
    }

    public static UserPermission getInstance(){

        return UserPermissionHolder.instance;
    }

    public boolean isCameraPermitted(Context context){

        PackageManager pm = context.getPackageManager();
        boolean permission = (PackageManager.PERMISSION_GRANTED ==
                pm.checkPermission("android.permission.CAMERA", "com.scnu.zhou.signer"));
        if (permission) {
            return true;
        }else {
            return false;
        }
    }


    public boolean isLocatePermitted(Context context){

        PackageManager pm = context.getPackageManager();
        //boolean permission1 = (PackageManager.PERMISSION_GRANTED ==
                //pm.checkPermission("android.permission.ACCESS_COARSE_LOCATION", "com.scnu.zhou.signer"));
        boolean permission = (PackageManager.PERMISSION_GRANTED ==
                pm.checkPermission("android.permission.ACCESS_FINE_LOCATION", "com.scnu.zhou.signer"));
        if (permission) {
            return true;
        }else {
            return false;
        }
    }
}
