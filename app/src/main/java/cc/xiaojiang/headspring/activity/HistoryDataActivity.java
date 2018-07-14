package cc.xiaojiang.headspring.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import butterknife.BindView;
import cc.xiaojiang.headspring.R;
import cc.xiaojiang.headspring.TestDataUtils;
import cc.xiaojiang.headspring.base.BaseActivity;
import cc.xiaojiang.headspring.utils.MPChartUtils;

public class HistoryDataActivity extends BaseActivity implements TabLayout.OnTabSelectedListener{
    @BindView(R.id.combinedChart)
    CombinedChart mCombinedChart;
    @BindView(R.id.tl_history_data)
    TabLayout mTlHistoryData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 1.配置基础图表配置
        initView();
        MPChartUtils.configChart(mCombinedChart, TestDataUtils.getChartLabel(), 50, 5, true);

        // 2,获取数据Data，这里有2条曲线
        LineDataSet targetDataSet = MPChartUtils.getLineData(TestDataUtils.getChartData(),
                "室外PM2.5", Color.BLACK, Color.BLUE, false);
        LineDataSet lineDataSet = MPChartUtils.getLineData(TestDataUtils.getChartData(),
                "室内PM2.5", Color.BLACK, Color.RED, false);
        //  3,初始化数据并绘制
        MPChartUtils.initData(mCombinedChart, new LineData(lineDataSet, targetDataSet));
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

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
