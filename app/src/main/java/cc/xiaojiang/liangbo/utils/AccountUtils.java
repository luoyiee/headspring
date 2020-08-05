package cc.xiaojiang.liangbo.utils;

import android.content.Intent;
import android.text.TextUtils;

import cc.xiaojiang.liangbo.activity.LoginActivity;
import cc.xiaojiang.liangbo.base.MyApplication;

public class AccountUtils {
    public static boolean isLogin() {
        return !TextUtils.isEmpty(DbUtils.getAccessToken());
    }

    public static void logout(){
        DbUtils.clear();
        Intent intent = new Intent(MyApplication.getInstance(), LoginActivity
                .class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent
                .FLAG_ACTIVITY_NEW_TASK);
        MyApplication.getInstance().getApplicationContext().startActivity(intent);
    }
}
