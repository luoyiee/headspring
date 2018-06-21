package cc.xiaojiang.headerspring.http;

import cc.xiaojiang.baselibrary.http.model.BaseModel;
import cc.xiaojiang.headerspring.model.http.LoginBody;
import cc.xiaojiang.headerspring.model.http.LoginModel;
import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface XJApis {

    @POST(HttpUrl.LOGIN)
    Observable<BaseModel<LoginModel>> login(@Body LoginBody loginBody);

    @POST(HttpUrl.FEEDBACK)
    Observable<BaseModel> feedback(@Field("view") String content);
}
