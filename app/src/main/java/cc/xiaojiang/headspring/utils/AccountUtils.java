package cc.xiaojiang.headspring.utils;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;

import cc.xiaojiang.baselibrary.app.AppManager;
import cc.xiaojiang.headspring.activity.LoginActivity;
import cc.xiaojiang.iotkit.mqtt.IotKitConnectionManager;

public class AccountUtils {
    public static boolean isLogin() {
        return !TextUtils.isEmpty(DbUtils.getAccessToken());
    }

    public static void logout() {
        DbUtils.clear();
        IotKitConnectionManager.getInstance().disconnected();
        Activity topActivity = AppManager.getInstance().getTopActivity();
        Intent intent = new Intent(topActivity, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        topActivity.startActivity(intent);
        topActivity.finish();
    }
}
