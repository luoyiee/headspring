package cc.xiaojiang.headspring.model.event;

import com.amap.api.location.AMapLocation;

/**
 * @author :jinjiafeng
 * date:  on 18-7-11
 * description:
 */
public class LocationEvent {
    private AMapLocation mMapLocation;

    public LocationEvent(AMapLocation mapLocation) {
        mMapLocation = mapLocation;
    }

    public AMapLocation getMapLocation() {
        return mMapLocation;
    }
}
