package cc.xiaojiang.liangbo.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import cc.xiaojiang.liangbo.R;
import cc.xiaojiang.liangbo.base.BaseActivity;
import cc.xiaojiang.liangbo.http.HttpResultFunc;
import cc.xiaojiang.liangbo.http.RetrofitHelper;
import cc.xiaojiang.liangbo.http.progress.ProgressObserver;
import cc.xiaojiang.liangbo.model.http.FeedbackBody;
import cc.xiaojiang.liangbo.utils.DbUtils;
import cc.xiaojiang.liangbo.utils.RxUtils;
import cc.xiaojiang.liangbo.utils.ToastUtils;

public class FeedbackActivity extends BaseActivity {

    @BindView(R.id.et_feedback_content)
    EditText mEtFeedbackContent;
    @BindView(R.id.tv_call_after_sell)
    TextView mTvCallAfterSell;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_feedback;
    }

    @OnClick({R.id.btn_submit,R.id.tv_call_after_sell})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_submit:
                requestSubmitFeedback();
                break;
            case R.id.tv_call_after_sell:
            call(mTvCallAfterSell.getText().toString().trim().split(":")[1]);
                break;
            default:
                break;
        }
    }
    /**
     * 调用拨号界面
     * @param phone 电话号码
     */
    private void call(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    private void requestSubmitFeedback() {
        String text = mEtFeedbackContent.getText().toString();
        if (TextUtils.isEmpty(text)) {
            ToastUtils.show("意见不能为空");
            return;
        }
        FeedbackBody feedbackBody = new FeedbackBody(text,null);
        String accountPhoneNumber = DbUtils.getAccountPhoneNumber();
        if(!TextUtils.isEmpty(accountPhoneNumber)){
            feedbackBody.setTelphone(Long.parseLong(accountPhoneNumber));
        }
        RetrofitHelper.getService().feedback(feedbackBody)
                .map(new HttpResultFunc<>())
                .compose(RxUtils.rxSchedulerHelper())
                .compose(bindToLifecycle())
                .subscribe(new ProgressObserver<Object>(this) {
                    @Override
                    public void onSuccess(Object o) {
                        ToastUtils.show("提交成功");
                        finish();
                    }
                });
    }
}
