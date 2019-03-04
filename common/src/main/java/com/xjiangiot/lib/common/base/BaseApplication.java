package com.xjiangiot.lib.common.base;

import android.app.Application;
import android.support.annotation.Nullable;

import com.aliyun.sls.android.sdk.SLSDatabaseManager;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.xjiangiot.lib.common.log.AndroidLog;

/**
 * Created by facexxyz on 2018/11/23
 */
public class BaseApplication extends Application {
    public static Application sInstance;
    public static boolean isDebug = false;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        Logger.addLogAdapter(new AndroidLogAdapter() {
            @Override
            public boolean isLoggable(int priority, @Nullable String tag) {
                return isDebug;
            }
        });
        //日志服务
        AndroidLog.getInstance().init(this, "");

    }

}
