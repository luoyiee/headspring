package cc.xiaojiang.iotkit.http;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface IotKitApis {

    @POST(IotKitHttpUrl.DEVELOP_TOKEN)
    Call<ResponseBody> developToken(@Body RequestBody requestBody);

    @POST(IotKitHttpUrl.USER_SECRET)
    Call<ResponseBody> userSecret(@Body RequestBody requestBody);

    @POST(IotKitHttpUrl.DEVICE_NICK)
    Call<ResponseBody> deviceNick(@Body RequestBody requestBody);

    @POST(IotKitHttpUrl.DEVICE_UNBIND)
    Call<ResponseBody> deviceUnbind(@Body RequestBody requestBody);

    @POST(IotKitHttpUrl.DEVICE_BIND)
    Call<ResponseBody> deviceBind(@Body RequestBody requestBody);

    @GET(IotKitHttpUrl.DEVICE_LIST)
    Call<ResponseBody> deviceList(@Query("userId") String UserId);


    @GET(IotKitHttpUrl.PRODUCT_LIST)
    Call<ResponseBody> productList();

    @GET(IotKitHttpUrl.PRODUCT_INFO)
    Call<ResponseBody> productInfo(@Query("productKey") String productKey);

    @GET(IotKitHttpUrl.DEVICE_INFO)
    Call<ResponseBody> deviceInfo(@Query("deviceId") String deviceId);

    @POST(IotKitHttpUrl.USER_REGISTER)
    Call<ResponseBody> register(@Body RequestBody requestBody);

    @POST(IotKitHttpUrl.SHARE_QRCODE)
    Call<ResponseBody> share(@Body RequestBody requestBody);

    @POST(IotKitHttpUrl.SHARE)
    Call<ResponseBody> acceptShare(@Body RequestBody requestBody);
}
