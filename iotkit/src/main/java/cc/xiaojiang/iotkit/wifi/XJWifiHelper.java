package cc.xiaojiang.iotkit.wifi;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import java.util.Iterator;
import java.util.List;

import static android.content.Context.WIFI_SERVICE;

public class XJWifiHelper {
    private Context mContext;

    public XJWifiHelper(Context context) {
        mContext = context;
    }

    interface ScanDeviceListener {
        void onDeviceScaned(String platform, String factory, String product);
    }

    public void scanDevice(ScanDeviceListener scanDeviceListener) {
        WifiManager wifiManager = (WifiManager) mContext.getApplicationContext().getSystemService
                (WIFI_SERVICE);
        if (wifiManager != null) {
            List<ScanResult> scanResults = wifiManager.getScanResults();
            Iterator<ScanResult> it = scanResults.iterator();
            while (it.hasNext()) {
                String ssid = it.next().SSID;
                if (ssid.contains("xiaojiang")) {
                    String[] strings = ssid.split("_");
                    scanDeviceListener.onDeviceScaned(strings[0], strings[1], strings[2]);
                }
            }
        } else {
            throw new NullPointerException("get WifiManager return null!");
        }
    }
}
