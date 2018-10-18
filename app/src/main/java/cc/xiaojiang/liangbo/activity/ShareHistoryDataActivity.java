package cc.xiaojiang.liangbo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import butterknife.BindView;
import cc.xiaojiang.iotkit.IotKit;
import cc.xiaojiang.iotkit.bean.http.Device;
import cc.xiaojiang.liangbo.R;
import cc.xiaojiang.liangbo.http.RetrofitHelper;
import cc.xiaojiang.liangbo.http.model.BaseModel;
import cc.xiaojiang.liangbo.http.progress.ProgressObserver;
import cc.xiaojiang.liangbo.iotkit.IotKitUtils;
import cc.xiaojiang.liangbo.model.http.UserInfoModel;
import cc.xiaojiang.liangbo.utils.ImageLoader;
import cc.xiaojiang.liangbo.utils.RxUtils;
import de.hdodenhof.circleimageview.CircleImageView;

public class ShareHistoryDataActivity extends ShareActivity {

    @BindView(R.id.tv_share_avatar)
    CircleImageView mTvShareAvatar;
    @BindView(R.id.tv_share_nickname)
    TextView mTvShareNickname;
    @BindView(R.id.tv_share_device_name)
    TextView mTvShareDeviceName;

    private Device mDevice;

    public static void actionStart(Context context, Device device) {
        Intent intent = new Intent(context, ShareHistoryDataActivity.class);
        intent.putExtra("device", device);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDevice = getIntent().getParcelableExtra("device");
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
                            if (TextUtils.isEmpty(data.getImgUrl())) {
                                ImageLoader.loadImage(ShareHistoryDataActivity.this, R.drawable
                                        .not_login_avatar, mTvShareAvatar);
                            } else {
                                ImageLoader.loadImage(ShareHistoryDataActivity.this, data
                                        .getImgUrl(), mTvShareAvatar);
                            }
                            if (TextUtils.isEmpty(data.getNickname())) {
                                mTvShareNickname.setText(String.valueOf(data.getTelphone()));
                            } else {
                                mTvShareNickname.setText(data.getNickname());
                            }
                            mTvShareDeviceName.setText(IotKitUtils.getDeviceName(mDevice));

                        }
                    }
                });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_share_history_data;
    }
}
