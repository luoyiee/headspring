package cc.xiaojiang.headspring.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.OnClick;
import cc.xiaojiang.headspring.R;
import cc.xiaojiang.headspring.base.BaseActivity;
import cc.xiaojiang.headspring.http.RetrofitHelper;
import cc.xiaojiang.headspring.http.progress.ProgressObserver;
import cc.xiaojiang.headspring.utils.RxUtils;
import cc.xiaojiang.headspring.utils.ToastUtils;

public class FeedbackActivity extends BaseActivity {

    @BindView(R.id.et_feedback_content)
    EditText mEtFeedbackContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_feedback;
    }

    @OnClick(R.id.btn_submit)
    public void onViewClicked() {
        String text = mEtFeedbackContent.getText().toString();
        if (TextUtils.isEmpty(text)) {
            ToastUtils.show("意见不能为空");
            return;
        }
        RetrofitHelper.getService().feedback(text)
                .compose(RxUtils.rxSchedulerHelper())
                .subscribe(new ProgressObserver<Object>(this) {
                    @Override
                    public void onSuccess(Object o) {
                        ToastUtils.show("提交成功");
                    }
                });
    }
}
