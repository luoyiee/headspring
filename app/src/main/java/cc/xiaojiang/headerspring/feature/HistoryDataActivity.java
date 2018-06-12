package cc.xiaojiang.headerspring.feature;

import android.graphics.Color;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import butterknife.BindView;
import cc.xiaojiang.headerspring.R;
import cc.xiaojiang.headerspring.TestDataUtils;
import cc.xiaojiang.headerspring.base.BaseActivity;
import cc.xiaojiang.headerspring.utils.MPChartUtils;

public class HistoryDataActivity extends BaseActivity {

    @BindView(R.id.combinedChart)
    CombinedChart mCombinedChart;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_history_data;
    }

    @Override
    protected void createInit() {
        // 1.配置基础图表配置
        MPChartUtils.configChart(mCombinedChart, TestDataUtils.getChartLabel(), 50, 5, true);

        // 2,获取数据Data，这里有2条曲线
        LineDataSet tartgetDataSet = MPChartUtils.getLineData(TestDataUtils.getChartData(), "室外PM2.5", Color.BLACK, Color.BLUE, false);
        LineDataSet lineDataSet = MPChartUtils.getLineData(TestDataUtils.getChartData(), "室内PM2.5", Color.BLACK, Color.YELLOW, false);
        //  3,初始化数据并绘制
        MPChartUtils.initData(mCombinedChart, new LineData(lineDataSet, tartgetDataSet));
    }

    @Override
    protected void resumeInit() {

    }
}
