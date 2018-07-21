package cc.xiaojiang.liangbo.utils;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;

import com.orhanobut.logger.Logger;

import cc.xiaojiang.liangbo.activity.LoginActivity;
import cc.xiaojiang.liangbo.base.MyApplication;
import cc.xiaojiang.iotkit.mqtt.IotKitConnectionManager;

public class AccountUtils {
    public static boolean isLogin() {
        return !TextUtils.isEmpty(DbUtils.getAccessToken());
    }

    public static void logout() {

    }
}
