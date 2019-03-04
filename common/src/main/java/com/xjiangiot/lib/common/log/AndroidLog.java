package com.xjiangiot.lib.common.log;

import android.app.Application;

import com.aliyun.sls.android.sdk.ClientConfiguration;
import com.aliyun.sls.android.sdk.LOGClient;
import com.aliyun.sls.android.sdk.LogException;
import com.aliyun.sls.android.sdk.SLSDatabaseManager;
import com.aliyun.sls.android.sdk.SLSLog;
import com.aliyun.sls.android.sdk.core.auth.CredentialProvider;
import com.aliyun.sls.android.sdk.core.auth.PlainTextAKSKCredentialProvider;
import com.aliyun.sls.android.sdk.core.auth.StsTokenCredentialProvider;
import com.aliyun.sls.android.sdk.core.callback.CompletedCallback;
import com.aliyun.sls.android.sdk.model.Log;
import com.aliyun.sls.android.sdk.model.LogGroup;
import com.aliyun.sls.android.sdk.request.PostLogRequest;
import com.aliyun.sls.android.sdk.result.PostLogResult;
import com.orhanobut.logger.Logger;
import com.xjiangiot.lib.common.base.BaseApplication;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by facexyz on 2018/11/9
 */
public class AndroidLog {
    private static final String TAG = "AndroidLog";
    private static final String END_POINT = "cn-hangzhou.log.aliyuncs.com";
    private static final String PROJECT_NAME = "xjiang-app-log";

    private LOGClient mLOGClient;
    private static Application sConApplication;
    private static String sLogStoreName;
    private static final AndroidLog ourInstance = new AndroidLog();
    private HashMap<String, LogGroup> mGroupHashMap = new HashMap<>();

    public static AndroidLog getInstance() {
        return ourInstance;
    }


    private AndroidLog() {
    }

    private CredentialProvider getProvider() {
        String AK = "STS.NJikxQ5PAEwPDv6YgPUDaX2jZ";
        String SK = "CowBDjvu2sBGW5BftNWZUChvJFpLdrCgkfKQa3MviyPs";
        return new PlainTextAKSKCredentialProvider(AK, SK);
    }

    private ClientConfiguration getConfig() {
        // 配置信息
        ClientConfiguration conf = new ClientConfiguration();
        // 连接超时，默认15秒
        conf.setConnectionTimeout(15 * 1000);
        // socket超时，默认15秒
        conf.setSocketTimeout(15 * 1000);
        // 最大并发请求书，默认5个
        conf.setMaxConcurrentRequest(5);
        // 失败后最大重试次数，默认2次
        conf.setMaxErrorRetry(2);
        // 设置日志发送失败时，是否支持本地缓存。
        conf.setCachable(true);
        // 设置缓存日志发送的网络策略。
        conf.setConnectType(ClientConfiguration.NetworkPolicy.WIFI_ONLY);
        return conf;
    }

    public void init(Application application, String logStoreName) {
        sConApplication = application;
        sLogStoreName = logStoreName;
        SLSDatabaseManager.getInstance().setupDB(application);
        if (BaseApplication.isDebug) {
            SLSLog.enableLog();
        } else {
            // log打印在控制台
            SLSLog.disableLog();
        }
    }

    public void put(String key, Log log) {
        LogGroup logGroup = mGroupHashMap.get(key);
        if (logGroup == null) {
            logGroup = new LogGroup();
        }
        logGroup.PutLog(log);
        mGroupHashMap.put(key, logGroup);

    }


    /*
     *  推荐使用的方式，直接调用异步接口，通过callback 获取回调信息
     */
    public void uploadLog(String key) {
        LogGroup logGroup = mGroupHashMap.get(key);
        if (logGroup == null) {
            Logger.e("no logGroup found!");
            return;
        }
        try {
            PostLogRequest request = new PostLogRequest(PROJECT_NAME, sLogStoreName, logGroup);
            getClient().asyncPostLog(request, new CompletedCallback<PostLogRequest, PostLogResult>
                    () {
                @Override
                public void onSuccess(PostLogRequest request, PostLogResult result) {
                    Logger.i("upload succeed：" + request.mLogGroup.LogGroupToJsonString());
                }

                @Override
                public void onFailure(PostLogRequest request, LogException exception) {
                    Logger.e("upload failed!");
                }
            });
        } catch (LogException e) {
            e.printStackTrace();
        }
    }

    public void uploadLogAll() {
        for (String key : mGroupHashMap.keySet()) {
            uploadLog(key);
        }
    }


    private LOGClient getClient() {
        if (sConApplication == null) {
            throw new NullPointerException("please call AndroidLog.init() first!");
        }
        // 初始化client
        if (mLOGClient == null) {
            mLOGClient = new LOGClient(sConApplication, END_POINT, getProvider(), getConfig());
        }

        return mLOGClient;

    }


}
