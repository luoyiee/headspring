package com.xjiangiot.lib.common.utils;

import android.widget.Toast;

import com.xjiangiot.lib.common.base.BaseApplication;


public class ToastUtils {

    public static void showLong(String msg) {
        Toast.makeText(BaseApplication.sInstance, msg, Toast.LENGTH_LONG).show();
    }

    public static void showShort(String msg) {
        Toast.makeText(BaseApplication.sInstance, msg, Toast.LENGTH_SHORT).show();
    }
}
