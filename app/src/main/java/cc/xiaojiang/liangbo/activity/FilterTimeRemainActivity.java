package cc.xiaojiang.liangbo.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import cc.xiaojiang.liangbo.R;
import cc.xiaojiang.liangbo.base.BaseActivity;
import cc.xiaojiang.liangbo.utils.ToastUtils;
import cc.xiaojiang.liangbo.view.WaveProgressView;

public class FilterTimeRemainActivity extends BaseActivity {
    private static final String INTENT_FILTER_TIME = "intent_filter_time";
    public static final int FLITER_MAX_VALUE = 2000;
    @BindView(R.id.view_filter_time_remain)
    WaveProgressView mViewFilterTimeRemain;
    @BindView(R.id.tv_filter_use_time)
    TextView mTvFilterUseTime;
    @BindView(R.id.tv_filter_reset)
    TextView mTvFilterReset;
    private int mUseTime;

    public static void actionStart(Context context, int userTime) {
        Intent intent = new Intent(context, FilterTimeRemainActivity.class);
        intent.putExtra(INTENT_FILTER_TIME, userTime);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setBackgroundColor(Color.TRANSPARENT);
        initData();
    }

    private void initData() {
        mUseTime = getIntent().getIntExtra(INTENT_FILTER_TIME, 0);
        mViewFilterTimeRemain.setMaxValue(FLITER_MAX_VALUE);
        mViewFilterTimeRemain.setValue(FLITER_MAX_VALUE - mUseTime);
        mTvFilterUseTime.setText(getString(R.string.filter_use_time, mUseTime));

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_filtere_time_remain;
    }


    @OnClick(R.id.tv_filter_reset)
    public void onViewClicked() {
        if (mUseTime >= FLITER_MAX_VALUE) {
            ToastUtils.show("重置滤芯成功");
        } else {
            ToastUtils.show("不需要重置滤芯");
        }

    }
}
