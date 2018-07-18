package cc.xiaojiang.headspring.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import butterknife.BindView;
import cc.xiaojiang.headspring.R;
import cc.xiaojiang.headspring.adapter.RankAdapter;
import cc.xiaojiang.headspring.base.BaseActivity;
import cc.xiaojiang.headspring.http.HttpResultFunc;
import cc.xiaojiang.headspring.http.RetrofitHelper;
import cc.xiaojiang.headspring.http.progress.ProgressObserver;
import cc.xiaojiang.headspring.model.http.AirRankModel;
import cc.xiaojiang.headspring.utils.DbUtils;
import cc.xiaojiang.headspring.utils.RxUtils;
import cc.xiaojiang.headspring.utils.ScreenShotUtils;
import cc.xiaojiang.headspring.utils.ToastUtils;

public class AirMapRankListActivity extends BaseActivity implements TabLayout
        .OnTabSelectedListener {
    private static final String TYPE_DAY = "day";
    private static final String TYPE_WEEK = "week";
    private static final String TYPE_MONTH = "month";
    private static  final String[] TYPES ={TYPE_DAY,TYPE_WEEK,TYPE_MONTH};
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
    private RankAdapter mRankAdapter;
    private SparseArray<List<AirRankModel>> mBufferRankArray = new SparseArray<>(TYPES.length);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTabLayout();
        initView();
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
        getRankList(0);
    }

    private void getRankList(int tabPosition) {
        String city = DbUtils.getLocationCity();
        if (TextUtils.isEmpty(city)) {
            ToastUtils.show("请同意我们的定位权限");
            return;
        }
        RetrofitHelper.getService().airRankList(city, TYPES[tabPosition])
                .map(new HttpResultFunc<>())
                .compose(RxUtils.rxSchedulerHelper())
                .subscribe(new ProgressObserver<List<AirRankModel>>(this) {
                    @Override
                    public void onSuccess(List<AirRankModel> airRankModels) {
                        mRankAdapter.setNewData(airRankModels);
                        mBufferRankArray.put(tabPosition,airRankModels);
                    }
                });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_share, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        int tabPosition = tab.getPosition();
        if(mBufferRankArray.get(tabPosition)!=null){
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
