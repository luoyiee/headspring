package com.xjiangiot.lib.common.utils;

import android.util.Log;



/**
 * Created by zhu on 16-2-24.
 */
public class LogUtils {
    public static final boolean DEBUG = true;

    public static void v(String tag, String msg) {
        if (DEBUG) {
            Log.v(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (DEBUG) {
            Log.d(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (DEBUG) {
            Log.i(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (DEBUG) {
            Log.w(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (DEBUG) {
            Log.e(tag, msg);
        }
    }

    public static String getClassName() {
        return Thread.currentThread().getStackTrace()[2].getClassName();
    }

    public static String getMethodName() {
        return Thread.currentThread().getStackTrace()[2].getMethodName();
    }

    public static int getLineNumber() {
        return Thread.currentThread().getStackTrace()[2].getLineNumber();
    }

    public static String getFileName() {
        return Thread.currentThread().getStackTrace()[2].getFileName();
    }

    public static String getClassInfo() {
        StackTraceElement element = new Throwable().getStackTrace()[3];
        StringBuilder buf = new StringBuilder(40);
        buf.append("[");
        buf.append("file:" + element.getFileName());
        buf.append("&class:" + element.getClassName());
        buf.append("&method:" + element.getMethodName());
        buf.append("&line:" + element.getLineNumber());
        buf.append("]");
        return buf.toString();
    }
}
