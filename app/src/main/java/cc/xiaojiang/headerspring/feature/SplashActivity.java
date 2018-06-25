package cc.xiaojiang.headerspring.feature;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import cc.xiaojiang.headerspring.R;
import cc.xiaojiang.headerspring.utils.AccountUtils;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AccountUtils.isLogin()) {
            startActivity(new Intent(this, MainActivity.class));
        } else {
            startActivity(new Intent(this, LoginActivity.class));
        }
        finish();

    }

    @Override
    public void onBackPressed() {
        /**
         *  禁用返回键
         */
    }
}
