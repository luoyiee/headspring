package cc.xiaojiang.headspring.activity;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cc.xiaojiang.headspring.R;
import cc.xiaojiang.headspring.base.BaseActivity;
import cc.xiaojiang.headspring.http.HttpResultFunc;
import cc.xiaojiang.headspring.http.RetrofitHelper;
import cc.xiaojiang.headspring.http.progress.ProgressObserver;
import cc.xiaojiang.headspring.model.http.HomeWeatherAirModel;
import cc.xiaojiang.headspring.model.event.LocationEvent;
import cc.xiaojiang.headspring.utils.DbUtils;
import cc.xiaojiang.headspring.utils.LocationClient;
import cc.xiaojiang.headspring.utils.RxUtils;
import cc.xiaojiang.headspring.utils.ScreenShotUtils;
import cc.xiaojiang.headspring.utils.ScreenUtils;
import cc.xiaojiang.headspring.utils.SpanUtils;
import cc.xiaojiang.headspring.utils.ToastUtils;
import cc.xiaojiang.headspring.view.CommonTextView;
import cc.xiaojiang.headspring.view.Point;
import cc.xiaojiang.headspring.view.PointEvaluator;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MainActivity extends BaseActivity {
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
    @BindView(R.id.tv_indoor_pm)
    TextView mTvIndoorPm;
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

    private boolean mAnimatorTag = true;

    private Point mChainPoint;
    private Point mMapPoint;
    private Point mDevicePoint;
    private Point mShopPoint;
    private Point mPersonalPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        MainActivityPermissionsDispatcher.locationWithPermissionCheck(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocationClient.getInstance().onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @NeedsPermission({Manifest.permission.ACCESS_COARSE_LOCATION})
    void location() {
        LocationClient.getInstance().initClient(this);
        LocationClient.getInstance().startLocation();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode,
                grantResults);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //开始定位
//        LocationClient.getInstance().startLocation();
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocationClient.getInstance().stopLocation();
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
        Logger.d("onWindowFocusChanged");
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

    @Subscribe
    public void onLocationEvent(LocationEvent locationEvent) {
        AMapLocation aMapLocation = locationEvent.getMapLocation();
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
    }

    private void requestWeatherAirData(String city) {
        RetrofitHelper.getService().queryCityWeatherAir(city)
                .map(new HttpResultFunc<>())
                .compose(RxUtils.rxSchedulerHelper())
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
                .appendImage(R.drawable.ic_logo2)
                .appendLine()
                .appendLine().setLineHeight(space)
                .appendLine(weatherBean.getLowTemp() + "-" + weatherBean.getHighTemp() + "°C")
                .create());
    }

    private void setAirView(HomeWeatherAirModel homeWeatherAirModel) {
        mTvOutdoorPm.setText(new SpanUtils()
                .append(homeWeatherAirModel.getPm25() + "").setFontSize(30, true).append("ug/m")
                .setFontSize(18, true).append
                        ("3").setSuperscript().setFontSize(16, true)
                .create());

        mTvOutdoorTemperature.setText(new SpanUtils()
                .append(homeWeatherAirModel.getOutTemp() + "").setFontSize(30, true).append("°C")
                .setFontSize(18, true)
                .create());

        mTvOutdoorHumidity.setText(new SpanUtils()
                .append(homeWeatherAirModel.getOutHumidity() + "").setFontSize(30, true).append
                        ("%").setFontSize(18, true)
                .create());

        mTvIndoorPm.setText(new SpanUtils()
                .append("0.25").setFontSize(50, true)
                .append("ug/m").setFontSize(20, true).append("3").setSuperscript().setFontSize
                        (16, true)
                .create());
    }
}
