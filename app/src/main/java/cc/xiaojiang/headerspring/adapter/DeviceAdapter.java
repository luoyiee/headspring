package cc.xiaojiang.headerspring.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cc.xiaojiang.headerspring.R;
import cc.xiaojiang.headerspring.model.bean.DeviceResp;

public class DeviceAdapter extends BaseQuickAdapter<DeviceResp.DataBean, BaseViewHolder> {
    public DeviceAdapter(int layoutResId, @Nullable List<DeviceResp.DataBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DeviceResp.DataBean item) {
//        helper.setVisible(R.id.tv_device_swipe_menu_modify, false);
        helper.addOnClickListener(R.id.tv_device_swipe_menu_modify)
                .addOnClickListener(R.id.tv_device_swipe_menu_share)
                .addOnClickListener(R.id.tv_device_swipe_menu_delete);

    }
}
