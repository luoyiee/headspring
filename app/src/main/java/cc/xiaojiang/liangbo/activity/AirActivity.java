package cc.xiaojiang.liangbo.activity;

import android.Manifest;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnPageChangeListener;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.OnClick;
import cc.xiaojiang.iotkit.bean.http.Device;
import cc.xiaojiang.iotkit.http.IotKitDeviceManager;
import cc.xiaojiang.iotkit.http.IotKitHttpCallback;
import cc.xiaojiang.iotkit.mqtt.IotKitActionCallback;
import cc.xiaojiang.iotkit.mqtt.IotKitMqttManager;
import cc.xiaojiang.iotkit.mqtt.IotKitReceivedCallback;
import cc.xiaojiang.liangbo.R;
import cc.xiaojiang.liangbo.WeatherIcon;
import cc.xiaojiang.liangbo.adapter.HomeIndoorPmHolder;
import cc.xiaojiang.liangbo.base.BaseActivity;
import cc.xiaojiang.liangbo.http.HttpResultFunc;
import cc.xiaojiang.liangbo.http.LoginInterceptor;
import cc.xiaojiang.liangbo.http.RetrofitHelper;
import cc.xiaojiang.liangbo.http.progress.ProgressObserver;
import cc.xiaojiang.liangbo.iotkit.BaseDataModel;
import cc.xiaojiang.liangbo.model.event.LoginEvent;
import cc.xiaojiang.liangbo.model.event.ShareBitmapEvent;
import cc.xiaojiang.liangbo.model.http.HomeWeatherAirModel;
import cc.xiaojiang.liangbo.utils.AccountUtils;
import cc.xiaojiang.liangbo.utils.LocationClient;
import cc.xiaojiang.liangbo.utils.RxUtils;
import cc.xiaojiang.liangbo.utils.ScreenShotUtils;
import cc.xiaojiang.liangbo.utils.ScreenUtils;
import cc.xiaojiang.liangbo.utils.SpanUtils;
import cc.xiaojiang.liangbo.utils.ToastUtils;
import cc.xiaojiang.liangbo.view.CommonTextView;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class AirActivity extends BaseActivity implements IotKitReceivedCallback,
        OnPageChangeListener {
    private static final String UNIT_PM25 = "ug/m";
    private static final String UNIT_TEMPERATURE = "°C";
    private static final String UNIT_HUMIDITY = "%";
    public static final String DEFAULT_DATA = "-/-";
    @BindView(R.id.tv_outdoor_pm)
    TextView mTvOutdoorPm;
    @BindView(R.id.tv_outdoor_temperature)
    TextView mTvOutdoorTemperature;
    @BindView(R.id.tv_outdoor_humidity)
    TextView mTvOutdoorHumidity;
    @BindView(R.id.tv_home_location)
    TextView mTvHomeLocation;
    @BindView(R.id.tv_weather_today)
    TextView mTvWeatherToday;
    @BindView(R.id.tv_weather_tomorrow)
    TextView mTvWeatherTomorrow;
    @BindView(R.id.tv_weather_after_tomorrow)
    TextView mTvWeatherAfterTomorrow;
    @BindView(R.id.ctv_select_device_name)
    CommonTextView mCtvSelectDeviceName;
    @BindView(R.id.convenientBanner)
    ConvenientBanner<String> mConvenientBanner;
    @BindView(R.id.fl_selected_devices)
    FrameLayout mFlSelectedDevices;
    @BindView(R.id.ll_device_add)
    LinearLayout mLlDeviceAdd;

    private LocationClient mLocationClient;
    private ArrayList<String> mIndoorPmList = new ArrayList<>();
    private CBViewHolderCreator mHolderCreator;
    private LinkedHashMap<String, String> mIndoorMap = new LinkedHashMap<>();
    private int mDeviceSize;
    private boolean firstLoad = true;
    private List<Device> mDeviceList = new ArrayList<>();

    private Timer mTimer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AccountUtils.isLogin()) {
            getDevices();
        } else {
            setAddDeviceViewVisibility(true);
        }
        EventBus.getDefault().register(this);
        mLocationClient = new LocationClient();
        AirActivityPermissionsDispatcher.locationWithPermissionCheck(this);
        initView();


    }

    private void openRealReporting(Device device, int isOpen) {
        HashMap<String, String> hashMap1 = new HashMap<>();
        hashMap1.put("RealReportingSwitch", String.valueOf(isOpen));
        sendCmd(device, hashMap1);
    }

    private void sendCmd(Device device, HashMap<String, String> hashMap) {
        IotKitMqttManager.getInstance().sendCmd(device, hashMap, new IotKitActionCallback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure(String msg) {

            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_air;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_share_dark, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_share) {
            Bitmap bitmap = ScreenShotUtils.screenShot(this, 0);
            EventBus.getDefault().postSticky(new ShareBitmapEvent(bitmap));
            startToActivity(ShareAirActivity.class);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        mLocationClient.stopLocation();
        mLocationClient.onDestroy();
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private void setAddDeviceViewVisibility(boolean showAddDeviceView) {
        mFlSelectedDevices.setVisibility(showAddDeviceView ? View.INVISIBLE : View.VISIBLE);
        mLlDeviceAdd.setVisibility(showAddDeviceView ? View.VISIBLE : View.INVISIBLE);
    }

    private void getDevices() {
        if (!IotKitMqttManager.getInstance().isConnected()) {
            return;
        }
        IotKitDeviceManager.getInstance().deviceList(new IotKitHttpCallback<List<Device>>() {
            @Override
            public void onSuccess(List<Device> data) {
                mDeviceSize = data.size();
                mDeviceList = data;
                if (mDeviceSize == 0) {
                    setAddDeviceViewVisibility(true);
                    return;
                }
                if (mDeviceSize > 1) {
                    mConvenientBanner.setPageIndicator(new int[]{R.drawable.dot_default_indicator,
                            R.drawable.dot_select_indicator});
                }
                for (Device datum : data) {
                    mIndoorMap.put(datum.getDeviceId(), "");
                }
                mCtvSelectDeviceName.setText(getDeviceName(data.get(0)));
                setAddDeviceViewVisibility(false);
                queryDevices(data);
            }

            @Override
            public void onError(String code, String errorMsg) {

            }
        });
    }

    private void initView() {
        setAirView(mTvOutdoorPm, DEFAULT_DATA, UNIT_PM25);
        setAirView(mTvOutdoorTemperature, DEFAULT_DATA + "", UNIT_TEMPERATURE);
        setAirView(mTvOutdoorHumidity, DEFAULT_DATA + "", UNIT_HUMIDITY);
        mIndoorPmList.add(DEFAULT_DATA);
        setIndoorPmBannerView();
    }

    /**
     * 批量查询设备状态
     */
    private void queryDevices(List<Device> data) {
        for (int i = 0; i < data.size(); i++) {
            queryDevice(data.get(i));
            openRealReporting(data.get(i), 1);
        }
    }

    private void setAirView(TextView textView, String date, String unit) {
        SpanUtils spanUtils = new SpanUtils()
                .append(date).setFontSize(26, true).append(unit)
                .setFontSize(14, true);
        if (UNIT_PM25.equals(unit)) {
            spanUtils.append("3").setSuperscript().setFontSize(16, true);
        }
        textView.setText(spanUtils.create());
    }

    @OnClick({R.id.iv_add_device})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_add_device:
                LoginInterceptor.interceptor(this, ProductListActivity.class.getName(), null);
                break;
            default:
                break;
        }
    }

    private void setIndoorPmBannerView() {
        mHolderCreator = new CBViewHolderCreator() {
            @Override
            public HomeIndoorPmHolder createHolder(View itemView) {
                return new HomeIndoorPmHolder(itemView);
            }

            @Override
            public int getLayoutId() {
                return R.layout.item_home_indoor_pm;
            }
        };
        mConvenientBanner.setPages(mHolderCreator, mIndoorPmList);
        mConvenientBanner.setOnPageChangeListener(this);
    }

    /**
     * 查询单个设备状态
     */
    public void queryDevice(Device device) {
        IotKitMqttManager.getInstance().queryStatus(device, new IotKitActionCallback() {
            @Override
            public void onSuccess() {
                Logger.d("查询设备成功，deviceId=" + device.getDeviceId());
            }

            @Override
            public void onFailure(String msg) {
                Logger.d("查询设备失败，deviceId=" + device.getDeviceId());

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!firstLoad) {
            getDevices();
        }
        firstLoad = false;
        IotKitMqttManager.getInstance().addDataCallback(this);
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                for (Device device : mDeviceList) {
                    openRealReporting(device, 1);
                }
            }
        }, 3000, 60 * 1000);
    }

    @Override
    protected void onPause() {
        IotKitMqttManager.getInstance().removeDataCallback(this);
        mTimer.cancel();
        super.onPause();
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
                    String district = aMapLocation.getDistrict();//城区信息
                    String street = aMapLocation.getStreet();//街道信息
                    String city = aMapLocation.getCity();//街道信息
                    mTvHomeLocation.setText(district + street);
                    requestWeatherAirData(aMapLocation.getCity());
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

    private void requestWeatherAirData(String city) {
        RetrofitHelper.getService().queryCityWeatherAir(city)
                .map(new HttpResultFunc<>())
                .compose(RxUtils.rxSchedulerHelper())
                .compose(bindToLifecycle())
                .subscribe(new ProgressObserver<HomeWeatherAirModel>(this) {
                    @Override
                    public void onSuccess(HomeWeatherAirModel homeWeatherAirModel) {
                        setView(homeWeatherAirModel);
                    }
                });
    }

    private void setView(HomeWeatherAirModel homeWeatherAirModel) {
        setAirView(homeWeatherAirModel);
        setWeather(homeWeatherAirModel.getNextWeather());

    }

    private void setAirView(HomeWeatherAirModel homeWeatherAirModel) {
        setAirView(mTvOutdoorPm, homeWeatherAirModel.getPm25() + "", UNIT_PM25);
        setAirView(mTvOutdoorTemperature, homeWeatherAirModel.getTemperature() + "",
                UNIT_TEMPERATURE);
        setAirView(mTvOutdoorHumidity, homeWeatherAirModel.getHumidity() + "", UNIT_HUMIDITY);
    }

    private void setWeather(List<HomeWeatherAirModel.NextWeatherBean> nextWeatherBeans) {
        int size = nextWeatherBeans.size();
        if (size == 3) {
            HomeWeatherAirModel.NextWeatherBean todayBean = nextWeatherBeans.get(0);
            HomeWeatherAirModel.NextWeatherBean tomorrowBean = nextWeatherBeans.get(1);
            HomeWeatherAirModel.NextWeatherBean AfterTomorrowBean = nextWeatherBeans.get(2);
            setWeatherView(mTvWeatherToday, todayBean);
            setWeatherView(mTvWeatherTomorrow, tomorrowBean);
            setWeatherView(mTvWeatherAfterTomorrow, AfterTomorrowBean);
        } else {
            Logger.e("error weather length:" + size);
        }
    }

    private void setWeatherView(TextView textView, HomeWeatherAirModel.NextWeatherBean
            weatherBean) {
        int space = ScreenUtils.dip2px(this, 2);
        int day = weatherBean.getDay();
        //方法1、
        int qianWei = day % 10000 / 1000;
        int baiWei = day % 1000 / 100;
        int shiWei = day % 100 / 10;
        int geWei = day % 10;
        textView.setText(new SpanUtils()
                .appendLine("" + qianWei + baiWei + "/" + shiWei + geWei)
                .appendLine().setLineHeight(space)
                .appendImage(WeatherIcon.ICONS[weatherBean.getCodeDay()])
                .appendLine()
                .appendLine().setLineHeight(space)
                .appendLine(weatherBean.getLowTemp() + "-" + weatherBean.getHighTemp() + "°C")
                .create());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        AirActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode,
                grantResults);
        AirActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode,
                grantResults);
    }

    /**
     * 当用户登录或退出,会执行
     *
     * @param loginEvent 登录或退出事件
     */
    @Subscribe
    public void onLoginEvent(LoginEvent loginEvent) {
        if (LoginEvent.CODE_LOGIN == loginEvent.getCode()) {
            mIndoorMap.clear();
        }
    }


    @Override
    public void messageArrived(String type, String deviceId, String productKey, String data) {
        if ("get".equals(type)) {
            BaseDataModel model = new Gson().fromJson(data, BaseDataModel.class);
            BaseDataModel.ParamsBean paramsBean = model.getParams();
            if (paramsBean == null || paramsBean.getPM205() == null || paramsBean.getOnlineStatus
                    () == null) {
                Logger.e("error getParams!");
                return;
            }
            String pm205 = paramsBean.getPM205().getValue();
            String onlineStatus = paramsBean.getOnlineStatus().getValue();
            if (onlineStatus.startsWith("online")) {
                mIndoorMap.put(deviceId, pm205);
            } else {
                mIndoorMap.put(deviceId, DEFAULT_DATA);
            }
            if (mIndoorMap.size() == mDeviceSize && mDeviceSize > 0) {
                mIndoorPmList.clear();
                // 遍历 ArrayMap
                mIndoorPmList.addAll(mIndoorMap.values());
                int currentItem = mConvenientBanner.getCurrentItem();
                mConvenientBanner.notifyDataSetChanged();
                mConvenientBanner.setCurrentItem(currentItem, false);
                mConvenientBanner.setFirstItemPos(currentItem);
                if (mIndoorMap.size() > 1) {
                    mConvenientBanner.setPageIndicator(new int[]{R.drawable.dot_default_indicator,
                            R.drawable.dot_select_indicator});
                }
            }
        }

    }

    @Override
    public boolean filter(String deviceId) {
        return false;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

    }

    @Override
    public void onPageSelected(int index) {
        final Device device = mDeviceList.get(index);
        if (mCtvSelectDeviceName != null) {
            mCtvSelectDeviceName.setText(getDeviceName(device));
        }
    }

    private String getDeviceName(Device device) {
        final boolean tag = TextUtils.isEmpty(device.getDeviceNickname());
        return tag ? device.getProductName() : device.getDeviceNickname();
    }

    @OnShowRationale({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission
            .ACCESS_COARSE_LOCATION})
    void locationRationale(final PermissionRequest request) {
        new AlertDialog.Builder(this)
                .setPositiveButton("同意", (dialog, which) -> request.proceed())
                .setNegativeButton("拒绝", (dialog, which) -> request.cancel())
                .setCancelable(false)
                .setMessage("请同意我们的定位权限申请")
                .show();
    }

    @OnPermissionDenied({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission
            .ACCESS_COARSE_LOCATION})
    void locationDenied() {
        ToastUtils.show("请同意权限后再试");
    }

    @OnNeverAskAgain({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission
            .ACCESS_COARSE_LOCATION})
    void locationNeverAskAgain() {
        ToastUtils.show("请在APP权限设置页面打开定位权限后再试");
    }
}
