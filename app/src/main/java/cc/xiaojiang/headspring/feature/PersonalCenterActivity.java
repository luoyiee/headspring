package cc.xiaojiang.headspring.feature;

import android.view.View;

import butterknife.OnClick;
import cc.xiaojiang.headspring.R;
import cc.xiaojiang.headspring.base.BaseActivity;

public class PersonalCenterActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_personal_center;
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
