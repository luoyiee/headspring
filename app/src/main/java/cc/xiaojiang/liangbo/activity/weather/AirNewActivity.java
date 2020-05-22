package cc.xiaojiang.liangbo.activity.weather;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.zxing.oned.rss.AbstractRSSReader;
import com.orhanobut.logger.Logger;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.xjiangiot.lib.common.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.ParseException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;
import cc.xiaojiang.liangbo.R;
import cc.xiaojiang.liangbo.WeatherIcon;
import cc.xiaojiang.liangbo.activity.AirMapActivity;
import cc.xiaojiang.liangbo.activity.DeviceListActivity;
import cc.xiaojiang.liangbo.activity.MainActivity;
import cc.xiaojiang.liangbo.activity.PersonalCenterActivity;
import cc.xiaojiang.liangbo.activity.ShareAirActivity;
import cc.xiaojiang.liangbo.base.BaseActivity;
import cc.xiaojiang.liangbo.http.LoginInterceptor;
import cc.xiaojiang.liangbo.http.MyObserver;
import cc.xiaojiang.liangbo.http.RetrofitHelper;
import cc.xiaojiang.liangbo.model.event.ShareBitmapEvent;
import cc.xiaojiang.liangbo.model.eventbus.MessageCitySwitch;
import cc.xiaojiang.liangbo.model.http.CityIdModel;
import cc.xiaojiang.liangbo.model.http.HomeNewWeatherModel;
import cc.xiaojiang.liangbo.utils.DateUtils;
import cc.xiaojiang.liangbo.utils.LocationClient;
import cc.xiaojiang.liangbo.utils.RxUtils;
import cc.xiaojiang.liangbo.utils.ScreenShotUtils;
import cc.xiaojiang.liangbo.utils.ScreenUtils;
import cc.xiaojiang.liangbo.utils.ToastUtils;
import cc.xiaojiang.liangbo.utils.WeatherUtils;
import cc.xiaojiang.liangbo.utils.constant.Constant;
import cc.xiaojiang.liangbo.view.AirIndicator;
import cc.xiaojiang.liangbo.view.CommonTextView;
import cc.xiaojiang.liangbo.view.MainMenuLayout;
import io.realm.Realm;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

import static cc.xiaojiang.liangbo.view.MainMenuLayout.ANIM_TIME;

