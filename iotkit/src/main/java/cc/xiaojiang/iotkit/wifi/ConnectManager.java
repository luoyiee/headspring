package cc.xiaojiang.iotkit.wifi;

import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;

import java.util.List;

import cc.xiaojiang.iotkit.IotKit;
import cc.xiaojiang.iotkit.util.LogUtil;

import static android.content.Context.WIFI_SERVICE;

public class ConnectManager {
//    private static final String TAG = "ConnectManager";
//    private static WifiManager mWifiManager;
//
//    public static void connectWifi(String ssid) {
//        mWifiManager = (WifiManager) IotKit.applicationContext.getSystemService
//                (WIFI_SERVICE);
//        if (mWifiManager != null) {
//            //createWifiConfig主要用于构建一个WifiConfiguration，代码中的例子主要用于连接不需要密码的Wifi
//            //WifiManager的addNetwork接口，传入WifiConfiguration后，得到对应的NetworkId
//            int netId = mWifiManager.addNetwork(createWifiConfig(ssid));
//            //WifiManager的enableNetwork接口，就可以连接到netId对应的wifi了
//            //其中boolean参数，主要用于指定是否需要断开其它Wifi网络
//            boolean enable = mWifiManager.enableNetwork(netId, true);
//            LogUtil.d(TAG, "enable: " + enable);
//
//            //可选操作，让Wifi重新连接最近使用过的接入点
//            //如果上文的enableNetwork成功，那么reconnect同样连接netId对应的网络
//            //若失败，则连接之前成功过的网络
//            boolean reconnect = mWifiManager.reconnect();
//            LogUtil.d(TAG, "reconnect: " + reconnect);
//        } else {
//            throw new NullPointerException("get WifiManager return null!");
//        }
//    }
//
//    private static WifiConfiguration createWifiConfig(String ssid) {
//        //初始化WifiConfiguration
//        WifiConfiguration config = new WifiConfiguration();
//        config.allowedAuthAlgorithms.clear();
//        config.allowedGroupCiphers.clear();
//        config.allowedKeyManagement.clear();
//        config.allowedPairwiseCiphers.clear();
//        config.allowedProtocols.clear();
//
//        //指定对应的SSID
//        config.SSID = "\"" + ssid + "\"";
//        //如果之前有类似的配置
//        WifiConfiguration tempConfig = isExist(ssid);
//        if (tempConfig != null) {
//            //则清除旧有配置
//            mWifiManager.removeNetwork(tempConfig.networkId);
//        }
//        //不需要密码的场景
//        config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
//        return config;
//    }
//
//    private static WifiConfiguration isExist(String ssid) {
//        List<WifiConfiguration> configs = mWifiManager.getConfiguredNetworks();
//        for (WifiConfiguration config : configs) {
//            if (config.SSID.equals("\"" + ssid + "\"")) {
//                return config;
//            }
//        }
//        return null;
//    }
}
