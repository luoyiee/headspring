package cc.xiaojiang.headspring.utils;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import org.greenrobot.eventbus.EventBus;

import cc.xiaojiang.baselibrary.app.XjConfig;
import cc.xiaojiang.headspring.model.event.LocationEvent;

/**
 * @author :jinjiafeng
 * date:  on 18-7-11
 * description:
 */
public class LocationClient implements AMapLocationListener {
    private static final LocationClient INSTANCE = new LocationClient();
    private AMapLocationClient mLocationClient;

    private LocationClient() {

    }

    public static LocationClient getInstance() {
        return INSTANCE;
    }

    public void startLocation() {
        if(mLocationClient!=null){
            mLocationClient.startLocation();
        }
    }

    public void initClient() {
        //初始化定位
        mLocationClient = new AMapLocationClient(XjConfig.getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(this);
        //初始化定位参数
        AMapLocationClientOption locationOption = new AMapLocationClientOption();
        //设置定位模式为Hight_Accuracy高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        locationOption.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        locationOption.setOnceLocation(false);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        locationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        locationOption.setInterval(1000 * 60);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(locationOption);
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        EventBus.getDefault().post(new LocationEvent(aMapLocation));
    }

    public void onDestroy() {
        if (mLocationClient != null) {
            mLocationClient.onDestroy();//销毁定位客户端。
        }
    }

    public void stopLocation() {
        mLocationClient.startLocation();
    }

}
