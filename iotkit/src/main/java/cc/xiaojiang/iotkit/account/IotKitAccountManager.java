package cc.xiaojiang.iotkit.account;

public class IotKitAccountManager {
    private IotKitAccountConfig iotKitAccountConfig;
    private static final IotKitAccountManager ourInstance = new IotKitAccountManager();

    public static IotKitAccountManager getInstance() {
        return ourInstance;
    }

    private IotKitAccountManager() {

    }

    public void init(IotKitAccountConfig iotKitAccountConfig) {
        this.iotKitAccountConfig = iotKitAccountConfig;
    }


    public void login(IotKitLoginParams params, IotKitAccountCallback callback) {
        checkArg(callback);
        iotKitAccountConfig.login(params,new IotKitLoginCallback(callback));
    }


    public void logout(IotKitAccountCallback callback) {
        checkArg(callback);
        iotKitAccountConfig.logout(new IotKitLogoutCallback(callback));

    }

    public String getAppSource() {
        return iotKitAccountConfig.getAppSource();
    }

    public String getXJUserId() {
        return iotKitAccountConfig.getXJUserId();
    }

    public String getDevelopKey() {
        return iotKitAccountConfig.getDevelopKey();
    }

    public String getDevelopSecret() {
        return iotKitAccountConfig.getDevelopSecret();
    }

    public boolean isLogin() {
        return iotKitAccountConfig.isLogin();
    }

    private void checkArg(IotKitAccountCallback iotKitAccountCallback) {
        if (null == iotKitAccountCallback) {
            throw new IllegalArgumentException("IotKitAccountCallback is null");
        }
    }


}
