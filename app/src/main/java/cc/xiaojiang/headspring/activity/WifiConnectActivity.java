package cc.xiaojiang.headspring.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import butterknife.BindView;
import cc.xiaojiang.headspring.Constant;
import cc.xiaojiang.headspring.R;
import cc.xiaojiang.headspring.base.BaseActivity;
import cc.xiaojiang.headspring.utils.ToastUtils;
import cc.xiaojiang.iotkit.bean.http.DeviceBindRes;
import cc.xiaojiang.iotkit.http.IotKitCallBack;
import cc.xiaojiang.iotkit.http.IotKitDeviceManager;
import cc.xiaojiang.iotkit.http.IotKitHttpCallback2;
import cc.xiaojiang.iotkit.wifi.WifiSetupInfo;
import cc.xiaojiang.iotkit.wifi.IotKitWifiSetupHelper;
import cc.xiaojiang.iotkit.wifi.add.IDeviceAddListener;

public class WifiConnectActivity extends BaseActivity {

    @BindView(R.id.tv_wifi_connect_status)
    TextView mTvWifiConnectStatus;
    @BindView(R.id.tv_wifi_connect_bind_status)
    TextView mTvWifiConnectBindStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String ssid = intent.getStringExtra(Constant.WIFI_SSID);
        String password = intent.getStringExtra(Constant.WIFI_PASSWORD);
        WifiSetupInfo wifiSetupInfo = intent.getParcelableExtra(Constant.DEVICE_INFO);
        startWifiConfig(ssid, password, wifiSetupInfo);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_wifi_connect;
    }

    private void startWifiConfig(String ssid, String password, WifiSetupInfo wifiSetupInfo) {
        IotKitWifiSetupHelper.getInstance().startAdd(this, wifiSetupInfo, ssid, password, new
                IDeviceAddListener() {
                    @Override
                    public void deviceConnected() {
                        mTvWifiConnectStatus.setText("设备联网成功");

                    }

                    @Override
                    public void deviceAddSucceed(String deviceId) {
                        // TODO: 2018/7/14 绑定设备写入sdk
                        IotKitDeviceManager.getInstance().deviceBind(wifiSetupInfo.getProductKey(),
                                deviceId, new IotKitHttpCallback2<DeviceBindRes>() {
                                    @Override
                                    public void onSuccess(DeviceBindRes data) {
                                        mTvWifiConnectBindStatus.setText("设备绑定成功");
                                    }

                                    @Override
                                    public void onError(String code, String errorMsg) {

                                    }
                                });

                    }

                    @Override
                    public void deviceAddFailed(String errorCode) {
                        ToastUtils.show("添加失败");

                    }

                    @Override
                    public void deviceAddTimeout() {
                        ToastUtils.show("添加超时");
                    }
                });
    }


    @Override
    public void onBackPressed() {
        ToastUtils.show("请等待配网结束");
    }
}
