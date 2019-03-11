package com.xjiangiot.lib.common.http.simple;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by facexxyz on 2019/1/23
 */
class SimpleHttp {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private OkHttpClient mOkHttpClient;

    private static final SimpleHttp ourInstance = new SimpleHttp();

    static SimpleHttp getInstance() {
        return ourInstance;
    }


    private SimpleHttp() {
        mOkHttpClient = new OkHttpClient();
    }

    public String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (Response response = mOkHttpClient.newCall(request).execute()) {
            return response.body().string();
        }
    }

    public String run(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = mOkHttpClient.newCall(request).execute()) {
            return response.body().string();
        }
    }
}
