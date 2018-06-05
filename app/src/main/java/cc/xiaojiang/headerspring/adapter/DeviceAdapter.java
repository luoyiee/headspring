package cc.xiaojiang.headerspring.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cc.xiaojiang.headerspring.R;
import cc.xiaojiang.headerspring.model.bean.Device;
import cc.xiaojiang.headerspring.model.bean.Product;

public class DeviceAdapter extends BaseQuickAdapter<Device, BaseViewHolder> {
    public DeviceAdapter(int layoutResId, @Nullable List<Device> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Device item) {
//        helper.setVisible(R.id.tv_device_swipe_menu_modify, false);

    }
}
