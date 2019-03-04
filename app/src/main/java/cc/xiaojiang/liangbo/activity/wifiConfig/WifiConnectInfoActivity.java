package cc.xiaojiang.liangbo.activity.wifiConfig;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import cc.xiaojiang.iotkit.wifi.IotKitWifiSetupManager;
import cc.xiaojiang.liangbo.Constants;
import cc.xiaojiang.liangbo.R;
import cc.xiaojiang.liangbo.base.BaseActivity;
import cc.xiaojiang.liangbo.utils.ActivityCollector;
import cc.xiaojiang.liangbo.utils.NetworkUtils;
import cc.xiaojiang.liangbo.utils.ToastUtils;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class WifiConnectInfoActivity extends BaseActivity {

    @BindView(R.id.btn_wifi_conform_next)
    Button mBtnWifiConformNext;
    @BindView(R.id.tv_wifi_confirm_ssid)
    TextView mTvWifiConfirmSsid;
    @BindView(R.id.edTxt_wifi_confirm_password)
    AppCompatEditText mEdTxtWifiConfirmPassword;
    @BindView(R.id.tv_wifi_confirm_change_wifi)
    TextView mTvWifiConfirmChangeWifi;
    @BindView(R.id.tv_not_support_5g)
    TextView mTvNotSupport5g;
    private String mSsid;
    private String mProductKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
        Intent intent = getIntent();
        mProductKey = intent.getStringExtra(Constants.Intent.PRODUCT_KEY);

    }


    public static void actionStart(Activity activity, String productKey) {
        Intent intent = new Intent(activity, WifiConnectInfoActivity.class);
        intent.putExtra(Constants.Intent.PRODUCT_KEY, productKey);
        activity.startActivity(intent);

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
    protected void onStart() {
        super.onStart();
        WifiConnectInfoActivityPermissionsDispatcher.getSsidWithPermissionCheck(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        com.orhanobut.logger.Logger.e("sssss");


    }

    @OnClick({R.id.btn_wifi_conform_next, R.id.tv_wifi_confirm_change_wifi})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_wifi_conform_next:
                checkNext();
                break;
            case R.id.tv_wifi_confirm_change_wifi:
                NetworkUtils.changeWifi(this);
                break;

        }
    }

    private void checkNext() {
        if (!IotKitWifiSetupManager.getInstance().is24GWifi(this)) {
            show5GInfo();
            return;
        }
        String password = mEdTxtWifiConfirmPassword.getText().toString();
        if (TextUtils.isEmpty(mSsid)) {
            ToastUtils.show("未检测到已连接的WiFi");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            ToastUtils.show("密码不能为空");
            return;
        }
        WifiResetInfoActivity.actionStart(this, mSsid, password, mProductKey);

    }

    private void show5GInfo() {
        new AlertDialog.Builder(this)
                .setTitle("不支持5G WiFi")
                .setView(R.layout.dialog_5g_solution)
                .setPositiveButton("好的", null)
                .show();

    }

    @NeedsPermission({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission
            .ACCESS_COARSE_LOCATION})
    void getSsid() {
        mSsid = IotKitWifiSetupManager.getInstance().getSsid(this);
        mTvWifiConfirmSsid.setText(mSsid);
        if (!IotKitWifiSetupManager.getInstance().is24GWifi(this)) {
            com.xjiangiot.lib.common.utils.ToastUtils.showLong("请检查是否连接了5G WiFi");
            mTvNotSupport5g.setVisibility(View.VISIBLE);
        } else {
            mTvNotSupport5g.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        WifiConnectInfoActivityPermissionsDispatcher.onRequestPermissionsResult(this,
                requestCode, grantResults);
    }

    @OnShowRationale({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission
            .ACCESS_COARSE_LOCATION})
    void onLocationRationale(final PermissionRequest request) {
        new AlertDialog.Builder(this)
                .setTitle("提示")
                .setPositiveButton("同意", (dialog, which) -> request.proceed())
                .setNegativeButton("拒绝", (dialog, which) -> request.cancel())
                .setCancelable(false)
                .setMessage("获取连接的WiFi信息需要获取您的定位权限，请点击同意。")
                .show();

    }

    @OnPermissionDenied({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission
            .ACCESS_COARSE_LOCATION})
    void onLocationDenied() {
        ToastUtils.show("请同意定位权限后再试");
    }

    @OnNeverAskAgain({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission
            .ACCESS_COARSE_LOCATION})
    void onNeverAsk() {
        ToastUtils.show("请在APP权限设置页面打开定位权限后再试");
        // TODO: 2019/1/24 重复调用

    }


}
