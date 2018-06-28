package cc.xiaojiang.headspring.http;

import cc.xiaojiang.baselibrary.http.model.BaseModel;
import cc.xiaojiang.headspring.model.http.LoginBody;
import cc.xiaojiang.headspring.model.http.LoginModel;
import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.POST;

public interface XJApis {

    @POST(HttpUrl.LOGIN)
    Observable<BaseModel<LoginModel>> login(@Body LoginBody loginBody);

    @POST(HttpUrl.FEEDBACK)
    Observable<BaseModel> feedback(@Field("view") String content);
}
