package cc.xiaojiang.liangbo.utils;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.Settings;

import com.github.mikephil.charting.formatter.IFillFormatter;

public class NetworkUtils {
    public static final int REQUEST_SETTING_GPS = 1;

    public static void changeWifi(Context context) {
        Intent intent2 = new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS);
        context.startActivity(intent2);
    }

    public static boolean isConnected(Context context) {
        NetworkInfo info = getActiveNetworkInfo(context);
        return info != null && info.isConnected();
    }


    public static NetworkInfo getActiveNetworkInfo(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }

    public static boolean isWifiConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm != null && cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;
    }

    /**
     * 判断GPS,网络是否开启
     *
     * @param context
     * @return true 表示开启
     */
    public static boolean isLocationAvailiable(final Context context) {
        return isAGPSAvailiable(context) || isGpsAvailiable(context);
    }

    /**
     * 判断GPS,网络是否开启
     *
     * @param context
     * @return true 表示开启
     */
    public static boolean isGpsAvailiable(final Context context) {
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        LocationManager locationManager
                = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager != null && locationManager.isProviderEnabled(LocationManager
                .GPS_PROVIDER);
    }

    /**
     * 判断GPS,网络是否开启
     *
     * @param context
     * @return true 表示开启
     */
    public static boolean isAGPSAvailiable(final Context context) {
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        LocationManager locationManager
                = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager != null && locationManager.isProviderEnabled(LocationManager
                .NETWORK_PROVIDER);
    }

    /**
     * 打开手机GPS设置界面
     *
     * @param activity
     */
    public static void openSettingGPS(Activity activity) {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        activity.startActivityForResult(intent, REQUEST_SETTING_GPS);
    }

}
