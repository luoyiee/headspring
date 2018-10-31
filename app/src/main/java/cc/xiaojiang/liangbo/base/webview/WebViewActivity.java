package cc.xiaojiang.liangbo.base.webview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;

import cc.xiaojiang.liangbo.R;
import cc.xiaojiang.liangbo.base.BaseActivity;

public abstract class WebViewActivity extends BaseActivity {
    public WebViewHelper mWebViewHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWebView();
    }


    public void initWebView() {
        mWebViewHelper = new WebViewHelper(this, getWebView());
        View loadView = getLayoutInflater().inflate(R.layout.layout_loading, null);
        View errorView = getLayoutInflater().inflate(R.layout.layout_load_error, null);
        mWebViewHelper.setErrorView(null, errorView, null);
//        mWebViewHelper.setWebViewClient(new XjWebViewClient(mWebViewHelper) {
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//                return false;
//            }
//        });
//        mWebViewHelper.loadUrl(STORE_URL);
    }

    public abstract WebView getWebView();


    @Override
    public void onBackPressed() {
        if (mWebViewHelper != null) {
            mWebViewHelper.onBackPressed();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        if (mWebViewHelper != null) {
            mWebViewHelper.destroy();
        }
        super.onDestroy();
    }
}
