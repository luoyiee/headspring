package cc.xiaojiang.liangbo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import cc.xiaojiang.iotkit.mqtt.IotKitMqttManager;
import cc.xiaojiang.liangbo.utils.AccountUtils;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivity(new Intent(this,MainActivity.class));
        if(AccountUtils.isLogin()){
            IotKitMqttManager.getInstance().startDataService(null);
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
