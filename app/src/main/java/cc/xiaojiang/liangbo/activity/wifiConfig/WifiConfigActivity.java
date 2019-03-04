package cc.xiaojiang.liangbo.activity.wifiConfig;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.OnClick;
import cc.xiaojiang.iotkit.wifi.IotKitWifiSetupManager;
import cc.xiaojiang.iotkit.wifi.WifiSetupCallback;
import cc.xiaojiang.iotkit.wifi.WifiSetupInfo;
import cc.xiaojiang.liangbo.Constants;
import cc.xiaojiang.liangbo.R;
import cc.xiaojiang.liangbo.base.BaseActivity;
import cc.xiaojiang.liangbo.utils.ActivityCollector;
import cc.xiaojiang.liangbo.utils.DbUtils;
import cc.xiaojiang.liangbo.utils.ToastUtils;
import cn.iwgang.simplifyspan.SimplifySpanBuild;
import cn.iwgang.simplifyspan.unit.SpecialTextUnit;

public class WifiConfigActivity extends BaseActivity {
    private static final int WIFI_CONFIG_TIMEOUT = 90 * 1000;
    @BindView(R.id.tv_wifi_connect_info)
    TextView mTvWifiConnectInfo;
    @BindView(R.id.iv_wifi_connect_failed)
    ImageView mIvWifiConnectFailed;
    @BindView(R.id.tv_wifi_connect_status)
    TextView mTvWifiConnectStatus;
    @BindView(R.id.tv_wifi_connect_percent)
    TextView mTvWifiConnectPercent;
    @BindView(R.id.btn_wifi_connect_retry)
    Button mBtnWifiConnectRetry;
    @BindView(R.id.iv_wifi_connect)
    ImageView mIvWifiConnect;

    private WifiSetupInfo mWifiSetupInfo;
    private ValueAnimator mValueAnimator;

    public static void actionStart(Activity activity, WifiSetupInfo wifiSetupInfo) {
        Intent intent = new Intent(activity, WifiConfigActivity.class);
        intent.putExtra(Constants.Intent.WIFI_CONFIG_INFO, wifiSetupInfo);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
        initData();
        startAnimator();
        startWifiSetup();

    }

    private void startAnimator() {
        mValueAnimator = ValueAnimator.ofInt(0, 100);
        mValueAnimator.setDuration(WIFI_CONFIG_TIMEOUT);
        mValueAnimator.setInterpolator(new DecelerateInterpolator());
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mTvWifiConnectPercent.setText(String.format("%s%%", animation.getAnimatedValue
                        ()));
            }
        });
        mValueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                Logger.e("onAnimationEnd");

            }

            @Override
            public void onAnimationCancel(Animator animation) {
//                isAnimCanceled = true;
                Logger.e("onAnimationCancel");
            }
        });
        mValueAnimator.start();
    }

    private void initData() {
        Intent intent = getIntent();
        mWifiSetupInfo = intent.getParcelableExtra(Constants.Intent.WIFI_CONFIG_INFO);
        if (mWifiSetupInfo == null) {
            ToastUtils.show("参数错误");
            finish();
        }
        setWifiConnecting();
    }

    private void setWifiConnecting() {
        setTitle("连接中");
        mTvWifiConnectStatus.setText(R.string.wifi_connecting);
        mIvWifiConnect.setSelected(false);
        mTvWifiConnectPercent.setVisibility(View.VISIBLE);
        mIvWifiConnectFailed.setVisibility(View.INVISIBLE);
        mBtnWifiConnectRetry.setVisibility(View.INVISIBLE);
        mTvWifiConnectInfo.setText(new SimplifySpanBuild("")
                .append(new SpecialTextUnit(" 网络连接中请确保").setTextSize(18).setTextColor
                        (getResources().getColor(R.color.text_32)))
                .append("\n")
                .append(new SpecialTextUnit("· 连接设备处于通电模式").setTextColor(getResources()
                        .getColor(R.color.text_64)))
                .append("\n")
                .append(new SpecialTextUnit("· 家庭WiFi网络信号良好").setTextColor(getResources()
                        .getColor(R.color.text_64)))
                .build());
    }

    @Override
    protected void onDestroy() {
        IotKitWifiSetupManager.getInstance().stopWifiSetup();
        mValueAnimator.cancel();
        ActivityCollector.removeActivity(this);
        super.onDestroy();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_wifi_connect;
    }

    private void startWifiSetup() {
        IotKitWifiSetupManager.getInstance().startWifiSetup2(this, mWifiSetupInfo,
                WIFI_CONFIG_TIMEOUT, new
                        WifiSetupCallback() {
                            @Override
                            public void connectSucceed() {
                                DbUtils.setWifiPwd(mWifiSetupInfo.getSsid(), mWifiSetupInfo
                                        .getPassword());
                                mTvWifiConnectStatus.setText("设备联网成功");
                            }

                            @Override
                            public void joinSucceed() {
                                ToastUtils.show("设备绑定成功");
                                ActivityCollector.finishAll();
                            }

                            @Override
                            public void joinFailed(String errorMsg) {
                                showConnectFailed(errorMsg);
                            }
                        });
    }

    private void showConnectFailed(String errorMsg) {
        setTitle("连接失败");
        mTvWifiConnectStatus.setText(errorMsg);
        mIvWifiConnect.setSelected(true);
        mTvWifiConnectPercent.setVisibility(View.INVISIBLE);
        mIvWifiConnectFailed.setVisibility(View.VISIBLE);
        mBtnWifiConnectRetry.setVisibility(View.VISIBLE);
        mTvWifiConnectInfo.setText(new SimplifySpanBuild("")
                .append(new SpecialTextUnit(" 请检查是否遇到以下问题").setTextSize(18)
                        .setTextColor(getResources().getColor(R.color.text_32)))
                .append("\n")
                .append(new SpecialTextUnit("· 设备与路由器的距离是否过远").setTextColor
                        (getResources().getColor(R.color.text_64)))
                .append("\n")
                .append(new SpecialTextUnit("· 设备是否处于待配网状态").setTextColor
                        (getResources().getColor(R.color.text_64)))
                .build());
    }


    @Override
    public void onBackPressed() {
        ToastUtils.show("请等待配网结束");
    }

    @OnClick(R.id.btn_wifi_connect_retry)
    public void onViewClicked() {
        ActivityCollector.finishAll();
        startToActivity(ProductListActivity.class);
    }
}
