package cc.xiaojiang.headspring.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import butterknife.BindView;
import butterknife.OnClick;
import cc.xiaojiang.headspring.R;
import cc.xiaojiang.headspring.base.BaseActivity;
import cc.xiaojiang.headspring.utils.ToastUtils;
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
        Intent intent = getIntent();
        if (intent == null) {
            ToastUtils.show("参数错误");
            finish();
            return;
        }
        mProductKey = intent.getStringExtra("product_key");
        getProductInfo(mProductKey);

    }

    private void getProductInfo(String productKey) {
        IotKitDeviceManager.getInstance().productInfo(productKey, new IotKitHttpCallback<ProductInfo>() {
            @Override
            public void onSuccess(ProductInfo data) {

            }

            @Override
            public void onError(String code, String errorMsg) {

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
