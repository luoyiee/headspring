//package com.xjiangiot.demo.common.base;
//
//import android.Manifest;
//import android.content.DialogInterface;
//import android.os.Build;
//import android.support.annotation.NonNull;
//import android.support.v7.app.AlertDialog;
//import android.text.TextUtils;
//
//import com.topright.baselibrary.util.ToastUtils;
//
//import cc.xiaojiang.sensiti.R;
//import cc.xiaojiang.sensiti.util.NetworkUtils;
//import permissions.dispatcher.NeedsPermission;
//import permissions.dispatcher.OnNeverAskAgain;
//import permissions.dispatcher.OnPermissionDenied;
//import permissions.dispatcher.OnShowRationale;
//import permissions.dispatcher.PermissionRequest;
//import permissions.dispatcher.RuntimePermissions;
//
///**
// * Created by facexxyz on 2018/11/27
// */
//@RuntimePermissions
//public abstract class BaseNetworkActivity extends BaseActivity {
//    // TODO: 2018/11/29 监听网络变化
//    @Override
//    protected void onResume() {
//        super.onResume();
//        initWifi();
//    }
//
//    private void initWifi() {
//        if (!(NetworkUtils.isWifiEnabled(this) || NetworkUtils.isWifiContected(this))) {
//            showDialog(getString(R.string.open_wifi_in_setting));
//            return;
//        }
//        if (checkWifi24G() && (!NetworkUtils.isWifi24G(this))) {
//            showDialog(getString(R.string.change_wifi_5g_not_support));
//            return;
//        }
//        if (getSsid()) {
//            getConnectedSsid();
//        }
//    }
//
//    public void showDialog(String msg) {
//        new AlertDialog.Builder(this)
//                .setPositiveButton(R.string.go, new DialogInterface
//                        .OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        NetworkUtils.openWifiSettings(BaseNetworkActivity.this);
//                    }
//                })
//                .setCancelable(false)
//                .setMessage(msg)
//                .show();
//    }
//
//
//    @NeedsPermission({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission
//            .ACCESS_COARSE_LOCATION})
//    void needLocation() {
//        getSsid2();
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
//                                           @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        BaseNetworkActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode,
//                grantResults);
//    }
//
//    @OnShowRationale({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission
//            .ACCESS_COARSE_LOCATION})
//    void onShowLocationRationale(final PermissionRequest request) {
//        new AlertDialog.Builder(this)
//                .setPositiveButton(R.string.device_search_dialog_positive, new DialogInterface
//                        .OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        request.proceed();
//                    }
//                })
//                .setNegativeButton(R.string.device_search_dialog_refuse, new DialogInterface
//                        .OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        request.cancel();
//                    }
//                })
//                .setCancelable(false)
//                .setMessage(R.string.get_ssid_need_location_permission)
//                .show();
//    }
//
//    @OnPermissionDenied({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission
//            .ACCESS_COARSE_LOCATION})
//    void onLocationPermissionDenied() {
//        ToastUtils.show(R.string.toast_location_denied);
//    }
//
//    @OnNeverAskAgain({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission
//            .ACCESS_COARSE_LOCATION})
//    void onNeverAskLocationAgain() {
//        ToastUtils.show(R.string.toast_location_never_ask);
//    }
//
//    public void getConnectedSsid() {
//        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.P) {
//            BaseNetworkActivityPermissionsDispatcher.needLocationWithPermissionCheck(this);
//        } else {
//            getSsid2();
//        }
//    }
//
//    private void getSsid2() {
//        String ssid = NetworkUtils.getWifiConnectedSsid(this);
//        if (TextUtils.isEmpty(ssid)) {
//            ToastUtils.show(R.string.can_not_get_ssid);
//        } else {
//            onSsidGeted(ssid);
//        }
//    }
//
//    public void openWifiSetting() {
//        NetworkUtils.openWifiSettings(this);
//    }
//
//
//    /**
//     * 获得的ssid
//     */
//    public abstract void onSsidGeted(String ssid);
//
//
//    /**
//     * 是否获取ssid
//     */
//    public abstract boolean getSsid();
//
//    /**
//     * 是否检测2.4G wifi
//     */
//    public abstract boolean checkWifi24G();
//}
