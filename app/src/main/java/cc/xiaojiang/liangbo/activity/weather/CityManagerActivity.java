package cc.xiaojiang.liangbo.activity.weather;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.amap.api.maps.offlinemap.City;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cc.xiaojiang.liangbo.R;
import cc.xiaojiang.liangbo.adapter.WeatherCityManagerAdapter;
import cc.xiaojiang.liangbo.base.BaseActivity;
import cc.xiaojiang.liangbo.http.MyObserver;
import cc.xiaojiang.liangbo.http.RetrofitHelper;
import cc.xiaojiang.liangbo.model.eventbus.MessageCitySwitch;
import cc.xiaojiang.liangbo.model.http.CityAddBody;
import cc.xiaojiang.liangbo.model.http.WeatherCityModel;
import cc.xiaojiang.liangbo.utils.RxUtils;
import cc.xiaojiang.liangbo.utils.ToastUtils;
import cc.xiaojiang.liangbo.utils.constant.Constant;

public class CityManagerActivity extends BaseActivity implements SwipeRefreshLayout
        .OnRefreshListener, BaseQuickAdapter.OnItemChildClickListener {

    @BindView(R.id.rv_city_manager)
    RecyclerView mRvCityManager;
    @BindView(R.id.srl_city_manager)
    SwipeRefreshLayout mSrlCityManager;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;


    private WeatherCityManagerAdapter mWeatherCityManagerAdapter;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_city_manager;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.primary_blue));
        init();

    }


    @Override
    protected void onResume() {
        super.onResume();
        getCityList();
    }

    private void init() {
        Bundle bundle = getIntent().getExtras();
        String city = "";
        if (bundle != null) {
            city = bundle.getString(Constant.CITY);
        }
        mSrlCityManager.setOnRefreshListener(this);
        mWeatherCityManagerAdapter = new WeatherCityManagerAdapter(R.layout
                .item_weather_city_manager, new ArrayList<>(),city);
        mRvCityManager.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager
                .VERTICAL, false));
        mRvCityManager.setAdapter(mWeatherCityManagerAdapter);
        mWeatherCityManagerAdapter.setOnItemChildClickListener(this);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_light, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_add_light) {
            startToActivity(CityAddActivity.class);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        getCityList();
    }

    private void getCityList() {
        RetrofitHelper.getService().cityList()
                .compose(RxUtils.rxSchedulerHelper())
                .compose(bindToLifecycle())
                .subscribe(new MyObserver<List<WeatherCityModel>>() {
                    @Override
                    public void onSucceed(List<WeatherCityModel> weatherCityModels) {
                        if (weatherCityModels == null) {
                            Logger.e("http response error!");
                            return;
                        }
                        mSrlCityManager.setRefreshing(false);
                        mWeatherCityManagerAdapter.setNewData(weatherCityModels);
                    }

                    @Override
                    public void onFailed(String code, String msg) {

                    }
                });
    }

    private void deleteCity(String countyId, int position) {
        RetrofitHelper.getService().cityDel(new CityAddBody(countyId))
                .compose(RxUtils.rxSchedulerHelper())
                .compose(bindToLifecycle())
                .subscribe(new MyObserver<Object>() {
                    @Override
                    public void onSucceed(Object o) {
                        mWeatherCityManagerAdapter.remove(position);
                    }

                    @Override
                    public void onFailed(String code, String msg) {

                    }
                });
    }


    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        WeatherCityModel weatherCityModel = (WeatherCityModel) adapter.getItem(position);
        if (view.getId() == R.id.tv_city_manager_swipe_menu_delete) {
            deleteCity(weatherCityModel.getCountyId(), position);
        }
        if (view.getId() == R.id.cl_city_manager_content) {
            if (TextUtils.isEmpty(weatherCityModel.getCountyId())) {
                ToastUtils.show("城市错误");
                return;
            }
            EventBus.getDefault().post(new MessageCitySwitch(weatherCityModel.getCountyId()));
            finish();
        }
    }
}
