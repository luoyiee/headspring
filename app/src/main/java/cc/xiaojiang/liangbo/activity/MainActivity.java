package cc.xiaojiang.liangbo.activity;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cc.xiaojiang.iotkit.bean.http.Device;
import cc.xiaojiang.iotkit.http.IotKitDeviceManager;
import cc.xiaojiang.iotkit.http.IotKitHttpCallback;
import cc.xiaojiang.iotkit.mqtt.IotKitActionCallback;
import cc.xiaojiang.iotkit.mqtt.IotKitMqttManager;
import cc.xiaojiang.iotkit.mqtt.IotKitReceivedCallback;
import cc.xiaojiang.iotkit.util.ByteUtils;
import cc.xiaojiang.liangbo.R;
import cc.xiaojiang.liangbo.WeatherIcon;
import cc.xiaojiang.liangbo.adapter.HomeIndoorPmHolder;
import cc.xiaojiang.liangbo.base.BaseActivity;
import cc.xiaojiang.liangbo.iotkit.BaseDataModel;
import cc.xiaojiang.liangbo.http.HttpResultFunc;
import cc.xiaojiang.liangbo.http.RetrofitHelper;
import cc.xiaojiang.liangbo.http.progress.ProgressObserver;
import cc.xiaojiang.liangbo.iotkit.ProductKey;
import cc.xiaojiang.liangbo.model.http.HomeWeatherAirModel;
import cc.xiaojiang.liangbo.utils.DbUtils;
import cc.xiaojiang.liangbo.utils.LocationClient;
import cc.xiaojiang.liangbo.utils.RxUtils;
import cc.xiaojiang.liangbo.utils.ScreenShotUtils;
import cc.xiaojiang.liangbo.utils.ScreenUtils;
import cc.xiaojiang.liangbo.utils.SpanUtils;
import cc.xiaojiang.liangbo.utils.ToastUtils;
import cc.xiaojiang.liangbo.view.CommonTextView;
import cc.xiaojiang.liangbo.view.Point;
import cc.xiaojiang.liangbo.view.PointEvaluator;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MainActivity extends BaseActivity implements IotKitReceivedCallback {
    private static final String UNIT_PM25 = "ug/m";
    private static final String UNIT_TEMPERATURE = "°C";
    private static final String UNIT_HUMIDITY = "%";
    private static final String DEFAULT_DATA = "-/-";
    @BindView(R.id.ctv_chain)
    CommonTextView mBtnChain;
    @BindView(R.id.ctv_map)
    CommonTextView mBtnMap;
    @BindView(R.id.ctv_device)
    CommonTextView mBtnDevice;
    @BindView(R.id.ctv_shop)
    CommonTextView mBtnShop;
    @BindView(R.id.ctv_personal)
    CommonTextView mBtnPersonal;
    @BindView(R.id.tv_outdoor_pm)
    TextView mTvOutdoorPm;
    @BindView(R.id.tv_outdoor_temperature)
    TextView mTvOutdoorTemperature;
    @BindView(R.id.tv_outdoor_humidity)
    TextView mTvOutdoorHumidity;
    @BindView(R.id.iv_home_control)
    ImageView mIvHomeControl;
    @BindView(R.id.tv_home_location)
    TextView mTvHomeLocation;
    @BindView(R.id.tv_weather_today)
    TextView mTvWeatherToday;
    @BindView(R.id.tv_weather_tomorrow)
    TextView mTvWeatherTomorrow;
    @BindView(R.id.tv_weather_after_tomorrow)
    TextView mTvWeatherAfterTomorrow;
    @BindView(R.id.convenientBanner)
    ConvenientBanner<String> mConvenientBanner;
    private boolean mAnimatorTag = true;

    private Point mChainPoint;
    private Point mMapPoint;
    private Point mDevicePoint;
    private Point mShopPoint;
    private Point mPersonalPoint;
    private LocationClient mLocationClient;
    private ArrayList<String> mIndoorPmList = new ArrayList<>();
    private CBViewHolderCreator mHolderCreator;
    private ArrayMap<String, String> mIndoorMap = new ArrayMap<>();
    private int mDeviceSize;
    private boolean firstLoad = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IotKitMqttManager.getInstance().startDataService(new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                Logger.e("mqtt connect success");
                getDevices();
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                Logger.e("mqtt connect error, " + exception.getMessage());

            }
        });
        MainActivityPermissionsDispatcher.locationWithPermissionCheck(this);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!firstLoad) {
            getDevices();
        }
        firstLoad = false;
        IotKitMqttManager.getInstance().addDataCallback(MainActivity.this);

    }

    @Override
    protected void onPause() {
        IotKitMqttManager.getInstance().removeDataCallback(this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mLocationClient.stopLocation();
        mLocationClient.onDestroy();
        IotKitMqttManager.getInstance().stopDataService();
        super.onDestroy();
    }

    private void initView() {
        setAirView(mTvOutdoorPm, DEFAULT_DATA, UNIT_PM25);
        setAirView(mTvOutdoorTemperature, DEFAULT_DATA + "", UNIT_TEMPERATURE);
        setAirView(mTvOutdoorHumidity, DEFAULT_DATA + "", UNIT_HUMIDITY);
        mIndoorPmList.add(DEFAULT_DATA);
        setIndoorPmBannerView();
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
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    private void getDevices() {
        if (IotKitMqttManager.getInstance().getMqttAndroidClient() == null) {
            return;
        }
        IotKitDeviceManager.getInstance().deviceList(new IotKitHttpCallback<List<Device>>() {
            @Override
            public void onSuccess(List<Device> data) {
                mDeviceSize = data.size();
                queryDevices(data);
            }

            @Override
            public void onError(String code, String errorMsg) {

            }
        });
    }

    @NeedsPermission({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission
            .ACCESS_COARSE_LOCATION})
    void location() {
        mLocationClient = new LocationClient();
        mLocationClient.initClient(this);
        mLocationClient.startLocation(aMapLocation -> {
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                    Logger.d(aMapLocation.toString());
                    //定位成功回调信息，设置相关消息
                    String district = aMapLocation.getDistrict();//城区信息
                    String street = aMapLocation.getStreet();//街道信息
                    String city = aMapLocation.getCity();//街道信息
                    DbUtils.setLocationCity(city);
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
        setAirView(mTvOutdoorTemperature, homeWeatherAirModel.getOutTemp() + "", UNIT_TEMPERATURE);
        setAirView(mTvOutdoorHumidity, homeWeatherAirModel.getOutHumidity() + "", UNIT_HUMIDITY);
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
                .appendImage(WeatherIcon.ICONS[weatherBean.getCode()])
                .appendLine()
                .appendLine().setLineHeight(space)
                .appendLine(weatherBean.getLowTemp() + "-" + weatherBean.getHighTemp() + "°C")
                .create());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode,
                grantResults);
    }

    @OnClick({R.id.ctv_chain, R.id.ctv_map, R.id.ctv_device, R.id.ctv_shop, R.id.ctv_personal, R
            .id.iv_home_control, R.id.iv_home_share})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_home_control:
                startAnimator();
                break;
            case R.id.iv_home_share:
                ScreenShotUtils.share(this);
                break;
            case R.id.ctv_chain:
                ToastUtils.show("区块链");
                break;
            case R.id.ctv_map:
                startToActivity(AirMapActivity.class);
                break;
            case R.id.ctv_device:
                startToActivity(DeviceListActivity.class);
                break;
            case R.id.ctv_shop:
                startToActivity(ShopActivity.class);
                break;
            case R.id.ctv_personal:
                startToActivity(PersonalCenterActivity.class);
                break;
        }
    }

    /**
     * 批量查询设备状态
     */
    private void queryDevices(List<Device> data) {
        for (int i = 0; i < data.size(); i++) {
            queryDevice(data.get(i));
        }
    }

    /**
     * 查询单个设备状态
     */
    public void queryDevice(Device device) {
        IotKitMqttManager.getInstance().queryStatus(device.getProductKey(),
                device.getDeviceId(), new IotKitActionCallback() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        Logger.d("查询设备成功，deviceId=" + device.getDeviceId());

                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable
                            exception) {
                        Logger.d("查询设备失败，deviceId=" + device.getDeviceId());
                    }
                });
    }

    private void startAnimator() {
        AnimatorSet animSet = new AnimatorSet();
        animSet.setDuration(200);
        animSet.playTogether(createAnimator(mBtnChain, mChainPoint),
                createAnimator(mBtnDevice, mDevicePoint),
                createAnimator(mBtnPersonal, mPersonalPoint),
                createAnimator(mBtnShop, mShopPoint),
                createAnimator(mBtnMap, mMapPoint));
        animSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mAnimatorTag = !mAnimatorTag;
            }
        });
        animSet.start();
    }

    private ValueAnimator createAnimator(View view, Point startPoint) {
        ValueAnimator animator;
        if (mAnimatorTag) {
            animator = ValueAnimator.ofObject(new PointEvaluator(), startPoint, getEndPoint());
        } else {
            animator = ValueAnimator.ofObject(new PointEvaluator(), getEndPoint(), startPoint);
        }
        animator.addUpdateListener(animation -> {
            Point point = (Point) animation.getAnimatedValue();
            // 将每次变化后的坐标值（估值器PointEvaluator中evaluate（）返回的Piont对象值）到当前坐标值对象（currentPoint）
            // 从而更新当前坐标值（currentPoint）
            view.setX(point.getX());
            view.setY(point.getY());
        });
        return animator;
    }

    private Point getEndPoint() {
        int centerX = (mIvHomeControl.getLeft() + mIvHomeControl.getRight()) / 2;
        int centerY = (mIvHomeControl.getBottom() + mIvHomeControl.getTop()) / 2;
        return new Point(centerX - mBtnChain.getWidth() / 2, centerY - mBtnChain.getHeight() / 2);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            //获取控件原先的位置
            mChainPoint = getPoint(mBtnChain);
            mMapPoint = getPoint(mBtnMap);
            mDevicePoint = getPoint(mBtnDevice);
            mShopPoint = getPoint(mBtnShop);
            mPersonalPoint = getPoint(mBtnPersonal);
        }
    }

    private Point getPoint(View view) {
        return new Point(view.getX(), view.getY());
    }

    @Override
    public void messageArrived(String deviceId, String productKey, String data) {
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
        if (mIndoorMap.size() == mDeviceSize) {
            mIndoorPmList.clear();
            // 遍历 ArrayMap
            for (int i = 0; i < mIndoorMap.size(); i++) {
                String val = mIndoorMap.valueAt(i);
                mIndoorPmList.add(val);
            }
            if (mDeviceSize > 1) {
                mConvenientBanner.setPageIndicator(new int[]{R.drawable.dot_default_indicator,
                        R.drawable.dot_select_indicator});
            }
            mConvenientBanner.setPages(mHolderCreator, mIndoorPmList);
        }
    }

    @Override
    public boolean filter(String deviceId) {
        return false;
    }
}
