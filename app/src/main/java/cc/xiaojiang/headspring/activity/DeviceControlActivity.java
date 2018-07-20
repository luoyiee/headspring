package cc.xiaojiang.headspring.activity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import org.eclipse.paho.client.mqttv3.IMqttToken;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import cc.xiaojiang.headspring.R;
import cc.xiaojiang.headspring.base.BaseActivity;
import cc.xiaojiang.headspring.iotkit.DeviceDataModel;
import cc.xiaojiang.headspring.utils.AP1Utils;
import cc.xiaojiang.headspring.utils.ScreenShotUtils;
import cc.xiaojiang.headspring.utils.ToastUtils;
import cc.xiaojiang.headspring.view.AP1View2;
import cc.xiaojiang.headspring.view.AP1View4;
import cc.xiaojiang.headspring.view.CommonTextView;
import cc.xiaojiang.headspring.widget.AP1TimingDialog;
import cc.xiaojiang.iotkit.bean.http.Device;
import cc.xiaojiang.iotkit.mqtt.IotKitActionCallback;
import cc.xiaojiang.iotkit.mqtt.IotKitConnectionManager;
import cc.xiaojiang.iotkit.mqtt.IotKitReceivedCallback;

public class DeviceControlActivity extends BaseActivity implements
        AP1View4.OnSeekBarChangeListener, IotKitReceivedCallback {
    @BindView(R.id.iv_pop_window)
    ImageView mIvPopWindow;
    @BindView(R.id.ic_air_purifier_wifi_off)
    CommonTextView mIcAirPurifierWifiOff;
    @BindView(R.id.tv_air_purifier_view1_timing)
    TextView mTvAirPurifierView1Timing;
    @BindView(R.id.tv_air_purifier_view3_temp)
    TextView mTvAirPurifierView3Temp;
    @BindView(R.id.tv_air_purifier_view3_humidity)
    TextView mTvAirPurifierView3Humidity;
    @BindView(R.id.view_air_purifier_pm25)
    AP1View2 mViewAirPurifierPm25;
    @BindView(R.id.view_air_purifier_gear)
    AP1View4 mViewAirPurifierGear;
    @BindView(R.id.textView16)
    TextView mTextView16;
    @BindView(R.id.textView17)
    TextView mTextView17;
    @BindView(R.id.textView14)
    TextView mTextView14;
    @BindView(R.id.tv_auto)
    CommonTextView mTvAuto;
    @BindView(R.id.tv_switch)
    CommonTextView mTvSwitch;
    @BindView(R.id.tv_timing)
    CommonTextView mTvTiming;

    private Device mDevice;
    private IotKitReceivedCallback mIotKitReceivedCallback;

    private int mControlGear;
    private int mSwitch;
    private int mControlMode;

    private int mTimingShutdown;
    private int mUseTime;
    private int mPM205;
//    private int mTempture;
//    private int mHumidity;

    private int mAirQuality;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_device_control;
    }

    private void initView() {
        getWindow().getDecorView().setBackgroundColor(Color.TRANSPARENT);
        mViewAirPurifierGear.setOnSeekBarChangeListener(this);
    }

    private void initData() {
        mDevice = getIntent().getParcelableExtra("device_data");
        if (mDevice == null) {
            ToastUtils.show("内部错误！");
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        IotKitConnectionManager.getInstance().addDataCallback(this);
        IotKitConnectionManager.getInstance().queryStatus(mDevice.getProductKey(), mDevice
                .getDeviceId(), new IotKitActionCallback() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {

            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

            }
        });
    }

    @Override
    protected void onPause() {
        IotKitConnectionManager.getInstance().removeDataCallback(this);
        super.onPause();
    }

    @OnClick({R.id.iv_pop_window, R.id.tv_auto, R.id.tv_switch, R.id.tv_timing, R.id
            .iv_air_purifier_view4_minus, R.id.iv_air_purifier_view4_plus})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_pop_window:
                showPupWindow();
                break;
            case R.id.tv_auto:
                sendMode(mControlMode);
                break;
            case R.id.tv_switch:
                sendSwitch(mSwitch);
                break;
            case R.id.tv_timing:
                doTiming();
                break;
            case R.id.iv_air_purifier_view4_minus:
                gearMinus();
                break;
            case R.id.iv_air_purifier_view4_plus:
                gearPlus();
                break;
        }
    }

    private void sendSwitch(int aSwitch) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("Switch", String.valueOf(aSwitch == 0 ? 1 : 0));
        sendCmd(hashMap);
    }

    private void sendMode(int controlMode) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("ControlMode", String.valueOf(controlMode == 0 ? 1 : 0));
        sendCmd(hashMap);
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

    private void gearPlus() {
        if (mControlGear >= 0 && mControlGear < 5) {
            sendGear(mControlGear + 1);
        }
    }

    private void doTiming() {
        AP1TimingDialog dialog = new AP1TimingDialog();
        dialog.setOnTimeSelectedListener(new AP1TimingDialog.OnTimeSelectedListener() {
            @Override
            public void onTimeSelected(int hour) {
                if (hour == 4) {
                    hour = 3;
                }
                if (hour == 8) {
                    hour = 4;
                }
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("TimingShutdown", hour + "");
                sendCmd(hashMap);
            }
        });
        dialog.show(getSupportFragmentManager(), "");
    }

    private void gearMinus() {
        if (mControlGear > 0 && mControlGear <= 5) {
            sendGear(mControlGear - 1);
        }
    }

    @Override
    public void onStart(int gear) {

    }

    @Override
    public void onChange(int gear) {

    }

    @Override
    public void onStop(int gear) {
        sendGear(gear);
    }

    private void sendGear(int gear) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("ControlGear", String.valueOf(gear));
        sendCmd(hashMap);
    }

    private void sendCmd(HashMap<String, String> hashMap) {
        IotKitConnectionManager.getInstance().sendCmd(mDevice.getProductKey(), mDevice
                .getDeviceId(), hashMap, new IotKitActionCallback() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {

            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                ToastUtils.show("发送失败，请重试");
            }
        });
    }

    @Override
    public void messageArrived(String deviceId, String onlineStatus, String data) {
        if (!deviceId.equals(mDevice.getDeviceId())) {
            Logger.e("error device!");
            return;
        }
        DeviceDataModel model = new Gson().fromJson(data, DeviceDataModel.class);
        DeviceDataModel.ParamsBean paramsBean = model.getParams();
        if (paramsBean == null) {
            Logger.e("error getParams!");
            return;
        }
        setView(paramsBean);
    }

    private void setView(DeviceDataModel.ParamsBean paramsBean) {
        mSwitch = Integer.parseInt(paramsBean.getSwitch().getValue());
        mControlMode = Integer.parseInt(paramsBean.getControlMode().getValue());
        mControlGear = Integer.parseInt(paramsBean.getControlGear().getValue());
        mTimingShutdown = Integer.parseInt(paramsBean.getTimingShutdown().getValue());
        mUseTime = Integer.parseInt(paramsBean.getUseTime().getValue());
        mPM205 = Integer.parseInt(paramsBean.getPM205().getValue());
//        mTempture = Integer.parseInt(paramsBean.getTempture().getValue());
//        mHumidity = Integer.parseInt(paramsBean.getHumidity().getValue());
        setBackground(mPM205);
        //绘制界面
        mTvAirPurifierView1Timing.setText(mTimingShutdown + "h");
//        mTvAirPurifierView1Strainer.setText(mUseTime + "h");
//        mTvAirPurifierView3Temp.setText(mTempture + "°C");
//        mTvAirPurifierView3Humidity.setText(mHumidity + "%");
        mViewAirPurifierPm25.setValue(mPM205);
        mViewAirPurifierGear.setGear(mControlGear);

        if (mControlMode == 0) {
            mTvAuto.setText("自动");
        } else {
            mTvAuto.setText("手动");
        }

        if (mSwitch == 0) {
            mTvSwitch.setText("关机");
        } else {
            mTvSwitch.setText("开机");
        }
        if (mUseTime >= 2000) {
            showChangeFilter();
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

    private void setBackground(int PM205) {
        String rate = AP1Utils.getRate(this, PM205);
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

    @Override
    public boolean filter(String deviceId) {
        return !deviceId.equals(mDevice.getDeviceId());
    }


}
