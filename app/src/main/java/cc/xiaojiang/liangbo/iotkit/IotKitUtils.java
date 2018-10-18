package cc.xiaojiang.liangbo.iotkit;

import android.text.TextUtils;

import cc.xiaojiang.iotkit.bean.http.Device;

public class IotKitUtils {
    public static String getDeviceName(Device mDevice) {
        return TextUtils.isEmpty(mDevice.getDeviceNickname()) ? mDevice
                .getProductName() : mDevice.getDeviceNickname();
    }
}
