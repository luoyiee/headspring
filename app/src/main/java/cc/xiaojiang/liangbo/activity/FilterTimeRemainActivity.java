package cc.xiaojiang.liangbo.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import cc.xiaojiang.iotkit.bean.http.Device;
import cc.xiaojiang.iotkit.mqtt.IotKitActionCallback;
import cc.xiaojiang.iotkit.mqtt.IotKitMqttManager;
import cc.xiaojiang.liangbo.R;
import cc.xiaojiang.liangbo.base.BaseActivity;
import cc.xiaojiang.liangbo.utils.ToastUtils;
import cc.xiaojiang.liangbo.view.WaveProgressView;

public class FilterTimeRemainActivity extends BaseActivity implements View.OnLongClickListener {
    private static final String INTENT_FILTER_TIME = "intent_filter_time";
    private static final String INTENT_DEVICE = "intent_device";
    public static final int FLITER_MAX_VALUE = 4000;
    @BindView(R.id.view_filter_time_remain)
    WaveProgressView mViewFilterTimeRemain;
    @BindView(R.id.tv_filter_use_time)
    TextView mTvFilterUseTime;
    @BindView(R.id.tv_filter_reset)
    TextView mTvFilterReset;
    private int mUseTime;
    private Device mDevice;

    public static void actionStart(Context context, int userTime, Device device) {
        Intent intent = new Intent(context, FilterTimeRemainActivity.class);
        intent.putExtra(INTENT_FILTER_TIME, userTime);
        intent.putExtra(INTENT_DEVICE, device);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setBackgroundColor(Color.TRANSPARENT);
        mTvFilterReset.setOnLongClickListener(this);
        initData();
    }

    private void initData() {
        mUseTime = getIntent().getIntExtra(INTENT_FILTER_TIME, 0);
        mDevice = getIntent().getParcelableExtra(INTENT_DEVICE);
        if (mDevice == null) {
            ToastUtils.show("参数错误");
            finish();
        }
        mViewFilterTimeRemain.setMaxValue(FLITER_MAX_VALUE);
        setView();
    }

    private void setView() {
        mViewFilterTimeRemain.setValue(FLITER_MAX_VALUE - mUseTime);
        mTvFilterUseTime.setText(getString(R.string.filter_use_time, mUseTime));

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_filtere_time_remain;
    }


    private void resetFilter() {
        new AlertDialog.Builder(this)
                .setPositiveButton("同意", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("UseTime", String.valueOf(0));
                        sendCmd(hashMap);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setCancelable(false)
                .setMessage("是否重置滤芯？")
                .show();

    }


    private void sendCmd(HashMap<String, String> hashMap) {
        IotKitMqttManager.getInstance().sendCmd(mDevice, hashMap, new IotKitActionCallback() {
            @Override
            public void onSuccess() {
                ToastUtils.show("重置滤芯成功");
                mUseTime = 0;
                setView();
            }

            @Override
            public void onFailure(String msg) {
                ToastUtils.show("重置滤芯失败");
                mTvFilterUseTime.setText(getString(R.string.filter_use_time, 0));
            }
        });
    }

    @Override
    public boolean onLongClick(View v) {
        resetFilter();
//        if (mUseTime >= FLITER_MAX_VALUE) {
//            resetFilter();
//        } else {
//            ToastUtils.show("滤芯未满足重置条件");
//        }
        return false;
    }
}
