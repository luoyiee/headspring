package cc.xiaojiang.iotkit.wifi.add;

/**
 * Created by facexxyz on 2018/3/25.
 */

public interface IDeviceAddListener {

    void deviceConnected();

    void deviceAddSucceed(String deviceId);

    void deviceAddFailed(String errorCode);

    void deviceAddTimeout();


}
