package cc.xiaojiang.liangbo.activity.weather;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;
import cc.xiaojiang.liangbo.LocationActivity;
import cc.xiaojiang.liangbo.R;
import cc.xiaojiang.liangbo.adapter.WeatherRecommendCityAdapter;
import cc.xiaojiang.liangbo.base.BaseActivity;
import cc.xiaojiang.liangbo.http.MyObserver;
import cc.xiaojiang.liangbo.http.RetrofitHelper;
import cc.xiaojiang.liangbo.model.eventbus.MessageCityAdd;
import cc.xiaojiang.liangbo.model.http.CityAddBody;
import cc.xiaojiang.liangbo.utils.LocationClient;
import cc.xiaojiang.liangbo.utils.RecomendCityUtils;
import cc.xiaojiang.liangbo.utils.RxUtils;
import cc.xiaojiang.liangbo.utils.ToastUtils;
import cc.xiaojiang.liangbo.utils.WeatherUtils;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class CityAddActivity extends LocationActivity implements BaseQuickAdapter
        .OnItemClickListener, View.OnTouchListener {

    @BindView(R.id.rv_weather_city_add)
    RecyclerView mRvWeatherCityAdd;
    @BindView(R.id.edTxt_city_search_city)
    AppCompatEditText mEdTxtCitySearchCity;
    @BindView(R.id.cl_city_search_view)
    ConstraintLayout mClCitySearchView;

    private WeatherRecommendCityAdapter mWeatherRecommendCityAdapter;
    private LocationClient mLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        init();
    }

    @Override
    public void locationAvailable() {
        CityAddActivityPermissionsDispatcher.requestMyLocationWithPermissionCheck
                (CityAddActivity.this);
    }

    private void init() {
        //??????EditText
        mEdTxtCitySearchCity.setFocusable(false);
        mEdTxtCitySearchCity.setClickable(true);
        mEdTxtCitySearchCity.setOnTouchListener(this);
        //???????????????
        mWeatherRecommendCityAdapter = new WeatherRecommendCityAdapter(R.layout
                .item_weather_recommend_city, RecomendCityUtils.getRecommendCities());
        mRvWeatherCityAdd.setLayoutManager(new GridLayoutManager(this, 3));
        mRvWeatherCityAdd.setAdapter(mWeatherRecommendCityAdapter);
        mWeatherRecommendCityAdapter.setOnItemClickListener(this);
        mWeatherRecommendCityAdapter.setHeaderViewAsFlow(true);
        mWeatherRecommendCityAdapter.addHeaderView(getHeaderView(), 0);
    }

    private View getHeaderView() {
        View view = getLayoutInflater().inflate(R.layout.header_recommend_city_location, null);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               checkGps();
            }
        });
        return view;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageCityAdd messageCityAdd) {
        finish();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_weather_city_add;
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        String city = (String) adapter.getItem(position);
        cityAdd(WeatherUtils.getXinZhiCityCode(city));

    }

    private void cityAdd(String id) {
        if (TextUtils.isEmpty(id)) {
            ToastUtils.show("?????????????????????");
            return;
        }
        CityAddBody cityAddBody = new CityAddBody(id);
        RetrofitHelper.getService().cityAdd(cityAddBody)
                .compose(RxUtils.rxSchedulerHelper())
                .compose(bindToLifecycle())
                .subscribe(new MyObserver<Object>() {
                    @Override
                    public void onSucceed(Object o) {
                        // TODO: 2018/10/12 ??????????????????
                        ToastUtils.show("????????????");
                        finish();
                    }

                    @Override
                    public void onFailed(String code, String msg) {
                        if ("1112".equals(code)) {
                            ToastUtils.show("??????????????????????????????");
                        }
                    }
                });
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
                    //?????????????????????????????????????????????
                    cityAdd(WeatherUtils.getXinZhiCityCode(aMapLocation.getCityCode(),
                            aMapLocation.getAdCode()));
                } else {
                    //??????????????????ErrCode???????????????errInfo???????????????????????????????????????
                    Log.e("AmapError", "location Error, ErrCode:"
                            + aMapLocation.getErrorCode() + ", errInfo:"
                            + aMapLocation.getErrorInfo());
                    Toast.makeText(getApplicationContext(), "????????????", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        CityAddActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode,
                grantResults);
    }

    @OnShowRationale({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission
            .ACCESS_COARSE_LOCATION})
    void onLocationRationale(final PermissionRequest request) {
        new AlertDialog.Builder(this)
                .setPositiveButton("??????", (dialog, which) -> request.proceed())
                .setNegativeButton("??????", (dialog, which) -> request.cancel())
                .setCancelable(false)
                .setMessage("????????????????????????????????????")
                .show();
    }

    @OnPermissionDenied({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission
            .ACCESS_COARSE_LOCATION})
    void onLocationDenied() {
        ToastUtils.show("????????????????????????");
    }

    @OnNeverAskAgain({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission
            .ACCESS_COARSE_LOCATION})
    void onNeverAskAgain() {
        ToastUtils.show("??????APP?????????????????????????????????????????????");
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            Intent intent = new Intent(CityAddActivity.this, CitySearchActivity.class);
            startActivity(intent);                //??????
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
        return false;
    }
}
