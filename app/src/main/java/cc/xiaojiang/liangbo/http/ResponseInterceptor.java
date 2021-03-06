package cc.xiaojiang.liangbo.http;

import android.content.Intent;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;

import org.eclipse.jetty.http.HttpMethods;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import cc.xiaojiang.iotkit.account.IotKitAccountCallback;
import cc.xiaojiang.iotkit.account.IotKitAccountManager;
import cc.xiaojiang.liangbo.activity.LoginActivity;
import cc.xiaojiang.liangbo.base.MyApplication;
import cc.xiaojiang.liangbo.http.model.BaseModel;
import cc.xiaojiang.liangbo.model.http.LoginModel;
import cc.xiaojiang.liangbo.model.http.RefreshTokenModel;
import cc.xiaojiang.liangbo.utils.AccountUtils;
import cc.xiaojiang.liangbo.utils.DbUtils;
import cc.xiaojiang.liangbo.utils.SignUtils;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

import static cc.xiaojiang.liangbo.utils.constant.Constant.ACCESS_TOKEN;
import static cc.xiaojiang.liangbo.utils.constant.Constant.REFRESH_TOKEN;
import static cc.xiaojiang.liangbo.utils.constant.Constant.SIGN;
import static cc.xiaojiang.liangbo.utils.constant.Constant.TIMESTAMP;


public class ResponseInterceptor implements Interceptor {

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        String url = request.url().toString();
        if (url.contains(HttpUrl.USER_INFO)
                || url.contains(HttpUrl.USER_MODIFY)
                || url.contains(HttpUrl.QINIU_TOKEN)
                || url.contains(HttpUrl.PM25_HISTORY)
                || url.contains(HttpUrl.CITY_ADD)
                || url.contains(HttpUrl.CITY_LIST)
                || url.contains(HttpUrl.CITY_QUERY)
                || url.contains(HttpUrl.CITY_DEL)) {
            String accessToken = DbUtils.getAccessToken();
            request = request.newBuilder()
                    .header(ACCESS_TOKEN, accessToken)
                    .build();
        } else if (url.contains(HttpUrl.REFRESH)) {
            request = request.newBuilder()
                    .header(REFRESH_TOKEN, DbUtils.getRefreshToken())
                    .build();
            return chain.proceed(request);
        } else {
            request = addHeaderSign(request);
        }

        //??????token
        Response response = chain.proceed(request);
        if (response.code() == 401 || response.code() == 403) {
            Logger.d("code: " + response.code());
            logout();
        }
        return response;
    }

    private Request addHeaderSign(Request request) throws IOException {
        //???????????????
        long timestamp = System.currentTimeMillis();
        HashMap<String, String> map = new HashMap<>();
        String method = request.method();
        if (HttpMethods.GET.equals(method)) {
            okhttp3.HttpUrl httpUrl = request.url();
            Set<String> parameterNames = httpUrl.queryParameterNames();
            for (String key : parameterNames) {  //??????????????????
                String parameter = httpUrl.queryParameter(key);
                map.put(key, parameter);
            }
        } else if (HttpMethods.POST.equals(method)) {
            RequestBody requestBody = request.body();
            if (requestBody != null) {
                if (requestBody instanceof FormBody) {
                    int size = ((FormBody) requestBody).size();
                    for (int i = 0; i < size; i++) {
                        String encodedName = ((FormBody) requestBody).encodedName(i);
                        String encodedValue = ((FormBody) requestBody).encodedValue(i);
                        map.put(encodedName, encodedValue);
                    }
                } else {
                    Buffer buffer = new Buffer();
                    requestBody.writeTo(buffer);
                    String params = buffer.readUtf8(); //?????? ????????? json ?????????
                    Gson gson = new GsonBuilder().registerTypeAdapter(Map.class, (JsonDeserializer
                            <Map>) (json, typeOfT, context) -> {
                        JsonObject jsonObject = json.getAsJsonObject();
                        Map p = new HashMap();
                        for (Map.Entry<String, JsonElement> e : jsonObject.entrySet()) {
                            if (e.getValue().isJsonPrimitive()) {
                                p.put(e.getKey(), e.getValue());
                            }
                            p.put(e.getKey(), e.getValue().getAsString());
                        }
                        return p;
                    }).create();

                    map = gson.fromJson(params, new TypeToken<Map<String, Object>>() {
                    }.getType());
                }
            }
        }
        map.put(TIMESTAMP, timestamp + "");
        String sign = SignUtils.sign(map);
        //???????????????
        request = request.newBuilder()
                .header(TIMESTAMP, timestamp + "")
                .header(SIGN, sign)
                .build();
        return request;
    }

    @NonNull
    private Disposable logout() {
        return Observable.just("").
                observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        AccountUtils.logout();
                    }
                });
    }
}
