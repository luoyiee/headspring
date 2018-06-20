package cc.xiaojiang.iotkit.http;

/**
 * Created by tong on 2018/1/9.
 */

public final class IotKitHttpUrl {
    public static final String HOST = "https://platform.xjiangiot.com/api/";
//    public static final String HOST = "http://user.xjiangiot.com/api/";

    private IotKitHttpUrl() {
    }

    //用户修改绑定设备昵称
    public static final String DEVICE_NICK = "device/nick";
    //用户解绑设备
    public static final String DEVICE_UNBIND = "device/unbind";
    //用户绑定设备
    public static final String DEVICE_BIND = "device/bind";
    //用户绑定设备
    public static final String DEVICE_LIST = "device/list";
    //获取设备详情
    public static final String DEVICE_INFO = "device/info";
    //appkey换token开发者账号秘钥验证，并获取token
    public static final String DEVELOP_TOKEN = "develop/token";
    //用户获取userSecret
    public static final String USER_SECRET = "user/secret";
    //获取产品列表
    public static final String PRODUCT_LIST = "product/list";
    //获取产品信息
    public static final String PRODUCT_INFO = "product/info";
    //新用户注册，生成用户密钥，且username+source唯一
    public static final String USER_REGISTER = "user/register";
    //设备分享二维码生成
    public static final String SHARE_QRCODE = "device/share/qrcode";
    //用户接收二维码绑定设备
    public static final String SHARE = "device/share";


}
