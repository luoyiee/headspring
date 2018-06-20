package cc.xiaojiang.iotkit.mqtt;

import java.util.HashMap;

/**
 * Created by facexxyz on 18-3-26.
 */

public interface IMqttConfig {

    String getMqttServerUrl();

    String getMqttClientId();

    String getMqttUsername();

    String getMqttPassword();

}
