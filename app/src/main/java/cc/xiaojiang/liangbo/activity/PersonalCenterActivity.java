package cc.xiaojiang.liangbo.activity;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;
import cc.xiaojiang.liangbo.R;
import cc.xiaojiang.liangbo.base.BaseActivity;
import cc.xiaojiang.liangbo.http.RetrofitHelper;
import cc.xiaojiang.liangbo.http.model.BaseModel;
import cc.xiaojiang.liangbo.http.progress.ProgressObserver;
import cc.xiaojiang.liangbo.model.http.UserInfoModel;
import cc.xiaojiang.liangbo.utils.ImageLoader;
import cc.xiaojiang.liangbo.utils.RxUtils;
import cc.xiaojiang.liangbo.utils.ToastUtils;
import de.hdodenhof.circleimageview.CircleImageView;

public class PersonalCenterActivity extends BaseActivity {


    @BindView(R.id.ll_personal_info)
    LinearLayout mLlPersonalInfo;
    @BindView(R.id.ll_personal_dynamic)
    RelativeLayout mLlPersonalDynamic;
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
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.appbar)
    AppBarLayout mAppbar;
    @BindView(R.id.iv_personal_center_avatar)
    CircleImageView mIvPersonalCenterAvatar;
    @BindView(R.id.tv_personal_center_nick)
    TextView mTvPersonalCenterNick;
    @BindView(R.id.tv_personal_center_phone)
    TextView mTvPersonalCenterPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onResume() {
        super.onResume();
        getUser();
    }

    private void getUser() {
        RetrofitHelper.getService().userInfo()
                .compose(RxUtils.rxSchedulerHelper())
                .compose(bindToLifecycle())
                .subscribe(new ProgressObserver<BaseModel<UserInfoModel>>(this) {
                    @Override
                    public void onSuccess(BaseModel<UserInfoModel> userInfoModel) {
                        UserInfoModel data = userInfoModel.getData();
                        if (data != null) {
                            mTvPersonalCenterNick.setText(data.getNickname());
                            mTvPersonalCenterPhone.setText(String.valueOf(data.getTelphone()));
                            ImageLoader.loadImage(PersonalCenterActivity.this,data.getImgUrl(),mIvPersonalCenterAvatar);
                        }
                    }
                });
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
                startToActivity(DynamicListActivity.class);
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
