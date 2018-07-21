package cc.xiaojiang.liangbo.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cc.xiaojiang.liangbo.R;
import cc.xiaojiang.liangbo.model.bean.ProductResp;
import cc.xiaojiang.liangbo.utils.ImageLoader;
import cc.xiaojiang.iotkit.bean.http.Product;

public class ProductAdapter extends BaseQuickAdapter<Product, BaseViewHolder> {
    public ProductAdapter(int layoutResId, @Nullable List<Product> data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, Product item) {
        helper.setText(R.id.tv_product_name, item.getProductName());
        if (TextUtils.isEmpty(item.getProductIcon())) {
            // TODO: 2018/6/21 加载本地图片
        } else {
            ImageLoader.loadImage(mContext, item.getProductIcon(), helper.getView(R.id
                    .iv_product_icon));
        }


    }
}
