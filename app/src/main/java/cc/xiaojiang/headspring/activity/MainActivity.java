package cc.xiaojiang.headspring.activity;

import android.os.Bundle;
import android.view.View;

import butterknife.BindView;
import butterknife.OnClick;
import cc.xiaojiang.headspring.R;
import cc.xiaojiang.headspring.base.BaseActivity;
import cc.xiaojiang.headspring.utils.ToastUtils;
import cc.xiaojiang.headspring.view.CommonTextView;

public class MainActivity extends BaseActivity {
    @BindView(R.id.ctv_chain)
    CommonTextView mBtnChain;
    @BindView(R.id.ctv_map)
    CommonTextView mBtnMap;
    @BindView(R.id.ctv_device)
    CommonTextView mBtnDevice;
    @BindView(R.id.ctv_shop)
    CommonTextView mBtnShop;
    @BindView(R.id.ctv_personal)
    CommonTextView mBtnPersonal;

//    @BindView(R.id.tv_outdoor_pm)
//    TextView mTvOutdoorPm;
//    @BindView(R.id.btn_bind)
//    Button btnBind;
//    @BindView(R.id.btn_device_list)
//    Button btnDeviceList;
//    @BindView(R.id.guideline)
//    Guideline guideline;
//    @BindView(R.id.textView9)
//    TextView textView9;
//    @BindView(R.id.textView10)
//    TextView textView10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //        mTvOutdoorPm.setText(new SpanUtils()
//                .append("50").setFontSize(28,true).append("ug/m").append("3").setSuperscript()
//                .appendLine()
//                .appendLine("室外PM2.5")
//                .create());
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @OnClick({R.id.ctv_chain, R.id.ctv_map, R.id.ctv_device, R.id.ctv_shop, R.id.ctv_personal})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ctv_chain:
                ToastUtils.show("区块链");
//                IotKitDeviceManager.getInstance().deviceBind("bff503",
//                        "xBcqwpFWINOUzjO21X3E", new
//                                IotKitCallBack() {
//                                    @Override
//                                    public void onSuccess(String response) {
//
//                                    }
//
//                                    @Override
//                                    public void onError(int code, String errorMsg) {
//
//                                    }
//                                });
                break;
            case R.id.ctv_map:
                startToActivity(AirMapActivity.class);
                break;
            case R.id.ctv_device:
                startToActivity(DeviceListActivity.class);
                break;
            case R.id.ctv_shop:
                startToActivity(ShopActivity.class);
                break;
            case R.id.ctv_personal:
                startToActivity(PersonalCenterActivity.class);
                break;
        }
    }


//    @OnClick({R.id.btn_bind, R.id.btn_device_list})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.btn_bind:
//                IotKitDeviceManager.getInstance().deviceBind("bff503", "bopVpXFcyVbkYBhrhmHK", new
//                        IotKitCallBack() {
//                            @Override
//                            public void onSuccess(String response) {
//                                ToastUtils.show("绑定成功");
//
//                            }
//
//                            @Override
//                            public void onError(int code, String errorMsg) {
//
//                            }
//                        });
//                break;
//            case R.id.btn_device_list:
//                IotKitDeviceManager.getInstance().deviceList(new IotKitCallBack() {
//                    @Override
//                    public void onSuccess(String response) {
//                        startToActivity(DeviceListActivity.class);
//
//                    }
//
//                    @Override
//                    public void onError(int code, String errorMsg) {
//
//                    }
//                });
//                break;
//        }
//    }
}
