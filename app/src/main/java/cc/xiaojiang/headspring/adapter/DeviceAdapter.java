package cc.xiaojiang.headspring.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cc.xiaojiang.headspring.R;
import cc.xiaojiang.headspring.model.bean.DeviceResponse;
import cc.xiaojiang.headspring.utils.ImageLoader;
import cc.xiaojiang.iotkit.bean.http.Device;

public class DeviceAdapter extends BaseQuickAdapter<Device, BaseViewHolder> {
    public DeviceAdapter(int layoutResId, @Nullable List<Device> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Device item) {
        if ("Y".equals(item.getIsAdmin())) {
            helper.setGone(R.id.iv_device_admin, false);
            helper.setGone(R.id.tv_device_swipe_menu_modify, true);
            helper.setGone(R.id.tv_device_swipe_menu_share, true);
        } else {
            helper.setGone(R.id.iv_device_admin, true);
            helper.setGone(R.id.tv_device_swipe_menu_modify, false);
            helper.setGone(R.id.tv_device_swipe_menu_share, false);
        }
        String deviceName = TextUtils.isEmpty(item.getDeviceNickname()) ? item
                .getProductName() : item.getDeviceNickname();
        helper.setText(R.id.tv_device_name, deviceName)
                .setText(R.id.tv_device_status, "已开启");
        ImageLoader.loadImage(mContext, item.getProductIcon(), helper.getView(R.id.iv_device_icon));
        // TODO: 2018/6/29 设备状态
//        CircleImageView circleImageView = helper.getView(R.id.iv_device_icon);
//        circleImageView.setBorderColor();
        helper.addOnClickListener(R.id.ll_device_content)
                .addOnClickListener(R.id.tv_device_swipe_menu_modify)
                .addOnClickListener(R.id.tv_device_swipe_menu_share)
                .addOnClickListener(R.id.tv_device_swipe_menu_delete);

    }
}
