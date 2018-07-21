package cc.xiaojiang.headspring.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public class NetworkUtils {

    public static void changeWifi(Context context) {
        Intent intent2 = new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS);
        context.startActivity(intent2);
    }
}
