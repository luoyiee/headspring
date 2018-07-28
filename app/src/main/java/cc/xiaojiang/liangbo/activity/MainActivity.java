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
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;
import cc.xiaojiang.liangbo.R;
import cc.xiaojiang.liangbo.adapter.DynamicAdapter;
import cc.xiaojiang.liangbo.base.BaseActivity;
import cc.xiaojiang.liangbo.http.LoginInterceptor;
import cc.xiaojiang.liangbo.model.http.DynamicModel;
import cc.xiaojiang.liangbo.utils.ScreenUtils;
import cc.xiaojiang.liangbo.utils.ToastUtils;
import cc.xiaojiang.liangbo.view.MainMenuLayout;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;
import static cc.xiaojiang.liangbo.view.MainMenuLayout.ANIM_TIME;

public class MainActivity extends BaseActivity implements BaseQuickAdapter.OnItemClickListener,
        SwipeRefreshLayout.OnRefreshListener{
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
        initRecycleView();
    }

    private void initRecycleView() {
        final ArrayList<DynamicModel> dynamicData = TestData.getDynamicData();
        DynamicAdapter dynamicAdapter = new DynamicAdapter(R.layout.item_main_dynamic, dynamicData);
        mSrlMain.setOnRefreshListener(this);
        mRvMain.setLayoutManager(new LinearLayoutManager(this));
        mRvMain.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mRvMain.setAdapter(dynamicAdapter);
        dynamicAdapter.setOnItemClickListener(this);
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
        animatorSet.playTogether(leftAnimator,rightAnimator);
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
        DynamicModel dynamicModel = (DynamicModel) adapter.getItem(position);
        if(dynamicModel!=null){
            Intent intent = new Intent(this, BrowserActivity.class);
            intent.putExtra("dynamic_title", "title");
            intent.putExtra("dynamic_url", dynamicModel.getUrl());
            startActivity(intent);
        }
    }

    @Override
    public void onRefresh() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(mSrlMain.isRefreshing()){
                    mSrlMain.setRefreshing(false);
                }
            }
        }, 2000);
    }
}
