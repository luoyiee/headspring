package cc.xiaojiang.iotkit.mqtt;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.HashSet;
import java.util.Set;

import cc.xiaojiang.iotkit.util.LogUtil;


/**
 * Created by zhu on 17-10-20.
 */

public class MqttCallbackListener implements MqttCallbackExtended {
    private static final String TAG = "MqttCallbackListener";
    private static final int MSG_RECEIVED = 1;

    private Set<IotKitReceivedCallback> iotKitReceivedCallbacks;
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (iotKitReceivedCallbacks == null) {
                LogUtil.w(TAG, "请设置IDataCallback");
                return;
            }
            if (msg.what == MSG_RECEIVED) {
                Bundle bundle = msg.getData();
                if (bundle != null) {
                    String deviceId = bundle.getString("deviceId");
                    String data = bundle.getString("data");
                    for (IotKitReceivedCallback iotKitReceivedCallback : iotKitReceivedCallbacks) {
                        if (!iotKitReceivedCallback.filter(deviceId)) {
                            iotKitReceivedCallback.messageArrived(deviceId, data);
                        }
                    }
                }
            }
        }
    };

    public MqttCallbackListener() {
        iotKitReceivedCallbacks = new HashSet<>();
    }


    @Override
    public void connectComplete(boolean reconnect, String serverURI) {
        if (reconnect) {
            LogUtil.w(TAG, "reconnect to server: " + serverURI);
            //断线重连后，重新订阅topic
//            Set<String> subscribeTopics = IotKitConnectionManager.getInstance().getSubscribeSet();
//            for (String topic : subscribeTopics) {
//                try {
//                    IotKitConnectionManager.getInstance().getMqttAndroidClient().subscribe(topic,
//                            IotKitConnectionManager.QOS_SUBCRIBE);
//                } catch (MqttException e) {
//                    e.printStackTrace();
//                }
//            }
        } else {
            LogUtil.i(TAG, "connect to server: " + serverURI);
        }
    }

    @Override
    public void connectionLost(Throwable cause) {
        LogUtil.e(TAG, "connect lost");
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) {
        String data = new String(message.getPayload());
        LogUtil.i(TAG, "received topic: " + topic);
        LogUtil.i(TAG, "<<<<<<<<< " + data);
        String[] splits = topic.split("/");
        if (splits.length != 4) {
            LogUtil.i(TAG, "received error topic");
            return;
        }
        Message msg = new Message();
        Bundle bundle = new Bundle();
        bundle.putString("deviceId", splits[3]);
        bundle.putString("data", data);
        msg.what = MSG_RECEIVED;
        msg.setData(bundle);
        mHandler.sendMessage(msg);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
    }

    public void add(IotKitReceivedCallback dataCallback) {
        iotKitReceivedCallbacks.add(dataCallback);
    }

    public void remove(IotKitReceivedCallback dataCallback) {
        iotKitReceivedCallbacks.add(dataCallback);
    }

    public void removeAll() {
        iotKitReceivedCallbacks.clear();
    }
}
