package cc.xiaojiang.liangbo.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cc.xiaojiang.iotkit.bean.http.Article;
import cc.xiaojiang.liangbo.R;
import cc.xiaojiang.liangbo.activity.AirActivity;
import cc.xiaojiang.liangbo.activity.DeviceListActivity;
import cc.xiaojiang.liangbo.model.DeviceStatus;
import cc.xiaojiang.liangbo.utils.ImageLoader;
import cc.xiaojiang.liangbo.utils.SpanUtils;

public class DeviceStatusAdapter extends BaseQuickAdapter<DeviceStatus, BaseViewHolder> {


    public DeviceStatusAdapter(int layoutResId, @Nullable List<DeviceStatus> data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, DeviceStatus item) {
        helper.setText(R.id.tv_device_status_name, item.getName());
        String pm25 = item.getPm25();

        TextView tvPm25 = helper.getView(R.id.tv_device_status_pm25);
        TextView tvQuality = helper.getView(R.id.tv_device_status_air_quality);
        tvPm25.setText(new SpanUtils()
                .append(pm25).setFontSize(52, true)
                .append("ug/m").setFontSize(18, true).append("3").setSuperscript().setFontSize
                        (16, true)
                .create());
        if (TextUtils.isEmpty(pm25) || AirActivity.DEFAULT_DATA.equals(pm25)) {
            tvQuality.setVisibility(View.INVISIBLE);
        } else {
            tvQuality.setVisibility(View.VISIBLE);
            int pm = Integer.parseInt(pm25);
            if (pm <= 35) {
                tvQuality.setText("优");
                tvQuality.setBackgroundResource(R.drawable.home_btn_excellent);
            } else if (pm <= 75) {
                tvQuality.setText("良");
                tvQuality.setBackgroundResource(R.drawable.home_btn_good);
            } else {
                tvQuality.setText("差");
                tvQuality.setBackgroundResource(R.drawable.home_btn_difference);
            }
        }
    }
}
