package com.scnu.zhou.signer.component.util.gps;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by Kelvin on 2015/12/5.
 */
public class GPSManager {

    private double latitude = 0.0;
    private double longitude =0.0;
    private Context context;

    private boolean isReturned = false;
    private onGetLocationListener listener;

    public GPSManager(Context context){
        this.context = context;
    }

    public void startManager() {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();

                Log.e("1","经度："+location.getLatitude()+"  纬度："+location.getLongitude());

                if (listener != null && !isReturned){
                    listener.onGetLocation(latitude, longitude);
                    isReturned = true;
                }
            }
        } else {
            LocationListener locationListener = new LocationListener() {

                // Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                // Provider被enable时触发此函数，比如GPS被打开
                @Override
                public void onProviderEnabled(String provider) {

                }

                // Provider被disable时触发此函数，比如GPS被关闭
                @Override
                public void onProviderDisabled(String provider) {

                }

                //当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
                @Override
                public void onLocationChanged(Location location) {
                    if (location != null) {
                        //Log.e("Map", "Location changed : Lat: "
                        //        + location.getLatitude() + " Lng: "
                        //        + location.getLongitude());

                        Log.e("2","经度："+location.getLatitude()+"  纬度："+location.getLongitude());

                        if (listener != null && !isReturned){
                            listener.onGetLocation(latitude, longitude);
                            isReturned = true;
                        }
                    }
                }
            };
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, locationListener);
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location != null) {
                latitude = location.getLatitude(); //经度
                longitude = location.getLongitude(); //纬度

                Log.e("3","经度："+location.getLatitude()+"  纬度："+location.getLongitude());
                if (listener != null && !isReturned){
                    listener.onGetLocation(latitude, longitude);
                    isReturned = true;
                }
            }
        }
    }

    public void setonGetLocationListener(onGetLocationListener listener){

        this.listener = listener;
    }

    public interface onGetLocationListener{

        void onGetLocation(double latitude, double longitude);
    }

    /**
     * 计算距离:单位为米
     */
    private final static double EARTH_RADIUS = 6378.137;//地球半径
    private static double rad(double d)
    {
        return d * Math.PI / 180.0;
    }

    public static double GetDistance(double lat1, double lng1, double lat2, double lng2)
    {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);

        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) +
                Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10;
        return s;
    }

    /**
     * 返回为m，适合短距离测量
     *
     * @param lon1
     * @param lat1
     * @param lon2
     * @param lat2
     * @return
     */
    private static double DEF_PI = 3.14159265359; // PI
    private static double DEF_2PI = 6.28318530712; // 2*PI
    private static double DEF_PI180 = 0.01745329252; // PI/180.0
    private static double DEF_R = 6370693.5; // radius of earth

    public static double getShortDistance(double lat1, double lon1, double lat2, double lon2) {
        double ew1, ns1, ew2, ns2;
        double dx, dy, dew;
        double distance;
        // 角度转换为弧度
        ew1 = lon1 * DEF_PI180;
        ns1 = lat1 * DEF_PI180;
        ew2 = lon2 * DEF_PI180;
        ns2 = lat2 * DEF_PI180;
        // 经度差
        dew = ew1 - ew2;
        // 若跨东经和西经180 度，进行调整
        if (dew > DEF_PI)
            dew = DEF_2PI - dew;
        else if (dew < -DEF_PI)
            dew = DEF_2PI + dew;
        dx = DEF_R * Math.cos(ns1) * dew; // 东西方向长度(在纬度圈上的投影长度)
        dy = DEF_R * (ns1 - ns2); // 南北方向长度(在经度圈上的投影长度)
        // 勾股定理求斜边长
        distance = Math.sqrt(dx * dx + dy * dy);
        return distance;
    }
}
