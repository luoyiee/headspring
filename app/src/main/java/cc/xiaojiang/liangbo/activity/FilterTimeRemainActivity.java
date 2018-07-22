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
        int useTime = getIntent().getIntExtra(INTENT_FILTER_TIME, 0);
        mViewFilterTimeRemain.setMaxValue(FLITER_MAX_VALUE);
        // TODO: 2018/7/22 百分比显示bug
        // TODO: 2018/7/22 无限刷新界面
        mViewFilterTimeRemain.setValue(FLITER_MAX_VALUE - useTime);
        mTvFilterUseTime.setText(getString(R.string.filter_use_time, useTime));
        if (useTime >= FLITER_MAX_VALUE) {
            mTvFilterReset.setVisibility(View.VISIBLE);
        } else {
            mTvFilterReset.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_filtere_time_remain;
    }


    @OnClick(R.id.tv_filter_reset)
    public void onViewClicked() {
        ToastUtils.show("重置滤芯");
    }
}
