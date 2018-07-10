package cc.xiaojiang.headspring.http;

import java.math.BigDecimal;
import java.util.List;

import cc.xiaojiang.baselibrary.http.model.BaseModel;
import cc.xiaojiang.headspring.model.http.AqiModel;
import cc.xiaojiang.headspring.model.http.LoginBody;
import cc.xiaojiang.headspring.model.http.LoginModel;
import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface XJApis {

    @POST(HttpUrl.LOGIN)
    Observable<BaseModel<LoginModel>> login(@Body LoginBody loginBody);

    @FormUrlEncoded
    @POST(HttpUrl.FEEDBACK)
    Observable<BaseModel> feedback(@Field("view") String content);

    @FormUrlEncoded
    @POST(HttpUrl.REFRESH)
    Call<BaseModel<LoginModel>> refreshToken();

//    @GET(HttpUrl.AIR)
//    Observable<BaseModel<List<AqiModel>>> getAqi(@Query("level") float level,
//                                                 @Query("longtitude") double longtitude,
//                                                 @Query("latitude") double latitude,
//                                                 @Query("longtitude2") double longtitude2,
//                                                 @Query("latitude2") double latitude2);
    @FormUrlEncoded
    @POST(HttpUrl.AIR)
    Observable<BaseModel<List<AqiModel>>> getAqi(@Field("level") int level,
                                                 @Field("longtitude") BigDecimal longtitude,
                                                 @Field("latitude") BigDecimal latitude,
                                                 @Field("longtitude2") BigDecimal longtitude2,
                                                 @Field("latitude2") BigDecimal latitude2);
}
