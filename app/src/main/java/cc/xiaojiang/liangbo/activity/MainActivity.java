package cc.xiaojiang.liangbo.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;

import com.app.hubert.guide.util.LogUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;
import cc.xiaojiang.iotkit.account.IotKitAccountManager;
import cc.xiaojiang.iotkit.bean.http.Article;
import cc.xiaojiang.iotkit.community.IotKitCommunityManager;
import cc.xiaojiang.iotkit.http.IotKitHttpCallback;
import cc.xiaojiang.liangbo.R;
import cc.xiaojiang.liangbo.adapter.ArticleAdapter;
import cc.xiaojiang.liangbo.base.BaseActivity;
import cc.xiaojiang.liangbo.http.LoginInterceptor;
import cc.xiaojiang.liangbo.model.http.DynamicModel;
import cc.xiaojiang.liangbo.utils.DbUtils;
import cc.xiaojiang.liangbo.utils.ScreenUtils;
import cc.xiaojiang.liangbo.utils.ToastUtils;
import cc.xiaojiang.liangbo.view.MainMenuLayout;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;
import static cc.xiaojiang.liangbo.view.MainMenuLayout.ANIM_TIME;

public class MainActivity extends BaseActivity implements BaseQuickAdapter.OnItemClickListener,
        SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {
    @BindView(R.id.view_main_menu)
    MainMenuLayout viewMainMenu;
    @BindView(R.id.iv_device_air)
    ImageView mIvDeviceAir;
    @BindView(R.id.rv_main)
    RecyclerView mRvMain;
    @BindView(R.id.srl_main)
    SwipeRefreshLayout mSrlMain;
    private boolean isMenuHide = false;
    private int mDy;
    private Runnable mMenuShowRunnable;
    private Handler mHandler = new Handler();
    private ArticleAdapter mArticleAdapter;
    private List<Article.ListsBean> mListsBeans;
    private int mCurrentPage = 1;
    private int mTotalPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
        mTvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        initRecycleView();
        getArticleList();
    }

    private void getArticleList() {
        IotKitCommunityManager.getInstance().getArticleList(DbUtils.getUserId(), mCurrentPage,
                20, new IotKitHttpCallback<Article>() {
                    @Override
                    public void onSuccess(Article data) {
                        hideRefresh();
                        mCurrentPage++;
                        mTotalPage = data.getLastPage();
                        mListsBeans.addAll(data.getLists());
                        mArticleAdapter.setNewData(mListsBeans);
                    }

                    @Override
                    public void onError(String code, String errorMsg) {
                        hideRefresh();
                    }

                });
    }

    private void hideRefresh() {
        if (mSrlMain != null && mSrlMain.isRefreshing()) {
            mSrlMain.setRefreshing(false);
        }
    }

    private void initRecycleView() {
        mListsBeans = new ArrayList<>();
        mArticleAdapter = new ArticleAdapter(R.layout.item_main_dynamic, mListsBeans);
        mSrlMain.setOnRefreshListener(this);
        mRvMain.setLayoutManager(new LinearLayoutManager(this));
        mRvMain.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mRvMain.setAdapter(mArticleAdapter);
        mArticleAdapter.setOnItemClickListener(this);
        mArticleAdapter.setOnLoadMoreListener(this, mRvMain);
        mRvMain.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                mDy = dy;
                if (dy > 0) {
                    // scrolling up
                    hideMenu();
                } else {
                    // scrolling down
                    showMenu();
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (mDy > 0 && newState == SCROLL_STATE_IDLE) {
                    mMenuShowRunnable = () -> showMenu();
                    mHandler.postDelayed(mMenuShowRunnable, ANIM_TIME);
                }
            }
        });

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    private void showMenu() {
        if (!isMenuHide) {
            return;
        }
        startAnim();
        isMenuHide = false;
    }

    private void hideMenu() {
        if (isMenuHide) {
            return;
        }
        if (viewMainMenu.isMenuOpen()) {
            viewMainMenu.closeMenu();
        }
        startAnim();
        isMenuHide = true;
    }

    public void startAnim() {
        int translationX = isMenuHide ? 0 : ScreenUtils.dip2px(this, 72);
        final AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator leftAnimator = ObjectAnimator.ofFloat(mIvDeviceAir, "translationX",
                -translationX);
        ObjectAnimator rightAnimator = ObjectAnimator.ofFloat(viewMainMenu, "translationX",
                translationX);
        animatorSet.playTogether(leftAnimator, rightAnimator);
        animatorSet.setDuration(ANIM_TIME);
        animatorSet.start();
    }

    @OnClick({R.id.ctv_chain, R.id.ctv_map, R.id.ctv_device, R.id.ctv_shop, R.id.ctv_personal,
            R.id.iv_device_air})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ctv_chain:
                ToastUtils.show("区块链");
                break;
            case R.id.ctv_map:
                startToActivity(AirMapActivity.class);
                break;
            case R.id.ctv_device:
                LoginInterceptor.interceptor(this, DeviceListActivity.class.getName(), null);
                break;
            case R.id.ctv_shop:
                startToActivity(ShopActivity.class);
                break;
            case R.id.ctv_personal:
                startToActivity(PersonalCenterActivity.class);
                break;
            case R.id.iv_device_air:
                startToActivity(AirActivity.class);
                break;
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Article.ListsBean item = (Article.ListsBean) adapter.getItem(position);
        if (item == null) {
            return;
        }
        BrowserActivity.actionStart(this, item.getInfo_link(), item.getTitle());
    }

    @Override
    public void onRefresh() {
        mListsBeans.clear();
        mCurrentPage = 1;
        getArticleList();
    }

    @Override
    protected void onDestroy() {
        IotKitAccountManager.getInstance().logout();
        super.onDestroy();
    }

    @Override
    public void onLoadMoreRequested() {
        mRvMain.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mCurrentPage <= mTotalPage) {
                    getArticleList();
                } else {
                    mArticleAdapter.loadMoreEnd();
                }
            }
        }, 0);
    }
}
