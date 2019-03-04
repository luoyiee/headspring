package com.xjiangiot.lib.common.log;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by facexxyz on 2019/1/23
 */
public class TokenRequest {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static void getToken() {
        OkHttpClient client = new OkHttpClient();
    }

    public String post(String url, String json) throws IOException {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }
}
