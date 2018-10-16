package cc.xiaojiang.liangbo.activity.weather;

import android.Manifest;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.ParseException;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import cc.xiaojiang.liangbo.R;
import cc.xiaojiang.liangbo.WeatherIcon;
import cc.xiaojiang.liangbo.activity.ShareAirActivity;
import cc.xiaojiang.liangbo.base.BaseActivity;
import cc.xiaojiang.liangbo.http.LoginInterceptor;
import cc.xiaojiang.liangbo.http.MyObserver;
import cc.xiaojiang.liangbo.http.RetrofitHelper;
import cc.xiaojiang.liangbo.model.event.ShareBitmapEvent;
import cc.xiaojiang.liangbo.model.eventbus.MessageCitySwitch;
import cc.xiaojiang.liangbo.model.http.HomeNewWeatherModel;
import cc.xiaojiang.liangbo.model.realm.WeatherCityCodeRealm;
import cc.xiaojiang.liangbo.utils.DateUtils;
import cc.xiaojiang.liangbo.utils.GetJsonDataUtil;
import cc.xiaojiang.liangbo.utils.LocationClient;
import cc.xiaojiang.liangbo.utils.RxUtils;
import cc.xiaojiang.liangbo.utils.ScreenShotUtils;
import cc.xiaojiang.liangbo.utils.ScreenUtils;
import cc.xiaojiang.liangbo.utils.ToastUtils;
import cc.xiaojiang.liangbo.utils.WeatherUtils;
import cc.xiaojiang.liangbo.utils.constant.Constant;
import cc.xiaojiang.liangbo.view.AirIndicator;
import io.realm.Realm;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class AirNewActivity extends BaseActivity {
    private static final int LOCATION = 1;
    private static final int CITY = 0;
    @BindView(R.id.view_air_new_quality)
    AirIndicator mViewAirNewQuality;
    @BindView(R.id.tv_air_new_air_quality_rank)
    TextView mTvAirNewAirQualityRank;
    @BindView(R.id.tv_air_new_air_quality_aqi)
    TextView mTvAirNewAirQualityAqi;
    @BindView(R.id.tv_air_new_air_quality_pm10)
    TextView mTvAirNewAirQualityPm10;
    @BindView(R.id.tv_air_new_air_quality_pm25)
    TextView mTvAirNewAirQualityPm25;
    @BindView(R.id.tv_air_new_air_quality_no2)
    TextView mTvAirNewAirQualityNo2;
    @BindView(R.id.tv_air_new_air_quality_so2)
    TextView mTvAirNewAirQualitySo2;
    @BindView(R.id.tv_air_new_air_quality_o3)
    TextView mTvAirNewAirQualityO3;
    @BindView(R.id.tv_air_new_air_quality_co)
    TextView mTvAirNewAirQualityCo;
    @BindView(R.id.view_air_new_comfort)
    AirIndicator mViewAirNewComfort;
    @BindView(R.id.tv_air_new_comfort_humidity)
    TextView mTvAirNewComfortHumidity;
    @BindView(R.id.tv_air_new_comfort_feelsLike)
    TextView mTvAirNewComfortFeelsLike;
    @BindView(R.id.tv_air_new_weather_text)
    TextView mTvAirNewWeatherText;
    @BindView(R.id.tv_air_new_weather_temperature)
    TextView mTvAirNewWeatherTemperature;
    @BindView(R.id.tv_air_new_weather_wind_direction)
    TextView mTvAirNewWeatherWindDirection;
    @BindView(R.id.tv_air_new_weather_wind_scale)
    TextView mTvAirNewWeatherWindScale;
    @BindView(R.id.tv_air_new_forecast_date0)
    TextView mTvAirNewForecastDate0;
    @BindView(R.id.iv_air_new_forecast_icon0)
    ImageView mIvAirNewForecastIcon0;
    @BindView(R.id.tv_air_new_forecast_temperature0)
    TextView mTvAirNewForecastTemperature0;
    @BindView(R.id.tv_air_new_forecast_date1)
    TextView mTvAirNewForecastDate1;
    @BindView(R.id.iv_air_new_forecast_icon1)
    ImageView mIvAirNewForecastIcon1;
    @BindView(R.id.tv_air_new_forecast_temperature1)
    TextView mTvAirNewForecastTemperature1;
    @BindView(R.id.tv_air_new_forecast_date2)
    TextView mTvAirNewForecastDate2;
    @BindView(R.id.iv_air_new_forecast_icon2)
    ImageView mIvAirNewForecastIcon2;
    @BindView(R.id.tv_air_new_forecast_temperature2)
    TextView mTvAirNewForecastTemperature2;
    @BindView(R.id.tv_air_new_forecast_date3)
    TextView mTvAirNewForecastDate3;
    @BindView(R.id.iv_air_new_forecast_icon3)
    ImageView mIvAirNewForecastIcon3;
    @BindView(R.id.tv_air_new_forecast_temperature3)
    TextView mTvAirNewForecastTemperature3;
    @BindView(R.id.tv_air_new_forecast_date4)
    TextView mTvAirNewForecastDate4;
    @BindView(R.id.iv_air_new_forecast_icon4)
    ImageView mIvAirNewForecastIcon4;
    @BindView(R.id.tv_air_new_forecast_temperature4)
    TextView mTvAirNewForecastTemperature4;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.sv_air_new_content)
    NestedScrollView mSvAirNewContent;
    @BindView(R.id.iv_location)
    ImageView mIvLocation;

    private String mMyLocation;
    private LocationClient mLocationClient;
    private Realm mRealm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        init();

    }

    private void init() {
        mRealm = Realm.getDefaultInstance();
        initWeatherCityCode();
        //初始化界面
        mViewAirNewQuality.setMin(0);
        mViewAirNewQuality.setMax(500);
        mViewAirNewComfort.setMin(0);
        mViewAirNewComfort.setMax(100);
        //定位当前位置
        AirNewActivityPermissionsDispatcher.requestMyLocationWithPermissionCheck(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageCitySwitch messageCitySwitch) {
        mIvLocation.setVisibility(View.GONE);
        getNewWeather(messageCitySwitch.getCityId(), CITY);
    }

    /**
     * 本地存储高德对应心知城市id
     */
    private void initWeatherCityCode() {
        if (mRealm.where(WeatherCityCodeRealm.class).findAll().isEmpty()) {
            String weatherCodeStr = new GetJsonDataUtil().getJson(this,
                    "weather_city_code_v1.0.json");
            List<WeatherCityCodeRealm> cityCodeRealmList = new Gson().fromJson(weatherCodeStr, new
                    TypeToken<List<WeatherCityCodeRealm>>() {
                    }.getType());
            mRealm.beginTransaction();
            mRealm.insert(cityCodeRealmList);
            mRealm.commitTransaction();
            Logger.d("insert weather city code");
        }
    }


    @Override
    protected void onDestroy() {
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
        mRealm.close();
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_air_new;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_share_dark, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_share) {
            Bitmap bitmap = ScreenShotUtils.getBitmapByView(mSvAirNewContent);
            EventBus.getDefault().postSticky(new ShareBitmapEvent(bitmap));
            ShareAirActivity.actionStart(this, mTvTitle.getText().toString());
        }
        return super.onOptionsItemSelected(item);
    }

    private void setView(HomeNewWeatherModel homeNewWeatherModel, int type) throws ParseException {
        if (homeNewWeatherModel.getCountyWeather() != null) {
            HomeNewWeatherModel.CountyWeatherBean countyWeather = homeNewWeatherModel
                    .getCountyWeather();
            if (type == 1) {
                mMyLocation = countyWeather.getCountyName();
            }
            mTvTitle.setText(countyWeather.getCountyName());
            //天气实况
            mTvAirNewWeatherText.setText(countyWeather.getText());
            mTvAirNewWeatherTemperature.setText(String.format(Locale.getDefault(), "%d℃",
                    countyWeather.getTemperature()));
            mTvAirNewWeatherWindDirection.setText(String.format("%s风", countyWeather
                    .getWindDirection()));
            mTvAirNewWeatherWindScale.setText(String.format("%s级", countyWeather.getWindScale()));
            //舒适度
            mViewAirNewComfort.setValue(countyWeather.getHumidity());
            mTvAirNewComfortHumidity.setText(String.format(Locale.getDefault(), "%d%%",
                    countyWeather.getHumidity()));
            mTvAirNewComfortFeelsLike.setText(String.format(Locale.getDefault(), "%d℃",
                    countyWeather.getFeelsLike()));
        }
        //天气预报

        if (homeNewWeatherModel.getCountyForecast() != null) {
            List<HomeNewWeatherModel.CountyForecastBean> countyForecastBeans = homeNewWeatherModel
                    .getCountyForecast();
            for (int i = 0; i < countyForecastBeans.size(); i++) {
                HomeNewWeatherModel.CountyForecastBean countyForecastBean = countyForecastBeans
                        .get(i);

                switch (i) {
                    case 0:
                        mTvAirNewForecastDate0.setText(DateUtils.formatDate(countyForecastBean
                                .getYmdh()));
                        if (countyForecastBean.getCode() >= 0 && countyForecastBean.getCode() <=
                                39) {
                            mIvAirNewForecastIcon0.setImageResource(WeatherIcon
                                    .ICONS[countyForecastBean.getCode()]);
                        } else {
                            mIvAirNewForecastIcon0.setImageResource(WeatherIcon
                                    .ICONS[39]);
                        }
                        mTvAirNewForecastTemperature0.setText(String.format(Locale.getDefault(),
                                "%d-%d℃", countyForecastBean.getLowTemperature(),
                                countyForecastBean.getHighTemperature()));
                        break;
                    case 1:
                        mTvAirNewForecastDate1.setText(DateUtils.formatDate(countyForecastBean
                                .getYmdh()));
                        if (countyForecastBean.getCode() >= 0 && countyForecastBean.getCode() <=
                                39) {
                            mIvAirNewForecastIcon1.setImageResource(WeatherIcon
                                    .ICONS[countyForecastBean.getCode()]);
                        } else {
                            mIvAirNewForecastIcon1.setImageResource(WeatherIcon.ICONS[39]);
                        }
                        mTvAirNewForecastTemperature1.setText(String.format(Locale.getDefault(),
                                "%d-%d℃", countyForecastBean.getLowTemperature(),
                                countyForecastBean.getHighTemperature()));
                        break;
                    case 2:
                        mTvAirNewForecastDate2.setText(DateUtils.formatDate(countyForecastBean
                                .getYmdh()));
                        if (countyForecastBean.getCode() >= 0 && countyForecastBean.getCode() <=
                                39) {
                            mIvAirNewForecastIcon2.setImageResource(WeatherIcon
                                    .ICONS[countyForecastBean.getCode()]);
                        } else {
                            mIvAirNewForecastIcon2.setImageResource(WeatherIcon.ICONS[39]);
                        }
                        mTvAirNewForecastTemperature2.setText(String.format(Locale.getDefault(),
                                "%d-%d℃", countyForecastBean.getLowTemperature(),
                                countyForecastBean.getHighTemperature()));
                        break;
                    case 3:
                        mTvAirNewForecastDate3.setText(DateUtils.formatDate(countyForecastBean
                                .getYmdh()));
                        if (countyForecastBean.getCode() >= 0 && countyForecastBean.getCode() <=
                                39) {
                            mIvAirNewForecastIcon3.setImageResource(WeatherIcon
                                    .ICONS[countyForecastBean.getCode()]);
                        } else {
                            mIvAirNewForecastIcon3.setImageResource(WeatherIcon.ICONS[39]);
                        }
                        mTvAirNewForecastTemperature3.setText(String.format(Locale.getDefault(),
                                "%d-%d℃", countyForecastBean.getLowTemperature(),
                                countyForecastBean.getHighTemperature()));
                        break;
                    case 4:
                        mTvAirNewForecastDate4.setText(DateUtils.formatDate(countyForecastBean
                                .getYmdh()));
                        if (countyForecastBean.getCode() >= 0 && countyForecastBean.getCode() <=
                                39) {
                            mIvAirNewForecastIcon4.setImageResource(WeatherIcon
                                    .ICONS[countyForecastBean.getCode()]);
                        } else {
                            mIvAirNewForecastIcon4.setImageResource(WeatherIcon.ICONS[39]);
                        }
                        mTvAirNewForecastTemperature4.setText(String.format(Locale.getDefault(),
                                "%d-%d℃", countyForecastBean.getLowTemperature(),
                                countyForecastBean.getHighTemperature()));
                        break;
                }
            }
        }

        //空气质量
        if (homeNewWeatherModel.getAirQuality() != null) {
            HomeNewWeatherModel.AirQualityBean airQualityBean = homeNewWeatherModel
                    .getAirQuality();
            mTvAirNewAirQualityRank.setText(WeatherUtils.getAirQuality(airQualityBean.getAqi()));
            mViewAirNewQuality.setValue(airQualityBean.getAqi());
            mTvAirNewAirQualityAqi.setText(String.valueOf(airQualityBean.getAqi()));
            mTvAirNewAirQualityPm10.setText(String.valueOf(airQualityBean.getPm10()));
            mTvAirNewAirQualityPm25.setText(String.valueOf(airQualityBean.getPm25()));
            mTvAirNewAirQualityNo2.setText(String.valueOf(airQualityBean.getNo2()));
            mTvAirNewAirQualitySo2.setText(String.valueOf(airQualityBean.getSo2()));
            mTvAirNewAirQualityO3.setText(String.valueOf(airQualityBean.getO3()));
            mTvAirNewAirQualityCo.setText(String.valueOf(airQualityBean.getCo()));

        }
    }

    @NeedsPermission({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission
            .ACCESS_COARSE_LOCATION})
    void requestMyLocation() {
        if (mLocationClient == null) {
            mLocationClient = new LocationClient();
        }
        mLocationClient.initClient(this);
        mLocationClient.startLocation(aMapLocation -> {
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                    Logger.d(aMapLocation.toString());
                    //定位成功回调信息，设置相关消息
                    getNewWeather(WeatherUtils.getXinZhiCityCode(aMapLocation.getCityCode(),
                            aMapLocation.getAdCode()), LOCATION);
                    mMyLocation = aMapLocation.getDistrict();
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        AirNewActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode,
                grantResults);
    }

    @OnShowRationale({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission
            .ACCESS_COARSE_LOCATION})
    void onShowRationale(final PermissionRequest request) {
        new AlertDialog.Builder(this)
                .setPositiveButton("同意", (dialog, which) -> request.proceed())
                .setNegativeButton("拒绝", (dialog, which) -> request.cancel())
                .setCancelable(false)
                .setMessage("请同意我们的定位权限申请")
                .show();
    }

    @OnPermissionDenied({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission
            .ACCESS_COARSE_LOCATION})
    void onPermissionDenied() {
        ToastUtils.show("请同意权限后再试");
    }

    @OnNeverAskAgain({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission
            .ACCESS_COARSE_LOCATION})
    void onNeverAskAgain() {
        ToastUtils.show("请在APP权限设置页面打开定位权限后再试");
    }

    @OnClick(R.id.tv_title)
    public void onViewClicked() {
        Bundle bundle = new Bundle();
        bundle.putString(Constant.CITY, mMyLocation);
        LoginInterceptor.interceptor(this, CityManagerActivity.class.getName(), bundle);
    }

    private void getNewWeather(String cityId, int type) {
        if (TextUtils.isEmpty(cityId)) {
            ToastUtils.show("城市暂时不支持");
            return;
        }
        RetrofitHelper.getService().newWeather(cityId)
                .compose(RxUtils.rxSchedulerHelper())
                .compose(bindToLifecycle())
                .subscribe(new MyObserver<HomeNewWeatherModel>() {
                    @Override
                    public void onSucceed(HomeNewWeatherModel homeNewWeatherModel) {
                        if (homeNewWeatherModel == null) {
                            return;
                        }
                        try {
                            setView(homeNewWeatherModel, type);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailed(String code, String msg) {

                    }
                });
    }

}
