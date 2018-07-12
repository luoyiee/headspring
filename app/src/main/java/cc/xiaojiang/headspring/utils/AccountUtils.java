package cc.xiaojiang.headspring.utils;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;

import com.orhanobut.logger.Logger;

import cc.xiaojiang.headspring.activity.LoginActivity;
import cc.xiaojiang.headspring.base.MyApplication;
import cc.xiaojiang.iotkit.mqtt.IotKitConnectionManager;

public class AccountUtils {
    public static boolean isLogin() {
        return !TextUtils.isEmpty(DbUtils.getAccessToken());
    }

    public static void logout() {
        DbUtils.clear();
        IotKitConnectionManager.getInstance().disconnected();
        Intent intent = new Intent(MyApplication.getInstance(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        MyApplication.getInstance().getApplicationContext().startActivity(intent);
        Logger.d("重新登陆");
    }
}
