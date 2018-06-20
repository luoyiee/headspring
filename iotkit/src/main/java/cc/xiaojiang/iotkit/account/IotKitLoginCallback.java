package cc.xiaojiang.iotkit.account;

import org.eclipse.paho.client.mqttv3.IMqttToken;

import cc.xiaojiang.iotkit.http.IotKitCallBack;
import cc.xiaojiang.iotkit.http.IotKitDeviceManager;
import cc.xiaojiang.iotkit.mqtt.IotKitConnectCallback;
import cc.xiaojiang.iotkit.mqtt.IotKitConnectionManager;

class IotKitLoginCallback implements IotKitAccountCallback {
    private IotKitAccountCallback iotKitAccountCallback;

    public IotKitLoginCallback(IotKitAccountCallback callback) {
        this.iotKitAccountCallback = callback;
    }

    @Override
    public void onSuccess() {
        IotKitDeviceManager.getInstance().userSecret(new IotKitCallBack() {
            @Override
            public void onSuccess(String response) {
                IotKitConnectionManager.getInstance().connectPre(new IotKitConnectCallback() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        iotKitAccountCallback.onSuccess();
                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                        iotKitAccountCallback.onFailed(exception.getMessage());
                    }
                });
            }

            @Override
            public void onError(int code, String errorMsg) {
                iotKitAccountCallback.onFailed(errorMsg);
            }
        });

    }

    @Override
    public void onFailed(String msg) {
        iotKitAccountCallback.onFailed(msg);
    }


}
