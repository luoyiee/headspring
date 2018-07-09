package cc.xiaojiang.headspring.activity;

import android.os.Bundle;
import android.view.View;

import butterknife.BindView;
import butterknife.OnClick;
import cc.xiaojiang.headspring.BuildConfig;
import cc.xiaojiang.headspring.R;
import cc.xiaojiang.headspring.base.BaseActivity;
import cc.xiaojiang.headspring.view.CommonTextView;
import cc.xiaojiang.headspring.view.ItemView;

public class AppUpdateActivity extends BaseActivity {

    @BindView(R.id.tv_app_version)
    CommonTextView mTvAppVersion;
    @BindView(R.id.item_update)
    ItemView mItemUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTvAppVersion.setText(getString(R.string.update_version_name, BuildConfig.VERSION_NAME));
        mItemUpdate.setItemValue(getString(R.string.update_version_name, BuildConfig.VERSION_NAME));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_app_update;
    }

    @OnClick(R.id.item_update)
    public void onViewClicked(View view) {

    }
}
