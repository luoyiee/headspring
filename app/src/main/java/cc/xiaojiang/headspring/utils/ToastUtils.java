package cc.xiaojiang.headspring.utils;

import android.support.annotation.StringRes;
import android.widget.Toast;

import cc.xiaojiang.headspring.base.MyApplication;


public class ToastUtils {

    public static void show(String msg) {
        Toast.makeText(MyApplication.getInstance(), msg, Toast.LENGTH_SHORT).show();
    }

    public static void show(@StringRes int msg) {
        Toast.makeText(MyApplication.getInstance(), msg, Toast.LENGTH_SHORT).show();
    }
}
