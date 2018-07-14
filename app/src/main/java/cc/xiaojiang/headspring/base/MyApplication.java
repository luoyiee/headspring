package cc.xiaojiang.headspring.base;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import cc.xiaojiang.headspring.BuildConfig;
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

//       XjConfig.init(this).configure();
//        MobSDK.init(this);
        IotKit.init(this, new IotKitAccountImpl());
//        if(!BuildConfig.DEBUG){
        Beta.autoCheckUpgrade = false;
        Bugly.init(getApplicationContext(), "be4413cd77", false);
//        }
        ZXingLibrary.initDisplayOpinion(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
