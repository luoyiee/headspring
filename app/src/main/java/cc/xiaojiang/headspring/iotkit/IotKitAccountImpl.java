package cc.xiaojiang.headspring.iotkit;

import android.content.Context;
import android.content.Intent;

import com.orhanobut.logger.Logger;

import org.eclipse.jetty.servlet.listener.ELContextCleaner;

import cc.xiaojiang.headspring.BuildConfig;
import cc.xiaojiang.headspring.activity.LoginActivity;
import cc.xiaojiang.headspring.base.MyApplication;
import cc.xiaojiang.headspring.http.HttpResultFunc;
import cc.xiaojiang.headspring.http.RetrofitHelper;
import cc.xiaojiang.headspring.http.progress.ProgressObserver;
import cc.xiaojiang.headspring.model.http.LoginBody;
import cc.xiaojiang.headspring.model.http.LoginModel;
import cc.xiaojiang.headspring.utils.AccountUtils;
import cc.xiaojiang.headspring.utils.DbUtils;
import cc.xiaojiang.headspring.utils.RxUtils;
import cc.xiaojiang.iotkit.account.IotKitAccountCallback;
import cc.xiaojiang.iotkit.account.IotKitAccountConfig;
import cc.xiaojiang.iotkit.mqtt.IotKitConnectionManager;

public class IotKitAccountImpl implements IotKitAccountConfig {
    public static final String TEST_APP_SOURCE = "zd0c383";
    public static final String TEST_DEVELOP_KEY = "6cda7fee67ebbc8e545dd6e7da150c98";
    public static final String TEST_DEVELOP_SECRET = "3465690c0ba5d4597632e121bc61764f";
    public static final String APP_SOURCE = "i8d53";
    public static final String DEVELOP_KEY = "2b23ae32e90589b554611f19b3de923d";
    public static final String DEVELOP_SECRET = "20f3eceb81a7ecb559dd093bf0480c13";

    public boolean isDebug = true;


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
    public void login(Context context, Object params, IotKitAccountCallback callback) {
        LoginBody loginBody = (LoginBody) params;
        RetrofitHelper.getService().login(loginBody)
                .map(new HttpResultFunc<>())
                .compose(RxUtils.rxSchedulerHelper())
                .subscribe(new ProgressObserver<LoginModel>(context) {
                    @Override
                    public void onSuccess(LoginModel loginModel) {
                        DbUtils.setXJUserId(loginModel.getUserId());
                        DbUtils.setAccessToken(loginModel.getAccessToken());
                        DbUtils.setRefreshToken(loginModel.getRefreshToken());
                        callback.onSuccess();
                    }
                });
    }

    @Override
    public void logout(IotKitAccountCallback iotKitAccountCallback) {
        DbUtils.clear();
        Intent intent = new Intent(MyApplication.getInstance(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        MyApplication.getInstance().getApplicationContext().startActivity(intent);
        Logger.d("重新登陆");
        iotKitAccountCallback.onSuccess();
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
