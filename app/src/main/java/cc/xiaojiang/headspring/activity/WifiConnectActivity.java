package cc.xiaojiang.headspring.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import butterknife.BindView;
import cc.xiaojiang.headspring.Constant;
import cc.xiaojiang.headspring.R;
import cc.xiaojiang.headspring.base.BaseActivity;
import cc.xiaojiang.headspring.utils.ToastUtils;
import cc.xiaojiang.iotkit.wifi.WifiSetupInfo;
import cc.xiaojiang.iotkit.wifi2.IotKitWifiSetupManager;
import cc.xiaojiang.iotkit.wifi2.WifiSetupCallback;

public class WifiConnectActivity extends BaseActivity {

    @BindView(R.id.tv_wifi_connect_status)
    TextView mTvWifiConnectStatus;
    @BindView(R.id.tv_wifi_connect_bind_status)
    TextView mTvWifiConnectBindStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        WifiSetupInfo wifiSetupInfo = intent.getParcelableExtra(Constant.DEVICE_INFO);
        startWifiConfig(wifiSetupInfo);
    }

    @Override
    protected void onDestroy() {
        IotKitWifiSetupManager.getInstance().stopWifiSetup();
        super.onDestroy();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_wifi_connect;
    }

    private void startWifiConfig(WifiSetupInfo wifiSetupInfo) {
        IotKitWifiSetupManager.getInstance().startWifiSetup(this, wifiSetupInfo, 60 * 1000, new
                WifiSetupCallback() {
                    @Override
                    public void connectSucceed() {

                    }

                    @Override
                    public void joinSucceed(String deviceId) {

                    }

                    @Override
                    public void joinFailed(String errorMsg) {

                    }
                });
    }


    @Override
    public void onBackPressed() {
        ToastUtils.show("请等待配网结束");
    }
}
