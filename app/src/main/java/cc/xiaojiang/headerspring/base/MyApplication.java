package cc.xiaojiang.headerspring.base;

import android.app.Application;
import android.content.Context;

import cc.xiaojiang.baselibrary.BaseLibrary;
import cc.xiaojiang.baselibrary.app.XjConfig;

public class MyApplication extends Application {
    private static Application instance;

    public static Context getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        BaseLibrary.init(this);

        XjConfig.init(this)
                .configure();
//        MobSDK.init(this);
    }
}
