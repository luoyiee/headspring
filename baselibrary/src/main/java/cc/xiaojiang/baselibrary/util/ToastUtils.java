package cc.xiaojiang.baselibrary.util;

import android.support.annotation.StringRes;
import android.widget.Toast;

import cc.xiaojiang.baselibrary.BaseLibrary;


public class ToastUtils {

    public static void show(String msg) {
        Toast.makeText(BaseLibrary.context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void show(@StringRes int msg) {
        Toast.makeText(BaseLibrary.context, msg, Toast.LENGTH_SHORT).show();
    }
}
