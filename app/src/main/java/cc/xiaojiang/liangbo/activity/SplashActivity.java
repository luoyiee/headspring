package cc.xiaojiang.liangbo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.orhanobut.logger.Logger;

import cc.xiaojiang.liangbo.base.BaseActivity;
import cc.xiaojiang.liangbo.utils.AccountUtils;
import cc.xiaojiang.liangbo.utils.ToastUtils;
import cc.xiaojiang.iotkit.account.IotKitAccountCallback;
import cc.xiaojiang.iotkit.account.IotKitAccountManager;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AccountUtils.isLogin()) {
            IotKitAccountManager.getInstance().login(this, null, new IotKitAccountCallback() {
                @Override
                public void onSuccess() {
                    startActivity(MainActivity.class);
                }

                @Override
                public void onFailed(String msg) {
                    ToastUtils.show("连接失败");
                    Logger.e("连接失败");
                    startActivity(MainActivity.class);
                }
            });
        } else {
            startActivity(LoginActivity.class);
        }
    }

    private void startActivity(Class<? extends BaseActivity> clazz) {
        startActivity(new Intent(this, clazz));
        finish();
    }

    @Override
    public void onBackPressed() {
        /**
         *  禁用返回键
         */
    }
}
