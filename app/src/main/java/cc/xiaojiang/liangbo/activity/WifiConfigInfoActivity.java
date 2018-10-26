package cc.xiaojiang.liangbo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.OnClick;
import cc.xiaojiang.iotkit.bean.http.ProductInfo;
import cc.xiaojiang.iotkit.http.IotKitDeviceManager;
import cc.xiaojiang.iotkit.http.IotKitHttpCallback;
import cc.xiaojiang.iotkit.wifi.IotKitWifiSetupManager;
import cc.xiaojiang.liangbo.R;
import cc.xiaojiang.liangbo.base.BaseActivity;
import cc.xiaojiang.liangbo.iotkit.ProductKey;
import cc.xiaojiang.liangbo.utils.ActivityCollector;
import cc.xiaojiang.liangbo.utils.GlideApp;
import cc.xiaojiang.liangbo.utils.NetworkUtils;
import cc.xiaojiang.liangbo.utils.ToastUtils;
import cc.xiaojiang.liangbo.view.CommonTextView;

public class WifiConfigInfoActivity extends BaseActivity {

    @BindView(R.id.btn_config_info_next)
    Button mBtnConfigInfoNext;
    @BindView(R.id.tv_config_info)
    CommonTextView mTvConfigInfo;
    @BindView(R.id.iv_wifi_reset_guide)
    ImageView mIvWifiResetGuide;
    private String mProductKey;
    private ProductInfo mProductInfo;

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

        getProductInfo(mProductKey);

    }

    private void setGuide(int id) {
        GlideApp.with(this).load(id)
                .skipMemoryCache(true)
                .centerCrop().into(mIvWifiResetGuide);
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
                        if (data == null) {
                            return;
                        }
                        mProductInfo = data;
                        showInfo(data);
                    }

                    @Override
                    public void onError(String code, String errorMsg) {
                        ToastUtils.show(errorMsg);
                    }
                });
    }

    private void showInfo(ProductInfo productInfo) {
        String productKey = productInfo.getProductKey();
        if (ProductKey.LB.equals(productKey)) {
            mTvConfigInfo.setText(productInfo.getConfigNetworkCharacter());
            setGuide(R.drawable.ic_wifi_setup_guide_lb);
        } else if (ProductKey.KZZ.equals(productKey)) {
            mTvConfigInfo.setText(productInfo.getConfigNetworkCharacter());
            setGuide(R.drawable.ic_wifi_setup_guide_kzz);
        } else {
            mTvConfigInfo.setText("无效的设备");
        }
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
        intent.putExtra("product_info", mProductInfo);
        startActivity(intent);
    }

}
