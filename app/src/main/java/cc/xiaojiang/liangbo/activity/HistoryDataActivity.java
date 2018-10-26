package cc.xiaojiang.liangbo.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import cc.xiaojiang.iotkit.bean.http.Device;
import cc.xiaojiang.liangbo.R;
import cc.xiaojiang.liangbo.base.BaseActivity;
import cc.xiaojiang.liangbo.http.HttpResultFunc;
import cc.xiaojiang.liangbo.http.RetrofitHelper;
import cc.xiaojiang.liangbo.http.progress.ProgressObserver;
import cc.xiaojiang.liangbo.model.event.ShareBitmapEvent;
import cc.xiaojiang.liangbo.model.http.Pm25HistoryModel;
import cc.xiaojiang.liangbo.utils.DateUtils;
import cc.xiaojiang.liangbo.utils.LocationClient;
import cc.xiaojiang.liangbo.utils.MPChartUtils;
import cc.xiaojiang.liangbo.utils.RxUtils;
import cc.xiaojiang.liangbo.utils.ScreenShotUtils;
import cc.xiaojiang.liangbo.utils.ToastUtils;
import cc.xiaojiang.liangbo.view.MyMarkerView;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
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
    private LocationClient mLocationClient;
    private String mCity;
    private MyMarkerView mMyMarkerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        initView();
        initChart();
        mLocationClient = new LocationClient();
        HistoryDataActivityPermissionsDispatcher.locationWithPermissionCheck(this);
        // TODO: 2018/10/26 首次定位不显示数据
    }

    @Override
    protected void onDestroy() {
        mLocationClient.stopLocation();
        mLocationClient.onDestroy();
        super.onDestroy();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_share, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_share) {
            Bitmap bitmap = ScreenShotUtils.getViewBitmap(mLineChart);
            EventBus.getDefault().postSticky(new ShareBitmapEvent(bitmap));

            ShareHistoryDataActivity.actionStart(this, mDevice);
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        mTlHistoryData.addTab(mTlHistoryData.newTab().setText(R.string.day));
        mTlHistoryData.addTab(mTlHistoryData.newTab().setText(R.string.week));
        mTlHistoryData.addTab(mTlHistoryData.newTab().setText(R.string.month));
        mTlHistoryData.addOnTabSelectedListener(this);
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
            getPm25History(WEEK, Integer.parseInt(DateUtils.getFirstDayWeek()));
        }
        if (mPosition == 2) {
            getPm25History(MONTH, Integer.parseInt(DateUtils.getCurrentMonth()));
        }
    }

    @NeedsPermission({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission
            .ACCESS_COARSE_LOCATION})
    void location() {
        mLocationClient.initClient(this);
        mLocationClient.startLocation(aMapLocation -> {
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                    Logger.d(aMapLocation.toString());
                    //定位成功回调信息，设置相关消息
                    //街道信息
                    mCity = aMapLocation.getCity();
                    getPm25History(DAY, DateUtils.getToday());
                } else {
                    //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + aMapLocation.getErrorCode() + ", errInfo:"
                            + aMapLocation.getErrorInfo());
                    Toast.makeText(getApplicationContext(), "定位失败", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void getPm25History(String type, int date) {
        if (TextUtils.isEmpty(mCity)) {
            ToastUtils.show("获取位置信息失败");
            return;
        }
        RetrofitHelper.getService().pm25History(mDevice.getDeviceId(), mCity, type, date)
                .map(new HttpResultFunc<>())
                .compose(RxUtils.rxSchedulerHelper())
                .compose(bindToLifecycle())
                .subscribe(new ProgressObserver<Pm25HistoryModel>(this) {
                    @Override
                    public void onSuccess(Pm25HistoryModel pm25HistoryModel) {
                        if (pm25HistoryModel != null) {
                            showData(pm25HistoryModel);
                            setAvg(pm25HistoryModel);
                        }
                    }
                });
    }

    private void setAvg(Pm25HistoryModel pm25HistoryModel) {
        List<Pm25HistoryModel.IndoorBean> indoors = pm25HistoryModel.getIndoor();
        if (indoors != null && indoors.size() > 0) {
            float sum = 0;
            for (int i = 0; i < indoors.size(); i++) {
                sum += indoors.get(i).getPm25();
            }
            mTvHistoryDataIndoor.setText((int) sum / indoors.size() + "μg/m³");
        }
        List<Pm25HistoryModel.OutdoorBean> outers = pm25HistoryModel.getOutdoor();
        if (outers != null && outers.size() > 0) {
            float sum = 0;
            for (int i = 0; i < outers.size(); i++) {
                sum += outers.get(i).getPm25();
            }
            mTvHistoryDataOutdoor.setText((int) sum / outers.size() + "μg/m³");
        }


        //            mTvHistoryDataOutdoor.setText(outdoorDataSet.getEntryForIndex((int) e.getX
        // ()).getY()
//                    + "μg/m³");
//            mTvHistoryDataIndoor.setText(indoorDataSet.getEntryForIndex((int) e.getX()).getY() +
//                    "μg/m³");
    }

    private void showData(Pm25HistoryModel pm25HistoryModel) {
        IAxisValueFormatter formatter = null;
        LineData lineData = null;
        int index = 0;
        switch (mPosition) {
            case 0:
                lineData = MPChartUtils.formatData(pm25HistoryModel, DAY);
                formatter = MPChartUtils.getFormat(DAY);
                index = DateUtils.getHourIndex();
                Logger.d("day: " + DateUtils.getHourIndex());
                break;
            case 1:
                lineData = MPChartUtils.formatData(pm25HistoryModel, WEEK);
                formatter = MPChartUtils.getFormat(WEEK);
                index = DateUtils.getWeekIndex();
                Logger.d("week: " + DateUtils.getWeekIndex());
                break;
            default:
                lineData = MPChartUtils.formatData(pm25HistoryModel, MONTH);
                formatter = MPChartUtils.getFormat(MONTH);
                index = DateUtils.getMonthIndex();
                Logger.d("month: " + DateUtils.getMonthIndex());
                break;
        }
        refreshData(lineData, index, formatter);
    }

    private void refreshData(LineData lineData, int index, IAxisValueFormatter
            iAxisValueFormatter) {
        if (lineData == null) {
            return;
        }
        mLineChart.setData(lineData);
        mMyMarkerView = new MyMarkerView(this, R.layout.item_mark_layout);
        mLineChart.setMarkerView(mMyMarkerView);
        mLineChart.highlightValue(index, 0, true);
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


            TextView inner = mMyMarkerView.findViewById(R.id.tv_marker_inner);
            TextView outer = mMyMarkerView.findViewById(R.id.tv_marker_outer);
            inner.setText(indoorDataSet.getEntryForIndex((int) e.getX()).getY() +
                    "μg/m³");
            outer.setText(outdoorDataSet.getEntryForIndex((int) e.getX()).getY()
                    + "μg/m³");
        } else {
            Logger.e("error data!");
        }

    }

    @Override
    public void onNothingSelected() {

    }
}
