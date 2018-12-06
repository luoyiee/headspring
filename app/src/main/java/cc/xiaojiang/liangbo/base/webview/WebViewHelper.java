package cc.xiaojiang.liangbo.base.webview;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import java.util.logging.Logger;

import cc.xiaojiang.liangbo.BuildConfig;
import cc.xiaojiang.liangbo.R;
import cc.xiaojiang.liangbo.utils.ToastUtils;


public class WebViewHelper {
    private static final String TAG = "WebViewHelper";
    private WebView mWebView;
    private XjWebViewClient mXjWebViewClient;
    private View mErrorView;
    private View mLoadView;
    private Activity mActivity;
    private boolean isLoadError = false;
    private View.OnClickListener mListener;
    private ViewGroup mParent;

    public WebViewHelper(Activity activity, WebView webView) {
        if (webView == null) {
            throw new NullPointerException("init WebViewHelper with null webView!");
        }
        mActivity = activity;
        mWebView = webView;
        mParent = (ViewGroup) webView.getParent();
    }


    public WebView getWebView() {
        return mWebView;
    }


    public void loadUrl(String url) {
        if (mXjWebViewClient == null) {
            mXjWebViewClient = new XjWebViewClient(this);
        }
        mWebView.setWebViewClient(mXjWebViewClient);
        mWebView.loadUrl(url);
    }


    public void quickLoadUrl(String url) {
        WebSettings webSettings = mWebView.getSettings();
        // 设置与Js交互的权限
        webSettings.setJavaScriptEnabled(true);
        // 设置允许JS弹窗
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        this.loadUrl(url);
    }

    public void reLoad() {
        mWebView.reload();
        isLoadError = false;
        if (BuildConfig.DEBUG) {
            Log.i(TAG, "webView reload");
        }
    }


    public void setErrorView(View loadView, View errorView, View.OnClickListener listener) {
        mLoadView = loadView;
        mErrorView = errorView;
        mListener = listener;
    }

    public void setWebViewClient(XjWebViewClient webViewClient) {
        mXjWebViewClient = webViewClient;

    }


    public void destroy() {
        //释放资源
        mWebView.destroy();
        mWebView = null;
    }

    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            mActivity.finish();
        }
    }


    public void onReceivedError() {
        isLoadError = true;
    }

    public void onPageFinished() {
        if (mParent == null) {
            if (BuildConfig.DEBUG) {
                Log.e(TAG, "webView getParent() null !");
            }
            return;
        }
        if (mListener == null) {
            mParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    reLoad();
                }
            });
        } else {
            mErrorView.setOnClickListener(mListener);
        }
        mParent.removeAllViews(); //移除当前View
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup
                .LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        if (isLoadError) {
            if (mErrorView == null) {
                if (BuildConfig.DEBUG) {
                    Log.i(TAG, "not show error page");
                }
                return;
            }
            //添加自定义的错误提示的View
            mParent.addView(mErrorView, 0, layoutParams);
        } else {
            //显示webView
            mParent.addView(mWebView, 0, layoutParams);
        }
    }

    public void onPageStarted() {
        if (mParent == null) {
            if (BuildConfig.DEBUG) {
                Log.e(TAG, "webView getParent() null !");
            }
            return;
        }
        mParent.removeAllViews(); //移除当前View
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup
                .LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        if (mLoadView == null) {
            if (BuildConfig.DEBUG) {
                Log.i(TAG, "not show error page");
            }
            return;
        }
        //添加自定义的错误提示的View
        mParent.addView(mLoadView, 0, layoutParams);
    }
}
