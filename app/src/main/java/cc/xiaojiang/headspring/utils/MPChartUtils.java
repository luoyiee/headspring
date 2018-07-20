package cc.xiaojiang.headspring.utils;

import android.graphics.Color;
import android.graphics.Matrix;
import android.support.annotation.ColorInt;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

import cc.xiaojiang.headspring.R;
import cc.xiaojiang.headspring.model.http.Pm25HistoryModel;

/**
 * @author :jinjiafeng
 * date:  on 18-6-12
 * description:
 */
public class MPChartUtils {
    /**
     * 不显示无数据的提示
     *
     * @param mChart
     */
    public static void NotShowNoDataText(Chart mChart) {
        mChart.clear();
        mChart.notifyDataSetChanged();
        mChart.setNoDataText("你还没有记录数据");
        mChart.setNoDataTextColor(Color.WHITE);
        mChart.invalidate();
    }

    /**
     * 配置Chart 基础设置
     *
     * @param mChart       图表
     * @param yMax         y 轴最大值
     * @param yMin         y 轴最小值
     * @param isShowLegend 是否显示图例
     */
    public static void configChart(LineChart mChart, float yMax, float
            yMin, boolean isShowLegend) {

        mChart.setDrawGridBackground(false);
//        mChart.setDrawBorders(false);
        mChart.setBackgroundColor(Color.parseColor("#F7F7F7"));
        mChart.setScaleEnabled(false);
        mChart.setDragEnabled(true);
        mChart.setNoDataText("没有数据");
//        // 不显示描述数据
        mChart.getDescription().setEnabled(false);
        mChart.getAxisRight().setEnabled(false);
        mChart.getLegend().setEnabled(false);
//        mChart.setTouchEnabled(true);

        XAxis xAxis = mChart.getXAxis();
        // 是否显示x轴线
        xAxis.setDrawAxisLine(false);
        // 设置x轴线的颜色
        xAxis.setAxisLineColor(Color.TRANSPARENT);
        // 是否绘制x方向网格线
        xAxis.setDrawGridLines(false);
        //x方向网格线的颜色
//        xAxis.setGridColor(Color.RED);

        // 设置x轴数据的位置
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        // 设置x轴文字的大小
        xAxis.setTextSize(12);
        xAxis.setLabelCount(7, false);
//        final List<String> labels = mLabels;
        // 显示x轴标签
//        IAxisValueFormatter formatter = new IAxisValueFormatter() {
//            @Override
//            public String getFormattedValue(float value, AxisBase axis) {
//                int index = (int) value;
//                if (index < 0 || index >= labels.size()) {
//                    return "";
//                }
//                return labels.get(index);
//                // return labels.get(Math.min(Math.max((int) value, 0), labels.size() - 1));
//            }
//
//        };
        // 引用标签
//        xAxis.setValueFormatter(formatter);
        // 设置x轴文字颜色
        xAxis.setTextColor(mChart.getResources().getColor(R.color.char_text_color));
        // 设置x轴每最小刻度 interval
        xAxis.setGranularity(1f);

        YAxis yAxis = mChart.getAxisLeft();
        yAxis.setEnabled(false);
        //设置x轴的最大值
//        yAxis.setAxisMaximum(yMax);
        // 设置y轴的最大值
        yAxis.setAxisMinimum(0);
        // 不显示y轴
        yAxis.setDrawAxisLine(false);
        // 设置y轴数据的位置
        yAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        // 不从y轴发出横向直线
        yAxis.setDrawGridLines(false);
        // 是否显示y轴坐标线
        yAxis.setDrawZeroLine(false);
        // 设置y轴的文字颜色
        yAxis.setTextColor(mChart.getResources().getColor(R.color.char_text_color));
        // 设置y轴文字的大小
        yAxis.setTextSize(12);
        // 设置y轴数据偏移量
        //yAxis.setXOffset(30);
        // yAxis.setYOffset(-3);
//        yAxis.setXOffset(15);
        // yAxis.setGranularity(yGranularity);
        //      yAxis.setLabelCount(7, false);
        //yAxis.setGranularity(5);//interval

        Matrix matrix = new Matrix();
//        // 根据数据量来确定 x轴缩放大倍
//        if (mLabels.size() <= 7) {
        matrix.postScale(5.0f, 1.0f);
//        } else if (mLabels.size() <= 14) {
//            matrix.postScale(2f, 1.0f);
//        } else if (mLabels.size() <= 21) {
//            matrix.postScale(3f, 1.0f);
//        } else {
//            matrix.postScale(4f, 1.0f);
//        }

        // 在图表动画显示之前进行缩放
//        mChart.getViewPortHandler().refresh(matrix, mChart, false);
        // x轴执行动画
        mChart.animateX(1500);

    }


