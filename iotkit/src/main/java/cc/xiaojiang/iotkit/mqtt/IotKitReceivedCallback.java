package cc.xiaojiang.iotkit.mqtt;

/**
 * Created by zhu on 17-10-27.
 */

public interface IotKitReceivedCallback {
    void messageArrived(String deviceId, String data);

    boolean filter(String deviceId);
}
