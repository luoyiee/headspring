package cc.xiaojiang.liangbo.widget;

import android.app.Dialog;
import android.os.Build;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.View;
import android.view.WindowManager;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cc.xiaojiang.liangbo.R;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * Created by zhu on 16-9-14.
 */
public class ShareSelectDialog extends BottomSheetDialogFragment {
    private Unbinder mUnbinder;
    private OnShareSelectedListener mOnShareSelectedListener;

    public ShareSelectDialog setOnTimeSelectedListener(OnShareSelectedListener onShareSelectedListener) {
        this.mOnShareSelectedListener = onShareSelectedListener;
        return this;
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        /*
         *  避免statusBar变黑
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        View contentView = View.inflate(getContext(), R.layout.fragment_share_select, null);
        dialog.setContentView(contentView);
        mUnbinder = ButterKnife.bind(this, contentView);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
    }

    @OnClick({R.id.tv_share_wechat, R.id.tv_share_wechat_comment, R.id.tv_share_qq, R.id
            .tv_share_qq_zone, R.id.btn_cancel})
    public void onViewClicked(View view) {
        dismiss();
        if (mOnShareSelectedListener != null) {
            switch (view.getId()) {
                case R.id.tv_share_wechat:
                    mOnShareSelectedListener.onShareSelected(Wechat.NAME);
                    break;
                case R.id.tv_share_wechat_comment:
                    mOnShareSelectedListener.onShareSelected(WechatMoments.NAME);
                    break;
                case R.id.tv_share_qq:
                    mOnShareSelectedListener.onShareSelected(QQ.NAME);
                    break;
                case R.id.tv_share_qq_zone:
                    mOnShareSelectedListener.onShareSelected(QZone.NAME);
                    break;
            }
        }
    }

    public interface OnShareSelectedListener {
        void onShareSelected(String platformName);
    }
}
