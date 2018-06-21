package cc.xiaojiang.headerspring.iotkit;

import android.content.Intent;
import android.text.TextUtils;

import com.orhanobut.logger.Logger;

import cc.xiaojiang.baselibrary.http.progress.ProgressObserver;
import cc.xiaojiang.baselibrary.util.RxUtils;
import cc.xiaojiang.headerspring.feature.MainActivity;
import cc.xiaojiang.headerspring.http.HttpResultFunc;
import cc.xiaojiang.headerspring.http.RetrofitHelper;
import cc.xiaojiang.headerspring.model.http.LoginBody;
import cc.xiaojiang.headerspring.model.http.LoginModel;
import cc.xiaojiang.headerspring.utils.AccountUtils;
import cc.xiaojiang.headerspring.utils.DbUtils;
import cc.xiaojiang.iotkit.account.IotKitAccountCallback;
import cc.xiaojiang.iotkit.account.IotKitAccountConfig;
import cc.xiaojiang.iotkit.account.IotKitLoginParams;

public class IotKitAccountImpl implements IotKitAccountConfig {
    public static final String APP_Source = "a0cfec0";
    public static final String DEVELOP_KEY = "383d825b7b1e087f62ffc4184e09395f";
    public static final String DEVELOP_SECRET = "3618d6a768d9365afe4329ae364cb02e";


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
    public void login(IotKitLoginParams params, IotKitAccountCallback callback) {
        DbUtils.setXJUserId("a8bfc1eae87a431c9ecfe72d70c7bdd6");
        DbUtils.setAccessToken("test");
        callback.onSuccess();
//        LoginBody loginBody = (LoginBody) params.getArg1();
//        RetrofitHelper.getService().login(loginBody)
//                .map(new HttpResultFunc<>())
//                .compose(RxUtils.rxSchedulerHelper())
//                .subscribe(new ProgressObserver<LoginModel>(params.getContext()) {
//                    @Override
//                    public void onSuccess(LoginModel loginModel) {
//                        DbUtils.setXJUserId("a8bfc1eae87a431c9ecfe72d70c7bdd6");
//                        DbUtils.setAccessToken(loginModel.getAccessToken());
//                        DbUtils.setRefreshToken(loginModel.getRefreshToken());
//                        callback.onSuccess();
//                    }
//                });
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
