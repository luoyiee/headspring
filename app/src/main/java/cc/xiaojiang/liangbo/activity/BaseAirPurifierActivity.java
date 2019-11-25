package cc.xiaojiang.liangbo.activity;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.app.hubert.guide.NewbieGuide;
import com.app.hubert.guide.core.Controller;
import com.app.hubert.guide.model.GuidePage;
import com.app.hubert.guide.model.HighlightOptions;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;
import cc.xiaojiang.iotkit.bean.http.Device;
import cc.xiaojiang.iotkit.mqtt.IotKitActionCallback;
import cc.xiaojiang.iotkit.mqtt.IotKitMqttManager;
import cc.xiaojiang.iotkit.mqtt.IotKitReceivedCallback;
import cc.xiaojiang.liangbo.R;
import cc.xiaojiang.liangbo.base.BaseActivity;
import cc.xiaojiang.liangbo.iotkit.IotKitUtils;
import cc.xiaojiang.liangbo.iotkit.KzzDataModel;
import cc.xiaojiang.liangbo.iotkit.ProductKey;
import cc.xiaojiang.liangbo.model.event.ShareBitmapEvent;
import cc.xiaojiang.liangbo.utils.AP1Utils;
import cc.xiaojiang.liangbo.utils.ScreenShotUtils;
import cc.xiaojiang.liangbo.utils.ScreenUtils;
import cc.xiaojiang.liangbo.utils.ToastUtils;
import cc.xiaojiang.liangbo.view.AP1View2;
import cc.xiaojiang.liangbo.view.AP1View4;
import cc.xiaojiang.liangbo.view.CommonTextView;
import cc.xiaojiang.liangbo.widget.AP1TimingDialog;

import static cc.xiaojiang.liangbo.activity.FilterTimeRemainActivity.FLITER_MAX_VALUE;

