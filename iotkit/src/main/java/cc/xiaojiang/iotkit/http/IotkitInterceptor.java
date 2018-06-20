package cc.xiaojiang.iotkit.http;

import android.text.TextUtils;

import com.orhanobut.logger.Logger;

import java.io.IOException;

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
        String token = IotkitInterceptor.token;
        Logger.e("token: " + token);
        /**
         * 添加token
         */
        if (!TextUtils.isEmpty(token)) {
            request = request.newBuilder()
                    .header(API_TOKEN, token)
                    .build();
        }
        return chain.proceed(request);
    }
}
