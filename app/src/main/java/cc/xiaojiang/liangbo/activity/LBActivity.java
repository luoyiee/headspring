package cc.xiaojiang.liangbo.activity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.constraint.Guideline;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.app.hubert.guide.NewbieGuide;
import com.app.hubert.guide.core.Controller;
import com.app.hubert.guide.model.GuidePage;
import com.app.hubert.guide.model.HighlightOptions;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import org.eclipse.paho.client.mqttv3.IMqttToken;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import cc.xiaojiang.iotkit.bean.http.Device;
import cc.xiaojiang.iotkit.mqtt.IotKitActionCallback;
import cc.xiaojiang.iotkit.mqtt.IotKitMqttManager;
import cc.xiaojiang.iotkit.mqtt.IotKitReceivedCallback;
import cc.xiaojiang.liangbo.R;
import cc.xiaojiang.liangbo.base.BaseActivity;
import cc.xiaojiang.liangbo.iotkit.IotKitUtils;
import cc.xiaojiang.liangbo.iotkit.LbDataModel;
import cc.xiaojiang.liangbo.utils.AP1Utils;
import cc.xiaojiang.liangbo.utils.ScreenShotUtils;
import cc.xiaojiang.liangbo.utils.ScreenUtils;
import cc.xiaojiang.liangbo.utils.ToastUtils;
import cc.xiaojiang.liangbo.view.AP1View2;
import cc.xiaojiang.liangbo.view.AP1View4;
import cc.xiaojiang.liangbo.view.AP1View43;
import cc.xiaojiang.liangbo.view.CommonTextView;
import cc.xiaojiang.liangbo.widget.AP1TimingDialog;

