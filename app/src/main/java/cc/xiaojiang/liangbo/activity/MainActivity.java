package cc.xiaojiang.liangbo.activity;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;

import butterknife.BindView;
import butterknife.OnClick;
import cc.xiaojiang.liangbo.R;
import cc.xiaojiang.liangbo.base.BaseActivity;
import cc.xiaojiang.liangbo.http.LoginInterceptor;
import cc.xiaojiang.liangbo.utils.ScreenUtils;
import cc.xiaojiang.liangbo.utils.ToastUtils;
import cc.xiaojiang.liangbo.view.MainMenuLayout;

import static cc.xiaojiang.liangbo.view.MainMenuLayout.ANIM_TIME;

public class MainActivity extends BaseActivity {
    @BindView(R.id.view_main_menu)
    MainMenuLayout viewMainMenu;
    private boolean isMenuHide = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        ObjectAnimator rightAnimator = ObjectAnimator.ofFloat(viewMainMenu, "translationX",
                translationX);
        rightAnimator.setDuration(ANIM_TIME);
        rightAnimator.start();
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
                LoginInterceptor.interceptor(this, DeviceListActivity.class.getSimpleName(), null);
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
}
