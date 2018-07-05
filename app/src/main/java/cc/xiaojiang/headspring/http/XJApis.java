package cc.xiaojiang.headspring.http;

import java.math.BigDecimal;
import java.util.List;

import cc.xiaojiang.baselibrary.http.model.BaseModel;
import cc.xiaojiang.headspring.model.http.AqiModel;
import cc.xiaojiang.headspring.model.http.LoginBody;
import cc.xiaojiang.headspring.model.http.LoginModel;
import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface XJApis {

    @POST(HttpUrl.LOGIN)
    Observable<BaseModel<LoginModel>> login(@Body LoginBody loginBody);

    @POST(HttpUrl.FEEDBACK)
    Observable<BaseModel> feedback(@Field("view") String content);

//    @GET(HttpUrl.AIR)
//    Observable<BaseModel<List<AqiModel>>> getAqi(@Query("level") float level,
//                                                 @Query("longtitude") double longtitude,
//                                                 @Query("latitude") double latitude,
//                                                 @Query("longtitude2") double longtitude2,
//                                                 @Query("latitude2") double latitude2);
    @GET(HttpUrl.AIR)
    Observable<BaseModel<List<AqiModel>>> getAqi(@Query("level") int level,
                                                 @Query("longtitude") int longtitude,
                                                 @Query("latitude") int latitude,
                                                 @Query("longtitude2") int longtitude2,
                                                 @Query("latitude2") int latitude2);
}
