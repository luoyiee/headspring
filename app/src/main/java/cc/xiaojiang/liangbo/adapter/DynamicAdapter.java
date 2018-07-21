package cc.xiaojiang.liangbo.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cc.xiaojiang.liangbo.R;
import cc.xiaojiang.liangbo.model.http.DynamicModel;
import cc.xiaojiang.liangbo.utils.ImageLoader;

public class DynamicAdapter extends BaseQuickAdapter<DynamicModel, BaseViewHolder> {


    public DynamicAdapter(int layoutResId, @Nullable List<DynamicModel> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DynamicModel item) {
        ImageView ivDynamicCover = helper.getView(R.id.iv_dynamic_cover);
        ImageLoader.loadImageNoMemory(mContext, item.getCover(), ivDynamicCover);
        helper.setText(R.id.tv_dynamic_title, item.getTitle())
                .setText(R.id.tv_dynamic_like, item.getLike() + "")
                .setText(R.id.tv_dynamic_comment, item.getComment()+"");
    }
}
