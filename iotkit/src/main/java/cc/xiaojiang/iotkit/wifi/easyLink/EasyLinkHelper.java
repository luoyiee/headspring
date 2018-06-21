package cc.xiaojiang.iotkit.wifi.easyLink;

import android.content.Context;
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
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cc.xiaojiang.iotkit.bean.AddDevicePacket;
import cc.xiaojiang.iotkit.wifi.DeviceInfo;
import cc.xiaojiang.iotkit.wifi.HeadUtils;
import cc.xiaojiang.iotkit.wifi.add.IDeviceAddListener;
import io.fogcloud.sdk.easylink.api.EasyLink;
import io.fogcloud.sdk.easylink.helper.EasyLinkCallBack;
import io.fogcloud.sdk.easylink.helper.EasyLinkParams;

public class EasyLinkHelper implements EasyLinkCallBack {
    // TODO: 18-4-20 策略模式
    private static final EasyLinkHelper ourInstance = new EasyLinkHelper();
    private static final String TAG = "EasyLinkHelper";
    private static final int SEND_TIME = 3;
    private static final int RECEIVE_SOTIMEOUT = 20 * 1000;
    private static final int MSG_DEVICE_CONNECTED = 3;
    private static final int MSG_ADD_DEVICE_SUCCEED = 4;
    private static final int MSG_ADD_DEVICE_FAILED = 5;
    private static final int MSG_TIMEOUT = 6;
    private IDeviceAddListener mIDeviceAddListener;
    private DeviceInfo deviceInfo;
    private String ssid;
    private String password;
    private String mIp;
    private EasyLink mEasyLink;
    private boolean wifiConnected = false;
    private Timer mAppNotifyTimer = new Timer();

    public static EasyLinkHelper getInstance() {
        return ourInstance;
    }

    private ExecutorService mExecutorService = Executors.newSingleThreadExecutor();


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
                    Logger.d("设备连网成功");
                    //step5,收到设备联网成功指令,回复设备端
                    mExecutorService.execute(new AppNotifyReceiveRunnable());
                    sentAddDevice();
                    break;
                case MSG_ADD_DEVICE_SUCCEED:
                    mIDeviceAddListener.deviceAddSucceed((String) msg.obj);
                    Logger.d("设备接入小匠云成功");
                    break;
                case MSG_ADD_DEVICE_FAILED:
                    mIDeviceAddListener.deviceAddFailed((String) msg.obj);
                    Logger.d("设备接入小匠云失败");
                    break;

                case MSG_TIMEOUT:
                    mIDeviceAddListener.deviceAddTimeout();
                    Logger.d("设备接入小匠云超时");
                    break;
            }
        }
    };

    /**
     * 每500ms发送一次
     */
    private void sentAddDevice() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                try {
                    DatagramSocket socket = new DatagramSocket();
                    InetAddress local = InetAddress.getByName(mIp);
                    byte[] bytes = getAddDevicePacket();
                    DatagramPacket packet = new DatagramPacket(bytes, bytes.length, local,
                            9999);
                    socket.send(packet);
                    Logger.i("send packet");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        mAppNotifyTimer.schedule(task, 0, 500);
    }


    private EasyLinkHelper() {
        // TODO: 18-4-8 超时时间自定义 ,小于0不设置超时时间

    }


    public void cancelAdd() {
        mExecutorService.shutdownNow();
        mAppNotifyTimer.cancel();
        mEasyLink.stopEasyLink(this);
        mIDeviceAddListener = null;
        Logger.i("EasyLinkHelper cancel");
    }

    public void startAdd(Context context, DeviceInfo deviceInfo, String ssid, String password,
                         IDeviceAddListener iDeviceAddListener) {
        if (deviceInfo == null) {
            throw new NullPointerException("null DeviceInfo!");
        }
        if (TextUtils.isEmpty(ssid)) {
            throw new NullPointerException("null ssid!");
        }
        if (TextUtils.isEmpty(password)) {
            throw new NullPointerException("null password!");
        }
        this.deviceInfo = deviceInfo;
        this.ssid = ssid;
        this.password = password;
        mIDeviceAddListener = iDeviceAddListener;
        startEasyLink(context);
    }

    public String getSSID(Context context) {
        return new EasyLink(context).getSSID();
    }

    private void startEasyLink(Context context) {
        EasyLinkParams params = new EasyLinkParams();
        params.ssid = ssid;
        params.password = password;
        mEasyLink = new EasyLink(context);
        Logger.d("start easylink");
        mEasyLink.startEasyLink(params, this);
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

    @Override
    public void onSuccess(int code, String message) {
    }

    @Override
    public void onFailure(int code, String message) {
    }


    public class AppNotifyReceiveRunnable implements Runnable {
        @Override
        public void run() {
            try {
                DatagramSocket socket = new DatagramSocket(8888);
                socket.setSoTimeout(RECEIVE_SOTIMEOUT);
                byte[] recBuf = new byte[1024];
                DatagramPacket recPacket = new DatagramPacket(recBuf, recBuf.length);
                while (true) {
                    socket.receive(recPacket);
                    byte[] received = Arrays.copyOf(recPacket.getData(), recPacket.getLength());
                    mIp = recPacket.getAddress().getHostAddress();
                    Logger.d("receive: " + Arrays.toString(received));
                    decodePacket(received);
                }
            } catch (IOException e) {
                e.printStackTrace();
                mHandler.sendEmptyMessage(MSG_TIMEOUT);
                cancelAdd();
            }
        }

    }

    private void decodePacket(byte[] received) {
        String dataStr = HeadUtils.unpackData(received);
        Logger.i("receive body: " + dataStr);
        if (dataStr == null) {
            return;
        }
        try {
            JSONObject jsonObject = new JSONObject(dataStr);
            if (jsonObject.has("msg_type")) {
                switch (jsonObject.getString("msg_type")) {
                    case "app_notify":
                        if (!wifiConnected) {
                            mHandler.sendEmptyMessage(MSG_DEVICE_CONNECTED);
                            wifiConnected = true;
                        }
                        break;
                    case "add_device":
                        cancelAdd();
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
