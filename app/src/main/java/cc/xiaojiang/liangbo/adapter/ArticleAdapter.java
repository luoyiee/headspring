package cc.xiaojiang.liangbo.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cc.xiaojiang.iotkit.bean.http.Article;
import cc.xiaojiang.liangbo.R;
import cc.xiaojiang.liangbo.utils.ImageLoader;

public class ArticleAdapter extends BaseQuickAdapter<Article.ListsBean, BaseViewHolder> {


    public ArticleAdapter(int layoutResId, @Nullable List<Article.ListsBean> data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, Article.ListsBean item) {
        ImageView ivDynamicCover = helper.getView(R.id.iv_dynamic_cover);
//        ImageLoader.loadImageNoMemory(mContext, item.getCover(), ivDynamicCover);
        ImageLoader.loadImage(mContext, item.getCover(), ivDynamicCover);
        helper.setText(R.id.tv_dynamic_title, item.getTitle())
                .setText(R.id.tv_dynamic_like, item.getPraise_quantity() + "")
                .setText(R.id.tv_dynamic_comment, item.getRead_quantity() + "")
                .setText(R.id.tv_dynamic_author, item.getAuthor())
                .setText(R.id.tv_content, item.getAbstractX());
    }
}
