package com.xjiangiot.lib.common.http;


import okhttp3.Dispatcher;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitHelper {

    private static final int DEFAULT_MAX_REQUESTS = 1;
    private static final int DEFAULT_MAX_REQUESTS_PER_HOST = 1;

    private OkHttpClient.Builder mBuilder = new OkHttpClient.Builder();
    private Dispatcher mDispatcher;

    public RetrofitHelper() {
        mDispatcher = new Dispatcher();
        mDispatcher.setMaxRequests(DEFAULT_MAX_REQUESTS);
        mDispatcher.setMaxRequestsPerHost(DEFAULT_MAX_REQUESTS_PER_HOST);
    }

    public <T> T create(Class<T> service, String host) {
        mBuilder.dispatcher(mDispatcher);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(host)
                .client(mBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(service);
    }


    public void setDispatcher(Dispatcher dispatcher) {
        mDispatcher = dispatcher;
    }

    public void addInterceptor(Interceptor interceptor) {
        mBuilder.addInterceptor(interceptor);
    }

    public void setLogLevel(HttpLoggingInterceptor.Level level) {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(level);
        mBuilder.addNetworkInterceptor(loggingInterceptor);
    }
}
