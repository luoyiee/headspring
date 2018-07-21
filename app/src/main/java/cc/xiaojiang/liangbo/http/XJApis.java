package cc.xiaojiang.liangbo.http;

import android.util.ArrayMap;

import java.math.BigDecimal;
import java.util.List;

import cc.xiaojiang.liangbo.http.model.BaseModel;
import cc.xiaojiang.liangbo.model.http.AirRankModel;
import cc.xiaojiang.liangbo.model.http.AqiModel;
import cc.xiaojiang.liangbo.model.http.DynamicModel;
import cc.xiaojiang.liangbo.model.http.HomeWeatherAirModel;
import cc.xiaojiang.liangbo.model.http.LoginBody;
import cc.xiaojiang.liangbo.model.http.LoginModel;
import cc.xiaojiang.liangbo.model.http.LunarInfoModel;
import cc.xiaojiang.liangbo.model.http.Pm25HistoryModel;
import cc.xiaojiang.liangbo.model.http.UserInfoModel;
import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface XJApis {

    @POST(HttpUrl.LOGIN)
    Observable<BaseModel<LoginModel>> login(@Body LoginBody loginBody);

    @FormUrlEncoded
    @POST(HttpUrl.FEEDBACK)
    Observable<BaseModel<Object>> feedback(@Field("view") String content);

    @POST(HttpUrl.REFRESH)
    Call<BaseModel<LoginModel>> refreshToken();

    @GET(HttpUrl.AIR)
    Observable<BaseModel<List<AqiModel>>> getAqi(@Query("level") int level,
                                                 @Query("longtitude") BigDecimal longtitude,
                                                 @Query("latitude") BigDecimal latitude,
                                                 @Query("longtitude2") BigDecimal longtitude2,
                                                 @Query("latitude2") BigDecimal latitude2);

    @GET(HttpUrl.WEATHER_AIR_HOUR)
    Observable<BaseModel<HomeWeatherAirModel>> queryCityWeatherAir(@Query("city") String city);

    @GET(HttpUrl.DYNAMIC_LIST)
    Observable<BaseModel<List<DynamicModel>>> dynamicList();

    @GET(HttpUrl.AIR_RANK)
    Observable<BaseModel<List<AirRankModel>>> airRankList(@Query("city") String city,
                                                          @Query("type") String type);

    @GET(HttpUrl.LUNAR_INFO)
    Observable<BaseModel<LunarInfoModel>> lunarInfo(@Query("day") String day);

    @GET(HttpUrl.USER_INFO)
    Observable<BaseModel<UserInfoModel>> userInfo();

    @FormUrlEncoded
    @POST(HttpUrl.USER_MODIFY)
    Observable<BaseModel<Object>> userModify(@FieldMap ArrayMap<String, Object> map);

    @GET(HttpUrl.QINIU_TOKEN)
    Observable<BaseModel<String>> qiniuToken();

    @GET(HttpUrl.PM25_HISTORY)
    Observable<BaseModel<Pm25HistoryModel>> pm25History(@Query("deviceId") String deviceId,
                                                        @Query("city") String city,
                                                        @Query("type") String type,
                                                        @Query("day") int day);

    @FormUrlEncoded
    @POST(HttpUrl.DYNAMIC_LIKE)
    Observable<BaseModel<String>> dynamicLike(@Field("id")int id);

}
