package cc.xiaojiang.liangbo.activity;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.math.BigDecimal;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import cc.xiaojiang.liangbo.R;
import cc.xiaojiang.liangbo.base.BaseActivity;
import cc.xiaojiang.liangbo.http.HttpResultFunc;
import cc.xiaojiang.liangbo.http.RetrofitHelper;
import cc.xiaojiang.liangbo.http.progress.ProgressObserver;
import cc.xiaojiang.liangbo.model.event.ShareBitmapEvent;
import cc.xiaojiang.liangbo.model.http.AqiModel;
import cc.xiaojiang.liangbo.utils.RxUtils;
import cc.xiaojiang.liangbo.utils.ScreenShotUtils;
import cc.xiaojiang.liangbo.utils.ScreenUtils;
import cc.xiaojiang.liangbo.utils.ToastUtils;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class AirMapActivity extends BaseActivity implements AMap.OnMarkerClickListener, AMap
        .InfoWindowAdapter, AMap.OnCameraChangeListener, AMap.OnMapClickListener {

    @BindView(R.id.mv_map)
    MapView mMapView;
    @BindString(R.string.air_knowledge_excellent)
    String mStrAirExcellent;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    private AMap aMap;
    private View mInfoWindow;
    private Marker mCurrentMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);
        if (aMap == null) {
            aMap = mMapView.getMap();
        }
        //监听地图移动和缩放
        aMap.setOnCameraChangeListener(this);
        aMap.setOnMapClickListener(this);
        AirMapActivityPermissionsDispatcher.showMyLocationWithPermissionCheck(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_air_map;
    }

    @Override
    protected void onDestroy() {
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
        super.onDestroy();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_more) {
            showPupWindow();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showPupWindow() {
        final View contentView = getLayoutInflater().inflate(R.layout.layout_pop_window_air_map, null);
        final PopupWindow popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams
                .WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        contentView.findViewById(R.id.tv_air_map_rank).setOnClickListener(v -> {
            startToActivity(AirMapRankListActivity.class);
            popupWindow.dismiss();
        });
        popupWindow.getContentView().measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec
                .UNSPECIFIED);
        contentView.findViewById(R.id.tv_air_map_share).setOnClickListener(v -> {
            aMap.getMapScreenShot(new AMap.OnMapScreenShotListener() {
                @Override
                public void onMapScreenShot(Bitmap bitmap) {
                    if (null == bitmap) {
                        ToastUtils.show("截取失败,无法分享");
                        return;
                    }
                    EventBus.getDefault().postSticky(new ShareBitmapEvent(bitmap));
                    startToActivity(ShareActivity.class);
                }

                @Override
                public void onMapScreenShot(Bitmap bitmap, int status) {

                }
            });
            popupWindow.dismiss();

        });
        popupWindow.showAsDropDown(mToolbar, ScreenUtils.getScreenWidth(this) - popupWindow
                .getContentView().getMeasuredWidth(), 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_more_dark, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
        //定位

    }

    @Override
    protected void onPause() {
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
        super.onPause();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        mCurrentMarker = marker;
        if (marker.isInfoWindowShown()) {
            marker.hideInfoWindow();
        } else {
            marker.showInfoWindow();
        }
        return true;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        if (mInfoWindow == null) {
            mInfoWindow = LayoutInflater.from(AirMapActivity.this)
                    .inflate(R.layout.custom_info_window, null);
        }
        setInfoWindowData(marker, mInfoWindow);
        return mInfoWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    private void setInfoWindowData(Marker marker, View infoWindow) {
        AqiModel aqiModel = (AqiModel) marker.getObject();
        TextView mTvInfoWindowPm25 = infoWindow.findViewById(R.id.tv_info_window_pm25);
        TextView mTvInfoWindowNo2 = infoWindow.findViewById(R.id.tv_info_window_no2);
        TextView mTvInfoWindowO3 = infoWindow.findViewById(R.id.tv_info_window_o3);
        TextView mTvInfoWindowPm10 = infoWindow.findViewById(R.id.tv_info_window_pm10);
        TextView mTvInfoWindowSo2 = infoWindow.findViewById(R.id.tv_info_window_so2);
        TextView mTvInfoWindowCo = infoWindow.findViewById(R.id.tv_info_window_co);
        TextView mTvInfoWindowLocation = infoWindow.findViewById(R.id.tv_info_window_location);
        mTvInfoWindowPm25.setText(String.valueOf(aqiModel.getPm25()));
        mTvInfoWindowNo2.setText(String.valueOf(aqiModel.getNo2()));
        mTvInfoWindowO3.setText(String.valueOf(aqiModel.getO3()));
        mTvInfoWindowPm10.setText(String.valueOf(aqiModel.getPm10()));
        mTvInfoWindowSo2.setText(String.valueOf(aqiModel.getSo2()));
        mTvInfoWindowCo.setText(String.valueOf(aqiModel.getCo()));
        String title = aqiModel.getCity();
        if (aqiModel.getStation() != null) {
            title = title + aqiModel.getStation();
        }
        mTvInfoWindowLocation.setText(title);
    }

    @NeedsPermission({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission
            .ACCESS_COARSE_LOCATION})
    void showMyLocation() {
        MyLocationStyle myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle
        //定位一次，且将视角移动到地图中心点。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_SHOW);
        myLocationStyle.showMyLocation(false);
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        //显示定位按钮
        aMap.getUiSettings().setMyLocationButtonEnabled(true);
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        aMap.setOnMyLocationChangeListener(new AMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                Logger.d(location.toString());
                aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location
                        .getLatitude(), location.getLongitude()), 10));
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        AirMapActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode,
                grantResults);
    }

    @OnShowRationale({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission
            .ACCESS_COARSE_LOCATION})
    void showLocationRationale(final PermissionRequest request) {
    }

    @OnPermissionDenied({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission
            .ACCESS_COARSE_LOCATION})
    void onLocationDenied() {
    }

    @OnNeverAskAgain({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission
            .ACCESS_COARSE_LOCATION})
    void onNeverAskLocationAgain() {
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        LatLngBounds latLngBounds = aMap.getProjection().getVisibleRegion().latLngBounds;
        float zoom = aMap.getCameraPosition().zoom;
        Logger.d("zoom:" + zoom);
        if (zoom <= 9) {
            getAqi(1, latLngBounds.northeast, latLngBounds.southwest);
        } else {
            getAqi(2, latLngBounds.northeast, latLngBounds.southwest);
        }
    }

    private void getAqi(int level, LatLng northeast, LatLng southwest) {
        RetrofitHelper.getService().getAqi(level, BigDecimal.valueOf(southwest.longitude),
                BigDecimal.valueOf(southwest.latitude),
                BigDecimal.valueOf(northeast.longitude),
                BigDecimal.valueOf(northeast.latitude))
                .map(new HttpResultFunc<>())
                .compose(RxUtils.rxSchedulerHelper())
                .compose(bindToLifecycle())
                .subscribe(new ProgressObserver<List<AqiModel>>(this) {
                    @Override
                    public void onSuccess(List<AqiModel> aqiModels) {
                        aMap.clear();
                        showMarkers(aqiModels);
                    }
                });
    }

    private void showMarkers(List<AqiModel> aqiModels) {
        for (AqiModel aqiModel : aqiModels) {
            showMarker(aqiModel);
        }
    }

    private void showMarker(AqiModel aqiModel) {
        LatLng latLng = new LatLng(aqiModel.getLatitude(), aqiModel.getLongtitude());
        //自定义marker
        View view = getLayoutInflater().inflate(R.layout.layout_marker, null);
        TextView aqi = view.findViewById(R.id.tv_marker_aqi);
        ImageView ivMarker = view.findViewById(R.id.iv_marker_marker);

        ivMarker.setImageResource(getMarkerIcon(aqiModel.getAqi()));
        aqi.setText(String.valueOf(aqiModel.getAqi()));
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.fromView(view));
        Marker marker = aMap.addMarker(markerOptions);
        marker.setObject(aqiModel);
        aMap.setOnMarkerClickListener(this);
        aMap.setInfoWindowAdapter(this);
    }

    private int getMarkerIcon(int aqi) {
        int resourceId;
        if (aqi <= 50) {
            resourceId = R.drawable.ic_aqi_1;
        } else if (aqi <= 100) {
            resourceId = R.drawable.ic_aqi_2;
        } else if (aqi <= 150) {
            resourceId = R.drawable.ic_aqi_3;
        } else if (aqi <= 200) {
            resourceId = R.drawable.ic_aqi_4;
        } else if (aqi <= 300) {
            resourceId = R.drawable.ic_aqi_5;
        } else {
            resourceId = R.drawable.ic_aqi_6;
        }
        return resourceId;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if (mCurrentMarker.isInfoWindowShown()) {
            mCurrentMarker.hideInfoWindow();
        }
    }
}
