package cc.xiaojiang.headspring.activity;

import android.os.Bundle;
import android.view.View;

import butterknife.OnClick;
import cc.xiaojiang.headspring.R;
import cc.xiaojiang.headspring.base.BaseActivity;

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
                break;
            case R.id.item_two_instrument:
                break;
            case R.id.item_three_instrument:
                break;
        }
    }
}
