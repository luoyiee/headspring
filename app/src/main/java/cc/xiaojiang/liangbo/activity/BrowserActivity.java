package cc.xiaojiang.liangbo.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.orhanobut.logger.Logger;
import com.tencent.sonic.sdk.SonicConfig;
import com.tencent.sonic.sdk.SonicEngine;
import com.tencent.sonic.sdk.SonicSession;
import com.tencent.sonic.sdk.SonicSessionConfig;

import java.io.IOException;
import java.util.HashMap;

import butterknife.BindView;
import cc.xiaojiang.liangbo.R;
import cc.xiaojiang.liangbo.base.BaseActivity;
import cc.xiaojiang.liangbo.utils.ScreenShotUtils;
import cc.xiaojiang.liangbo.utils.ShareUtils;
import cc.xiaojiang.liangbo.utils.ToastUtils;
import cc.xiaojiang.liangbo.vassonic.SonicRuntimeImpl;
import cc.xiaojiang.liangbo.vassonic.SonicSessionClientImpl;
import cc.xiaojiang.liangbo.widget.ShareSelectDialog;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class BrowserActivity extends BaseActivity {

    @BindView(R.id.webView)
    WebView mWebView;
    private SonicSession sonicSession;
    private SonicSessionClientImpl mSonicSessionClient;
    private String mUrl;
    private String mTitle;
    private String mText;
    private boolean mShare;

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

    public static void actionStart(Context context, String url, String title, String text,
                                   boolean share) {
        Intent intent = new Intent(context, BrowserActivity.class);
        intent.putExtra("dynamic_url", url);
        intent.putExtra("dynamic_title", title);
        intent.putExtra("dynamic_text", text);
        intent.putExtra("share", share);
        context.startActivity(intent);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mUrl = getIntent().getStringExtra("dynamic_url");
        mTitle = getIntent().getStringExtra("dynamic_title");
        mText = getIntent().getStringExtra("dynamic_text");
        mShare = getIntent().getBooleanExtra("share", false);
        invalidateOptionsMenu();
        setTitle(mTitle);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        // init sonic engine if necessary, or maybe u can do this when application created
        BrowserActivityPermissionsDispatcher.createSonicWithPermissionCheck(this, mUrl);
        super.onCreate(savedInstanceState);

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (sonicSession != null) {
                    sonicSession.getSessionClient().pageFinish(url);
                }
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                if (sonicSession != null) {
                    return (WebResourceResponse) sonicSession.getSessionClient().requestResource
                            (url);
                }
                return null;
            }

            @TargetApi(21)
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest
                    request) {
                return shouldInterceptRequest(view, request.getUrl().toString());
            }
        });

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Logger.e("url:" + url);
                // TODO: 2018/8/14 跳转浏览器
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
        WebSettings webSettings = mWebView.getSettings();

        // add java script interface
        webSettings.setJavaScriptEnabled(true);
        // init webView settings
        webSettings.setAllowContentAccess(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setSaveFormData(false);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);

        // mWebView is ready now, just tell session client to bind
        if (mSonicSessionClient != null) {
            mSonicSessionClient.bindWebView(mWebView);
            mSonicSessionClient.clientReady();
        } else {
            mWebView.loadUrl(mUrl);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        BrowserActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode,
                grantResults);
    }

    @NeedsPermission({Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission
            .READ_EXTERNAL_STORAGE})
    void createSonic(String url) {
        if (!SonicEngine.isGetInstanceAllowed()) {
            SonicEngine.createInstance(new SonicRuntimeImpl(getApplication()), new SonicConfig
                    .Builder().build());
        }

        mSonicSessionClient = null;

        // if it's sonic mode , startup sonic session at first time/
        SonicSessionConfig.Builder sessionConfigBuilder = new SonicSessionConfig.Builder();
        sessionConfigBuilder.setSupportLocalServer(true);

        // create sonic session and run sonic flow
        sonicSession = SonicEngine.getInstance().createSession(url, sessionConfigBuilder.build());
        if (null != sonicSession) {
            sonicSession.bindClient(mSonicSessionClient = new SonicSessionClientImpl());
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_browser;
    }

    @Override
    protected void onDestroy() {
        if (null != sonicSession) {
            sonicSession.destroy();
            sonicSession = null;
        }
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
