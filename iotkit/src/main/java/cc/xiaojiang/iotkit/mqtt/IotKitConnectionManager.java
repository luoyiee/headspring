package cc.xiaojiang.iotkit.mqtt;

import android.content.Context;
import android.text.TextUtils;

import com.orhanobut.logger.Logger;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import cc.xiaojiang.iotkit.account.IotKitAccountManager;
import cc.xiaojiang.iotkit.bean.Constants;
import cc.xiaojiang.iotkit.http.IotKitCallBack;
import cc.xiaojiang.iotkit.http.IotKitDeviceManager;
import cc.xiaojiang.iotkit.http.IotKitHttpCallback;
import cc.xiaojiang.iotkit.http.IotkitInterceptor;
import cc.xiaojiang.iotkit.http.PlatformRetrofitHelp;
import cc.xiaojiang.iotkit.util.LogUtil;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by zhu on 17-10-23.
 */

public class IotKitConnectionManager implements IMqttActionListener {
    private static final int QOS_AT_MOST_ONCE = 0;
    private static final int QOS_AT_LEAST_ONCE = 1;
    public static final int QOS_EXACTLY_ONCE = 2;
    private static final int QOS_PUBLISH = QOS_AT_LEAST_ONCE;
    private static final int QOS_SUBCRIBE = QOS_AT_MOST_ONCE;
    private static final String TAG = "IotKitConnectionManager";
    private MqttAndroidClient mqttAndroidClient;
    private Context context;
    private Set<String> subscribeSet = new HashSet<>();

    private MqttCallbackListener mMqttCallbackListener;
    private MqttConfig mMqttConfig;


    private static final IotKitConnectionManager ourInstance = new IotKitConnectionManager();

    public static IotKitConnectionManager getInstance() {
        return ourInstance;
    }

    private IotKitConnectionManager() {

    }

    public void init(Context context) {
        this.context = context;
        connectPre(null);

    }

