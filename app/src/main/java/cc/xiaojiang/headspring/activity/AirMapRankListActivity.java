package cc.xiaojiang.headspring.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import cc.xiaojiang.headspring.R;
import cc.xiaojiang.headspring.adapter.RankAdapter;
import cc.xiaojiang.headspring.base.BaseActivity;
import cc.xiaojiang.headspring.model.http.RankModel;
import cc.xiaojiang.headspring.utils.ScreenShotUtils;

public class AirMapRankListActivity extends BaseActivity implements TabLayout
        .OnTabSelectedListener {

    @BindView(R.id.tl_ranking)
    TabLayout mTlRanking;
    @BindView(R.id.rv_rank_city)
    RecyclerView mRvRankCity;
    @BindView(R.id.appbar)
    AppBarLayout mAppbar;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    private String[] rankTitles = {"日排行", "周排行", "月排行"};
    private static final int RANK_DAY = 0;
    private static final int RANK_WEEK = 1;
    private static final int RANK_MONTH = 2;
    private RankAdapter mRankAdapter;
    private List<RankModel> mRankModels;
    private TextView mHeaderRank;
    private TextView mHeaderProvice;
    private TextView mHeaderCity;
    private TextView mHeaderAqi;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTabLayout();
        initView();
    }

    private void initView() {
        mRankModels = new ArrayList<>();
        mRankAdapter = new RankAdapter(R.layout.item_rank, mRankModels);
        mRvRankCity.setLayoutManager(new LinearLayoutManager(this));
        mRvRankCity.setAdapter(mRankAdapter);
        mRankAdapter.addHeaderView(getHeaderView());
        getRankList(0);
    }

    private View getHeaderView() {
        View headerView = getLayoutInflater().inflate(R.layout.item_rank, null);
        LinearLayout linearLayout = headerView.findViewById(R.id.ll_rank);
        linearLayout.setBackgroundColor(Color.parseColor("#9ADFDA"));
        mHeaderRank = headerView.findViewById(R.id.tv_rank_rank);
        mHeaderProvice = headerView.findViewById(R.id.tv_rank_province);
        mHeaderCity = headerView.findViewById(R.id.tv_rank_city);
        mHeaderAqi = headerView.findViewById(R.id.tv_rank_aqi);
        return headerView;
    }

    private void getRankList(int position) {
        mRankModels.clear();
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            RankModel rankModel = new RankModel();
            rankModel.setRank(String.valueOf(i + 1));
            rankModel.setProvince("省份" + position);
            rankModel.setCity("城市" + position);
            rankModel.setAqi(String.valueOf(random.nextInt(10000)));
            mRankModels.add(rankModel);
        }
        mRankAdapter.setNewData(mRankModels);
        mHeaderRank.setText("56");
        mHeaderProvice.setText("浙江省");
        mHeaderCity.setText("宁波市");
        mHeaderAqi.setText("100");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_map;
    }

    private void initTabLayout() {
        mTlRanking.addTab(mTlRanking.newTab().setText(rankTitles[RANK_DAY]));
        mTlRanking.addTab(mTlRanking.newTab().setText(rankTitles[RANK_WEEK]));
        mTlRanking.addTab(mTlRanking.newTab().setText(rankTitles[RANK_MONTH]));
        mTlRanking.addOnTabSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_share, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_share) {
            ScreenShotUtils.share(this);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        getRankList(tab.getPosition());
    }


    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
