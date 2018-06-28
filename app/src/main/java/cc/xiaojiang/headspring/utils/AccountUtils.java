package cc.xiaojiang.headspring.utils;

import android.text.TextUtils;

public class AccountUtils {
    public static boolean isLogin() {
        return !TextUtils.isEmpty(DbUtils.getAccessToken());
    }
}
