package cc.xiaojiang.liangbo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import butterknife.OnClick;
import cc.xiaojiang.iotkit.http.IotKitHttpUrl;
import cc.xiaojiang.liangbo.R;
import cc.xiaojiang.liangbo.base.BaseActivity;

public class InstructionActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_instruction;
    }

    @OnClick({R.id.item_one_instrument, R.id.item_two_instrument, R.id.item_three_instrument})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.item_one_instrument:
                InstructionsActivity.actionStart(this, 1);
                break;
            case R.id.item_two_instrument:
                InstructionsActivity.actionStart(this, 2);
                break;
            case R.id.item_three_instrument:
                Intent intent = new Intent(this, BrowserActivity.class);
                intent.putExtra("show_title", "APP使用说明书");
                intent.putExtra("dynamic_url", "http://dev.xjiangiot" +
                        ".com/app-preview/instruction/info.html?client_key=i8d53f&value=1");
                startActivity(intent);
                break;
        }
    }
}
