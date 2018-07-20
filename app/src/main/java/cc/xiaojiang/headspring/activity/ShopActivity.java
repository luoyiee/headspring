package cc.xiaojiang.headspring.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import butterknife.OnClick;
import cc.xiaojiang.headspring.R;
import cc.xiaojiang.headspring.base.BaseActivity;

public class ShopActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_shop;
    }

    @OnClick({R.id.imageView12, R.id.imageView11, R.id.imageView10, R.id.imageView9, R.id
            .imageView8, R.id.imageView})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imageView:
            case R.id.imageView12:
                to("https://item.jd.com/12112606588.html");
                break;
            case R.id.imageView9:
            case R.id.imageView11:
                to("https://item.jd.com/12112606586.html");
                break;
            case R.id.imageView10:
            case R.id.imageView8:
                to("https://item.jd.com/12112606587.html");
                break;
        }
    }

    private void to(String url) {
        Intent intent = new Intent(this, BrowserActivity.class);
        intent.putExtra("dynamic_url", url);
        startActivity(intent);
    }
}
