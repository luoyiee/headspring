package cc.xiaojiang.iotkit.http;

import com.orhanobut.logger.Logger;

import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.json.JSONException;
import org.json.JSONObject;

import cc.xiaojiang.iotkit.IotKit;
import cc.xiaojiang.iotkit.account.IotKitAccountManager;
import cc.xiaojiang.iotkit.bean.Constants;
import cc.xiaojiang.iotkit.mqtt.IotKitActionCallback;
import cc.xiaojiang.iotkit.mqtt.IotKitConnectionManager;
import okhttp3.MediaType;
import okhttp3.RequestBody;


public class IotKitDeviceManager {


    private static final IotKitDeviceManager ourInstance = new IotKitDeviceManager();

    public static IotKitDeviceManager getInstance() {
        return ourInstance;
    }

    private IotKitDeviceManager() {

    }

    private static RequestBody createRequestBody(String jsonStr) {
        return RequestBody.create(MediaType.parse(Constants.APPLICATION_STRING), jsonStr);
    }

    public void userSecret(IotKitCallBack iotKitCallBack) {
        if (isLogin(iotKitCallBack)) {
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
    }


    public void deviceNick(String deviceId, String nickname, IotKitCallBack callBack) {
        if (isLogin(callBack)) {
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
    }

    public void deviceUnbind(String productKey, String deviceId, final IotKitCallBack callBack) {
        if (isLogin(callBack)) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", IotKitAccountManager.getInstance().getXJUserId());
                jsonObject.put("deviceId", deviceId);
                PlatformRetrofitHelp.getService()
                        .deviceUnbind(createRequestBody(jsonObject.toString()))
                        .enqueue(new IotKitHttpCallback(new IotKitCallBack() {
                            @Override
                            public void onSuccess(String response) {
                                callBack.onSuccess(response);
                                //取消订阅
                                IotKitConnectionManager.getInstance().unSubscribe(productKey,
                                        deviceId, new IotKitActionCallback() {

                                    @Override
                                    public void onSuccess(IMqttToken asyncActionToken) {

                                    }

                                    @Override
                                    public void onFailure(IMqttToken asyncActionToken, Throwable
                                            exception) {

                                    }
                                });

                            }

                            @Override
                            public void onError(int code, String errorMsg) {
                                callBack.onError(code, errorMsg);

                            }
                        }));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void deviceBind(String productKey, String deviceId, final IotKitCallBack callBack) {
        if (isLogin(callBack)) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", IotKitAccountManager.getInstance().getXJUserId());
                jsonObject.put("deviceId", deviceId);
                PlatformRetrofitHelp.getService()
                        .deviceBind(createRequestBody(jsonObject.toString()))
                        .enqueue(new IotKitHttpCallback(new IotKitCallBack() {
                            @Override
                            public void onSuccess(String response) {
                                callBack.onSuccess(response);
                                //订阅设备
                                IotKitConnectionManager.getInstance().subscribe(productKey,
                                        deviceId, new IotKitActionCallback() {
                                            @Override
                                            public void onSuccess(IMqttToken asyncActionToken) {
                                                Logger.d("");
                                            }

                                            @Override
                                            public void onFailure(IMqttToken asyncActionToken,
                                                                  Throwable exception) {
                                            }
                                        });
                            }

                            @Override
                            public void onError(int code, String errorMsg) {
                                callBack.onError(code, errorMsg);
                            }
                        }));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void deviceList(final IotKitCallBack callBack) {
        if (isLogin(callBack)) {
            if (isLogin(callBack)) {
                PlatformRetrofitHelp.getService()
                        .deviceList(IotKitAccountManager.getInstance().getXJUserId())
                        .enqueue(new IotKitHttpCallback(new IotKitCallBack() {
                            @Override
                            public void onSuccess(String response) {
                                callBack.onSuccess(response);
                                // TODO: 2018/6/22 订阅


                            }

                            @Override
                            public void onError(int code, String errorMsg) {
                                callBack.onError(code, errorMsg);

                            }
                        }));
            }
        }
    }


    public void productList(final IotKitCallBack callBack) {
        if (isLogin(callBack)) {
            PlatformRetrofitHelp.getService()
                    .productList()
                    .enqueue(new IotKitHttpCallback(callBack));
        }
    }

    public void productInfo(String productKey, final IotKitCallBack callBack) {
        if (isLogin(callBack)) {
            PlatformRetrofitHelp.getService()
                    .productInfo(productKey)
                    .enqueue(new IotKitHttpCallback(callBack));
        }
    }

    public void deviceInfo(String deviceId, final IotKitCallBack callBack) {
        if (isLogin(callBack)) {
            PlatformRetrofitHelp.getService()
                    .deviceInfo(deviceId)
                    .enqueue(new IotKitHttpCallback(callBack));
        }
    }

    public void deviceShare(String productKey,String deviceId, final IotKitCallBack callBack) {
        if (isLogin(callBack)) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", IotKitAccountManager.getInstance().getXJUserId());
                jsonObject.put("productKey", productKey);
                jsonObject.put("deviceId", deviceId);
                PlatformRetrofitHelp.getService()
                        .share(createRequestBody(jsonObject.toString()))
                        .enqueue(new IotKitHttpCallback(callBack));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void acceptDeviceShare(String qrcode, final IotKitCallBack callBack) {
        if (isLogin(callBack)) {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("userId", IotKitAccountManager.getInstance().getXJUserId());
                jsonObject.put("qrcode", qrcode);
                PlatformRetrofitHelp.getService()
                        .acceptShare(createRequestBody(jsonObject.toString()))
                        .enqueue(new IotKitHttpCallback(new IotKitCallBack() {
                            @Override
                            public void onSuccess(String response) {
                                callBack.onSuccess(response);
                                // TODO: 2018/6/22 订阅

                            }

                            @Override
                            public void onError(int code, String errorMsg) {
                                callBack.onError(code, errorMsg);
                            }
                        }));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isLogin(IotKitCallBack callBack) {
        callBack.onError(-1, "未登录");
        return IotKitAccountManager.getInstance().isLogin();
    }


}
