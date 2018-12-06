package cc.xiaojiang.liangbo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import cc.xiaojiang.iotkit.bean.http.ProductInfo;
import cc.xiaojiang.iotkit.wifi.IotKitWifiSetupManager;
import cc.xiaojiang.iotkit.wifi.WifiSetupInfo;
import cc.xiaojiang.liangbo.utils.constant.Constant;
import cc.xiaojiang.liangbo.R;
import cc.xiaojiang.liangbo.base.BaseActivity;
import cc.xiaojiang.liangbo.utils.ActivityCollector;
import cc.xiaojiang.liangbo.utils.DbUtils;
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
    @BindView(R.id.iv_wifi_config_show_pwd)
    ImageView mIvWifiConfigShowPwd;
    private ProductInfo mProductInfo;
    private String mSsid;
    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
        Intent intent = getIntent();
        mProductInfo = intent.getParcelableExtra("product_info");
        if (mProductInfo == null) {
            ToastUtils.show("参数错误");
            finish();
        }

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
        String pwd = DbUtils.getPwdBySsid(mSsid);
        if (!TextUtils.isEmpty(pwd)) {
            mEdTxtWifiConfirmPassword.setText(pwd);
            mEdTxtWifiConfirmPassword.setSelection(pwd.length());
        }

    }

    @OnClick({R.id.btn_wifi_conform_next, R.id.tv_wifi_confirm_change_wifi, R.id
            .iv_wifi_config_show_pwd})
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
                wifiSetupInfo.setProductKey(mProductInfo.getProductKey());
                wifiSetupInfo.setPassword(password);
                wifiSetupInfo.setSsid(mSsid);
                wifiSetupInfo.setWifiVendor(mProductInfo.getModuleVendor());
                wifiSetupInfo.setConfigType(mProductInfo.getConfigNetworkType());
                Intent intent = new Intent(this, WifiConnectActivity.class);
                intent.putExtra(Constant.DEVICE_INFO, wifiSetupInfo);
                startActivity(intent);
                break;
            case R.id.tv_wifi_confirm_change_wifi:
                NetworkUtils.changeWifi(this);
                break;
            case R.id.iv_wifi_config_show_pwd:
                showHidePassword();
                break;
        }
    }

    private void showHidePassword() {
        isPasswordVisible = !isPasswordVisible;
        mIvWifiConfigShowPwd.setSelected(isPasswordVisible);
        if (isPasswordVisible) {
            mEdTxtWifiConfirmPassword.setInputType(EditorInfo.TYPE_CLASS_TEXT | EditorInfo
                    .TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
            mEdTxtWifiConfirmPassword.setInputType(EditorInfo.TYPE_CLASS_TEXT | EditorInfo
                    .TYPE_TEXT_VARIATION_PASSWORD);
        }
        mEdTxtWifiConfirmPassword.setSelection(mEdTxtWifiConfirmPassword.getText().toString()
                .length());
    }


}
