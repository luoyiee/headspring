package com.xjiangiot.lib.common.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;

import com.xjiangiot.lib.common.base.BaseApplication;

import static android.content.Context.WIFI_SERVICE;

public class NetworkUtils {
    /**
     * 判断是不是2.4G wifi,smartConfig不支持5G wifi,只在LOLLIPOP及以上Android版本判断
     */
    public static boolean isWifi24G() {
        WifiManager wifiManager = (WifiManager) BaseApplication.sInstance.getSystemService
                (WIFI_SERVICE);
        if (wifiManager == null) {
            return false;
        }
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return wifiInfo.getFrequency() > 2400 && wifiInfo.getFrequency() < 2500;
        } else {
            return true;
        }
    }

    /**
     * 获取用户连接设备wifi的ssid
     * An app must hold
     * {@link android.Manifest.permission#ACCESS_COARSE_LOCATION ACCESS_COARSE_LOCATION} or
     * {@link android.Manifest.permission#ACCESS_FINE_LOCATION ACCESS_FINE_LOCATION} permission
     */
    public static String getWifiConnectedSsid() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O || Build.VERSION.SDK_INT == Build
                .VERSION_CODES.P) {
            WifiManager mWifiManager = (WifiManager) BaseApplication.sInstance
                    .getSystemService(Context.WIFI_SERVICE);
            assert mWifiManager != null;
            WifiInfo info = mWifiManager.getConnectionInfo();
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                return info.getSSID();
            } else {
                return info.getSSID().replace("\"", "");
            }
        } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O_MR1) {
            ConnectivityManager connManager = (ConnectivityManager) BaseApplication.sInstance
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            assert connManager != null;
            NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
            if (networkInfo.isConnected()) {
                if (networkInfo.getExtraInfo() != null) {
                    return networkInfo.getExtraInfo().replace("\"", "");
                }
            }
        }
        return null;
    }


    /**
     * 获取连接设备的MAC地址
     */
    public static String getBssid() {
        WifiManager mWifiManager = (WifiManager) BaseApplication.sInstance
                .getSystemService(Context.WIFI_SERVICE);
        if (mWifiManager == null) {
            return "";
        }
        WifiInfo info = mWifiManager.getConnectionInfo();
        return info.getBSSID();
    }


    /**
     * 判断是不是2.4G ic_wifi,smartConfig不支持5G ic_wifi,只在LOLLIPOP及以上Android版本判断
     */
    public static boolean isWifi24G(Context context) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService
                (WIFI_SERVICE);
        if (wifiManager == null) {
            return false;
        }
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return wifiInfo.getFrequency() > 2400 && wifiInfo.getFrequency() < 2500;
        } else {
            return true;
        }
    }


    /**
     * 打开网络设置
     */
    public static void openWirelessSettings() {
        Intent intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
        BaseApplication.sInstance.startActivity(intent);
    }

    /**
     * 打开wifi设置
     */
    public static void openWifiSettings() {
        Intent intent = new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS);
        BaseApplication.sInstance.startActivity(intent);
    }

    /**
     * 判断WIFI网络是否开启
     */
    public static boolean isWifiEnabled() {
        WifiManager wm = (WifiManager) BaseApplication.sInstance.getSystemService(Context
                .WIFI_SERVICE);
        return wm != null && wm.isWifiEnabled();
    }

    /**
     * 判断WIFI是否连接成功
     */
    public static boolean isWifiContected() {
        ConnectivityManager cm = (ConnectivityManager) BaseApplication.sInstance
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return info != null && info.isConnected();
    }

    /**
     * 判断GPS,网络是否开启
     *
     * @return true 表示开启
     */
    public static boolean isLocationAvailiable() {
        return isAGPSAvailiable() || isGpsAvailiable();
    }

    /**
     * 判断GPS,网络是否开启
     *
     * @return true 表示开启
     */
    public static boolean isGpsAvailiable() {
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        LocationManager locationManager
                = (LocationManager) BaseApplication.sInstance.getSystemService(Context
                .LOCATION_SERVICE);
        return locationManager != null && locationManager.isProviderEnabled(LocationManager
                .GPS_PROVIDER);
    }

    /**
     * 判断GPS,网络是否开启
     *
     * @return true 表示开启
     */
    public static boolean isAGPSAvailiable() {
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        LocationManager locationManager
                = (LocationManager) BaseApplication.sInstance.getSystemService(Context
                .LOCATION_SERVICE);
        return locationManager != null && locationManager.isProviderEnabled(LocationManager
                .NETWORK_PROVIDER);
    }

    /**
     * 打开手机GPS设置界面
     *
     * @param activity
     */
    public static void openSettingGPS(Activity activity, int requestCode) {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        activity.startActivityForResult(intent, requestCode);
    }
}
