package cc.xiaojiang.liangbo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

import com.orhanobut.logger.Logger;

import java.io.IOException;

import butterknife.BindView;
import cc.xiaojiang.iotkit.account.IotKitAccountCallback;
import cc.xiaojiang.iotkit.account.IotKitAccountManager;
import cc.xiaojiang.liangbo.R;
import cc.xiaojiang.liangbo.activity.weather.AirNewActivity;
import cc.xiaojiang.liangbo.base.BaseActivity;
import cc.xiaojiang.liangbo.base.MyApplication;
import cc.xiaojiang.liangbo.http.HttpResultFunc;
import cc.xiaojiang.liangbo.http.RetrofitHelper;
import cc.xiaojiang.liangbo.http.model.BaseModel;
import cc.xiaojiang.liangbo.http.progress.ProgressObserver;
import cc.xiaojiang.liangbo.model.http.RefreshTokenModel;
import cc.xiaojiang.liangbo.utils.AccountUtils;
import cc.xiaojiang.liangbo.utils.DbUtils;
import cc.xiaojiang.liangbo.utils.RxUtils;
import cc.xiaojiang.liangbo.utils.ToastUtils;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static cc.xiaojiang.liangbo.utils.constant.Constant.ACCESS_TOKEN;

public class SplashActivity extends BaseActivity {
    @BindView(R.id.iv_splash_logo)
    ImageView mIvSplashLogo;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                startActivity(new Intent(SplashActivity.this, AirNewActivity.class));
                finish();

            } else {
                AccountUtils.logout();
            }
        }
    };
    private Runnable refreshRunnable = new Runnable() {
        @Override
        public void run() {
            RetrofitHelper.getService().refreshToken()
                    .map(new HttpResultFunc<>())
                    .compose(RxUtils.rxSchedulerHelper())
                    .compose(bindToLifecycle())
                    .subscribe(new Observer<RefreshTokenModel>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(RefreshTokenModel refreshTokenModel) {
                            DbUtils.setAccessToken(refreshTokenModel.getAccessToken());
                            DbUtils.setRefreshToken(refreshTokenModel.getRefreshToken());
                            mHandler.sendEmptyMessage(1);
                        }

                        @Override
                        public void onError(Throwable e) {
                            IotKitAccountManager.getInstance().logout(new IotKitAccountCallback() {
                                @Override
                                public void onCompleted(boolean isSucceed, String msg) {
                                    mHandler.sendEmptyMessage(2);
                                }
                            });
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startAnimate();
        IotKitAccountManager.getInstance().login(new IotKitAccountCallback() {
            @Override
            public void onCompleted(boolean isSucceed, String msg) {

            }
        });
        if (AccountUtils.isLogin()) {
            mHandler.post(refreshRunnable);
        } else {
            mHandler.sendEmptyMessage(1);
        }
    }

    private void startAnimate() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setDuration(1800);
        mIvSplashLogo.startAnimation(alphaAnimation);
    }

    @Override
    protected void onDestroy() {
        mIvSplashLogo.clearAnimation();
        mHandler.removeCallbacks(refreshRunnable);
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
