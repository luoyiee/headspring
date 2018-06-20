package cc.xiaojiang.iotkit.account;

import org.eclipse.paho.client.mqttv3.IMqttToken;

import cc.xiaojiang.iotkit.http.IotKitCallBack;
import cc.xiaojiang.iotkit.http.IotKitDeviceManager;
import cc.xiaojiang.iotkit.mqtt.IotKitConnectCallback;
import cc.xiaojiang.iotkit.mqtt.IotKitConnectionManager;
import cc.xiaojiang.iotkit.util.IotDbUtils;

class IotKitLogoutCallback implements IotKitAccountCallback {
    private IotKitAccountCallback iotKitAccountCallback;

    public IotKitLogoutCallback(IotKitAccountCallback callback) {
        this.iotKitAccountCallback = callback;
    }

    @Override
    public void onSuccess() {
        IotKitConnectionManager.getInstance().disconnected();
        IotDbUtils.clear();
        iotKitAccountCallback.onSuccess();

    }

    @Override
    public void onFailed(String msg) {
        iotKitAccountCallback.onFailed(msg);
    }


}
