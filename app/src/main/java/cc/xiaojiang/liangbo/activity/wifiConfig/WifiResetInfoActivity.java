package cc.xiaojiang.liangbo.activity.wifiConfig;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.OnClick;
import cc.xiaojiang.iotkit.bean.http.ProductInfo;
import cc.xiaojiang.iotkit.http.IotKitDeviceManager;
import cc.xiaojiang.iotkit.http.IotKitHttpCallback;
import cc.xiaojiang.iotkit.wifi.WifiSetupInfo;
import cc.xiaojiang.liangbo.Constants;
import cc.xiaojiang.liangbo.R;
import cc.xiaojiang.liangbo.base.BaseActivity;
import cc.xiaojiang.liangbo.iotkit.ProductKey;
import cc.xiaojiang.liangbo.utils.ActivityCollector;
import cc.xiaojiang.liangbo.utils.GlideApp;
import cc.xiaojiang.liangbo.utils.ToastUtils;
import cc.xiaojiang.liangbo.view.CommonTextView;

public class WifiResetInfoActivity extends BaseActivity {

    @BindView(R.id.btn_config_info_next)
    Button mBtnConfigInfoNext;
    @BindView(R.id.tv_config_info)
    CommonTextView mTvConfigInfo;
    @BindView(R.id.iv_wifi_reset_guide)
    ImageView mIvWifiResetGuide;
    @BindView(R.id.tv_confirm_operation)
    TextView mTvConfirmOperation;
    private String mProductKey;
    private String mSsid;
    private String mPassword;
    private ProductInfo mProductInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
        initData();


    }

    private void initData() {
        Intent intent = getIntent();
        mSsid = intent.getStringExtra(Constants.Intent.SSID);
        mPassword = intent.getStringExtra(Constants.Intent.PASSWORD);
        mProductKey = intent.getStringExtra(Constants.Intent.PRODUCT_KEY);
        getProductInfo(mProductKey);
    }

    public static void actionStart(Activity activity, String ssid, String password, String
            productKey) {
        Intent intent = new Intent(activity, WifiResetInfoActivity.class);
        intent.putExtra(Constants.Intent.SSID, ssid);
        intent.putExtra(Constants.Intent.PASSWORD, password);
        intent.putExtra(Constants.Intent.PRODUCT_KEY, productKey);
        activity.startActivity(intent);

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

    @OnClick({R.id.btn_config_info_next, R.id.tv_confirm_operation})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_config_info_next:
                if (mProductInfo == null) {
                    ToastUtils.show("不能获取产品详情");
                    return;
                }
                WifiSetupInfo wifiSetupInfo = new WifiSetupInfo();
                wifiSetupInfo.setProductKey(mProductInfo.getProductKey());
                wifiSetupInfo.setPassword(mPassword);
                wifiSetupInfo.setSsid(mSsid);
                wifiSetupInfo.setWifiVendor(mProductInfo.getModuleVendor());
                wifiSetupInfo.setConfigType(mProductInfo.getConfigNetworkType());
                WifiConfigActivity.actionStart(this, wifiSetupInfo);
                break;
            case R.id.tv_confirm_operation:
                mTvConfirmOperation.setSelected(!mTvConfirmOperation.isSelected());
                Logger.e(mTvConfirmOperation.isSelected()+"");
                mBtnConfigInfoNext.setEnabled(mTvConfirmOperation.isSelected());
                break;
        }
    }
}
