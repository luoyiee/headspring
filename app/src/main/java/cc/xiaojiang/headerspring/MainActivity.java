package cc.xiaojiang.headerspring;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import butterknife.BindView;
import butterknife.OnClick;
import cc.xiaojiang.headerspring.base.BaseActivity;
import cc.xiaojiang.headerspring.feature.AirKnowledgeActivity;
import cc.xiaojiang.headerspring.feature.PersonalCenterActivity;
import cc.xiaojiang.headerspring.feature.ProductListActivity;

public class MainActivity extends BaseActivity {


    @BindView(R.id.btn_test)
    Button btnTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void createInit() {

    }

    @Override
    protected void resumeInit() {

    }

    @OnClick(R.id.btn_test)
    public void onViewClicked() {
        startToActivity(PersonalCenterActivity.class);
    }
}
