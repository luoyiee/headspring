package cc.xiaojiang.headspring.feature;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;

import cc.xiaojiang.headspring.R;
import cc.xiaojiang.headspring.base.BaseActivity;

public class FilterTimeRemainActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setBackgroundColor(Color.TRANSPARENT);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_filtere_time_remain;
    }


}
