package cc.xiaojiang.iotkit.http;

public interface IotKitCallBack {

    void onSuccess(String response);

    void onError(int code, String errorMsg);
}
