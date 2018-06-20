package cc.xiaojiang.iotkit.wifi.find;

/**
 * Created by facexxyz on 2018/3/25.
 */

public interface IDeviceFindListener {

    void onDeviceFind(String deviceId);

    void onFindTimeout();
}
