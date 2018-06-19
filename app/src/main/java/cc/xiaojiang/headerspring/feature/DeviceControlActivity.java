package cc.xiaojiang.headerspring.feature;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import cc.xiaojiang.headerspring.R;
import cc.xiaojiang.headerspring.base.BaseActivity;
import cc.xiaojiang.headerspring.view.AP1View4;
import cc.xiaojiang.headerspring.view.CommonTextView;
import cc.xiaojiang.headerspring.widget.AP1TimingDialog;

public class DeviceControlActivity extends BaseActivity implements AP1View4.OnSeekBarChangeListener {
    @BindView(R.id.iv_pop_window)
    ImageView mIvPopWindow;
    @BindView(R.id.ic_air_purifier_wifi_off)
    CommonTextView mIcAirPurifierWifiOff;
    @BindView(R.id.tv_air_purifier_view1_timing)
    TextView mTvAirPurifierView1Timing;
    @BindView(R.id.tv_air_purifier_view3_temp)
    TextView mTvAirPurifierView3Temp;
    @BindView(R.id.tv_air_purifier_view1_strainer)
    TextView mTvAirPurifierView1Strainer;
    @BindView(R.id.tv_air_purifier_view3_humidity)
    TextView mTvAirPurifierView3Humidity;
    @BindView(R.id.custom_air_purifier_view4_gear)
    AP1View4 mCustomAirPurifierView4Gear;

    private int mGear = 0;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_device_control;
    }

    @Override
    protected void createInit() {
        getWindow().getDecorView().setBackgroundColor(Color.TRANSPARENT);
        mCustomAirPurifierView4Gear.setOnSeekBarChangeListener(this);
    }

    @Override
    protected void resumeInit() {

    }

    @OnClick({R.id.iv_pop_window,R.id.tv_auto, R.id.tv_switch, R.id.tv_timing, R.id.iv_air_purifier_view4_minus, R.id.iv_air_purifier_view4_plus})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_pop_window:
                showPupWindow();
                break;
            case R.id.tv_auto:
                break;
            case R.id.tv_switch:
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

    private void showPupWindow() {
        final View contentView = getLayoutInflater().inflate(R.layout.device_pop_window, null);
        final PopupWindow popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams
                .WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        popupWindow.getContentView().measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        contentView.findViewById(R.id.ctv_popup_time_remain).setOnClickListener(v->startToActivity(FilterTimeRemainActivity.class));
        popupWindow.showAsDropDown(mIvPopWindow, -popupWindow.getContentView().getMeasuredWidth() + 40, 20);
    }

    private void gearPlus() {
        if (mGear >= 0 && mGear < 3) {
            mGear++;
            handOutGear();
        }
    }


    private void doTiming() {
        AP1TimingDialog dialog = new AP1TimingDialog();
        dialog.setOnTimeSelectedListener(new AP1TimingDialog.OnTimeSelectedListener() {
            @Override
            public void onTimeSelected(int hour) {
//                onCmdChange(Endec_A1.CMD_TIMING, (byte) hour);
            }
        });
        dialog.show(getSupportFragmentManager(), "");
    }

    private void gearMinus() {
        if (mGear > 0 && mGear <=3) {
            mGear--;
            handOutGear();
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
        mGear = gear;
        handOutGear();
    }

    private void handOutGear() {
        //        onCmdChange(Endec_A1.CMD_GEAR, (byte) mGear);
        setGear();
    }

    private void setGear() {
        mCustomAirPurifierView4Gear.setGear(mGear);
    }
}
