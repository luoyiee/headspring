package cc.xiaojiang.headspring.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cc.xiaojiang.headspring.R;
import cc.xiaojiang.headspring.model.bean.DeviceResponse;

public class DeviceAdapter extends BaseQuickAdapter<DeviceResponse.DataBean, BaseViewHolder> {
    public DeviceAdapter(int layoutResId, @Nullable List<DeviceResponse.DataBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DeviceResponse.DataBean item) {
        helper.addOnClickListener(R.id.ll_device_content)
                .addOnClickListener(R.id.tv_device_swipe_menu_modify)
                .addOnClickListener(R.id.tv_device_swipe_menu_share)
                .addOnClickListener(R.id.tv_device_swipe_menu_delete);

    }
}
