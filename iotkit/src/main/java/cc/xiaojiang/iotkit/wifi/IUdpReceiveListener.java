package cc.xiaojiang.iotkit.wifi;

public interface IUdpReceiveListener {
    void onUdpReceived(byte[] bytes);

    void onUdpTimeout();
}