    /**
     * 获取LineDataSet
     *
     * @param entries
     * @param label
     * @param lineColor
     * @return
     */
    public static LineDataSet getLineData(List<Entry> entries, String label, @ColorInt int
            lineColor) {
        LineDataSet dataSet = new LineDataSet(entries, label);
        // 设置曲线的颜色
        dataSet.setColor(lineColor);
        // 模式为贝塞尔曲线
        dataSet.setMode(LineDataSet.Mode.LINEAR);
        // 是否绘制数据值
        dataSet.setDrawValues(false);
//        dataSet.setValueTextSize(16);
        // 是否绘制圆点
        dataSet.setDrawCircles(true);
        dataSet.setDrawCircleHole(true);
        dataSet.setCircleColorHole(lineColor);
        //设置高亮线
        dataSet.setHighlightEnabled(true);
        dataSet.setDrawHorizontalHighlightIndicator(false);
        dataSet.setHighLightColor(Color.BLUE);
        dataSet.enableDashedHighlightLine(10f, 5f, 0f);
        //设置圆点的颜色
        dataSet.setCircleColor(Color.WHITE);
        // 设置圆点半径
        dataSet.setCircleRadius(3f);
        dataSet.setCircleHoleRadius(1f);
        // 设置线的宽度
        dataSet.setLineWidth(1f);
        return dataSet;
    }

    public static LineData formatWeekDatas(Pm25HistoryModel pm25HistoryModel) {
        List<Entry> outEntries = formatOutdoorData(pm25HistoryModel.getOutdoor());
        List<Entry> inEntries = formatIndoorData(pm25HistoryModel.getIndoor());
        LineDataSet inLineDataSet = getLineData(inEntries, "indoor", Color.parseColor("#81d8d0"));
        LineDataSet outLineDataSet = getLineData(outEntries, "outdoor", Color.parseColor
                ("#6ca7f0"));
        return new LineData(inLineDataSet, outLineDataSet);
    }

    private static List<Entry> formatOutdoorData(List<Pm25HistoryModel.OutdoorBean> outdoorBeans) {
        List<Entry> outEntries = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            int x = DateUtils.getWeek(i + 2);
            int y = 0;
            for (int j = 0; j < outdoorBeans.size(); j++) {
                Pm25HistoryModel.OutdoorBean outdoorBean = outdoorBeans.get(j);
                if (x == outdoorBean.getTime()) {
                    y = outdoorBean.getOutPm25();
                }
            }
            Entry outEntry = new Entry(i, y);
            outEntries.add(outEntry);
        }
        return outEntries;
    }

    private static List<Entry> formatIndoorData(List<Pm25HistoryModel.IndoorBean> indoorBeans) {
        List<Entry> inEntries = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            int x = DateUtils.getWeek(i + 2);
            int y = 0;
            for (int j = 0; j < indoorBeans.size(); j++) {
                Pm25HistoryModel.IndoorBean indoorBean = indoorBeans.get(j);
                if (x == indoorBean.getTime()) {
                    y = indoorBean.getOutPm25();
                }
            }
            Entry outEntry = new Entry(i, y);
            inEntries.add(outEntry);
        }
        return inEntries;
    }
}