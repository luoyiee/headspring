package cc.xiaojiang.headspring.activity;

import android.Manifest;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
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

import butterknife.BindView;
import cc.xiaojiang.baselibrary.http.progress.ProgressObserver;
import cc.xiaojiang.baselibrary.util.RxUtils;
import cc.xiaojiang.headspring.R;
import cc.xiaojiang.headspring.base.BaseActivity;
import cc.xiaojiang.headspring.http.HttpResultFunc;
import cc.xiaojiang.headspring.http.RetrofitHelper;
import cc.xiaojiang.headspring.model.event.ShareBitmapEvent;
import cc.xiaojiang.headspring.model.http.AqiModel;
import cc.xiaojiang.headspring.utils.ToastUtils;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class AirMapActivity extends BaseActivity implements AMap.OnMarkerClickListener, AMap
        .InfoWindowAdapter, AMap.OnCameraChangeListener {

    @BindView(R.id.mv_map)
    MapView mMapView;

    private AMap aMap;
    private View mInfoWindow;

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
        int itemId = item.getItemId();
        if (itemId == R.id.action_rank) {
            startToActivity(AirMapRankListActivity.class);
        } else if (itemId == R.id.action_share) {
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
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_map, menu);
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
        mTvInfoWindowPm25.setText(String.valueOf(aqiModel.getPm25()));
        mTvInfoWindowNo2.setText(String.valueOf(aqiModel.getNo2()));
        mTvInfoWindowO3.setText(String.valueOf(aqiModel.getO3()));
        mTvInfoWindowPm10.setText(String.valueOf(aqiModel.getPm10()));
        mTvInfoWindowSo2.setText(String.valueOf(aqiModel.getSo2()));
        mTvInfoWindowCo.setText(String.valueOf(aqiModel.getCo()));
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

        getAqi(zoom, latLngBounds.northeast, latLngBounds.southwest);
    }

    private void getAqi(float level, LatLng northeast, LatLng southwest) {
        // TODO: 2018/7/5 根据最终api文档修改参数
        RetrofitHelper.getService().getAqi(1, BigDecimal.valueOf(northeast.longitude),
                BigDecimal.valueOf(northeast.latitude),
                BigDecimal.valueOf(southwest.longitude),
                BigDecimal.valueOf(southwest.latitude))
                .map(new HttpResultFunc<>())
                .compose(RxUtils.rxSchedulerHelper())
                .subscribe(new ProgressObserver<List<AqiModel>>(this) {
                    @Override
                    public void onSuccess(List<AqiModel> aqiModels) {
                        for (AqiModel aqiModel : aqiModels) {
                            showMarker(aqiModel);
                        }
                    }
                });
    }

    private void showMarker(AqiModel aqiModel) {
        LatLng latLng = new LatLng(aqiModel.getLatitude(), aqiModel
                .getLongitude());
        //自定义marker
        View view = getLayoutInflater().inflate(R.layout.layout_marker, null);
        TextView aqi = view.findViewById(R.id.tv_marker_aqi);
        ImageView ivMarker = view.findViewById(R.id.iv_marker_marker);
        // TODO: 2018/7/5 根据aqi设置不同颜色的marker
        //        ivMarker.setImageResource(R.drawable.ic_air_map_marker);
        aqi.setText(String.valueOf(aqiModel.getAqi()));
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.fromView(view));
        Marker marker = aMap.addMarker(markerOptions);
        marker.setObject(aqiModel);
        aMap.setOnMarkerClickListener(this);
        aMap.setInfoWindowAdapter(this);
    }
}
