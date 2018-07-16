package cc.xiaojiang.headspring.activity;

import android.os.Bundle;

import cc.xiaojiang.headspring.R;
import cc.xiaojiang.headspring.base.BaseActivity;
import cc.xiaojiang.headspring.utils.ToastUtils;
import cc.xiaojiang.iotkit.bean.http.Device;
import cc.xiaojiang.iotkit.bean.http.DeviceShareRes;
import cc.xiaojiang.iotkit.http.IotKitDeviceManager;
import cc.xiaojiang.iotkit.http.IotKitHttpCallback;

public class ShareQrCodeActivity extends BaseActivity {

    private Device mDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        getShareQrCode();

    }

    private void getShareQrCode() {
        IotKitDeviceManager.getInstance().sendDeviceShare(mDevice.getProductKey(), mDevice
                .getDeviceId(), new IotKitHttpCallback<DeviceShareRes>() {
            @Override
            public void onSuccess(DeviceShareRes data) {

            }

            @Override
            public void onError(String code, String errorMsg) {

            }
        });
    }

    private void initData() {
        mDevice = getIntent().getParcelableExtra("device_data");
        if (mDevice == null) {
            ToastUtils.show("内部错误！");
            finish();
        }
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_share_qr_code;
    }

}
