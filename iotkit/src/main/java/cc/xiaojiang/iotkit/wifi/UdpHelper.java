package cc.xiaojiang.iotkit.wifi;

import android.os.Looper;
import android.os.Message;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cc.xiaojiang.iotkit.util.LogUtil;

public class UdpHelper {
    private static final int DEFAULT_RECEIVE_TIMEOUT = 30 * 1000;
    private static final int DEFAULT_SEND_TIME = 1;
    private static final int DEFAULT_SEND_INTERVAL = 200;
    private static final int MSG_UDP_TIMEOUT = 1;
    private static final int MSG_UDP_RECEIVED = 2;
    private static final String TAG = "UdpHelper";
    private ExecutorService mExecutorService;
    private IUdpReceiveListener mIUdpReceiveListener;

    private android.os.Handler mHandler = new android.os.Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (mIUdpReceiveListener == null) {
                LogUtil.w(TAG, "请设置IUdpReceiveListener!");
                return;
            }
            switch (msg.what) {
                case MSG_UDP_TIMEOUT:
                    mIUdpReceiveListener.onUdpTimeout();
                    break;
                case MSG_UDP_RECEIVED:
                    mIUdpReceiveListener.onUdpReceived((byte[]) msg.obj);
                    break;
            }
        }
    };


    public void setOnUdpReceivedListener(int port, IUdpReceiveListener iUdpReceiveListener) {
        setOnUdpReceivedListener(port, DEFAULT_RECEIVE_TIMEOUT, iUdpReceiveListener);
    }

    public void setOnUdpReceivedListener(final int port, final int timeout, IUdpReceiveListener
            iUdpReceiveListener) {
        mIUdpReceiveListener = iUdpReceiveListener;
        if (mExecutorService == null) {
            mExecutorService = Executors.newCachedThreadPool();
        }
        mExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    DatagramSocket socket = new DatagramSocket(port);
                    socket.setSoTimeout(timeout);
                    byte[] recBuf = new byte[500];
                    DatagramPacket recPacket = new DatagramPacket(recBuf, recBuf.length);
                    while (true) {
                        socket.receive(recPacket);
                        byte[] received = Arrays.copyOf(recPacket.getData(), recPacket.getLength());
                        LogUtil.d(TAG, "receive <<<<<<" + Arrays.toString(received));
                        Message message = new Message();
                        message.what = MSG_UDP_RECEIVED;
                        message.obj = received;
                        mHandler.sendMessage(message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    mHandler.sendEmptyMessage(MSG_UDP_TIMEOUT);
                }
            }
        });
    }


    public void shutdown() {
        mExecutorService.shutdownNow();
        mExecutorService = null;
    }

    public void setDataReceiveListener(IUdpReceiveListener iUdpReceiveListener) {
        mIUdpReceiveListener = iUdpReceiveListener;
    }


    public void send(byte[] bytes, String host, int port) {
        send(bytes, host, port, DEFAULT_SEND_TIME);
    }

    public void send(byte[] bytes, String host, int port, int time) {
        send(bytes, host, port, time, DEFAULT_SEND_INTERVAL);
    }

    public void send(final byte[] data, final String host, final int port, final int time, final
    int interval) {
        if (mExecutorService == null) {
            mExecutorService = Executors.newCachedThreadPool();
        }
        mExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    DatagramSocket socket = new DatagramSocket();
                    InetAddress local = InetAddress.getByName(host);
                    for (int i = 0; i < time; i++) {
                        DatagramPacket packet = new DatagramPacket(data, data.length, local, port);
                        socket.send(packet);
                        LogUtil.i(TAG, "send >>>>>>" + Arrays.toString(data));
                        Thread.sleep(interval);
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}