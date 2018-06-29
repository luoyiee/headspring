package cc.xiaojiang.headspring.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import cc.xiaojiang.headspring.R;
import cc.xiaojiang.headspring.base.BaseActivity;
import cc.xiaojiang.headspring.utils.ToastUtils;
import cc.xiaojiang.headspring.view.WaveProgressView;

public class FilterTimeRemainActivity extends BaseActivity {
    @BindView(R.id.view_filter_time_remain)
    WaveProgressView mViewFilterTimeRemain;
    @BindView(R.id.tv_filter_use_time)
    TextView mTvFilterUseTime;
    @BindView(R.id.ll_filter_reset)
    LinearLayout mLlFilterReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setBackgroundColor(Color.TRANSPARENT);
        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        int useTime = intent.getIntExtra("use_time", 0);
//        int useTime = intent.getIntExtra("use_time", 0);
//        mViewFilterTimeRemain.setValue(useTime);
        mTvFilterUseTime.setText(getString(R.string.filter_use_time, useTime));

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_filtere_time_remain;
    }


    @OnClick(R.id.ll_filter_reset)
    public void onViewClicked() {
        ToastUtils.show("重置滤芯");
    }
}
