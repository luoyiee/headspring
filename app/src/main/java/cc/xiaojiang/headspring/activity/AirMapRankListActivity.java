package cc.xiaojiang.headspring.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;

import butterknife.BindView;
import cc.xiaojiang.headspring.R;
import cc.xiaojiang.headspring.base.BaseActivity;

public class AirMapRankListActivity extends BaseActivity implements TabLayout.OnTabSelectedListener{

    @BindView(R.id.tl_ranking)
    TabLayout mTlRanking;
    @BindView(R.id.rv_rank_city)
    RecyclerView mRvRankCity;
    private String[] rankTitles = {"日排行", "周排行", "月排行"};
    private static final int RANK_DAY = 0;
    private static final int RANK_WEEK = 1;
    private static final int RANK_MONTH = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTabLayout();
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
        getMenuInflater().inflate(R.menu.menu_map_city_rank, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        switch (tab.getPosition()) {
            case RANK_DAY:

                break;
            case RANK_WEEK:

                break;
            case RANK_MONTH:

                break;

            default:
                break;
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
