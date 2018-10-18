package cc.xiaojiang.liangbo.activity;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

import java.util.List;

import butterknife.BindView;
import cc.xiaojiang.liangbo.R;
import cc.xiaojiang.liangbo.adapter.RankAdapter;
import cc.xiaojiang.liangbo.base.BaseActivity;
import cc.xiaojiang.liangbo.http.HttpResultFunc;
import cc.xiaojiang.liangbo.http.RetrofitHelper;
import cc.xiaojiang.liangbo.http.progress.ProgressObserver;
import cc.xiaojiang.liangbo.model.http.AirRankModel;
import cc.xiaojiang.liangbo.utils.LocationClient;
import cc.xiaojiang.liangbo.utils.RxUtils;
import cc.xiaojiang.liangbo.utils.ScreenShotUtils;
import cc.xiaojiang.liangbo.utils.ToastUtils;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class AirMapRankListActivity extends BaseActivity implements TabLayout
        .OnTabSelectedListener {
    private static final String TYPE_DAY = "day";
    private static final String TYPE_WEEK = "week";
    private static final String TYPE_MONTH = "month";
    private static final String[] TYPES = {TYPE_DAY, TYPE_WEEK, TYPE_MONTH};
    private static final int RANK_DAY = 0;
    private static final int RANK_WEEK = 1;
    private static final int RANK_MONTH = 2;
    @BindView(R.id.tl_ranking)
    TabLayout mTlRanking;
    @BindView(R.id.rv_rank_city)
    RecyclerView mRvRankCity;
    @BindView(R.id.appbar)
    AppBarLayout mAppbar;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    private String[] rankTitles = {"日排行", "周排行", "月排行"};
    private LocationClient mLocationClient;
    private RankAdapter mRankAdapter;
    private SparseArray<List<AirRankModel>> mBufferRankArray = new SparseArray<>(TYPES.length);
    private String mCity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTabLayout();
        initView();
        mLocationClient = new LocationClient();
        AirMapRankListActivityPermissionsDispatcher.locationWithPermissionCheck(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_map;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_share) {
            ScreenShotUtils.share(this);
        }
        return super.onOptionsItemSelected(item);
    }

    private void initTabLayout() {
        mTlRanking.addTab(mTlRanking.newTab().setText(rankTitles[RANK_DAY]));
        mTlRanking.addTab(mTlRanking.newTab().setText(rankTitles[RANK_WEEK]));
        mTlRanking.addTab(mTlRanking.newTab().setText(rankTitles[RANK_MONTH]));
        mTlRanking.addOnTabSelectedListener(this);
    }

    private void initView() {
        mRankAdapter = new RankAdapter(R.layout.item_rank, null);
        mRvRankCity.setLayoutManager(new LinearLayoutManager(this));
        mRvRankCity.setAdapter(mRankAdapter);
    }

    @NeedsPermission({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission
            .ACCESS_COARSE_LOCATION})
    void location() {
        mLocationClient.initClient(this);
        mLocationClient.startLocation(aMapLocation -> {
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                    Logger.d(aMapLocation.toString());
                    //定位成功回调信息，设置相关消息
                    //街道信息
                    mCity = aMapLocation.getCity();
                    getRankList(0);
                } else {
                    //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + aMapLocation.getErrorCode() + ", errInfo:"
                            + aMapLocation.getErrorInfo());
                    Toast.makeText(getApplicationContext(), "定位失败", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void getRankList(int tabPosition) {
        if (TextUtils.isEmpty(mCity)) {
            ToastUtils.show("获取位置信息失败");
            return;
        }
        RetrofitHelper.getService().airRankList(mCity, TYPES[tabPosition])
                .map(new HttpResultFunc<>())
                .compose(RxUtils.rxSchedulerHelper())
                .compose(bindToLifecycle())
                .subscribe(new ProgressObserver<List<AirRankModel>>(this) {
                    @Override
                    public void onSuccess(List<AirRankModel> airRankModels) {
                        mRankAdapter.setNewData(airRankModels);
                        mBufferRankArray.put(tabPosition, airRankModels);
                    }
                });
    }

    @Override
    protected void onDestroy() {
        mLocationClient.stopLocation();
        mLocationClient.onDestroy();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_share_dark, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        AirMapRankListActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode,
                grantResults);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        int tabPosition = tab.getPosition();
        if (mBufferRankArray.get(tabPosition) != null) {
            mRankAdapter.setNewData(mBufferRankArray.get(tabPosition));
            return;
        }
        getRankList(tabPosition);
    }


    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
