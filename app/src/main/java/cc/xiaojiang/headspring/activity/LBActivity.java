package cc.xiaojiang.headspring.activity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.constraint.Guideline;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import org.eclipse.paho.client.mqttv3.IMqttToken;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import cc.xiaojiang.headspring.R;
import cc.xiaojiang.headspring.base.BaseActivity;
import cc.xiaojiang.headspring.iotkit.KzzDataModel;
import cc.xiaojiang.headspring.iotkit.LbDataModel;
import cc.xiaojiang.headspring.utils.AP1Utils;
import cc.xiaojiang.headspring.utils.ScreenShotUtils;
import cc.xiaojiang.headspring.utils.ToastUtils;
import cc.xiaojiang.headspring.view.AP1View2;
import cc.xiaojiang.headspring.view.AP1View4;
import cc.xiaojiang.headspring.view.AP1View43;
import cc.xiaojiang.headspring.view.CommonTextView;
import cc.xiaojiang.headspring.widget.AP1TimingDialog;
import cc.xiaojiang.iotkit.bean.http.Device;
import cc.xiaojiang.iotkit.mqtt.IotKitActionCallback;
import cc.xiaojiang.iotkit.mqtt.IotKitConnectionManager;
import cc.xiaojiang.iotkit.mqtt.IotKitReceivedCallback;

public class LBActivity extends BaseActivity implements
        AP1View4.OnSeekBarChangeListener, IotKitReceivedCallback, AP1TimingDialog
        .OnTimeSelectedListener, AP1View43.OnSeekBarChangeListener {


    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.iv_pop_window)
    ImageView mIvPopWindow;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.frameLayout4)
    FrameLayout mFrameLayout4;
    @BindView(R.id.tv_lb_view1_filter)
    TextView mTvLbView1Filter;
    @BindView(R.id.view4)
    View mView4;
    @BindView(R.id.textView14)
    TextView mTextView14;
    @BindView(R.id.view_air_purifier_pm25)
    AP1View2 mViewAirPurifierPm25;
    @BindView(R.id.view_lb_gear)
    AP1View43 mViewLbGear;
    @BindView(R.id.tv_lb_auto)
    CommonTextView mTvLbAuto;
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

    private int mTimingShutdown;
    private int mUseTime;
    private int mPM205;

    private AP1TimingDialog mAP1TimingDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    @Override
    public void onStart(int gear) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        IotKitConnectionManager.getInstance().addDataCallback(this);
        IotKitConnectionManager.getInstance().queryStatus(mDevice.getProductKey(), mDevice
                .getDeviceId(), null);
    }

    @Override
    protected void onPause() {
        IotKitConnectionManager.getInstance().removeDataCallback(this);
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
        getWindow().getDecorView().setBackgroundColor(Color.TRANSPARENT);
        mViewLbGear.setOnSeekBarChangeListener(this);
        mAP1TimingDialog = new AP1TimingDialog();
        mAP1TimingDialog.setOnTimeSelectedListener(this);

    }

    private void initData() {
        mDevice = getIntent().getParcelableExtra("device_data");
        if (mDevice == null) {
            ToastUtils.show("内部错误！");
            finish();
        }
    }

    @OnClick({R.id.iv_pop_window, R.id.tv_lb_auto, R.id.tv_switch, R.id.tv_timing, R.id
            .iv_air_purifier_view4_minus, R.id.iv_air_purifier_view4_plus})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_pop_window:
                showPupWindow();
                break;
            case R.id.tv_lb_auto:
                mControlMode = mControlMode == 0 ? 1 : 0;
                HashMap<String, String> hashMap1 = new HashMap<>();
                hashMap1.put("ControlMode", String.valueOf(mControlMode));
                sendCmd(hashMap1);
                break;
            case R.id.tv_switch:
                HashMap<String, String> hashMap2 = new HashMap<>();
                hashMap2.put("Switch", String.valueOf(mSwitch == 0 ? 1 : 0));
                sendCmd(hashMap2);
                break;
            case R.id.tv_timing:
                mAP1TimingDialog.show(getSupportFragmentManager(), "");
                break;
            case R.id.iv_air_purifier_view4_minus:
                if (mControlGear > 0 && mControlGear <= 5) {
                    sendGear(mControlGear - 1);
                }
                break;
            case R.id.iv_air_purifier_view4_plus:
                if (mControlGear >= 0 && mControlGear < 5) {
                    sendGear(mControlGear + 1);
                }
                break;
        }
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

        popupWindow.showAsDropDown(mIvPopWindow, -popupWindow.getContentView().getMeasuredWidth()
                + 40, 20);
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
        IotKitConnectionManager.getInstance().sendCmd(mDevice, hashMap, new IotKitActionCallback() {
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
            mTimingShutdown = Integer.parseInt(paramsBean.getShutdownRemainingTime().getValue());
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
        mTvLbView1Filter.setText(String.format("%02d", (2000 - mUseTime) / 2000) + "%");
        if (mUseTime >= 2000) {
            showChangeFilter();
        }
    }

    private void refreshShutDown() {
        mTvLbShutDown.setText(getFormatTime(mTimingShutdown));
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
            mTvLbAuto.setText("自动");
        } else {
            mTvLbAuto.setText("手动");
        }
    }


    private void refreshSwitch() {
        if (mSwitch == 0) {
            mTvLbSwitch.setText("关机");
        } else {
            mTvLbSwitch.setText("开机");
        }
        // TODO: 2018/7/21 设置按钮
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
