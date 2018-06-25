package cc.xiaojiang.headerspring.utils;

import android.support.annotation.StringRes;
import android.widget.Toast;

import cc.xiaojiang.baselibrary.BaseLibrary;
import cc.xiaojiang.headerspring.base.MyApplication;


public class ToastUtils {

    public static void show(String msg) {
        Toast.makeText(MyApplication.getInstance(), msg, Toast.LENGTH_SHORT).show();
    }

    public static void show(@StringRes int msg) {
        Toast.makeText(MyApplication.getInstance(), msg, Toast.LENGTH_SHORT).show();
    }
}
