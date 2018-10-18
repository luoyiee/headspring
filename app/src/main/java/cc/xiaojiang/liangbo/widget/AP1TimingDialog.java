package cc.xiaojiang.liangbo.widget;

import android.app.Dialog;
import android.os.Build;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cc.xiaojiang.liangbo.R;

/**
 * Created by zhu on 16-9-14.
 */
public class AP1TimingDialog extends BottomSheetDialogFragment implements View.OnClickListener {
    @BindView(R.id.tv_a1_timing_8)
    TextView tvA1Timing8;
    @BindView(R.id.tv_a1_timing_4)
    TextView tvA1Timing4;
    @BindView(R.id.tv_a1_timing_2)
    TextView tvA1Timing2;
    @BindView(R.id.tv_a1_timing_1)
    TextView tvA1Timing1;
    @BindView(R.id.tv_a1_timing_0)
    TextView tvA1Timing0;
    private OnTimeSelectedListener mOnTimeSelectedListener;
    private Unbinder mUnbinder;

    public void setOnTimeSelectedListener(OnTimeSelectedListener mOnTimeSelectedListener) {
        this.mOnTimeSelectedListener = mOnTimeSelectedListener;
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        /*
         *  避免statusBar变黑
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        View contentView = View.inflate(getContext(), R.layout.fragment_a1_timing_dialog, null);
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

    @OnClick({R.id.tv_a1_timing_8, R.id.tv_a1_timing_4, R.id.tv_a1_timing_2, R.id.tv_a1_timing_1, R.id.tv_a1_timing_0})
    public void onClick(View view) {
        dismiss();
        if (mOnTimeSelectedListener != null) {
            switch (view.getId()) {
                case R.id.tv_a1_timing_8:
                    mOnTimeSelectedListener.onTimeSelected(8);
                    break;
                case R.id.tv_a1_timing_4:
                    mOnTimeSelectedListener.onTimeSelected(4);
                    break;
                case R.id.tv_a1_timing_2:
                    mOnTimeSelectedListener.onTimeSelected(2);
                    break;
                case R.id.tv_a1_timing_1:
                    mOnTimeSelectedListener.onTimeSelected(1);
                    break;
                case R.id.tv_a1_timing_0:
                    mOnTimeSelectedListener.onTimeSelected(0);
                    break;
            }
        }
    }

    public interface OnTimeSelectedListener {
        void onTimeSelected(int hour);
    }
}
