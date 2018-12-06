package cc.xiaojiang.liangbo.base.webview;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import cc.xiaojiang.liangbo.BuildConfig;

public class XjWebViewClient extends WebViewClient {
    private static final String TAG = "WebViewHelper";
    private WebViewHelper mHelper;

    public XjWebViewClient(WebViewHelper helper) {
        mHelper = helper;
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        mHelper.onPageStarted();
    }

    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError
            error) {
        super.onReceivedError(view, request, error);
        mHelper.onReceivedError();
    }


    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        mHelper.onPageFinished();
    }
}