package cc.xiaojiang.liangbo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import butterknife.BindView;
import butterknife.OnClick;
import cc.xiaojiang.iotkit.bean.http.ProductInfo;
import cc.xiaojiang.iotkit.http.IotKitDeviceManager;
import cc.xiaojiang.iotkit.http.IotKitHttpCallback;
import cc.xiaojiang.iotkit.wifi.IotKitWifiSetupManager;
import cc.xiaojiang.liangbo.R;
import cc.xiaojiang.liangbo.base.BaseActivity;
import cc.xiaojiang.liangbo.utils.ActivityCollector;
import cc.xiaojiang.liangbo.utils.NetworkUtils;
import cc.xiaojiang.liangbo.utils.ToastUtils;
import cc.xiaojiang.liangbo.view.CommonTextView;

public class ConfigInfoActivity extends BaseActivity {

    @BindView(R.id.btn_config_info_next)
    Button mBtnConfigInfoNext;
    @BindView(R.id.tv_config_info)
    CommonTextView mTvConfigInfo;

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
        showInfo(mProductKey);
        getProductInfo(mProductKey);

    }

    private void showInfo(String productKey) {
        if ("jbb600".equals(productKey)) {
            mTvConfigInfo.setText(getString(R.string.wifi_config_explain_lb));
        } else {
            mTvConfigInfo.setText(getString(R.string.wifi_config_explain_kzz));
        }
    }

    @Override
    protected void onDestroy() {
        ActivityCollector.removeActivity(this);
        super.onDestroy();
    }

    private void getProductInfo(String productKey) {
        IotKitDeviceManager.getInstance().productInfo(productKey, new
                IotKitHttpCallback<ProductInfo>() {
                    @Override
                    public void onSuccess(ProductInfo data) {

                    }

                    @Override
                    public void onError(String code, String errorMsg) {
                        ToastUtils.show(errorMsg);
                    }
                });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_config_info;
    }


    @OnClick(R.id.btn_config_info_next)
    public void onViewClicked() {
        if (!NetworkUtils.isWifiConnected(this)) {
            ToastUtils.show("请连接wifi");
            return;
        }
        if (!IotKitWifiSetupManager.getInstance().is24GWifi(this)) {
            ToastUtils.show("暂不支持5G WiFi");
            return;
        }
        Intent intent = new Intent(this, WifiConfirmActivity.class);
        intent.putExtra("product_key", mProductKey);
        startActivity(intent);
    }

}
