package cc.xiaojiang.iotkit.wifi.find;//package cc.xiaojiang.icx.iotkit.device;

import android.os.Looper;
import android.os.Message;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cc.xiaojiang.iotkit.wifi.DeviceInfo;
import cc.xiaojiang.iotkit.wifi.HeadUtils;

/**
 * Created by facexxyz on 2018/3/25.
 */

public class DeviceFindManager {
    private static final DeviceFindManager ourInstance = new DeviceFindManager();
    private static final String TAG = "DeviceFindManager";
    private static final int SEND_TIME = 3;
    private static final int SEND_INTERVAL = 2 * 100;
    private static final int RECEIVE_SOTIMEOUT = 30 * 1000;
    private static final int MSG_ON_FIND = 1;
    private static final int MSG_TIMEOUT = 2;
    private IDeviceFindListener mIDeviceFindListener;

    public static DeviceFindManager getInstance() {
        return ourInstance;
    }

    private ExecutorService mExecutorService = Executors.newCachedThreadPool();


    private android.os.Handler mHandler = new android.os.Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (mIDeviceFindListener == null) {
                Logger.w("please set IDeviceFindListener!");
                return;
            }
            switch (msg.what) {
                case MSG_ON_FIND:
                    String deviceId = (String) msg.obj;
                    mIDeviceFindListener.onDeviceFind(deviceId);
                    break;
                case MSG_TIMEOUT:
                    mIDeviceFindListener.onFindTimeout();
                    break;
            }
        }
    };

    private DeviceFindManager() {
        // TODO: 18-4-8 超时时间自定义 ,小于0不设置超时时间
    }


    public void startFind(final DeviceInfo deviceInfo, IDeviceFindListener deviceFindListener) {
        mIDeviceFindListener = deviceFindListener;
        if (deviceInfo == null) {
            throw new NullPointerException("null DeviceInfo!");
        }
        mExecutorService.execute(new ListenRunnable());
        mExecutorService.execute(new BroadcastRunnable(deviceInfo));
    }

    public void stop() {
        if (mExecutorService == null || mExecutorService.isShutdown()) {
            Logger.i("mExecutorService is null or already shutdown");
            return;
        }
        mExecutorService.shutdownNow();
        mIDeviceFindListener = null;
        Logger.i("DeviceFindManager shutdown");
    }

    public class BroadcastRunnable implements Runnable {
        private DeviceInfo deviceInfo;

        public BroadcastRunnable(DeviceInfo deviceInfo) {
            this.deviceInfo = deviceInfo;
        }

        @Override
        public void run() {
            try {
                DatagramSocket socket = new DatagramSocket();
                InetAddress local = InetAddress.getByName("localhost");
                for (int i = 0; i < SEND_TIME; i++) {
                    byte[] dataPacket = HeadUtils.PackData(0, getDeviceFindPacket());
                    DatagramPacket packet = new DatagramPacket(dataPacket, dataPacket.length,
                            local, 8888);
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

        private byte[] getDeviceFindPacket() {
            DeviceFindPacket deviceFindPacket = new DeviceFindPacket();
            DeviceFindPacket.DataBean dataBean = new DeviceFindPacket.DataBean();
            dataBean.setProduct_key(deviceInfo.getProductKey());
            dataBean.setApp_port("9999");
            deviceFindPacket.setData(dataBean);
            deviceFindPacket.setMsg_type("add_device");
            deviceFindPacket.setMsg_id(String.valueOf(System.currentTimeMillis()));
            String msg = new Gson().toJson(deviceFindPacket);
            Logger.i("packet: " + msg);
            return msg.getBytes();
        }
    }

    public class ListenRunnable implements Runnable {
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
                mHandler.sendEmptyMessage(MSG_TIMEOUT);
            }
        }

        private void decodePacket(byte[] received) {
            String data = HeadUtils.unpackData(received);
            Logger.i("receive body: " + data);
            DeviceFindBean deviceFindBean = new Gson().fromJson(data, DeviceFindBean.class);
            Message message = new Message();
            message.what = MSG_ON_FIND;
            // TODO: 18-4-8 data null判断
            message.obj = deviceFindBean.getData().getDevice_id();
            mHandler.sendMessage(message);
        }
    }
}
