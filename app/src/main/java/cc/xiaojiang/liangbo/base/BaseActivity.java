package cc.xiaojiang.liangbo.base;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.Objects;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import cc.xiaojiang.liangbo.R;
import cc.xiaojiang.liangbo.utils.NetworkUtils;
import cc.xiaojiang.liangbo.utils.ScreenUtils;
import cc.xiaojiang.liangbo.utils.ToastUtils;


/**
 * @author jinjiafeng
 * Time :18-4-22
 */

public abstract class BaseActivity extends RxAppCompatActivity {

    protected final String TAG = this.getClass().getSimpleName();
    private Unbinder unBinder;
    protected TextView mTvTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.d("onCreate……");
        ScreenUtils.setCustomDensity(this);
        setContentView(getLayoutId());
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        AppManager.getInstance().addActivity(this);
        //  ButterKnife注解注入
        unBinder = ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        mTvTitle = findViewById(R.id.tv_title);
        if (toolbar != null) {
            toolbar.setTitle("");
            setSupportActionBar(toolbar);
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        }
        checkNetwork();
    }

    private void checkNetwork() {
        if (!NetworkUtils.isConnected(this)) {
            ToastUtils.show("没有检测到网络连接，请检查手机网络");
        }
    }
    /**
     * 初始化布局id
     * @return 布局id
     */
    protected abstract int getLayoutId();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unBinder != null && unBinder != Unbinder.EMPTY) {
            unBinder.unbind();
            unBinder = null;
        }
//        AppManager.getInstance().finishActivity(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onTitleChanged(CharSequence title, int color) {
        super.onTitleChanged(title, color);
        if (mTvTitle != null) {
            mTvTitle.setText(title);
            Logger.d("set title: " + title);
        }
    }

    /**
     * 启动一个Activity
     *
     * @param activity 需要启动的Activity的Class
     */
    public void startToActivity(Class<? extends Activity> activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }
}
