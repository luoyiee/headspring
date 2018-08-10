package cc.xiaojiang.liangbo.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cc.xiaojiang.iotkit.bean.http.Goods;
import cc.xiaojiang.liangbo.R;
import cc.xiaojiang.liangbo.utils.ImageLoader;

public class GoodsAdapter extends BaseQuickAdapter<Goods, BaseViewHolder> {
    public GoodsAdapter(int layoutResId, @Nullable List<Goods> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Goods item) {
        helper.setText(R.id.tv_shop_goods_title, item.getTitle())
                .setText(R.id.tv_shop_goods_price, item.getPrice());
        ImageLoader.loadImage(mContext, item.getCover(), helper.getView(R.id.iv_shop_goods_icon));

    }
}
