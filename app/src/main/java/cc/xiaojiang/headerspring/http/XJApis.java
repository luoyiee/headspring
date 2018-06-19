package cc.xiaojiang.headerspring.http;

import cc.xiaojiang.baselibrary.http.model.BaseModel;
import cc.xiaojiang.headerspring.model.bean.LoginModel;
import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface XJApis {

    /**
     * 手机号登录
      * @param telphone 手机号
     * @param verifyCode 验证码
     * @return LoginModel
     */
    @FormUrlEncoded
    @POST(HttpUrl.LOGIN)
    Observable<BaseModel<LoginModel>> login(@Field("telphone")long telphone, @Field("verifyCode")int verifyCode);
}