    public void connectPre(final IotKitConnectCallback iotKitConnectCallback) {
        if (!IotKitAccountManager.getInstance().isLogin()) {
            Logger.i("need login");
            return;
        }
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("developerKey", IotKitAccountManager.getInstance().getDevelopKey());
            jsonObject.put("developerSecret", IotKitAccountManager.getInstance().getDevelopSecret
                    ());
            PlatformRetrofitHelp.getService()
                    .developToken(RequestBody.create(MediaType.parse(Constants
                            .APPLICATION_STRING), jsonObject.toString()))
                    .enqueue(new IotKitHttpCallback(new IotKitCallBack() {
                        @Override
                        public void onSuccess(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                JSONObject jsonData = jsonResponse.getJSONObject("data");
                                IotkitInterceptor.token = jsonData.getString("access_token");
                                IotKitDeviceManager.getInstance().userSecret(new IotKitCallBack() {
                                    @Override
                                    public void onSuccess(String response) {
                                        try {
                                            JSONObject jsonResponse = new JSONObject(response);
                                            JSONObject jsonData = jsonResponse.getJSONObject
                                                    ("data");
                                            connect(jsonData.getString("userSecret"),
                                                    iotKitConnectCallback);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    @Override
                                    public void onError(int code, String errorMsg) {

                                    }
                                });
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(int code, String errorMsg) {

                        }
                    }));
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public void connect(String userSecret, IotKitConnectCallback iotKitConnectCallback) {
        String userId = IotKitAccountManager.getInstance().getXJUserId();
        if (TextUtils.isEmpty(userId)) {
            Logger.e("userId is empty");
            return;
        }
        if (TextUtils.isEmpty(userSecret)) {
            Logger.e("userSecret is empty");
            return;
        }
        mMqttConfig = new MqttConfig(userId, userSecret);
        if (mqttAndroidClient != null && mqttAndroidClient.isConnected()) {
            Logger.w("IotKitConnectionManager is already connected");
            return;
        }
        //create connect
        mqttAndroidClient = new MqttAndroidClient(context, mMqttConfig.getMqttServerUrl(),
                mMqttConfig.getMqttClientId());
        mMqttCallbackListener = new MqttCallbackListener();
        mqttAndroidClient.setCallback(mMqttCallbackListener);
        try {
            MqttConnectOptions mqttConnectOptions = getMqttConnectOptions();
            mqttAndroidClient.connect(mqttConnectOptions, null, iotKitConnectCallback);
            LogUtil.i(TAG, "connect " + mMqttConfig.getMqttServerUrl() + "...");
        } catch (MqttException e) {
            e.printStackTrace();
            LogUtil.e(TAG, "connect " + TAG + " error," + e.toString());
        }
    }


    public Set<String> getSubscribeSet() {
        return subscribeSet;
    }

    public void addDataCallback(IotKitReceivedCallback dataCallback) {
        if (dataCallback == null) {
            LogUtil.i(TAG, "remove all DataCallbacks");
            return;
        }
        mMqttCallbackListener.add(dataCallback);
        LogUtil.i(TAG, "setDataCallback");
    }

    public void removeDataCallback(IotKitReceivedCallback dataCallback) {
        if (dataCallback == null) {
            LogUtil.i(TAG, "remove all DataCallbacks");
            return;
        }
        mMqttCallbackListener.remove(dataCallback);
        LogUtil.i(TAG, "remove called");
    }

    public void removeAllDataCallback() {
        mMqttCallbackListener.removeAll();
        LogUtil.i(TAG, "remove all DataCallback called");
    }

    public MqttAndroidClient getMqttAndroidClient() {
        return mqttAndroidClient;
    }

    private MqttConnectOptions getMqttConnectOptions() {
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setConnectionTimeout(5 * 1000);
        mqttConnectOptions.setCleanSession(false);
        mqttConnectOptions.setKeepAliveInterval(20);
        mqttConnectOptions.setUserName(mMqttConfig.getMqttUsername());
        mqttConnectOptions.setPassword(mMqttConfig.getMqttPassword().toCharArray());
        //ssl
        try {
            TrustManager[] trustAllCerts = new TrustManager[1];
            TrustManager trustManager = new TrustManagerImpl();
            trustAllCerts[0] = trustManager;
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, null);
            SocketFactory factory = sslContext.getSocketFactory();
            mqttConnectOptions.setSocketFactory(factory);
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }
        return mqttConnectOptions;
    }


    public void publish(String productKey, String deviceId, HashMap<String, Object> hashMap,
                        IotKitActionCallback callBack) {
        String topic = "/set/" + productKey + "/" + deviceId;
        byte[] payload = publishPayload(hashMap);
        try {
            // TODO: 2018/6/1 屏蔽快速点击
            mqttAndroidClient.publish(topic, payload,
                    QOS_PUBLISH, false, null, callBack);
            LogUtil.i(TAG, "send topic: " + topic);
            LogUtil.i(TAG, ">>>>>>>>>> " + new String(payload));
        } catch (MqttException e) {
            e.printStackTrace();
            LogUtil.e(TAG, "Exception occurred during send data:" + e.getMessage());
        }
    }


    public void subscribe(String productKey, String deviceId, IotKitActionCallback callback) {
        String topic = "/get/" + productKey + "/" + deviceId;
        try {
            mqttAndroidClient.subscribe(topic, QOS_SUBCRIBE, null, callback);
            subscribeSet.add(topic);
            LogUtil.i(TAG, "register topic: " + topic);
        } catch (MqttException e) {
            e.printStackTrace();
            LogUtil.e(TAG, "Exception occurred during receive data:" + e.getMessage());
        }
    }

    public void unSubscribe(String productKey, String deviceId, IotKitActionCallback callback) {
        String topic = "/get/" + productKey + "/" + deviceId;
        try {
            mqttAndroidClient.unsubscribe(topic, null, callback);
            subscribeSet.remove(topic);
        } catch (MqttException e) {
            e.printStackTrace();
            LogUtil.e(TAG, "Exception occurred during unSubscribe:" + e.getMessage());
        }
    }

    public void disconnected() {
        try {
            mqttAndroidClient.disconnect();
            mqttAndroidClient = null;
            subscribeSet.clear();
        } catch (MqttException e) {
            e.printStackTrace();
            LogUtil.e(TAG, "Exception occurred during disconnect:" + e.getMessage());
        }
    }

    public boolean isConnected() {
        return mqttAndroidClient != null && mqttAndroidClient.isConnected();
    }


    @Override
    public void onSuccess(IMqttToken asyncActionToken) {
        LogUtil.i(TAG, "connect " + mMqttConfig.getMqttServerUrl() + " succeed");
    }

    @Override
    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
        LogUtil.e(TAG, "connect " + mMqttConfig.getMqttServerUrl() + " failed");
        exception.printStackTrace();
    }

    private byte[] publishPayload(HashMap<String, Object> hashMap) {
        JSONObject playload = new JSONObject();
        try {
            playload.put("msg_type", "set");
            playload.put("msg_id", System.currentTimeMillis());
            JSONObject paramsObject = new JSONObject();
            JSONArray attrObject = new JSONArray();
            for (String key : hashMap.keySet()) {
                Object value = hashMap.get(key);
                attrObject.put(key);
                JSONObject valueObject = new JSONObject();
                valueObject.put("value", value);
                paramsObject.put(key, valueObject);
            }
            playload.put("params", paramsObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return playload.toString().getBytes();
    }

}
