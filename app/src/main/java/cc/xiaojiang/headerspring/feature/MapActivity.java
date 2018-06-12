package cc.xiaojiang.headerspring.feature;

import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;

import butterknife.BindView;
import cc.xiaojiang.headerspring.R;
import cc.xiaojiang.headerspring.base.BaseActivity;

public class MapActivity extends BaseActivity implements TabLayout.OnTabSelectedListener{

    @BindView(R.id.tl_ranking)
    TabLayout mTlRanking;
    @BindView(R.id.rv_rank_city)
    RecyclerView mRvRankCity;
    private String[] rankTitles = {"日排行", "周排行", "月排行"};
    private static final int RANK_DAY = 0;
    private static final int RANK_WEEK = 1;
    private static final int RANK_MONTH = 2;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_map;
    }

    @Override
    protected void createInit() {
        //禁止手势滑动
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        //当侧边栏显示,不添加阴影
        mDrawerLayout.setScrimColor(Color.TRANSPARENT);
        initTabLayout();

    }

    private void initTabLayout() {
        mTlRanking.addTab(mTlRanking.newTab().setText(rankTitles[RANK_DAY]));
        mTlRanking.addTab(mTlRanking.newTab().setText(rankTitles[RANK_WEEK]));
        mTlRanking.addTab(mTlRanking.newTab().setText(rankTitles[RANK_MONTH]));
        mTlRanking.addOnTabSelectedListener(this);
    }

    @Override
    protected void resumeInit() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_map_city_rank, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_city_rank) {
            if (mDrawerLayout.isDrawerOpen(Gravity.END)) {
                mDrawerLayout.closeDrawer(Gravity.END);
            } else {
                mDrawerLayout.openDrawer(Gravity.END);
            }
        }
        return super.onOptionsItemSelected(item);
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
