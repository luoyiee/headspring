package cc.xiaojiang.iotkit.http;

import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IotKitHttpCallback implements Callback<ResponseBody> {
    private static final String TAG = "IotKitHttpCallback";
    private IotKitCallBack iotKitCallBack;

    public IotKitHttpCallback(IotKitCallBack callBack) {
        this.iotKitCallBack = callBack;
    }


    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
        if (iotKitCallBack == null) {
            Logger.e("please set IotKitCallBack");
            return;
        }
        int httpCode = response.code();
        switch (httpCode) {
            case 200:
                if (response.body() != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        int code = jsonObject.getInt("code");
                        String msg = jsonObject.getString("msg");
                        if (code == 1000) {
                            iotKitCallBack.onSuccess(jsonObject.toString());
                        } else {
                            iotKitCallBack.onError(code, "服务器错误");
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Logger.e("response.body() is null");
                }
            case 401:
                break;
            default:
                iotKitCallBack.onError(-1, "服务器错误");
        }
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        if (iotKitCallBack == null) {
            Logger.e("please set IotKitCallBack");
            return;
        }
        iotKitCallBack.onError(-2, t.getMessage());
    }
}
