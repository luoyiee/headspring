package cc.xiaojiang.headspring.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;

import butterknife.BindView;
import cc.xiaojiang.headspring.Constant;
import cc.xiaojiang.headspring.R;
import cc.xiaojiang.headspring.base.BaseActivity;
import cc.xiaojiang.headspring.utils.ActivityCollector;
import cc.xiaojiang.headspring.utils.ToastUtils;
import cc.xiaojiang.iotkit.wifi.WifiSetupInfo;
import cc.xiaojiang.iotkit.wifi2.IotKitWifiSetupManager;
import cc.xiaojiang.iotkit.wifi2.WifiSetupCallback;

public class WifiConnectActivity extends BaseActivity {

    @BindView(R.id.tv_wifi_connect_status)
    TextView mTvWifiConnectStatus;
    @BindView(R.id.tv_wifi_connect_bind_status)
    TextView mTvWifiConnectBindStatus;
    @BindView(R.id.tv_wifi_connect_count_dowm)
    TextView mTvWifiConnectCountDowm;

    private CountDownTimer mCountDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
        Intent intent = getIntent();
        WifiSetupInfo wifiSetupInfo = intent.getParcelableExtra(Constant.DEVICE_INFO);
        mCountDownTimer = new CountDownTimer(60 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTvWifiConnectCountDowm.setText(millisUntilFinished / 1000 + "s");
            }

            @Override
            public void onFinish() {

            }
        };
        startWifiConfig(wifiSetupInfo);

    }

    @Override
    protected void onDestroy() {
        mCountDownTimer.cancel();
        IotKitWifiSetupManager.getInstance().stopWifiSetup();
        ActivityCollector.removeActivity(this);
        super.onDestroy();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_wifi_connect;
    }

    private void startWifiConfig(WifiSetupInfo wifiSetupInfo) {
        mCountDownTimer.start();
        IotKitWifiSetupManager.getInstance().startWifiSetup(this, wifiSetupInfo, 60 * 1000, new
                WifiSetupCallback() {
                    @Override
                    public void connectSucceed() {
                        mTvWifiConnectStatus.setText("设备联网成功");

                    }

                    @Override
                    public void joinSucceed(String deviceId) {
                        mTvWifiConnectStatus.setText("设备绑定成功");
                        ToastUtils.show("设备绑定成功");
                        ActivityCollector.finishAll();
                    }

                    @Override
                    public void joinFailed(String errorMsg) {
                        mTvWifiConnectStatus.setText("设备绑定失败：" + errorMsg);
                        mCountDownTimer.cancel();
                    }
                });
    }


    @Override
    public void onBackPressed() {
        ToastUtils.show("请等待配网结束");
    }
}
