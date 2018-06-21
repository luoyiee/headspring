package cc.xiaojiang.headerspring.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;
import java.util.function.ToDoubleBiFunction;

import cc.xiaojiang.headerspring.R;
import cc.xiaojiang.headerspring.model.bean.ProductResp;
import cc.xiaojiang.headerspring.utils.ImageLoader;

public class ProductAdapter extends BaseQuickAdapter<ProductResp.DataBean, BaseViewHolder> {
    public ProductAdapter(int layoutResId, @Nullable List<ProductResp.DataBean> data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, ProductResp.DataBean item) {
        helper.setText(R.id.tv_product_name, item.getProductName());
        if (TextUtils.isEmpty(item.getProductIcon())) {
            // TODO: 2018/6/21 加载本地图片
        } else {
            ImageLoader.loadImage(mContext, item.getProductIcon(), helper.getView(R.id
                    .iv_product_icon));
        }


    }
}
