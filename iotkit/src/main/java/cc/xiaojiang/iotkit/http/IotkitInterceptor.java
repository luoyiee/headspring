package cc.xiaojiang.iotkit.http;

import android.text.TextUtils;

import com.orhanobut.logger.Logger;

import java.io.IOException;

import cc.xiaojiang.iotkit.util.IotDbUtils;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by tong on 2018/1/10.
 */

public class IotkitInterceptor implements Interceptor {
    private static final String API_TOKEN = "api-token";
    public static String token;

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        String token = IotDbUtils.getApiToken();
        if (!request.url().toString().contains(IotKitHttpUrl.DEVELOP_TOKEN)) {
            /**
             * 添加token
             */
            if (!TextUtils.isEmpty(token)) {
                Logger.d("add token: " + token);
                request = request.newBuilder()
                        .header(API_TOKEN, token)
                        .build();
            }
        }

        return chain.proceed(request);
    }
}
