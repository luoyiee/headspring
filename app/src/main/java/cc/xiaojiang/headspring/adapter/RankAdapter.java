package cc.xiaojiang.headspring.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cc.xiaojiang.headspring.R;
import cc.xiaojiang.headspring.model.bean.DeviceResponse;
import cc.xiaojiang.headspring.model.http.RankModel;
import cc.xiaojiang.headspring.utils.ImageLoader;

public class RankAdapter extends BaseQuickAdapter<RankModel, BaseViewHolder> {

    public RankAdapter(int layoutResId, @Nullable List<RankModel> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, RankModel item) {
        helper.setText(R.id.tv_rank_rank, item.getRank())
                .setText(R.id.tv_rank_province, item.getProvince())
                .setText(R.id.tv_rank_city, item.getCity())
                .setText(R.id.tv_rank_aqi, item.getAqi());
    }
}
