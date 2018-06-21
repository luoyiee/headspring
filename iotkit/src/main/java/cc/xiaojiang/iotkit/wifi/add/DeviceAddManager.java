package cc.xiaojiang.iotkit.wifi.add;//package cc.xiaojiang.icx.iotkit.device;

import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cc.xiaojiang.iotkit.bean.AddDevicePacket;
import cc.xiaojiang.iotkit.bean.AppNotifyPacket;
import cc.xiaojiang.iotkit.bean.ConfigWifiPacket;
import cc.xiaojiang.iotkit.wifi.DeviceInfo;
import cc.xiaojiang.iotkit.wifi.HeadUtils;

/**
 * Created by facexxyz on 2018/3/25.
 */

public class DeviceAddManager {
    private static final DeviceAddManager ourInstance = new DeviceAddManager();
    private static final String TAG = "DeviceAddManager";
    private static final String AP_IP = "192.168.0.1";
    private static final int SEND_TIME = 3;
    private static final int SEND_INTERVAL = 2 * 100;
    private static final int RECEIVE_SOTIMEOUT = 30 * 1000;
    private static final int MSG_CONFIG_WIFI_SUCCEED = 1;
    private static final int MSG_CONFIG_WIFI_FAILED = 2;
    private static final int MSG_DEVICE_CONNECTED = 3;
    private static final int MSG_ADD_DEVICE_SUCCEED = 4;
    private static final int MSG_ADD_DEVICE_FAILED = 5;
    private IDeviceAddListener mIDeviceAddListener;
    private DeviceInfo deviceInfo;
    private String ap;
    private String ssid;
    private String password;

    public static DeviceAddManager getInstance() {
        return ourInstance;
    }

    private ExecutorService mExecutorService = Executors.newCachedThreadPool();


