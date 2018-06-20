package cc.xiaojiang.iotkit.account;

public interface IotKitAccountCallback {
    void onSuccess();

    void onFailed(String msg);
}