@RuntimePermissions
public class AirNewActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {
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
    @BindView(R.id.ll_air_new_page1)
    LinearLayout mLlAirNewPage1;
    @BindView(R.id.ll_air_new_page2)
    LinearLayout mLlAirNewPage2;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.srl_air_new_content)
    SwipeRefreshLayout mSrlAirNewContent;
    @BindView(R.id.linearLayout3)
    LinearLayout linearLayout3;
    @BindView(R.id.ctv_map)
    CommonTextView ctvMap;
    @BindView(R.id.ctv_shop)
    CommonTextView ctvShop;
    @BindView(R.id.ctv_chain)
    CommonTextView ctvChain;
    @BindView(R.id.ctv_device)
    CommonTextView ctvDevice;
    @BindView(R.id.ctv_personal)
    CommonTextView ctvPersonal;
    @BindView(R.id.iv_main_menu)
    ImageView ivMainMenu;
    @BindView(R.id.view_main_menu)
    MainMenuLayout viewMainMenu;


    private String mMyLocation;
    private LocationClient mLocationClient;
    private Realm mRealm;
    private IWXAPI api;
    private boolean isMenuHide = false;
    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {

            if (msg.what == 1) {
                int scrollY = mSvAirNewContent.getScrollY();
                if (scrollY == (int) msg.obj) {
                    LogUtils.d(TAG, "stop");
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            showMenu();
                        }
                    }, ANIM_TIME);
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);

        EventBus.getDefault().register(this);
        init();
        mSvAirNewContent.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView nestedScrollView, int i, int i1, int i2,
                                       int i3) {
                LogUtils.e(TAG, "ss" + (i3 - i1));
                Message message = new Message();
                message.what = 1;
                message.obj = i1;
                handler.sendMessageDelayed(message, 100);
                if ((i3 - i1) > 0) {
                    //下滑
                    showMenu();
                } else {
                    //上滑
                    hideMenu();

                }
            }
        });
    }

    private void showMenu() {
        if (!isMenuHide) {
            return;
        }
        startAnim();
        isMenuHide = false;
    }

    private void hideMenu() {
        if (isMenuHide) {
            return;
        }
        if (viewMainMenu.isMenuOpen()) {
            viewMainMenu.closeMenu();
        }
        startAnim();
        isMenuHide = true;
    }

    public void startAnim() {
        int translationX = isMenuHide ? 0 : ScreenUtils.dip2px(this, 72);
        final AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator rightAnimator = ObjectAnimator.ofFloat(viewMainMenu, "translationX",
                translationX);
        animatorSet.playTogether(rightAnimator);
        animatorSet.setDuration(ANIM_TIME);
        animatorSet.start();
    }

    private void showPupWindow() {
        final View contentView = getLayoutInflater().inflate(R.layout.layout_pop_window_new_air,
                null);
        final PopupWindow popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams
                .WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        contentView.findViewById(R.id.tv_new_air_city).setOnClickListener(v -> {
            startToCityManagerActivity();
            popupWindow.dismiss();
        });
        popupWindow.getContentView().measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec
                .UNSPECIFIED);
        contentView.findViewById(R.id.tv_new_air_share).setOnClickListener(v -> {
            Bitmap bitmap = ScreenShotUtils.getBitmapByView(mSvAirNewContent);
            EventBus.getDefault().postSticky(new ShareBitmapEvent(bitmap));
            ShareAirActivity.actionStart(this, mTvTitle.getText().toString());
            popupWindow.dismiss();

        });
        popupWindow.showAsDropDown(mToolbar, ScreenUtils.getScreenWidth(this) - popupWindow
                .getContentView().getMeasuredWidth(), 0);
    }

    private void init() {
        setViewHeight();
        mRealm = Realm.getDefaultInstance();
        //初始化界面
        mViewAirNewQuality.setMin(0);
        mViewAirNewQuality.setMax(500);
        mViewAirNewComfort.setMin(0);
        mViewAirNewComfort.setMax(100);
        mSrlAirNewContent.setOnRefreshListener(this);
        AirNewActivityPermissionsDispatcher.requestMyLocationWithPermissionCheck(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void setViewHeight() {
        int pageHeight = ScreenUtils.getScreenHeight(this) - ScreenUtils
                .getActionBarHeight(this) - ScreenUtils.getStatusBarHeight(this);
        //设置第一页
        LinearLayout.LayoutParams layoutParamsPage1 = (LinearLayout.LayoutParams)
                mLlAirNewPage1.getLayoutParams();
        layoutParamsPage1.height = pageHeight;
        mLlAirNewPage1.setLayoutParams(layoutParamsPage1);
        //设置第二页
        LinearLayout.LayoutParams layoutParamsPage2 = (LinearLayout.LayoutParams)
                mLlAirNewPage2.getLayoutParams();
        layoutParamsPage2.height = pageHeight;
        mLlAirNewPage2.setLayoutParams(layoutParamsPage2);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageCitySwitch messageCitySwitch) {
        mIvLocation.setVisibility(View.GONE);
        getNewWeather(messageCitySwitch.getCityId(), CITY);
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
        getMenuInflater().inflate(R.menu.menu_more, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_more) {

            showPupWindow();
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
            mSrlAirNewContent.setRefreshing(false);
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                    Logger.d(aMapLocation.toString());
                    //定位成功回调信息，设置相关消息
                    RetrofitHelper.getService().cityId(aMapLocation.getCityCode(),
                            aMapLocation.getAdCode(), (float) aMapLocation.getLatitude(),
                            (float) aMapLocation.getLongitude())
                            .compose(RxUtils.rxSchedulerHelper())
                            .compose(bindToLifecycle())
                            .subscribe(new MyObserver<CityIdModel>() {
                                @Override
                                public void onSucceed(CityIdModel cityIdModel) {
                                    if (cityIdModel != null) {
                                        getNewWeather(cityIdModel.getCityId(), LOCATION);
                                    } else {
                                        ToastUtils.show("城市暂时不支持");
                                    }
                                }

                                @Override
                                public void onFailed(String code, String msg) {
                                    ToastUtils.show("城市暂时不支持");
                                }
                            });

                    mMyLocation = aMapLocation.getDistrict();
                } else {
                    //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + aMapLocation.getErrorCode() + ", errInfo:"
                            + aMapLocation.getErrorInfo());
//                    Toast.makeText(getApplicationContext(), "定位失败", Toast.LENGTH_LONG).show();
                    mTvTitle.setText("定位失败,请选择城市");
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
        startToCityManagerActivity();
    }

    private void startToCityManagerActivity() {
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

    @Override
    public void onRefresh() {
        AirNewActivityPermissionsDispatcher.requestMyLocationWithPermissionCheck(this);
    }

    @OnClick({R.id.ctv_chain, R.id.ctv_map, R.id.ctv_device, R.id.ctv_shop, R.id.ctv_personal})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ctv_chain:
                startToActivity(MainActivity.class);
                break;
            case R.id.ctv_map:
                startToActivity(AirMapActivity.class);
                break;
            case R.id.ctv_device:
                LoginInterceptor.interceptor(this, DeviceListActivity.class.getName(), null);
                break;
            case R.id.ctv_shop:
                startToWeixin();
                break;
            case R.id.ctv_personal:
                startToActivity(PersonalCenterActivity.class);
                break;
        }
    }

    private void startToWeixin() {
        String appId = "wxcb9058ed93e26360"; // 填应用AppId
        api = WXAPIFactory.createWXAPI(this, appId, false);
        api.registerApp(appId);
        if (!api.isWXAppInstalled()) {
            ToastUtils.show("请安装微信");
            return;
        }
        WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
        req.userName = "gh_814574060176"; // 填小程序原始id
        api.sendReq(req);
    }
}
