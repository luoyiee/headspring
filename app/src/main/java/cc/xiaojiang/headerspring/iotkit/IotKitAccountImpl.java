package cc.xiaojiang.headerspring.iotkit;

import android.content.Context;

import com.orhanobut.logger.Logger;

import cc.xiaojiang.baselibrary.http.progress.ProgressObserver;
import cc.xiaojiang.baselibrary.util.RxUtils;
import cc.xiaojiang.headerspring.http.HttpResultFunc;
import cc.xiaojiang.headerspring.http.RetrofitHelper;
import cc.xiaojiang.headerspring.model.http.LoginBody;
import cc.xiaojiang.headerspring.model.http.LoginModel;
import cc.xiaojiang.headerspring.utils.AccountUtils;
import cc.xiaojiang.headerspring.utils.DbUtils;
import cc.xiaojiang.iotkit.account.IotKitAccountCallback;
import cc.xiaojiang.iotkit.account.IotKitAccountConfig;

public class IotKitAccountImpl implements IotKitAccountConfig {
    public static final String APP_Source = "zd0c383";
    public static final String DEVELOP_KEY = "6cda7fee67ebbc8e545dd6e7da150c98";
    public static final String DEVELOP_SECRET = "3465690c0ba5d4597632e121bc61764f";


    @Override
    public boolean isLogin() {
        boolean isLogin = AccountUtils.isLogin();
        Logger.d("get isLogin: " + isLogin);
        return isLogin;
    }

    @Override
    public String getAppSource() {
        return APP_Source;
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

    }

    @Override
    public String getDevelopKey() {
        return DEVELOP_KEY;
    }

    @Override
    public String getDevelopSecret() {
        return DEVELOP_SECRET;
    }

    @Override
    public String getXJUserId() {
        String xjUserId = DbUtils.getXJUserId();
        Logger.d("get xj user id: " + xjUserId);
        return xjUserId;
    }


}
