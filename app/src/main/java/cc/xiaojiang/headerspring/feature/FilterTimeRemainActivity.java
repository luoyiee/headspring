package cc.xiaojiang.headerspring.feature;

import android.graphics.Color;

import cc.xiaojiang.headerspring.R;
import cc.xiaojiang.headerspring.base.BaseActivity;

public class FilterTimeRemainActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_filtere_time_remain;
    }

    @Override
    protected void createInit() {
        getWindow().getDecorView().setBackgroundColor(Color.TRANSPARENT);
    }

    @Override
    protected void resumeInit() {

    }
}
