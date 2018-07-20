package cc.xiaojiang.headspring.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.util.Log;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import cc.xiaojiang.headspring.R;
import cc.xiaojiang.headspring.base.BaseActivity;
import cc.xiaojiang.headspring.utils.MPChartUtils;

public class HistoryDataActivity extends BaseActivity implements TabLayout.OnTabSelectedListener,
        OnChartValueSelectedListener {
    @BindView(R.id.LineChart)
    LineChart mLineChart;
    @BindView(R.id.tl_history_data)
    TabLayout mTlHistoryData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 1.配置基础图表配置
        initView();
        initChart();

    }

    private void initChart() {
        mLineChart.setOnChartValueSelectedListener(this);
        MPChartUtils.configChart(mLineChart, 200, 0, false);

    }


    private void initView() {
        mTlHistoryData.addOnTabSelectedListener(this);
        mTlHistoryData.addTab(mTlHistoryData.newTab().setText(R.string.day));
        mTlHistoryData.addTab(mTlHistoryData.newTab().setText(R.string.week));
        mTlHistoryData.addTab(mTlHistoryData.newTab().setText(R.string.month));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_history_data;
    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        Logger.d(tab.getPosition());
        int position = tab.getPosition();
        if (position == 0) {
            getTestData(24);
        }
        if (position == 1) {
            getTestData(7);
        }
        if (position == 2) {
            getTestData(30);
        }

    }

    private void getTestData(int count) {
        //  3,初始化数据并绘制
        MPChartUtils.initData(mLineChart, count);

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        Log.i("Entry selected", e.toString());
    }

    @Override
    public void onNothingSelected() {

    }
}
