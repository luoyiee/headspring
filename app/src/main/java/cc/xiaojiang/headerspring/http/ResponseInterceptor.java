package cc.xiaojiang.headerspring.http;

import android.text.TextUtils;


import java.io.IOException;

import cc.xiaojiang.headerspring.utils.DbUtils;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by tong on 2018/1/10.
 */

public class ResponseInterceptor implements Interceptor {
    private static final String Authorization = "Authorization";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        String token = DbUtils.getAccessToken();
        /**
         * 添加token
         */
        if (!TextUtils.isEmpty(token)) {
            request = request.newBuilder()
                    .header("Authorization", "Bearer "+token)
                    .build();
        }
        /**
         * 刷新token
         */
        Response response = chain.proceed(request);
        if (response.code() == 401) {
            // TODO: 18-4-18 处理多并发问题
//            String newToken = DbUtils.getAccessToken();
//            Request refreshRequest = new Request.Builder().url(HttpManager.HOST + HttpUrl
//                    .REFRESH)
//                    .removeHeader(Authorization)
//                    .header(Authorization, newToken)
//                    .get()
//                    .build();
//            Response refreshResponse = chain.proceed(refreshRequest);
//            if (refreshResponse != null && refreshResponse.isSuccessful()) {
//                String auth = refreshResponse.header("Authorization");
//                DbUtils.setAccessToken(auth);
//                if (auth != null) {
//                    Request newRequest = request.newBuilder()
//                            .removeHeader(Authorization)
//                            .addHeader(Authorization, auth)
//                            .build();
//                    response = chain.proceed(newRequest);
//                }
//            } else {
//                AppStatusUtils.logout();
////                IotKit.logout();
//
//            }

        }
        return response;
    }
}
