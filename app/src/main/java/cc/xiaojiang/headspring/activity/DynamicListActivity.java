package cc.xiaojiang.headspring.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

import butterknife.BindView;
import cc.xiaojiang.headspring.R;
import cc.xiaojiang.headspring.adapter.DynamicAdapter;
import cc.xiaojiang.headspring.base.BaseActivity;
import cc.xiaojiang.headspring.http.HttpResultFunc;
import cc.xiaojiang.headspring.http.RetrofitHelper;
import cc.xiaojiang.headspring.http.progress.ProgressObserver;
import cc.xiaojiang.headspring.model.http.DynamicModel;
import cc.xiaojiang.headspring.utils.RxUtils;

public class DynamicListActivity extends BaseActivity implements BaseQuickAdapter
        .OnItemClickListener {
    @BindView(R.id.rv_dynamic_list)
    RecyclerView mRvDynamicList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RetrofitHelper.getService().dynamicList()
                .map(new HttpResultFunc<>())
                .compose(RxUtils.rxSchedulerHelper())
                .subscribe(new ProgressObserver<List<DynamicModel>>(this) {
                    @Override
                    public void onSuccess(List<DynamicModel> dynamicModels ) {
                        initView(dynamicModels);
                    }
                });
    }

    private void initView(List<DynamicModel> dynamicModels) {
        DynamicAdapter dynamicAdapter = new DynamicAdapter(R.layout.item_dynamic, dynamicModels);
        mRvDynamicList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager
                .VERTICAL, false));
        mRvDynamicList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration
                .VERTICAL));
        mRvDynamicList.setAdapter(dynamicAdapter);
        dynamicAdapter.setOnItemClickListener(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_dynamic_list;
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        DynamicModel dynamicModel = (DynamicModel) adapter.getItem(position);
        if(dynamicModel!=null){
            Intent intent = new Intent(this, BrowserActivity.class);
            intent.putExtra("dynamic_title", "title");
            intent.putExtra("dynamic_url", dynamicModel.getUrl());
            startActivity(intent);
        }
    }
}
