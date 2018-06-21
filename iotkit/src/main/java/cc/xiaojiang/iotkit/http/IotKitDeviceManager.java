package cc.xiaojiang.iotkit.http;

import android.content.Context;
import android.text.TextUtils;

import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import cc.xiaojiang.iotkit.account.IotKitAccountManager;
import cc.xiaojiang.iotkit.bean.Constants;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;


public class IotKitDeviceManager {

    private Context context;

    private static final IotKitDeviceManager ourInstance = new IotKitDeviceManager();

    public static IotKitDeviceManager getInstance() {
        return ourInstance;
    }

    private IotKitDeviceManager() {

    }

    protected static RequestBody createRequestBody(String jsonStr) {
        return RequestBody.create(MediaType.parse(Constants.APPLICATION_STRING), jsonStr);
    }

    public void userSecret(IotKitCallBack iotKitCallBack) {
        if (TextUtils.isEmpty(IotKitAccountManager.getInstance().getXJUserId())) {
            Logger.e("userId is empty!");
            return;
        }
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("userId", IotKitAccountManager.getInstance().getXJUserId());
            PlatformRetrofitHelp.getService()
                    .userSecret(createRequestBody(jsonObject.toString()))
                    .enqueue(new IotKitHttpCallback(iotKitCallBack));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void deviceNick(String deviceId, String nickname, IotKitCallBack callBack) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("userId", IotKitAccountManager.getInstance().getXJUserId());
            jsonObject.put("nickname", nickname);
            jsonObject.put("deviceId", deviceId);
            PlatformRetrofitHelp.getService()
                    .deviceNick(createRequestBody(jsonObject.toString()))
                    .enqueue(new IotKitHttpCallback(callBack));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void deviceUnbind(String deviceId, final IotKitCallBack callBack) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("userId", IotKitAccountManager.getInstance().getXJUserId());
            jsonObject.put("deviceId", deviceId);
            PlatformRetrofitHelp.getService()
                    .deviceUnbind(createRequestBody(jsonObject.toString()))
                    .enqueue(new IotKitHttpCallback(callBack));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void deviceBind(String deviceId, final IotKitCallBack callBack) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("userId", IotKitAccountManager.getInstance().getXJUserId());
            jsonObject.put("deviceId", deviceId);
            PlatformRetrofitHelp.getService()
                    .deviceBind(createRequestBody(jsonObject.toString()))
                    .enqueue(new IotKitHttpCallback(callBack));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void deviceList(final IotKitCallBack callBack) {
        if (!IotKitAccountManager.getInstance().isLogin()) {
            callBack.onError(0, "need login");
            return;
        }
        PlatformRetrofitHelp.getService()
                .deviceList(IotKitAccountManager.getInstance().getXJUserId())
                .enqueue(new IotKitHttpCallback(callBack));
    }

    public void productList(final IotKitCallBack callBack) {
        PlatformRetrofitHelp.getService()
                .productList()
                .enqueue(new IotKitHttpCallback(callBack));
    }

    public void productInfo(String productKey, final IotKitCallBack callBack) {
        PlatformRetrofitHelp.getService()
                .productInfo(productKey)
                .enqueue(new IotKitHttpCallback(callBack));
    }

    public void deviceInfo(String deviceId, final IotKitCallBack callBack) {
        PlatformRetrofitHelp.getService()
                .deviceInfo(deviceId)
                .enqueue(new IotKitHttpCallback(callBack));
    }

    public void deviceShare(String deviceId, final IotKitCallBack callBack) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("userId", IotKitAccountManager.getInstance().getXJUserId());
            jsonObject.put("deviceId", deviceId);
            PlatformRetrofitHelp.getService()
                    .share(createRequestBody(jsonObject.toString()))
                    .enqueue(new IotKitHttpCallback(callBack));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void acceptDeviceShare(String qrcode, final IotKitCallBack callBack) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("userId", IotKitAccountManager.getInstance().getXJUserId());
            jsonObject.put("qrcode", qrcode);
            PlatformRetrofitHelp.getService()
                    .acceptShare(createRequestBody(jsonObject.toString()))
                    .enqueue(new IotKitHttpCallback(callBack));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
