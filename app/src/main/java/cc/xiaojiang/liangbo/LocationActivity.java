package cc.xiaojiang.liangbo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;

import com.github.mikephil.charting.formatter.IFillFormatter;

import java.util.logging.Logger;

import cc.xiaojiang.liangbo.base.BaseActivity;
import cc.xiaojiang.liangbo.utils.NetworkUtils;


/**
 * 定位activity父类
 */
public abstract class LocationActivity extends BaseActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * gps或网络定位开启时，调用该方法
     */
    public abstract void locationAvailable();

    /**
     * 检测GPS是否打开
     */
    public void checkGps() {
        // TODO: 2018/10/17 优化
//        com.orhanobut.logger.Logger.d(NetworkUtils.isGpsAvailiable(this));
//        com.orhanobut.logger.Logger.d(NetworkUtils.isAGPSAvailiable(this));
//        try {
//            int locationMode = Settings.Secure.getInt(getContentResolver(), Settings.Secure
//                    .LOCATION_MODE);
//            com.orhanobut.logger.Logger.d("locationMode: "+locationMode);
//        } catch (Settings.SettingNotFoundException e) {
//            e.printStackTrace();
//        }
        if (NetworkUtils.isLocationAvailiable(this)) {
            com.orhanobut.logger.Logger.d("location is open");
            locationAvailable();
        }
        if (!NetworkUtils.isGpsAvailiable(this)) {
            new AlertDialog.Builder(this)
                    .setPositiveButton("前往", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            NetworkUtils.openSettingGPS(LocationActivity.this);
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setCancelable(true)
                    .setMessage("请前往GPS设置页面打开GPS")
                    .show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NetworkUtils.REQUEST_SETTING_GPS) {
            if (NetworkUtils.isGpsAvailiable(this)) {
                locationAvailable();
            }
        }
    }
}
