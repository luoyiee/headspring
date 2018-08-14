package cc.xiaojiang.liangbo.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.SectionEntity;

import java.util.List;

import cc.xiaojiang.iotkit.bean.http.Goods;
import cc.xiaojiang.liangbo.R;
import cc.xiaojiang.liangbo.model.GoodsSection;
import cc.xiaojiang.liangbo.utils.ImageLoader;

public class GoodsAdapter extends BaseSectionQuickAdapter<GoodsSection, BaseViewHolder> {
    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param layoutResId      The layout resource id of each item.
     * @param sectionHeadResId The section head layout id for each item
     * @param data             A new list is created out of this one to avoid mutable list
     */
    public GoodsAdapter(int layoutResId, int sectionHeadResId, List data) {
        super(layoutResId, sectionHeadResId, data);
    }


    @Override
    protected void convertHead(BaseViewHolder helper, GoodsSection item) {
        helper.setText(R.id.tv_shop_goods_section_header, item.header);
    }

    @Override
    protected void convert(BaseViewHolder helper, GoodsSection item) {
        Goods goods = item.t;
        helper.setText(R.id.tv_shop_goods_title, goods.getTitle())
                .setText(R.id.tv_shop_goods_price, goods.getPrice());
        ImageLoader.loadImage(mContext, goods.getCover(), helper.getView(R.id.iv_shop_goods_icon));
    }
}
