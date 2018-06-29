package cc.xiaojiang.headspring.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.OnClick;
import cc.xiaojiang.headspring.R;
import cc.xiaojiang.headspring.base.BaseActivity;
import cc.xiaojiang.headspring.utils.ToastUtils;

public class PersonalCenterActivity extends BaseActivity {


    @BindView(R.id.ll_personal_info)
    LinearLayout mLlPersonalInfo;
    @BindView(R.id.ll_personal_dynamic)
    LinearLayout mLlPersonalDynamic;
    @BindView(R.id.ll_personal_share)
    LinearLayout mLlPersonalShare;
    @BindView(R.id.ll_personal_air_knowledge)
    LinearLayout mLlPersonalAirKnowledge;
    @BindView(R.id.ll_personal_lunar)
    LinearLayout mLlPersonalLunar;
    @BindView(R.id.ll_personal_instructions)
    LinearLayout mLlPersonalInstructions;
    @BindView(R.id.ll_personal_feedback)
    LinearLayout mLlPersonalFeedback;
    @BindView(R.id.ll_personal_update)
    LinearLayout mLlPersonalUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_personal_center;
    }

    @OnClick({R.id.ll_personal_info, R.id.ll_personal_dynamic, R.id.ll_personal_share, R.id
            .ll_personal_air_knowledge, R.id.ll_personal_lunar, R.id.ll_personal_instructions, R
            .id.ll_personal_feedback, R.id.ll_personal_update})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_personal_info:
                startToActivity(PersonalInfoActivity.class);
                break;
            case R.id.ll_personal_dynamic:
                startToActivity(DynamicActivity.class);
                break;
            case R.id.ll_personal_share:
                ToastUtils.show("共享服务");
                break;
            case R.id.ll_personal_air_knowledge:
                startToActivity(AirKnowledgeActivity.class);
                break;
            case R.id.ll_personal_lunar:
                startToActivity(LunarActivity.class);
                break;
            case R.id.ll_personal_instructions:
                startToActivity(InstructionActivity.class);
                break;
            case R.id.ll_personal_feedback:
                startToActivity(FeedbackActivity.class);
                break;
            case R.id.ll_personal_update:
                startToActivity(AppUpdateActivity.class);
                break;
        }
    }
}
