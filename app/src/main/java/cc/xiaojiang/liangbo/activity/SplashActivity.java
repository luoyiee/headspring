package cc.xiaojiang.liangbo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import cc.xiaojiang.iotkit.account.IotKitAccountManager;
import cc.xiaojiang.liangbo.utils.AccountUtils;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        finish();
        if (AccountUtils.isLogin()) {
            IotKitAccountManager.getInstance().login(null);
        }
    }

    @Override
    public void onBackPressed() {
        /**
         *  禁用返回键
         */
    }
}
