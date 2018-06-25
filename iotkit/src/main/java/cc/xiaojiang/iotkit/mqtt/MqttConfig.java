package cc.xiaojiang.iotkit.mqtt;

import com.orhanobut.logger.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cc.xiaojiang.iotkit.account.IotKitAccountManager;
import cc.xiaojiang.iotkit.util.SignUtil;

public class MqttConfig implements IMqttConfig {
    private static final String TEST_MQTT_SERVER_URL = "ssl://m.yun.xjiangiot.com:8883";
    private static final int SECUREMODE_TLS = 2;

    private long timeStamp;
    private String userId;
    private String userSecret;
    private String developKey;

    public MqttConfig(String userId, String userSecret) {
        timeStamp = System.currentTimeMillis();
        this.userId = userId;
        this.userSecret = userSecret;
        this.developKey = IotKitAccountManager.getInstance().getDevelopKey();

    }

    @Override
    public String getMqttServerUrl() {
        return TEST_MQTT_SERVER_URL;
    }

    @Override
    public String getMqttClientId() {
        String clientId = "a-" + userId + "-securemode=" + SECUREMODE_TLS + "," +
                "signmethod=hmacsha1," +
                "timestamp=" + timeStamp;
        Logger.d("mqtt clientId: " + clientId);
        return clientId;
    }

    @Override
    public String getMqttUsername() {
        String username = userId + "&" + developKey;
        Logger.d("mqtt username: " + username);
        return username;
    }

    @Override
    public String getMqttPassword() {
        String hashStr = "clientId" + userId + "deviceId" + userId + "productKey" +
                developKey + "timestamp" + System.currentTimeMillis();
        String mqttPassword = SignUtil.hamcsha1(userSecret.getBytes(), hashStr.getBytes());
        Logger.d("mqtt Password: " + mqttPassword);
        return mqttPassword;
    }

}
