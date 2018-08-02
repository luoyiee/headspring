package cc.xiaojiang.liangbo.iotkit;

import android.content.Context;

import com.orhanobut.logger.Logger;

import cc.xiaojiang.iotkit.account.IotKitAccountConfig2;
import cc.xiaojiang.liangbo.base.MyApplication;
import cc.xiaojiang.liangbo.utils.AccountUtils;
import cc.xiaojiang.liangbo.utils.DbUtils;

public class IotKitAccountImpl implements IotKitAccountConfig2 {
    public static final String TEST_APP_SOURCE = "zd0c383";
    public static final String TEST_DEVELOP_KEY = "6cda7fee67ebbc8e545dd6e7da150c98";
    public static final String TEST_DEVELOP_SECRET = "3465690c0ba5d4597632e121bc61764f";
    public static final String APP_SOURCE = "s42254d5";
    public static final String DEVELOP_KEY = "296ad5b67f91480e3ae9e64ff68626d5";
    public static final String DEVELOP_SECRET = "8dba5889f5a231492dec5824413b224b";

    public boolean isDebug = true;

    @Override
    public Context getApplicationContext() {
        return MyApplication.getInstance();    }

    @Override
    public boolean isLogin() {
        boolean isLogin = AccountUtils.isLogin();
        Logger.d("get isLogin: " + isLogin);
        return isLogin;
    }

    @Override
    public String getAppSource() {
        if (isDebug) {
            return TEST_APP_SOURCE;
        } else {
            return APP_SOURCE;
        }
    }

    @Override
    public String getDevelopKey() {
        if (isDebug) {
            return TEST_DEVELOP_KEY;
        } else {
            return DEVELOP_KEY;
        }
    }

    @Override
    public String getDevelopSecret() {
        if (isDebug) {
            return TEST_DEVELOP_SECRET;
        } else {
            return DEVELOP_SECRET;
        }
    }

    @Override
    public String getXJUserId() {
        String xjUserId = DbUtils.getXJUserId();
        Logger.d("get xj user id: " + xjUserId);
        return xjUserId;
    }


}
