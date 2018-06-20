package cc.xiaojiang.iotkit.http;

import android.support.annotation.Nullable;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import cc.xiaojiang.iotkit.IotKit;
import cc.xiaojiang.iotkit.bean.DevelopTokenBean;
import cc.xiaojiang.iotkit.util.IotDbUtils;
import okhttp3.Authenticator;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.Route;
import retrofit2.Call;

/**
 * Created by tong on 2018/1/10.
 */

//public class TokenAuthenticator implements Authenticator {
//
//    @Nullable
//    @Override
//    public Request authenticate(Route route, Response response) throws IOException {
////        // 重新获取token，此处要用到同步的retrofit请求
////        JSONObject jsonObject = new JSONObject();
////        try {
////            jsonObject.put("developerKey", IotKit.iotKitConfig.getDevelopKey());
////            jsonObject.put("developerSecret", IotKit.iotKitConfig.getDevelopSecret());
////        } catch (JSONException e) {
////            e.printStackTrace();
////        }
////        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),
////                jsonObject.toString());
////
////        Call<ResponseBody> call = PlatformRetrofitHelp.getService().developToken(requestBody);
////
////        //要用retrofit的同步方式
////        ResponseBody newToken = call.execute().body();
////        DevelopTokenBean developTokenBean = new Gson().fromJson(newToken.string(),
////                DevelopTokenBean.class);
////        IotDbUtils.setApiToken(developTokenBean.getData().getAccess_token());
////        return response.request().newBuilder()
////                .header("token", developTokenBean.getData().getAccess_token())
////                .build();
//    }
//}
