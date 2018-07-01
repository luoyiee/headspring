package cc.xiaojiang.headspring.base;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.mob.MobSDK;

import cc.xiaojiang.headspring.iotkit.IotKitAccountImpl;
import cc.xiaojiang.iotkit.IotKit;

public class MyApplication extends Application {
    private static Application instance;

    public static Context getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
//        BaseLibrary.init(this);

//        XjConfig.init(this)
//                .configure();
        MobSDK.init(this);
        IotKit.init(this, new IotKitAccountImpl());
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);

    }
}
