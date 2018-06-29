package cc.xiaojiang.headspring.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cc.xiaojiang.headspring.model.http.DynamicModel;

public class DynamicAdapter extends BaseQuickAdapter<DynamicModel, BaseViewHolder> {


    public DynamicAdapter(int layoutResId, @Nullable List<DynamicModel> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DynamicModel item) {

    }
}
