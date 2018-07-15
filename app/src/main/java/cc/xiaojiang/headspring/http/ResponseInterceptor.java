package cc.xiaojiang.headspring.http;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.io.IOException;

import cc.xiaojiang.headspring.http.model.BaseModel;
import cc.xiaojiang.headspring.model.http.LoginModel;
import cc.xiaojiang.headspring.utils.AccountUtils;
import cc.xiaojiang.headspring.utils.DbUtils;
import cc.xiaojiang.iotkit.account.IotKitAccountCallback;
import cc.xiaojiang.iotkit.account.IotKitAccountManager;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import static cc.xiaojiang.headspring.Constant.ACCESS_TOKEN;
import static cc.xiaojiang.headspring.Constant.REFRESH_TOKEN;


public class ResponseInterceptor implements Interceptor {


    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        String accessToken = DbUtils.getAccessToken();
        //添加token
        if (request.url().toString().contains(HttpUrl.LOGIN)) {
            //do nothing
        } else if (request.url().toString().contains(HttpUrl.REFRESH)) {
            request = request.newBuilder()
                    .header(REFRESH_TOKEN, DbUtils.getRefreshToken())
                    .build();
            com.orhanobut.logger.Logger.d("add header: refreshToken=" + DbUtils.getRefreshToken());
        } else {
            if (!TextUtils.isEmpty(accessToken)) {
                request = request.newBuilder()
                        .header(ACCESS_TOKEN, accessToken)
                        .build();
                com.orhanobut.logger.Logger.d("add header: accessToken= " + accessToken);
            } else {
                com.orhanobut.logger.Logger.e("accessToken is null");
            }
        }
        //刷新token
        Response response = chain.proceed(request);
        if (response.code() == 401) {
            com.orhanobut.logger.Logger.d("code: " + response.code());
            final retrofit2.Response<BaseModel<LoginModel>> execute =
                    RetrofitHelper.getService().refreshToken().execute();
            if (execute.code() == 200 && execute.body() != null) {
                //重新发起请求
                final BaseModel<LoginModel> body = execute.body();
                LoginModel loginModel = body.getData();
                DbUtils.setXJUserId(loginModel.getUserId());
                DbUtils.setAccessToken(loginModel.getAccessToken());
                DbUtils.setRefreshToken(loginModel.getRefreshToken());
                Request newRequest = request.newBuilder()
                        .header(ACCESS_TOKEN, loginModel.getAccessToken())  //添加新的token
                        .build();
                return chain.proceed(newRequest);//重新发起请求，此时是新的token
            } else {
                //                AppStatusUtils.logout();
                IotKitAccountManager.getInstance().logout(null);
            }
        }
        return response;

    }
}
