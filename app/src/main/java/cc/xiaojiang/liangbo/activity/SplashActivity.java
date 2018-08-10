package cc.xiaojiang.liangbo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

import butterknife.BindView;
import cc.xiaojiang.iotkit.account.IotKitAccountManager;
import cc.xiaojiang.liangbo.R;
import cc.xiaojiang.liangbo.base.BaseActivity;
import cc.xiaojiang.liangbo.utils.AccountUtils;

public class SplashActivity extends BaseActivity {
    @BindView(R.id.iv_splash_logo)
    ImageView mIvSplashLogo;
    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
            if (AccountUtils.isLogin()) {
                IotKitAccountManager.getInstance().login(null);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startAnimate();
        mHandler.postDelayed(mRunnable, 2000);

    }

    private void startAnimate() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setDuration(1800);
        mIvSplashLogo.startAnimation(alphaAnimation);
    }

    @Override
    protected void onDestroy() {
        mIvSplashLogo.clearAnimation();
        mHandler.removeCallbacks(mRunnable);
        super.onDestroy();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public void onBackPressed() {
        /**
         *  禁用返回键
         */
    }
}
