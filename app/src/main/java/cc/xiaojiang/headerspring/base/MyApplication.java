package cc.xiaojiang.headerspring.base;

import android.app.Application;
import android.content.Context;

import cc.xiaojiang.baselibrary.BaseLibrary;

public class MyApplication extends Application {
    private static Application instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        BaseLibrary.init(this);
    }

    public static Context getInstance() {
        return instance;
    }
}
