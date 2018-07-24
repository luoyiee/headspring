package cc.xiaojiang.liangbo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.formatter.IFillFormatter;

import butterknife.BindView;
import butterknife.OnClick;
import cc.xiaojiang.iotkit.wifi2.IotKitWifiSetupManager;
import cc.xiaojiang.iotkit.wifi2.WifiSetupInfo;
import cc.xiaojiang.liangbo.Constant;
import cc.xiaojiang.liangbo.R;
import cc.xiaojiang.liangbo.base.BaseActivity;
import cc.xiaojiang.liangbo.utils.ActivityCollector;
import cc.xiaojiang.liangbo.utils.NetworkUtils;
import cc.xiaojiang.liangbo.utils.ToastUtils;

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
    private String mSsid;

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
        mSsid = IotKitWifiSetupManager.getInstance().getSsid(this);
        mTvWifiConfirmSsid.setText(getString(R.string.wifi_config_connect_wifi, mSsid));

    }

    @OnClick({R.id.btn_wifi_conform_next, R.id.tv_wifi_confirm_change_wifi})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_wifi_conform_next:
                String password = mEdTxtWifiConfirmPassword.getText().toString();
                if (TextUtils.isEmpty(mSsid)) {
                    ToastUtils.show("未检测到已连接的WiFi");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    ToastUtils.show("密码不能为空");
                    return;
                }
                WifiSetupInfo wifiSetupInfo = new WifiSetupInfo();
                wifiSetupInfo.setProductKey(mProductKey);
                wifiSetupInfo.setPassword(password);
                wifiSetupInfo.setSsid(mSsid);
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
