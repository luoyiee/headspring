package cc.xiaojiang.headspring.adapter;

import android.graphics.Color;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cc.xiaojiang.headspring.R;
import cc.xiaojiang.headspring.model.http.AirRankModel;

public class RankAdapter extends BaseQuickAdapter<AirRankModel, BaseViewHolder> {

    public RankAdapter(int layoutResId, @Nullable List<AirRankModel> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AirRankModel item) {
        if(helper.getAdapterPosition() == 0){
           helper.setBackgroundColor(R.id.ll_rank, Color.parseColor("#9ADFDA")) ;
        }else{
            helper.setBackgroundColor(R.id.ll_rank, Color.TRANSPARENT) ;
        }
        String[] path = item.getPath().split(",");
        helper.setText(R.id.tv_rank_rank, item.getRank()+"")
                .setText(R.id.tv_rank_province, path[path.length-2])
                .setText(R.id.tv_rank_city, item.getCity())
                .setText(R.id.tv_rank_aqi, item.getAqi()+"");
    }
}
