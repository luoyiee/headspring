package cc.xiaojiang.headerspring.feature;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;

import butterknife.BindView;
import butterknife.OnClick;
import cc.xiaojiang.headerspring.R;
import cc.xiaojiang.headerspring.base.BaseActivity;

public class DeviceControlActivity extends BaseActivity {
    @BindView(R.id.iv_pop_window)
    ImageView mIvPopWindow;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_device_control;
    }

    @Override
    protected void createInit() {

    }

    @Override
    protected void resumeInit() {

    }

    @OnClick(R.id.iv_pop_window)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_pop_window:
                final View contentView = getLayoutInflater().inflate(R.layout.device_pop_window, null);
                final PopupWindow popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams
                        .WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
                popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                popupWindow.getContentView().measure(View.MeasureSpec.UNSPECIFIED,View.MeasureSpec.UNSPECIFIED);
                popupWindow.showAsDropDown(mIvPopWindow, -popupWindow.getContentView().getMeasuredWidth()+40,20);
                break;

            default:
                break;
        }
    }
}