public class BaseAirPurifierActivity extends BaseActivity implements
        AP1View4.OnSeekBarChangeListener, IotKitReceivedCallback, AP1TimingDialog
        .OnTimeSelectedListener {
    @BindView(R.id.iv_status_kzz2_24g)
    ImageView ivStatusKzz224g;
    @BindView(R.id.tv_status_kzz2_timing)
    TextView tvStatusKzz2Timing;
    @BindView(R.id.tv_status_kzz2_filter)
    TextView tvStatusKzz2Filter;
    @BindView(R.id.iv_status_kzz2_battery)
    ImageView ivStatusKzz2Battery;
    @BindView(R.id.tv_status_lb_timing)
    TextView tvStatusLbTiming;
    @BindView(R.id.tv_status_lb_filter)
    TextView tvStatusLbFilter;
    @BindView(R.id.ic_status_dy_wifi)
    ImageView icStatusDyWifi;
    @BindView(R.id.tv_status_dy_timing)
    TextView tvStatusDyTiming;
    @BindView(R.id.tv_status_dy_filter)
    TextView tvStatusDyFilter;
    @BindView(R.id.view_air_purifier_pm25)
    AP1View2 viewAirPurifierPm25;
    @BindView(R.id.iv_air_purifier_view4_minus)
    ImageView ivAirPurifierView4Minus;
    @BindView(R.id.view_air_purifier_gear)
    AP1View4 viewAirPurifierGear;
    @BindView(R.id.iv_air_purifier_view4_plus)
    ImageView ivAirPurifierView4Plus;
    @BindView(R.id.tv_lb_mode)
    CommonTextView tvLbMode;
    @BindView(R.id.tv_switch)
    CommonTextView tvSwitch;
    @BindView(R.id.tv_timing)
    CommonTextView tvTiming;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.ll_kzz2g_status)
    LinearLayout llKzz2gStatus;
    @BindView(R.id.ll_lb_status)
    LinearLayout llLbStatus;
    @BindView(R.id.ll_dy_status)
    LinearLayout llDyStatus;
    @BindView(R.id.tv_temperature)
    TextView tvTemperature;
    @BindView(R.id.tv_humidity)
    TextView tvHumidity;
    @BindView(R.id.ll_air_purifier_pm25)
    LinearLayout llAirPurifierPm25;
    private Device mDevice;

    private int mControlGear;
    private int mSwitch;
    private int mControlMode;

    private int mShutdownRemainingTime;
    private int mUseTime;
    private int mPM205;
    private int m24GState;
    private int mBattery;
    private int mTemperature;
    private int mHumidity;
    private Controller mGuidePage;
    private AP1TimingDialog mAP1TimingDialog;

    private boolean isShowGuidePage = false;
    private boolean isShowFilter = false;


    private Timer mTimer = new Timer();

    private void openRealReporting(int isOpen) {
        HashMap<String, String> hashMap1 = new HashMap<>();
        hashMap1.put("RealReportingSwitch", String.valueOf(isOpen));
        sendCmd(hashMap1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        initGuidePage();

    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_kzz;
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
            FilterTimeRemainActivity.actionStart(this, mUseTime, mDevice);
            popupWindow.dismiss();

        });
        contentView.findViewById(R.id.tv_popup_share_data).setOnClickListener(v -> {
            Bitmap bitmap = ScreenShotUtils.screenShot(this, 0);
            EventBus.getDefault().postSticky(new ShareBitmapEvent(bitmap));
            startToActivity(ShareActivity.class);
            popupWindow.dismiss();
        });

        popupWindow.showAsDropDown(toolbar, ScreenUtils.getScreenWidth(this) - popupWindow
                .getContentView().getMeasuredWidth(), 0);
    }

    private void init() {
        mDevice = getIntent().getParcelableExtra("device_data");
        if (mDevice == null) {
            ToastUtils.show("内部错误！");
            finish();
        }
        setTitle(IotKitUtils.getDeviceName(mDevice));
        switch (mDevice.getProductKey()) {
            case ProductKey.DY:
                viewAirPurifierGear.setGearCount(6);
                llDyStatus.setVisibility(View.VISIBLE);
                llAirPurifierPm25.setVisibility(View.VISIBLE);
                break;
            case ProductKey.LB:
                viewAirPurifierGear.setGearCount(4);
                llLbStatus.setVisibility(View.VISIBLE);
                llAirPurifierPm25.setVisibility(View.INVISIBLE);
                break;
            case ProductKey.KZZ2G:
                viewAirPurifierGear.setGearCount(6);
                llKzz2gStatus.setVisibility(View.VISIBLE);
                llAirPurifierPm25.setVisibility(View.VISIBLE);
                break;
            default:
                ToastUtils.show("暂不支持该设备！");
        }
        getWindow().getDecorView().setBackgroundColor(ContextCompat.getColor(this, R
                .color.air_purifier_background_off));
        viewAirPurifierGear.setOnSeekBarChangeListener(this);
        mAP1TimingDialog = new AP1TimingDialog();
        mAP1TimingDialog.setOnTimeSelectedListener(this);

    }

    private void initGuidePage() {
        HighlightOptions options = new HighlightOptions.Builder()
                .setOnClickListener(v -> tvSwitch.callOnClick())
                .build();
        GuidePage page = GuidePage.newInstance().addHighLightWithOptions(tvSwitch, options);
        mGuidePage = NewbieGuide.with(this)
                .setLabel("guide1")
                .alwaysShow(true)
                .addGuidePage(page
                        .setEverywhereCancelable(false)
                        .addHighLight(tvSwitch)
                        .setLayoutRes(R.layout.view_guide)).build();
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
        IotKitMqttManager.getInstance().sendCmd(mDevice, hashMap, new IotKitActionCallback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure(String msg) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        IotKitMqttManager.getInstance().addDataCallback(this);
        IotKitMqttManager.getInstance().queryStatus(mDevice, null);
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                openRealReporting(1);
            }
        }, 0, 60 * 1000);
    }

    @Override
    protected void onPause() {
        IotKitMqttManager.getInstance().removeDataCallback(this);
        mTimer.cancel();
        openRealReporting(0);
        super.onPause();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    @OnClick({R.id.tv_lb_mode, R.id.tv_switch, R.id.tv_timing, R.id
            .iv_air_purifier_view4_minus, R.id.iv_air_purifier_view4_plus})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_lb_mode:
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_more, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void messageArrived(String type, String deviceId, String productKey, String data) {
        if ("get".equals(type)) {
            if (!deviceId.equals(mDevice.getDeviceId())) {
                Logger.e("error device!");
                return;
            }
            KzzDataModel model = new Gson().fromJson(data, KzzDataModel.class);
            KzzDataModel.ParamsBean paramsBean = model.getParams();
            if (paramsBean == null) {
                Logger.e("error getParams!");
                return;
            }
            showData(paramsBean);
        }
    }

    @Override
    public boolean filter(String deviceId) {
        return !deviceId.equals(mDevice.getDeviceId());
    }


    private void showData(KzzDataModel.ParamsBean paramsBean) {
        if (paramsBean.getSwitch() != null) {
            mSwitch = Integer.parseInt(paramsBean.getSwitch().getValue());
            refreshSwitch();
        }
        if (paramsBean.getControlMode() != null) {
            mControlMode = Integer.parseInt(paramsBean.getControlMode().getValue());
            refreshMode();
        }
        if (paramsBean.getControlGear() != null) {
            mControlGear = Integer.parseInt(paramsBean.getControlGear().getValue());
            refreshGear();
        }
        if (paramsBean.getShutdownRemainingTime() != null) {
            mShutdownRemainingTime = Integer.parseInt(paramsBean.getShutdownRemainingTime()
                    .getValue());
            refreshShutDown();
        }
        if (paramsBean.getUseTime() != null) {
            mUseTime = Integer.parseInt(paramsBean.getUseTime().getValue());
            refreshFilter();

        }
        if (paramsBean.getPM205() != null) {
            mPM205 = Integer.parseInt(paramsBean.getPM205().getValue());
            refreshBackground();
            refreshPm25();
        }
        if (paramsBean.getTempture() != null) {
            mTemperature = Integer.parseInt(paramsBean.getTempture().getValue());
            refreshTemperature();

        }
        if (paramsBean.getHumidity() != null) {
            mHumidity = Integer.parseInt(paramsBean.getHumidity().getValue());
            refreshHumidity();

        }
        if (paramsBean.getBattery() != null) {
            mBattery = Integer.parseInt(paramsBean.getBattery().getValue());
            refreshBattery();

        }
        if (paramsBean.getWifiState() != null) {
            m24GState = Integer.parseInt(paramsBean.getWifiState().getValue());
            refresh24gState();

        }
    }

    private void refresh24gState() {
        if (m24GState == 0) {
            ivStatusKzz224g.setImageResource(R.drawable.ic_24_disconnect);
        } else {
            ivStatusKzz224g.setImageResource(R.drawable.ic_24g_connect);
        }
    }

    private void refreshBattery() {
        if (mBattery == 0) {
            ivStatusKzz2Battery.setImageResource(R.drawable.ic_battery_empty);
        } else {
            ivStatusKzz2Battery.setImageResource(R.drawable.ic_battery_full);
        }
    }

    private void refreshSwitch() {
        if (mSwitch == 0) {
            tvSwitch.setText("关机");
            tvSwitch.setIconNormal(getResources().getDrawable(R.drawable
                    .ic_air_purifier_switch_on));
            if (!isShowGuidePage) {
                mGuidePage.show();
                isShowGuidePage = true;
            }
        } else {
            tvSwitch.setText("开机");
            tvSwitch.setIconNormal(getResources().getDrawable(R.drawable
                    .ic_air_purifier_switch_off));
            if (isShowGuidePage) {
                mGuidePage.remove();
                isShowGuidePage = false;
            }
        }
    }

    private void refreshMode() {
        if (mControlMode == 0) {
            tvLbMode.setText("自动");
            tvLbMode.setIconNormal(getResources().getDrawable(R.drawable.ic_air_purifier_mode_auto));
        } else {
            tvLbMode.setText("手动");
            tvLbMode.setIconNormal(getResources().getDrawable(R.drawable
                    .ic_air_purifier_mode_manual));
        }
    }

    private void refreshGear() {
        viewAirPurifierGear.setGear(mControlGear);
    }

    private void refreshShutDown() {
        tvStatusDyTiming.setText(getFormatTime(mShutdownRemainingTime));
        tvStatusLbTiming.setText(getFormatTime(mShutdownRemainingTime));
        tvStatusKzz2Timing.setText(getFormatTime(mShutdownRemainingTime));
        if (mShutdownRemainingTime == 0) {
            tvTiming.setIconNormal(getResources().getDrawable(R.drawable
                    .ic_air_purifier_timing_off));
        } else {
            tvTiming.setIconNormal(getResources().getDrawable(R.drawable
                    .ic_air_purifier_timing_on));
        }
    }

    private void refreshFilter() {
        int percent = (int) Math.ceil(100f * (FLITER_MAX_VALUE - mUseTime) / FLITER_MAX_VALUE);
        if (percent < 0) {
            percent = 0;
        }
        tvStatusDyFilter.setText(String.format("%02d", percent) + "%");
        tvStatusLbFilter.setText(String.format("%02d", percent) + "%");
        tvStatusKzz2Filter.setText(String.format("%02d", percent) + "%");
        if (mUseTime >= FLITER_MAX_VALUE) {
            showChangeFilter();
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

    private void refreshPm25() {
        viewAirPurifierPm25.setValue(mPM205);
    }

    private void refreshTemperature() {
        tvTemperature.setText("温度 " + mTemperature + "°C");
    }

    private void refreshHumidity() {
        tvHumidity.setText("湿度 " + mHumidity + "%");
    }

    public String getFormatTime(int timingShutdown) {
        String hour = String.format("%02d", timingShutdown / 60);
        String minute = String.format("%02d", timingShutdown % 60);
        return hour + ":" + minute;
    }

    private void showChangeFilter() {
        if (isShowFilter) {
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("更换滤芯");
        builder.setMessage("滤网使用时间到了，请您及时更换滤芯!");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                isShowFilter = true;
            }
        });
        builder.show();
    }


    @Override
    public void onTimeSelected(int hour) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("TimingShutdown", hour + "");
        sendCmd(hashMap);
    }


}
