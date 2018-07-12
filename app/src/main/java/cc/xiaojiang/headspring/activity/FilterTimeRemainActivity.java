package cc.xiaojiang.headspring.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import cc.xiaojiang.headspring.R;
import cc.xiaojiang.headspring.base.BaseActivity;
import cc.xiaojiang.headspring.utils.ToastUtils;
import cc.xiaojiang.headspring.view.WaveProgressView;

public class FilterTimeRemainActivity extends BaseActivity {
    private static final String INTENT_FILTER_TIME = "intent_filter_time";
    @BindView(R.id.view_filter_time_remain)
    WaveProgressView mViewFilterTimeRemain;
    @BindView(R.id.tv_filter_use_time)
    TextView mTvFilterUseTime;

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
//        int useTime = intent.getIntExtra("use_time", 0);
        mViewFilterTimeRemain.setMaxValue(2000);
        mViewFilterTimeRemain.setValue(2000-useTime);
        mTvFilterUseTime.setText(getString(R.string.filter_use_time, useTime));

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