    private android.os.Handler mHandler = new android.os.Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (mIDeviceAddListener == null) {
                Logger.w("please set IDeviceAddListener!");
                return;
            }
            switch (msg.what) {
                case MSG_DEVICE_CONNECTED:
                    mIDeviceAddListener.deviceConnected();
                    //step4,收到设备联网成功指令,回复设备端
                    mExecutorService.execute(new sendRunnable(getAppNotifyPacket()));
                    //step5,发送product_key,app_port
                    mExecutorService.execute(new sendRunnable(getAddDevicePacket()));
                    break;
                case MSG_ADD_DEVICE_SUCCEED:
                    mIDeviceAddListener.deviceAddSucceed((String) msg.obj);
                    break;
                case MSG_ADD_DEVICE_FAILED:
                    mIDeviceAddListener.deviceAddFailed((String) msg.obj);
                    break;
            }

        }
    };


    private DeviceAddManager() {
        // TODO: 18-4-8 超时时间自定义 ,小于0不设置超时时间

    }


    public void cancelAdd() {
        if (mExecutorService == null || mExecutorService.isShutdown()) {
            Logger.i( "mExecutorService is null or already shutdown");
            return;
        }
        mExecutorService.shutdownNow();
        mIDeviceAddListener = null;
        Logger.i("DeviceFindManager shutdown");
    }

    public void startAdd(DeviceInfo deviceInfo, String ap, String ssid, String password,
                         IDeviceAddListener iDeviceAddListener) {
        if (deviceInfo == null) {
            throw new NullPointerException("null DeviceInfo!");
        }
        if (TextUtils.isEmpty(ap)) {
            throw new NullPointerException("null app!");
        }
        if (TextUtils.isEmpty(ssid)) {
            throw new NullPointerException("null ssid!");
        }
        if (TextUtils.isEmpty(password)) {
            throw new NullPointerException("null password!");
        }
        this.deviceInfo = deviceInfo;
        this.ap = ap;
        this.ssid = ssid;
        this.password = password;
        mIDeviceAddListener = iDeviceAddListener;
        mExecutorService.execute(new ReceiveRunnable());
        mExecutorService.execute(new sendRunnable(getConfigWifiPacket()));
    }

    private byte[] getConfigWifiPacket() {
        ConfigWifiPacket configWifiPacket = new ConfigWifiPacket();
        ConfigWifiPacket.DataBean dataBean = new ConfigWifiPacket.DataBean();
        dataBean.setSsid(ssid);
        dataBean.setPassword(password);
        configWifiPacket.setData(dataBean);
        configWifiPacket.setMsg_type("router_info");
        configWifiPacket.setProduct_key(deviceInfo.getProductKey());
        configWifiPacket.setMsg_id(String.valueOf(System.currentTimeMillis()));
        String msg = new Gson().toJson(configWifiPacket);
        Logger.i( "packet: " + msg);
        return HeadUtils.PackData(0, msg.getBytes());
    }

    private byte[] getAppNotifyPacket() {
        AppNotifyPacket appNotifyPacket = new AppNotifyPacket();
        appNotifyPacket.setMsg_type("app_notify");
        appNotifyPacket.setMsg_id(String.valueOf(System.currentTimeMillis()));
        appNotifyPacket.setResult_code("0");
        String msg = new Gson().toJson(appNotifyPacket);
        Logger.i("app_notify packet: " + msg);
        return HeadUtils.PackData(0, msg.getBytes());
    }

    private byte[] getAddDevicePacket() {
        AddDevicePacket addDevicePacket = new AddDevicePacket();
        addDevicePacket.setMsg_type("add_device");
        addDevicePacket.setMsg_id(String.valueOf(System.currentTimeMillis()));
        AddDevicePacket.DataBean dataBean = new AddDevicePacket.DataBean();
        dataBean.setProduct_key(deviceInfo.getProductKey());
        dataBean.setApp_port("8888");
        addDevicePacket.setData(dataBean);
        String msg = new Gson().toJson(addDevicePacket);
        Logger.i("app_notify packet: " + msg);
        return HeadUtils.PackData(0, msg.getBytes());
    }

    public class sendRunnable implements Runnable {
        private byte[] bytes;

        public sendRunnable(byte[] bytes) {
            this.bytes = bytes;
        }

        @Override
        public void run() {
            try {
                DatagramSocket socket = new DatagramSocket();
                InetAddress local = InetAddress.getByName(AP_IP);
                for (int i = 0; i < SEND_TIME; i++) {
                    DatagramPacket packet = new DatagramPacket(bytes, bytes.length, local, 8888);
                    socket.send(packet);
                    Logger.i( "send packet");
                    Thread.sleep(SEND_INTERVAL);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }

    public class ReceiveRunnable implements Runnable {
        @Override
        public void run() {
            try {
                DatagramSocket socket = new DatagramSocket(8888);
                socket.setSoTimeout(RECEIVE_SOTIMEOUT);
                byte[] recBuf = new byte[500];
                DatagramPacket recPacket = new DatagramPacket(recBuf, recBuf.length);
                while (true) {
                    socket.receive(recPacket);
                    byte[] received = Arrays.copyOf(recPacket.getData(), recPacket.getLength());
                    Logger.d("receive: " + Arrays.toString(received));
                    decodePacket(received);
                }
            } catch (IOException e) {
                e.printStackTrace();
//                mHandler.sendEmptyMessage(MSG_TIMEOUT);
            }

        }

    }

    private void decodePacket(byte[] received) {
        String dataStr = HeadUtils.unpackData(received);
        Logger.i("receive body: " + dataStr);
        try {
            JSONObject jsonObject = new JSONObject(dataStr);
            if (jsonObject.has("msg_type")) {
                switch (jsonObject.getString("msg_type")) {
                    case "router_info":
                        String resultCode1 = jsonObject.getString("result_code");
                        Message message1 = new Message();
                        if ("0".equals(resultCode1)) {
                            message1.what = MSG_CONFIG_WIFI_SUCCEED;
                        } else {
                            message1.what = MSG_CONFIG_WIFI_FAILED;
                            message1.obj = resultCode1;
                        }
                        mHandler.sendMessage(message1);
                        break;
                    case "app_notify":
                        mHandler.sendEmptyMessage(MSG_DEVICE_CONNECTED);
                        break;
                    case "add_device":
                        String resultCode2 = jsonObject.getString("result_code");
                        JSONObject dataObject = jsonObject.getJSONObject("data");
                        Message message2 = new Message();
                        if ("0".equals(resultCode2)) {
                            message2.what = MSG_ADD_DEVICE_SUCCEED;
                            message2.obj = dataObject.getString("device_id");
                        } else {
                            message2.what = MSG_ADD_DEVICE_FAILED;
                            message2.obj = resultCode2;
                        }
                        mHandler.sendMessage(message2);
                        break;
                }

            } else {
                Logger.e("received error data type");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
