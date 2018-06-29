package cc.xiaojiang.headspring.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import butterknife.BindView;
import cc.xiaojiang.headspring.R;
import cc.xiaojiang.headspring.base.BaseActivity;
import cc.xiaojiang.headspring.utils.ToastUtils;

public class DynamicActivity extends BaseActivity {

    @BindView(R.id.wv_dynamic)
    WebView mWvDynamic;
    private String mTitle;
    private String mUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();

    }

    private void initData() {
        Intent intent = getIntent();
        mTitle = intent.getStringExtra("dynamic_title");
        mUrl = intent.getStringExtra("dynamic_url");
        initView();
    }

    private void initView() {
        setTitle(mTitle);
        WebSettings settings = mWvDynamic.getSettings();
        settings.setJavaScriptEnabled(true);
        mWvDynamic.setWebViewClient(new WebViewClient());
        mWvDynamic.loadUrl(mUrl);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_dynamic;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_share, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_share) {
            ToastUtils.show("分享");
        }
        return super.onOptionsItemSelected(item);
    }
}
