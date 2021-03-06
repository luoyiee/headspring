package cc.xiaojiang.liangbo.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.UpgradeInfo;

import java.util.logging.Logger;

import butterknife.BindView;
import butterknife.OnClick;
import cc.xiaojiang.liangbo.BuildConfig;
import cc.xiaojiang.liangbo.R;
import cc.xiaojiang.liangbo.base.BaseActivity;
import cc.xiaojiang.liangbo.view.CommonTextView;
import cc.xiaojiang.liangbo.view.ItemView;

public class AppUpdateActivity extends BaseActivity {

    @BindView(R.id.tv_app_version)
    TextView mTvAppVersion;
    @BindView(R.id.item_update)
    ItemView mItemUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTvAppVersion.setText(getString(R.string.update_version_name, BuildConfig.VERSION_NAME));
        String appVersionName = Beta.appVersionName;
        if (TextUtils.isEmpty(appVersionName)) {
            mItemUpdate.setItemValue( BuildConfig.VERSION_NAME);
        } else {
            mItemUpdate.setItemValue(appVersionName);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_app_update;
    }

    @OnClick(R.id.item_update)
    public void onViewClicked(View view) {
        Beta.checkUpgrade();
    }
}
