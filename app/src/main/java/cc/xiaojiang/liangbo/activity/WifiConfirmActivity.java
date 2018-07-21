package cc.xiaojiang.liangbo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import cc.xiaojiang.liangbo.Constant;
import cc.xiaojiang.liangbo.R;
import cc.xiaojiang.liangbo.base.BaseActivity;
import cc.xiaojiang.liangbo.utils.ActivityCollector;
import cc.xiaojiang.liangbo.utils.NetworkUtils;
import cc.xiaojiang.liangbo.utils.ToastUtils;
import cc.xiaojiang.iotkit.wifi.IotKitWifiSetupHelper;
import cc.xiaojiang.iotkit.wifi.WifiSetupInfo;

public class WifiConfirmActivity extends BaseActivity {

    @BindView(R.id.btn_wifi_conform_next)
    Button mBtnWifiConformNext;
    @BindView(R.id.tv_wifi_confirm_ssid)
    TextView mTvWifiConfirmSsid;
    @BindView(R.id.edTxt_wifi_confirm_password)
    AppCompatEditText mEdTxtWifiConfirmPassword;
    @BindView(R.id.tv_wifi_confirm_change_wifi)
    TextView mTvWifiConfirmChangeWifi;
    private String mProductKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
        Intent intent = getIntent();
        if (intent == null) {
            ToastUtils.show("参数错误");
            finish();
            return;
        }
        mProductKey = intent.getStringExtra("product_key");
    }

    @Override
    protected void onDestroy() {
        ActivityCollector.removeActivity(this);
        super.onDestroy();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_wifi_confirm;
    }

    @Override
    protected void onResume() {
        super.onResume();
        String ssid = IotKitWifiSetupHelper.getInstance().getSSID(this);
        mTvWifiConfirmSsid.setText(ssid);

    }

    @OnClick({R.id.btn_wifi_conform_next, R.id.tv_wifi_confirm_change_wifi})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_wifi_conform_next:
                String password = mEdTxtWifiConfirmPassword.getText().toString();
                String ssid = mTvWifiConfirmSsid.getText().toString();
                WifiSetupInfo wifiSetupInfo = new WifiSetupInfo();
                wifiSetupInfo.setProductKey(mProductKey);
                wifiSetupInfo.setPassword(password);
                wifiSetupInfo.setSsid(ssid);
                wifiSetupInfo.setWifiVendor(WifiSetupInfo.VENDOR_ESPRESSIF);
                Intent intent = new Intent(this, WifiConnectActivity.class);
                intent.putExtra(Constant.DEVICE_INFO, wifiSetupInfo);
                startActivity(intent);
                break;
            case R.id.tv_wifi_confirm_change_wifi:
                NetworkUtils.changeWifi(this);
                break;
        }
    }


}
