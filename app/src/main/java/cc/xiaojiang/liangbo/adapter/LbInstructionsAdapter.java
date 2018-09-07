package cc.xiaojiang.liangbo.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cc.xiaojiang.iotkit.bean.http.Article;
import cc.xiaojiang.liangbo.R;
import cc.xiaojiang.liangbo.utils.ImageLoader;

public class LbInstructionsAdapter extends BaseQuickAdapter<Integer, BaseViewHolder> {


    public LbInstructionsAdapter(int layoutResId, @Nullable List<Integer> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Integer item) {
        helper.setImageResource(R.id.iv_lb_instructions, item);
    }
}
