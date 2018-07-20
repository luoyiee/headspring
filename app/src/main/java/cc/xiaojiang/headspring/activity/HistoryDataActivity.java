package cc.xiaojiang.headspring.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.util.Log;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.orhanobut.logger.Logger;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import cc.xiaojiang.headspring.R;
import cc.xiaojiang.headspring.base.BaseActivity;
import cc.xiaojiang.headspring.http.HttpResultFunc;
import cc.xiaojiang.headspring.http.RetrofitHelper;
import cc.xiaojiang.headspring.http.progress.ProgressObserver;
import cc.xiaojiang.headspring.iotkit.DeviceDataModel;
import cc.xiaojiang.headspring.model.http.Pm25HistoryModel;
import cc.xiaojiang.headspring.utils.DateUtils;
import cc.xiaojiang.headspring.utils.DbUtils;
import cc.xiaojiang.headspring.utils.MPChartUtils;
import cc.xiaojiang.headspring.utils.RxUtils;
import cc.xiaojiang.iotkit.bean.http.Device;

public class HistoryDataActivity extends BaseActivity implements TabLayout.OnTabSelectedListener,
        OnChartValueSelectedListener {
    @BindView(R.id.LineChart)
    LineChart mLineChart;
    @BindView(R.id.tl_history_data)
    TabLayout mTlHistoryData;


    private Device mDevice;
    private static final String DAY = "day";
    private static final String WEEK = "week";
    private static final String MONTH = "month";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        initView();
        initChart();

//        for (int i = 1; i <= 24; i++) {
//            Logger.d(DateUtils.getDay(i));
//        }
//        for (int i = 1; i <= 7; i++) {
//            Logger.d(DateUtils.getWeek(i+1));
//        }
//        for (int i = 1; i <= 30; i++) {
//            Logger.d(DateUtils.getMonth(i));
//        }

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
//            getPm25History(DAY, DateUtils.getDay());
        }
        if (position == 1) {
            getPm25History(WEEK, DateUtils.getWeek(2));
        }
        if (position == 2) {
            getPm25History(MONTH, DateUtils.getMonth(2));
        }

    }

    private void getPm25History(String type, int date) {
        RetrofitHelper.getService().pm25History(mDevice.getDeviceId(), DbUtils.getLocationCity(),
                type, date).map(new HttpResultFunc<>())
                .compose(RxUtils.rxSchedulerHelper())
                .subscribe(new ProgressObserver<Pm25HistoryModel>(this) {
                    @Override
                    public void onSuccess(Pm25HistoryModel pm25HistoryModel) {
                        LineData ss = MPChartUtils
                                .formatWeekDatas(pm25HistoryModel);
                        mLineChart.setData(ss);
                        mLineChart.invalidate();

//                        List<Pm25HistoryModel.IndoorBean> indoorDatas =
//                                pm25HistoryModel.getIndoor();
//                        List<Pm25HistoryModel.IndoorBean> indoorDatas =
//                                pm25HistoryModel.getIndoor();
                    }
                });
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
