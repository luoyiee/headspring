package cc.xiaojiang.headspring.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.widget.Button;

import butterknife.BindView;
import butterknife.OnClick;
import cc.xiaojiang.headspring.Constant;
import cc.xiaojiang.headspring.R;
import cc.xiaojiang.headspring.base.BaseActivity;
import cc.xiaojiang.headspring.utils.ActivityCollector;
import cc.xiaojiang.headspring.utils.ToastUtils;
import cc.xiaojiang.iotkit.wifi.WifiSetupInfo;
import cc.xiaojiang.iotkit.wifi.IotKitWifiSetupHelper;

public class WifiConfirmActivity extends BaseActivity {

    @BindView(R.id.btn_wifi_conform_next)
    Button mBtnWifiConformNext;
    @BindView(R.id.edTxt_wifi_confirm_ssid)
    AppCompatEditText mEdTxtWifiConfirmSsid;
    @BindView(R.id.edTxt_wifi_confirm_password)
    AppCompatEditText mEdTxtWifiConfirmPassword;
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
        mEdTxtWifiConfirmSsid.setText(ssid);


    }


    @OnClick(R.id.btn_wifi_conform_next)
    public void onViewClicked() {
        String password = mEdTxtWifiConfirmPassword.getText().toString();
        String ssid = mEdTxtWifiConfirmSsid.getText().toString();
        WifiSetupInfo wifiSetupInfo = new WifiSetupInfo();
        wifiSetupInfo.setProductKey(mProductKey);
        wifiSetupInfo.setPassword(password);
        wifiSetupInfo.setSsid(ssid);
        wifiSetupInfo.setWifiVendor(WifiSetupInfo.VENDOR_ESPRESSIF);
        Intent intent = new Intent(this, WifiConnectActivity.class);
        intent.putExtra(Constant.DEVICE_INFO, wifiSetupInfo);
        startActivity(intent);

    }
}
