package cc.xiaojiang.liangbo.http;


import cc.xiaojiang.liangbo.BuildConfig;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitHelper {

    private static RetrofitHelper instance = new RetrofitHelper();
    private XJApis mXJApis;


    public static XJApis getService() {
        return getInstance().service();
    }

    private static RetrofitHelper getInstance() {
        return instance;
    }

    private RetrofitHelper() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addNetworkInterceptor(loggingInterceptor);
        }
        builder.addInterceptor(new ResponseInterceptor());
//        builder.connectTimeout(20, TimeUnit.SECONDS);
//        builder.readTimeout(20, TimeUnit.SECONDS);
//        builder.writeTimeout(20, TimeUnit.SECONDS);
        builder.retryOnConnectionFailure(true);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(HttpUrl.HOST)
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
