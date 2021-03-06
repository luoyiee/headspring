package cc.xiaojiang.liangbo;


import cc.xiaojiang.liangbo.http.XJApis;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitHelperMob2 {

    private static RetrofitHelperMob2 instance = new RetrofitHelperMob2();
    private XJApis mXJApis;


    public static XJApis getService() {
        return getInstance().service();
    }

    private static RetrofitHelperMob2 getInstance() {
        return instance;
    }

    private RetrofitHelperMob2() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addNetworkInterceptor(loggingInterceptor);
        }
        builder.retryOnConnectionFailure(true);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://apicloud.mob.com/")
                .client(builder.build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mXJApis = retrofit.create(XJApis.class);
    }


    public XJApis service() {
        return mXJApis;
    }
}
