package cc.xiaojiang.liangbo.base;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.mob.MobSDK;
import com.tencent.bugly.Bugly;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import cc.xiaojiang.iotkit.IotKit;
import cc.xiaojiang.liangbo.iotkit.IotKitAccountImpl;

public class MyApplication extends Application {
    private static Application instance;

    public static Context getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        IotKit.init(this, new IotKitAccountImpl(), false);
//        if(!BuildConfig.DEBUG){
        Bugly.init(getApplicationContext(), "be4413cd77", false);
//        Beta.autoCheckUpgrade = true;
//        }
        ZXingLibrary.initDisplayOpinion(this);
        MobSDK.init(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
