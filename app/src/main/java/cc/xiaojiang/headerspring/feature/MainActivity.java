package cc.xiaojiang.headerspring.feature;

import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import cc.xiaojiang.headerspring.R;
import cc.xiaojiang.headerspring.base.BaseActivity;

public class MainActivity extends BaseActivity {

    @BindView(R.id.btn_test)
    Button btnTest;
    @BindView(R.id.tv_outdoor_pm)
    TextView mTvOutdoorPm;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void createInit() {
//        mTvOutdoorPm.setText(new SpanUtils()
//                .append("50").setFontSize(28,true).append("ug/m").append("3").setSuperscript()
//                .appendLine()
//                .appendLine("室外PM2.5")
//                .create());
    }

    @Override
    protected void resumeInit() {

    }

    @OnClick(R.id.btn_test)
    public void onViewClicked() {
        startToActivity(FilterTimeRemainActivity.class);
    }
}