public class LBActivity extends BaseActivity implements
        AP1View4.OnSeekBarChangeListener, IotKitReceivedCallback, AP1TimingDialog
        .OnTimeSelectedListener, AP1View43.OnSeekBarChangeListener {


    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.ll_kzz_device_status)
    FrameLayout mFrameLayout4;
    @BindView(R.id.tv_lb_view1_filter)
    TextView mTvLbView1Filter;
    @BindView(R.id.view_status_divider)
    View mView4;
    @BindView(R.id.tv_view1_timing_label)
    TextView mTextView14;
    @BindView(R.id.view_air_purifier_pm25)
    AP1View2 mViewAirPurifierPm25;
    @BindView(R.id.view_lb_gear)
    AP1View43 mViewLbGear;
    @BindView(R.id.tv_lb_mode)
    CommonTextView mTvLbMode;
    @BindView(R.id.tv_lb_switch)
    CommonTextView mTvLbSwitch;
    @BindView(R.id.tv_lb_timing)
    CommonTextView mTvLbTiming;
    @BindView(R.id.guideline2)
    Guideline mGuideline2;
    @BindView(R.id.iv_lb_view4_minus)
    ImageView mIvLbView4Minus;
    @BindView(R.id.iv_lb_view4_plus)
    ImageView mIvLbView4Plus;
    @BindView(R.id.tv_lb_shut_down)
    TextView mTvLbShutDown;
    @BindView(R.id.textView36)
    TextView mTextView36;
    private Device mDevice;

    private int mControlGear;
    private int mSwitch;
    private int mControlMode;

    private int mShutdownRemainingTime;
    private int mUseTime;
    private int mPM205;
    private Controller mGuidePage;

    private AP1TimingDialog mAP1TimingDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        initView();
        initGuidePage();

    }

    private void initGuidePage() {
        HighlightOptions options = new HighlightOptions.Builder()
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mTvLbSwitch.callOnClick();
                    }
                })
                .build();
        GuidePage page = GuidePage.newInstance().addHighLightWithOptions(mTvLbSwitch, options);
        mGuidePage = NewbieGuide.with(this)
                .setLabel("guide1")
                .alwaysShow(true)
                .addGuidePage(page
                        .setEverywhereCancelable(false)
                        .addHighLight(mTvLbSwitch)
                        .setLayoutRes(R.layout.view_guide)).build();
    }

    @Override
    public void onStart(int gear) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        IotKitMqttManager.getInstance().addDataCallback(this);
        IotKitMqttManager.getInstance().queryStatus(mDevice.getProductKey(), mDevice
                .getDeviceId(), null);
    }

    @Override
    protected void onPause() {
        IotKitMqttManager.getInstance().removeDataCallback(this);
        super.onPause();
    }

    @Override
    public void onStop(int gear) {
        sendGear(gear);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_lb;
    }

    private void initView() {
        getWindow().getDecorView().setBackgroundColor(ContextCompat.getColor(this, R.color
                .air_purifier_background_off));
        mViewLbGear.setOnSeekBarChangeListener(this);
        mAP1TimingDialog = new AP1TimingDialog();
        mAP1TimingDialog.setOnTimeSelectedListener(this);

    }

    private void initData() {
        mDevice = getIntent().getParcelableExtra("device_data");
        if (mDevice == null) {
            ToastUtils.show("参数错误！");
            finish();
        }
        setTitle(IotKitUtils.getDeviceName(mDevice));
    }

    @OnClick({R.id.tv_lb_mode, R.id.tv_lb_switch, R.id.tv_lb_timing, R.id
            .iv_lb_view4_minus, R.id.iv_lb_view4_plus})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.tv_lb_mode:
                mControlMode = mControlMode == 0 ? 1 : 0;
                HashMap<String, String> hashMap1 = new HashMap<>();
                hashMap1.put("ControlMode", String.valueOf(mControlMode));
                sendCmd(hashMap1);
                break;
            case R.id.tv_lb_switch:
                HashMap<String, String> hashMap2 = new HashMap<>();
                hashMap2.put("Switch", String.valueOf(mSwitch == 0 ? 1 : 0));
                sendCmd(hashMap2);
                break;
            case R.id.tv_lb_timing:
                mAP1TimingDialog.show(getSupportFragmentManager(), "");
                break;
            case R.id.iv_lb_view4_minus:
                if (mControlGear > 0 && mControlGear <= 3) {
                    sendGear(mControlGear - 1);
                }
                break;
            case R.id.iv_lb_view4_plus:
                if (mControlGear >= 0 && mControlGear < 3) {
                    sendGear(mControlGear + 1);
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_more, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_more) {
            showPupWindow();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showPupWindow() {
        final View contentView = getLayoutInflater().inflate(R.layout.device_pop_window, null);
        final PopupWindow popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams
                .WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        contentView.findViewById(R.id.tv_popup_history_data).setOnClickListener(v -> {
            HistoryDataActivity.actionStart(this, mDevice);

            popupWindow.dismiss();
        });
        popupWindow.getContentView().measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec
                .UNSPECIFIED);
        contentView.findViewById(R.id.tv_popup_time_remain).setOnClickListener(v -> {
            FilterTimeRemainActivity.actionStart(this, mUseTime);
            popupWindow.dismiss();

        });
        contentView.findViewById(R.id.tv_popup_share_data).setOnClickListener(v -> {
            ScreenShotUtils.share(this);
            popupWindow.dismiss();
        });

        popupWindow.showAsDropDown(mToolbar, ScreenUtils.getScreenWidth(this) - popupWindow
                .getContentView().getMeasuredWidth(), 0);
    }


    @Override
    public void onChange(int gear) {

    }

    private void sendGear(int gear) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("ControlGear", String.valueOf(gear));
        sendCmd(hashMap);
    }

    private void sendCmd(HashMap<String, String> hashMap) {
        IotKitMqttManager.getInstance().sendCmd(mDevice, hashMap, new IotKitActionCallback() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {


            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

            }
        });
    }

    @Override
    public void messageArrived(String deviceId, String onlineStatus, String data) {
        if (!deviceId.equals(mDevice.getDeviceId())) {
            Logger.e("error device!");
            return;
        }
        LbDataModel model = new Gson().fromJson(data, LbDataModel.class);
        LbDataModel.ParamsBean paramsBean = model.getParams();
        if (paramsBean == null) {
            Logger.e("error getParams!");
            return;
        }
        showData(paramsBean);
    }

    @Override
    public boolean filter(String deviceId) {
        return !deviceId.equals(mDevice.getDeviceId());
    }


    private void showData(LbDataModel.ParamsBean paramsBean) {
        if (paramsBean.getSwitch() != null) {
            mSwitch = Integer.parseInt(paramsBean.getSwitch().getValue());
        }
        if (paramsBean.getControlMode() != null) {
            mControlMode = Integer.parseInt(paramsBean.getControlMode().getValue());
        }
        if (paramsBean.getControlGear() != null) {
            mControlGear = Integer.parseInt(paramsBean.getControlGear().getValue());
        }
        if (paramsBean.getShutdownRemainingTime() != null) {
            mShutdownRemainingTime = Integer.parseInt(paramsBean.getShutdownRemainingTime()
                    .getValue());
        }
        if (paramsBean.getUseTime() != null) {
            mUseTime = Integer.parseInt(paramsBean.getUseTime().getValue());
        }
        if (paramsBean.getPM205() != null) {
            mPM205 = Integer.parseInt(paramsBean.getPM205().getValue());
        }
        refreshAll();
    }

    private void refreshAll() {
        refreshBackground();
        refreshSwitch();
        refreshMode();
        refreshGear();
        refreshShutDown();
        refreshFilter();
        refreshPm25();
    }


    private void refreshPm25() {
        mViewAirPurifierPm25.setValue(mPM205);
    }

    private void refreshFilter() {
        int percent = (int) (100f * (2000 - mUseTime) / 2000);
        mTvLbView1Filter.setText(String.format("%02d", percent) + "%");
        if (mUseTime >= 2000) {
            showChangeFilter();
        }
    }

    private void refreshShutDown() {
        mTvLbShutDown.setText(getFormatTime(mShutdownRemainingTime));
        if (mShutdownRemainingTime == 0) {
            mTvLbTiming.setIconNormal(getResources().getDrawable(R.drawable
                    .ic_air_purifier_timing_off));
        } else {
            mTvLbTiming.setIconNormal(getResources().getDrawable(R.drawable
                    .ic_air_purifier_timing_on));
        }
    }

    public String getFormatTime(int timingShutdown) {
        String hour = String.format("%02d", timingShutdown / 60);
        String minute = String.format("%02d", timingShutdown % 60);
        return hour + ":" + minute;
    }


    private void refreshGear() {
        mViewLbGear.setGear(mControlGear);
    }

    private void refreshMode() {
        if (mControlMode == 0) {
            mTvLbMode.setText("自动");
            mTvLbMode.setIconNormal(getResources().getDrawable(R.drawable
                    .ic_air_purifier_mode_auto));
        } else {
            mTvLbMode.setText("手动");
            mTvLbMode.setIconNormal(getResources().getDrawable(R.drawable
                    .ic_air_purifier_mode_manual));
        }
    }


    private void refreshSwitch() {
        if (mSwitch == 0) {
            mTvLbSwitch.setText("关机");
            mTvLbSwitch.setIconNormal(getResources().getDrawable(R.drawable
                    .ic_air_purifier_switch_on));
            mGuidePage.show();

        } else {
            mTvLbSwitch.setText("开机");
            mTvLbSwitch.setIconNormal(getResources().getDrawable(R.drawable
                    .ic_air_purifier_switch_off));
            mGuidePage.remove();
        }
    }

    private void refreshBackground() {
        String rate = AP1Utils.getRate(this, mPM205);
        if (mSwitch == 1) {
            if (rate.equals(getString(R.string.air_purifier_rate_excellent))) {
                getWindow().getDecorView().setBackgroundColor(ContextCompat.getColor(this, R
                        .color.air_purifier_background_excellent));
            } else if (rate.equals(getString(R.string.air_purifier_rate_good))) {
                getWindow().getDecorView().setBackgroundColor(ContextCompat.getColor(this, R
                        .color.air_purifier_background_good));
            } else if (rate.equals(getString(R.string.air_purifier_rate_poor))) {
                getWindow().getDecorView().setBackgroundColor(ContextCompat.getColor(this, R
                        .color.air_purifier_background_poor));
            }
        } else {
            getWindow().getDecorView().setBackgroundColor(ContextCompat.getColor(this, R.color
                    .air_purifier_background_off));
        }
    }

    private void showChangeFilter() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("更换滤芯");
        builder.setMessage("滤网使用时间到了，请您及时更换滤芯!");
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

    }


    @Override
    public void onTimeSelected(int hour) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("TimingShutdown", hour + "");
        sendCmd(hashMap);
    }
}
