package cc.xiaojiang.liangbo.base;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.mob.MobSDK;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;
import com.xjiangiot.lib.common.base.Base;
import com.xjiangiot.lib.common.base.BaseApplication;

import cc.xiaojiang.iotkit.IotKit;
import cc.xiaojiang.liangbo.BuildConfig;
import cc.xiaojiang.liangbo.iotkit.IotKitAccountImpl;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MyApplication extends BaseApplication {
    private static Application instance;

    public static Context getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        IotKit.init(this, new IotKitAccountImpl(), false);
        if (!BuildConfig.DEBUG) {
            Bugly.init(getApplicationContext(), "be4413cd77", false);
            Beta.autoCheckUpgrade = true;
        }
        ZXingLibrary.initDisplayOpinion(this);
        MobSDK.init(this);
        initRealm();
    }

    private void initRealm() {
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .name("liangbo.realm")
                .schemaVersion(1)
                .build();
        Realm.setDefaultConfiguration(config);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
