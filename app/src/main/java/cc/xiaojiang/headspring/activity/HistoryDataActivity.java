package cc.xiaojiang.headspring.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.util.Log;

import com.amap.api.maps.model.Marker;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import cc.xiaojiang.headspring.R;
import cc.xiaojiang.headspring.TestDataUtils;
import cc.xiaojiang.headspring.base.BaseActivity;
import cc.xiaojiang.headspring.utils.MPChartUtils;

public class HistoryDataActivity extends BaseActivity implements TabLayout.OnTabSelectedListener,
        OnChartValueSelectedListener {
    @BindView(R.id.CombinedChart)
    CombinedChart mCombinedChart;
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
        mCombinedChart.setOnChartValueSelectedListener(this);
        MPChartUtils.configChart(mCombinedChart,  200, 0, false);

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
        // 2,获取数据Data，这里有2条曲线
        LineDataSet targetDataSet = MPChartUtils.getLineData(mCombinedChart, TestDataUtils
                        .getChartData(count),
                "室外PM2.5", Color.BLACK, Color.parseColor("#6ca7f0"), false);
        LineDataSet lineDataSet = MPChartUtils.getLineData(mCombinedChart, TestDataUtils
                        .getChartData(count),
                "室内PM2.5", Color.BLACK, Color.parseColor("#81d8d0"), false);
        //  3,初始化数据并绘制
        MPChartUtils.initData(mCombinedChart, new LineData(lineDataSet, targetDataSet));

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
