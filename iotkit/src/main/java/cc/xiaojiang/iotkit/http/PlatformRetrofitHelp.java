package cc.xiaojiang.iotkit.http;


import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;

/**
 * Created by tong on 2018/1/9.
 */

public class PlatformRetrofitHelp {
    private static PlatformRetrofitHelp instance = new PlatformRetrofitHelp();
    private IotKitApis mIotKitApis;

    public static IotKitApis getService() {
        return getInstance().service();
    }

    private static PlatformRetrofitHelp getInstance() {
        return instance;
    }

    private PlatformRetrofitHelp() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (true) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addNetworkInterceptor(loggingInterceptor);
        }
        builder.addInterceptor(new IotkitInterceptor());
//        builder.authenticator(new TokenAuthenticator());
        builder.connectTimeout(20, TimeUnit.SECONDS);
        builder.readTimeout(20, TimeUnit.SECONDS);
        builder.writeTimeout(20, TimeUnit.SECONDS);
        builder.retryOnConnectionFailure(true);
        //enable "TLSv1", "TLSv1.1", "TLSv1.2" on API 16+
        X509TrustManager x509TrustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws
                    CertificateException {

            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws
                    CertificateException {

            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };
        try {
            builder.sslSocketFactory(new TLSSocketFactory(), x509TrustManager);
        } catch (KeyManagementException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(IotKitHttpUrl.HOST)
                .client(builder.build())
                .build();
        mIotKitApis = retrofit.create(IotKitApis.class);
    }


    public IotKitApis service() {
        return mIotKitApis;
    }

    public static void cancel(Call<?> call) {
        if (call != null) {
            call.cancel();
        }
    }
}
