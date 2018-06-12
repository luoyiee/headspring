package cc.xiaojiang.headerspring.feature;

import android.view.View;

import butterknife.OnClick;
import cc.xiaojiang.headerspring.R;
import cc.xiaojiang.headerspring.base.BaseActivity;

public class PersonalCenterActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_personal_center;
    }

    @Override
    protected void createInit() {

    }

    @Override
    protected void resumeInit() {

    }

    @OnClick(R.id.ll_personal_info)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_personal_info:
                startToActivity(PersonalInfoActivity.class);
                break;

            default:
                break;
        }
    }
}
