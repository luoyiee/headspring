package cc.xiaojiang.iotkit.account;

/**
 * Created by facexxyz on 18-3-26.
 */

public interface IotKitAccountConfig {

    /**
     * 登录状态
     */
    boolean isLogin();

    /**
     * APP标示
     */
    String getAppSource();

    /**
     * 登录
     *
     * @param callback
     */
    void login(IotKitLoginParams params,IotKitAccountCallback callback);

    /**
     * 注销
     */
    void logout(IotKitAccountCallback callback);


    /**
     * 小匠云userId
     */
    String getXJUserId();


    String getDevelopKey();


    String getDevelopSecret();


}
