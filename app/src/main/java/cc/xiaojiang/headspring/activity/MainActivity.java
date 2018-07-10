package cc.xiaojiang.headspring.activity;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;
import cc.xiaojiang.headspring.R;
import cc.xiaojiang.headspring.base.BaseActivity;
import cc.xiaojiang.headspring.model.event.ShareBitmapEvent;
import cc.xiaojiang.headspring.utils.ScreenShotUtils;
import cc.xiaojiang.headspring.utils.SpanUtils;
import cc.xiaojiang.headspring.utils.ToastUtils;
import cc.xiaojiang.headspring.view.CommonTextView;
import cc.xiaojiang.headspring.view.Point;
import cc.xiaojiang.headspring.view.PointEvaluator;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MainActivity extends BaseActivity implements AMapLocationListener {
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
    //声明AMapLocationClient类对象，定位发起端
    private AMapLocationClient mLocationClient = null;
    //声明mLocationOption对象，定位参数
    public AMapLocationClientOption mLocationOption = null;

    private boolean mAnimatorTag = true;

    private Point mChainPoint;
    private Point mMapPoint;
    private Point mDevicePoint;
    private Point mShopPoint;
    private Point mPersonalPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        //开始定位
        MainActivityPermissionsDispatcher.locationWithPermissionCheck(this);
    }

    @NeedsPermission({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission
            .ACCESS_COARSE_LOCATION})
     void location() {
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(this);
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为Hight_Accuracy高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        mLocationOption.setOnceLocation(false);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(1000*60);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode,
                grantResults);
    }

    private void initView() {
        mTvOutdoorPm.setText(new SpanUtils()
                .append("50").setFontSize(30, true).append("ug/m").setFontSize(18, true).append("3").setSuperscript().setFontSize(16, true)
                .create());

        mTvOutdoorTemperature.setText(new SpanUtils()
                .append("20").setFontSize(30, true).append("°C").setFontSize(18, true)
                .create());

        mTvOutdoorHumidity.setText(new SpanUtils()
                .append("49").setFontSize(30, true).append("%").setFontSize(18, true)
                .create());

        mTvIndoorPm.setText(new SpanUtils()
                .append("0.25").setFontSize(42, true)
                .append("ug/m").setFontSize(20, true).append("3").setSuperscript().setFontSize(16, true)
                .create());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mLocationClient!=null) {
            mLocationClient.onDestroy();//销毁定位客户端。
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        if(mLocationClient!=null) {
            mLocationClient.startLocation(); // 启动定位
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mLocationClient!=null) {
            mLocationClient.stopLocation();//停止定位
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @OnClick({R.id.ctv_chain, R.id.ctv_map, R.id.ctv_device, R.id.ctv_shop, R.id.ctv_personal, R.id.iv_home_control,
    R.id.iv_home_share})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_home_control:
                startAnimator();
                break;
            case R.id.iv_home_share:
                share();
                break;
            case R.id.ctv_chain:
                ToastUtils.show("区块链");
                //                IotKitDeviceManager.getInstance().deviceBind("bff503",
                //                        "xBcqwpFWINOUzjO21X3E", new
                //                                IotKitCallBack() {
                //                                    @Override
                //                                    public void onSuccess(String response) {
                //
                //                                    }
                //
                //                                    @Override
                //                                    public void onError(int code, String errorMsg) {
                //
                //                                    }
                //                                });
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

    private void share() {
        Bitmap bitmap = ScreenShotUtils.screenShot(this);
        EventBus.getDefault().postSticky(new ShareBitmapEvent(bitmap));
        startToActivity(ShareActivity.class);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus){
            //获取控件原先的位置
            mChainPoint = getPoint(mBtnChain);
            mMapPoint = getPoint(mBtnMap);
            mDevicePoint = getPoint(mBtnDevice);
            mShopPoint = getPoint(mBtnShop);
            mPersonalPoint = getPoint(mBtnPersonal);
        }
    }

    private void startAnimator() {
        AnimatorSet animSet = new AnimatorSet();
        animSet.setDuration(200);
        animSet.playTogether(createAnimator(mBtnChain,mChainPoint),
                createAnimator(mBtnDevice,mDevicePoint),
                createAnimator(mBtnPersonal,mPersonalPoint),
                createAnimator(mBtnShop,mShopPoint),
                createAnimator(mBtnMap,mMapPoint));
        animSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mAnimatorTag = !mAnimatorTag;
            }
        });
        animSet.start();
    }

    private Point getPoint(View view) {
        return new Point(view.getX(), view.getY());
    }

    private Point getEndPoint() {
        int centerX = (mIvHomeControl.getLeft() + mIvHomeControl.getRight()) / 2;
        int centerY = (mIvHomeControl.getBottom() + mIvHomeControl.getTop()) / 2;
        return new Point(centerX - mBtnChain.getWidth() / 2, centerY - mBtnChain.getHeight() / 2);
    }

    private ValueAnimator createAnimator(View view,Point startPoint) {
        ValueAnimator animator;
        if(mAnimatorTag){
             animator = ValueAnimator.ofObject(new PointEvaluator(), startPoint, getEndPoint());
        }else{
            animator = ValueAnimator.ofObject(new PointEvaluator(),  getEndPoint(),startPoint);
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

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                String district = aMapLocation.getDistrict();//城区信息
                String street = aMapLocation.getStreet();//街道信息
                mTvHomeLocation.setText(district+street);
            } else {
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
                Toast.makeText(getApplicationContext(), "定位失败", Toast.LENGTH_LONG).show();
            }
        }
    }


    //    @OnClick({R.id.btn_bind, R.id.btn_device_list})
    //    public void onViewClicked(View view) {
    //        switch (view.getId()) {
    //            case R.id.btn_bind:
    //                IotKitDeviceManager.getInstance().deviceBind("bff503", "bopVpXFcyVbkYBhrhmHK", new
    //                        IotKitCallBack() {
    //                            @Override
    //                            public void onSuccess(String response) {
    //                                ToastUtils.show("绑定成功");
    //
    //                            }
    //
    //                            @Override
    //                            public void onError(int code, String errorMsg) {
    //
    //                            }
    //                        });
    //                break;
    //            case R.id.btn_device_list:
    //                IotKitDeviceManager.getInstance().deviceList(new IotKitCallBack() {
    //                    @Override
    //                    public void onSuccess(String response) {
    //                        startToActivity(DeviceListActivity.class);
    //
    //                    }
    //
    //                    @Override
    //                    public void onError(int code, String errorMsg) {
    //
    //                    }
    //                });
    //                break;
    //        }
    //    }
}
