package cc.xiaojiang.headspring.http;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.io.IOException;

import cc.xiaojiang.headspring.http.model.BaseModel;
import cc.xiaojiang.headspring.model.http.LoginModel;
import cc.xiaojiang.headspring.utils.AccountUtils;
import cc.xiaojiang.headspring.utils.DbUtils;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by tong on 2018/1/10.
 */

public class ResponseInterceptor implements Interceptor {
    private static final String ACCESS_TOKEN = "accessToken";

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        String token = DbUtils.getAccessToken();
//        request = request.newBuilder()
//                .addHeader("Connection", "close")
//                .build();
        /**
         * 添加token
         */

        if (request.url().toString().contains(HttpUrl.LOGIN)) {
            //do nothing
        } else if (request.url().toString().contains(HttpUrl.REFRESH)) {
            request.newBuilder()
                    .addHeader("refreshToken", DbUtils.getRefreshToken())
                    .build();
            com.orhanobut.logger.Logger.d("add header: refreshToken=" + DbUtils.getRefreshToken());
        } else {
            if (!TextUtils.isEmpty(token)) {
                request = request.newBuilder()
                        .header(ACCESS_TOKEN, token)
                        .build();
                com.orhanobut.logger.Logger.d("add header: accessToken= " + token);
            } else {
                com.orhanobut.logger.Logger.e("token is null");
            }
        }

//        //判断刷新token连接，直接返回，不走下面代码，避免死循环。
//        if (request.url().toString().contains(HttpUrl.REFRESH) && "POST".equals(request.method
// ())) {
//            request.newBuilder()
//                    .addHeader("refreshToken", DbUtils.getRefreshToken())
//                    .build();
//            return chain.proceed(request);
//        }

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
//                        .removeHeader(ACCESS_TOKEN)   //移除旧的token
                        .header(ACCESS_TOKEN, loginModel.getAccessToken())  //添加新的token
                        .build();
                return chain.proceed(newRequest);//重新发起请求，此时是新的token
            } else {
                //                AppStatusUtils.logout();
                AccountUtils.logout();
                return null;
            }
        }
        return response;

    }
}
