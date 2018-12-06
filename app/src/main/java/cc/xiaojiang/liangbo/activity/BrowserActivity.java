package cc.xiaojiang.liangbo.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.orhanobut.logger.Logger;

import java.util.HashMap;

import butterknife.BindView;
import cc.xiaojiang.liangbo.BuildConfig;
import cc.xiaojiang.liangbo.R;
import cc.xiaojiang.liangbo.base.BaseActivity;
import cc.xiaojiang.liangbo.utils.DbUtils;
import cc.xiaojiang.liangbo.utils.ShareUtils;
import cc.xiaojiang.liangbo.utils.ToastUtils;
import cc.xiaojiang.liangbo.widget.ShareSelectDialog;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;

public class BrowserActivity extends BaseActivity {

    @BindView(R.id.webView)
    WebView mWebView;
    @BindView(R.id.fl_parent)
    FrameLayout mFlParent;
    private String mUrl;
    private String mTitle;
    private String mText;
    private String mShowTitle;
    private boolean mShare;
    private String mLink;

    private PlatformActionListener mPlatformActionListener = new PlatformActionListener() {
        @Override
        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
            ToastUtils.show("分享成功");
        }

        @Override
        public void onError(Platform platform, int i, Throwable throwable) {
            final String error = throwable.toString();
            runOnUiThread(() -> ToastUtils.show("分享失败：" + error));
        }

        @Override
        public void onCancel(Platform platform, int i) {

        }
    };

    public static void actionStart(Context context, String url, String title, String text, String
            buyLink, boolean share) {
        Intent intent = new Intent(context, BrowserActivity.class);
        intent.putExtra("dynamic_url", url);
        intent.putExtra("dynamic_title", title);
        intent.putExtra("dynamic_text", text);
        intent.putExtra("buy_link", buyLink);
        intent.putExtra("share", share);
        context.startActivity(intent);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mUrl = getIntent().getStringExtra("dynamic_url");
        mTitle = getIntent().getStringExtra("dynamic_title");
        mText = getIntent().getStringExtra("dynamic_text");
        mLink = getIntent().getStringExtra("buy_link");
        mShowTitle = getIntent().getStringExtra("show_title");
        mShare = getIntent().getBooleanExtra("share", false);
        invalidateOptionsMenu();
        setTitle("");
        setTitle(mShowTitle);
        super.onCreate(savedInstanceState);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Logger.e("url:" + url);
                if (url.equals(mLink)) {
                    Uri uri = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                    return true;
                }
                return false;

            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request,
                                        WebResourceError error) {
                super.onReceivedError(view, request, error);
                showErrorView();
            }
        });
        WebSettings webSettings = mWebView.getSettings();

        // add java script interface
        webSettings.setJavaScriptEnabled(true);
        // init webView settings
        webSettings.setAllowContentAccess(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setDatabaseEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setSaveFormData(false);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        mWebView.addJavascriptInterface(this, "app");
        mWebView.loadUrl(mUrl);
    }

    public void showErrorView() {
        View errorView = getLayoutInflater().inflate(R.layout.layout_load_error, null);
        if (mFlParent == null || errorView == null) {
            if (BuildConfig.DEBUG) {
                Log.i(TAG, "not show error page");
            }
            return;
        }
        mFlParent.removeAllViews(); //移除加载网页错误时，默认的提示信息
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout
                .LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        //添加自定义的错误提示的View
        mFlParent.addView(errorView, 0, layoutParams);
    }

    @JavascriptInterface
    public String getUuid() {
        return DbUtils.getUserId();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_browser;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem menuItem = menu.getItem(0);
        if (mShare) {
            menuItem.setVisible(true);
        } else {
            menuItem.setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_share, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_share) {
            new ShareSelectDialog()
                    .setOnTimeSelectedListener(platformName -> {
                        if (!platformName.contains("Q")) {
                            ShareUtils.shareWebPager(platformName, mTitle, mUrl, mText,
                                    mPlatformActionListener);
                        }
                    })
                    .show(getSupportFragmentManager(), "");
        }
        return super.onOptionsItemSelected(item);
    }

}
