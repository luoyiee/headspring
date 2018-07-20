package cc.xiaojiang.headspring.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.util.Log;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import cc.xiaojiang.headspring.R;
import cc.xiaojiang.headspring.base.BaseActivity;
import cc.xiaojiang.headspring.http.HttpResultFunc;
import cc.xiaojiang.headspring.http.RetrofitHelper;
import cc.xiaojiang.headspring.http.progress.ProgressObserver;
import cc.xiaojiang.headspring.model.http.Pm25HistoryModel;
import cc.xiaojiang.headspring.utils.DateUtils;
import cc.xiaojiang.headspring.utils.DbUtils;
import cc.xiaojiang.headspring.utils.MPChartUtils;
import cc.xiaojiang.headspring.utils.RxUtils;
import cc.xiaojiang.iotkit.bean.http.Device;

public class HistoryDataActivity extends BaseActivity implements TabLayout.OnTabSelectedListener,
        OnChartValueSelectedListener {
    public static final String DAY = "day";
    public static final String WEEK = "week";
    public static final String MONTH = "month";
    @BindView(R.id.LineChart)
    LineChart mLineChart;
    @BindView(R.id.tl_history_data)
    TabLayout mTlHistoryData;
    @BindView(R.id.tv_history_data_outdoor)
    TextView mTvHistoryDataOutdoor;
    @BindView(R.id.tv_history_data_indoor)
    TextView mTvHistoryDataIndoor;

    private int mPosition;
    private Device mDevice;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        initView();
        initChart();

    }

    private void initData() {
        Intent intent = getIntent();
        mDevice = intent.getParcelableExtra("device");

    }

    public static void actionStart(Context context, Device device) {
        Intent intent = new Intent(context, HistoryDataActivity.class);
        intent.putExtra("device", device);
        context.startActivity(intent);
    }

    private void initChart() {
        mLineChart.setOnChartValueSelectedListener(this);
        MPChartUtils.configChart(mLineChart);
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
        mPosition = tab.getPosition();
        if (mPosition == 0) {
            getPm25History(DAY, DateUtils.getToday());
        }
        if (mPosition == 1) {
            getPm25History(WEEK, Integer.parseInt(DateUtils.getWeek(1, "yyyyMMdd")));
        }
        if (mPosition == 2) {
            getPm25History(MONTH, Integer.parseInt(DateUtils.getMonth(2, "yyyyMMdd")));
        }

    }

    private void getPm25History(String type, int date) {
        RetrofitHelper.getService().pm25History(mDevice.getDeviceId(), DbUtils.getLocationCity(),
                type, date).map(new HttpResultFunc<>())
                .compose(RxUtils.rxSchedulerHelper())
                .subscribe(new ProgressObserver<Pm25HistoryModel>(this) {
                    @Override
                    public void onSuccess(Pm25HistoryModel pm25HistoryModel) {
                        showData(pm25HistoryModel);
                    }
                });
    }

    private void showData(Pm25HistoryModel pm25HistoryModel) {
        IAxisValueFormatter formatter = null;
        LineData lineData = null;
        switch (mPosition) {
            case 0:
                lineData = MPChartUtils.formatData(pm25HistoryModel, DAY);
                formatter = new IAxisValueFormatter() {
                    @Override
                    public String getFormattedValue(float value, AxisBase axis) {
                        return DateUtils.getDay((int) value, "HH");
                    }

                };
                break;
            case 1:
                lineData = MPChartUtils.formatData(pm25HistoryModel, WEEK);
                formatter = new IAxisValueFormatter() {
                    @Override
                    public String getFormattedValue(float value, AxisBase axis) {
                        return DateUtils.getWeek((int) value + 1, "MM/dd");
                    }

                };
                break;
            case 2:
                lineData = MPChartUtils.formatData(pm25HistoryModel, MONTH);
                formatter = new IAxisValueFormatter() {
                    @Override
                    public String getFormattedValue(float value, AxisBase axis) {
                        return DateUtils.getMonth((int) value + 1, "MM/dd");
                    }

                };
                break;
        }
        refreshData(lineData, formatter);
    }

    private void refreshData(LineData lineData, IAxisValueFormatter iAxisValueFormatter) {
        if (lineData == null) {
            return;
        }
        mLineChart.setData(lineData);
        mLineChart.getXAxis().setValueFormatter(iAxisValueFormatter);
        mLineChart.animateX(1000);
        mLineChart.invalidate();
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
        if (mLineChart.getData().getDataSets().size() == 2) {
            LineDataSet indoorDataSet = (LineDataSet) mLineChart.getData().getDataSets().get(0);
            LineDataSet outdoorDataSet = (LineDataSet) mLineChart.getData().getDataSets().get(1);
            mTvHistoryDataOutdoor.setText(outdoorDataSet.getEntryForIndex((int) e.getX()).getY()+"");
            mTvHistoryDataIndoor.setText(indoorDataSet.getEntryForIndex((int) e.getX()).getY()+"");
        } else {
            Logger.e("error data!");
        }

    }

    @Override
    public void onNothingSelected() {

    }
}
