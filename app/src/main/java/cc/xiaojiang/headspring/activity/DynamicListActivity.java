package cc.xiaojiang.headspring.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cc.xiaojiang.headspring.R;
import cc.xiaojiang.headspring.adapter.DynamicAdapter;
import cc.xiaojiang.headspring.base.BaseActivity;
import cc.xiaojiang.headspring.model.http.DynamicModel;

public class DynamicListActivity extends BaseActivity implements BaseQuickAdapter
        .OnItemClickListener {
    @BindView(R.id.rv_dynamic_list)
    RecyclerView mRvDynamicList;
    private DynamicAdapter mDynamicAdapter;
    private List<DynamicModel> mDynamicModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDynamicModels = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            mDynamicModels.add(new DynamicModel());
        }
        initView();

    }

    private void initView() {
        mDynamicAdapter = new DynamicAdapter(R.layout.item_dynamic, mDynamicModels);
        mRvDynamicList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager
                .VERTICAL, false));
        mRvDynamicList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration
                .VERTICAL));
        mRvDynamicList.setAdapter(mDynamicAdapter);
        mDynamicAdapter.setOnItemClickListener(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_dynamic_list;
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        DynamicModel dynamicModel = (DynamicModel) adapter.getItem(position);
        Intent intent = new Intent(this, DynamicActivity.class);
        intent.putExtra("dynamic_title", "title");
        intent.putExtra("dynamic_url", "https://www.baidu.com");
        startActivity(intent);
    }
}
