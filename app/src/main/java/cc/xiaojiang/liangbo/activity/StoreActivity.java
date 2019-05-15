package cc.xiaojiang.liangbo.activity;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.ViewGroup;
import android.view.ViewStructure;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import butterknife.BindView;
import cc.xiaojiang.liangbo.R;
import cc.xiaojiang.liangbo.base.BaseActivity;
import cc.xiaojiang.liangbo.base.webview.WebViewActivity;
import cc.xiaojiang.liangbo.base.webview.WebViewHelper;
import cc.xiaojiang.liangbo.base.webview.XjWebViewClient;

/**
 * @author facexyz
 */
public class StoreActivity extends WebViewActivity {
    private static final String STORE_URL = "https://liangboweilai123456.m.yswebportal.cc/col" +
            ".jsp?id=106";

    @BindView(R.id.wv_store_content)
    WebView mWvStoreContent;
    @BindView(R.id.cl_content)
    ConstraintLayout mClContent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWebViewHelper.quickLoadUrl(STORE_URL);
    }

    @Override
    public WebView getWebView() {
        return mWvStoreContent;
    }




    @Override
    protected int getLayoutId() {
        return R.layout.activity_store;
    }


}
