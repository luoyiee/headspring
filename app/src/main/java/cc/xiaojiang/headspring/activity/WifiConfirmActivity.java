package cc.xiaojiang.headspring.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.widget.Button;

import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.OnClick;
import cc.xiaojiang.headspring.R;
import cc.xiaojiang.headspring.base.BaseActivity;
import cc.xiaojiang.headspring.utils.ToastUtils;
import cc.xiaojiang.iotkit.http.IotKitCallBack;
import cc.xiaojiang.iotkit.http.IotKitDeviceManager;
import cc.xiaojiang.iotkit.wifi.DeviceInfo;
import cc.xiaojiang.iotkit.wifi.add.IDeviceAddListener;
import cc.xiaojiang.iotkit.wifi.easyLink.EasyLinkHelper;

public class WifiConfirmActivity extends BaseActivity {

    @BindView(R.id.btn_wifi_conform_next)
    Button mBtnWifiConformNext;
    @BindView(R.id.edTxt_wifi_confirm_ssid)
    AppCompatEditText mEdTxtWifiConfirmSsid;
    @BindView(R.id.edTxt_wifi_confirm_password)
    AppCompatEditText mEdTxtWifiConfirmPassword;
    private String mProductKey;
    private String mSsid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent == null) {
            ToastUtils.show("参数错误");
            finish();
            return;
        }
        mProductKey = intent.getStringExtra("product_key");

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_wifi_confirm;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSsid = EasyLinkHelper.getInstance().getSSID(this);
        mEdTxtWifiConfirmSsid.setText(mSsid);
    }


    @OnClick(R.id.btn_wifi_conform_next)
    public void onViewClicked() {
        String password = mEdTxtWifiConfirmPassword.getText().toString();
        DeviceInfo deviceInfo = new DeviceInfo();
//        deviceInfo.setProductKey(mProductKey);
        deviceInfo.setProductKey("duu990");
        EasyLinkHelper.getInstance().startAdd(this, deviceInfo, mSsid, password, new
                IDeviceAddListener() {
                    @Override
                    public void deviceConnected() {

                    }

                    @Override
                    public void deviceAddSucceed(String deviceId) {
                        IotKitDeviceManager.getInstance().deviceBind(mProductKey, deviceId, new
                                IotKitCallBack() {
                                    @Override
                                    public void onSuccess(String response) {
                                        ToastUtils.show("绑定成功");
                                    }

                                    @Override
                                    public void onError(int code, String errorMsg) {

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
}
