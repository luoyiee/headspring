package cc.xiaojiang.headspring.http;

import java.math.BigDecimal;
import java.util.List;

import cc.xiaojiang.headspring.http.model.BaseModel;
import cc.xiaojiang.headspring.model.http.AqiModel;
import cc.xiaojiang.headspring.model.http.LoginBody;
import cc.xiaojiang.headspring.model.http.LoginModel;
import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
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
}
