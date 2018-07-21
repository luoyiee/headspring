package cc.xiaojiang.liangbo.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import cc.xiaojiang.liangbo.R;
import cc.xiaojiang.liangbo.model.bean.DeviceResponse;
import cc.xiaojiang.liangbo.utils.ImageLoader;
import cc.xiaojiang.iotkit.bean.http.Device;
import de.hdodenhof.circleimageview.CircleImageView;

public class DeviceAdapter extends BaseQuickAdapter<Device, BaseViewHolder> {
    private Map<String, String> mOnlineStatusMap = new HashMap();

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
        helper.setText(R.id.tv_device_name, deviceName);
        ImageLoader.loadImage(mContext, item.getProductIcon(), helper.getView(R.id.iv_device_icon));
        CircleImageView circleImageView = helper.getView(R.id.iv_device_icon);
        String onlineStatus = mOnlineStatusMap.get(item.getDeviceId());
        if (onlineStatus != null && onlineStatus.equals("online")) {
            helper.setText(R.id.tv_device_status, "设备在线");
            circleImageView.setBorderColor(mContext.getResources().getColor(R.color
                    .device_status_online));
        } else {
            helper.setText(R.id.tv_device_status, "设备离线");
            circleImageView.setBorderColor(mContext.getResources().getColor(R.color
                    .device_status_offline));
        }
        helper.addOnClickListener(R.id.ll_device_content)
                .addOnClickListener(R.id.tv_device_swipe_menu_modify)
                .addOnClickListener(R.id.tv_device_swipe_menu_share)
                .addOnClickListener(R.id.tv_device_swipe_menu_delete);

    }

    public void updateOnlineStatus(String deviceId, String onlineStatus) {
        mOnlineStatusMap.put(deviceId, onlineStatus);
        notifyDataSetChanged();
    }
}
