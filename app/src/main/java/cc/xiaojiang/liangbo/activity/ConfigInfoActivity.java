package cc.xiaojiang.liangbo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import butterknife.BindView;
import butterknife.OnClick;
import cc.xiaojiang.liangbo.R;
import cc.xiaojiang.liangbo.base.BaseActivity;
import cc.xiaojiang.liangbo.utils.ActivityCollector;
import cc.xiaojiang.liangbo.utils.ToastUtils;
import cc.xiaojiang.iotkit.bean.http.ProductInfo;
import cc.xiaojiang.iotkit.http.IotKitDeviceManager;
import cc.xiaojiang.iotkit.http.IotKitHttpCallback;

public class ConfigInfoActivity extends BaseActivity {

    @BindView(R.id.btn_config_info_next)
    Button mBtnConfigInfoNext;

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
        getProductInfo(mProductKey);

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
        Intent intent = new Intent(this, WifiConfirmActivity.class);
        intent.putExtra("product_key", mProductKey);
        startActivity(intent);
    }
}
