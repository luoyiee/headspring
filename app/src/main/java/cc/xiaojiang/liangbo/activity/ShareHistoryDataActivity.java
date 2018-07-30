package cc.xiaojiang.liangbo.activity;

import android.os.Bundle;
import android.widget.TextView;

import butterknife.BindView;
import cc.xiaojiang.liangbo.R;
import cc.xiaojiang.liangbo.http.RetrofitHelper;
import cc.xiaojiang.liangbo.http.model.BaseModel;
import cc.xiaojiang.liangbo.http.progress.ProgressObserver;
import cc.xiaojiang.liangbo.model.http.UserInfoModel;
import cc.xiaojiang.liangbo.utils.ImageLoader;
import cc.xiaojiang.liangbo.utils.RxUtils;
import de.hdodenhof.circleimageview.CircleImageView;

public class ShareHistoryDataActivity extends ShareActivity {

    @BindView(R.id.tv_share_avatar)
    CircleImageView mTvShareAvatar;
    @BindView(R.id.tv_share_nickname)
    TextView mTvShareNickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                            mTvShareNickname.setText(data.getNickname());
                            ImageLoader.loadImage(ShareHistoryDataActivity.this,data.getImgUrl(),mTvShareAvatar);
                        }
                    }
                });
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_share_history_data;
    }
}
