package cc.xiaojiang.liangbo.activity;

import android.os.Bundle;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.sql.DataTruncation;

import butterknife.BindView;
import cc.xiaojiang.liangbo.R;
import cc.xiaojiang.liangbo.base.BaseActivity;

public class StoreActivity extends BaseActivity {
    private static final String STORE_URL = "https://liangboweilai123456.m.yswebportal.cc/col" +
            ".jsp?id=106";

    @BindView(R.id.wv_store_content)
    WebView mWvStoreContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();

    }

    private void init() {
        WebSettings webSettings = mWvStoreContent.getSettings();

        // add java script interface
        webSettings.setJavaScriptEnabled(true);
        // init webView settings
//        webSettings.setAllowContentAccess(true);
//        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
//        webSettings.setDatabaseEnabled(true);
//        webSettings.setDomStorageEnabled(true);
//        webSettings.setAppCacheEnabled(true);
//        webSettings.setSaveFormData(false);
        webSettings.setUseWideViewPort(true);
//        webSettings.setLoadWithOverviewMode(true);
        mWvStoreContent.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false;
            }
        });
        mWvStoreContent.loadUrl(STORE_URL);
    }

    @Override
    public void onBackPressed() {
        if (mWvStoreContent.canGoBack()) {
            mWvStoreContent.goBack();
        } else {
            finish();
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_store;
    }

    @Override
    protected void onDestroy() {
        //释放资源
        mWvStoreContent.destroy();
        mWvStoreContent = null;
        super.onDestroy();
    }
}
