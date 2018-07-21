package cc.xiaojiang.liangbo.activity;

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
import cc.xiaojiang.liangbo.R;
import cc.xiaojiang.liangbo.adapter.DynamicAdapter;
import cc.xiaojiang.liangbo.base.BaseActivity;
import cc.xiaojiang.liangbo.model.http.DynamicModel;

public class DynamicListActivity extends BaseActivity implements BaseQuickAdapter
        .OnItemClickListener {
    @BindView(R.id.rv_dynamic_list)
    RecyclerView mRvDynamicList;
    private String[] titles = {"空气清新,愉悦家人心","灵动吹风,清爽一夏","时尚,简约,多种模式",
            "大能量,小声音,让你安然入睡","重新发现生活中的美好","只有风,没有声,舒适睡眠体验"};
    private String[] covers = {"http://www.0351zhuangxiu.com/uploads/images/212.jpg",
    "http://www.jiajujiazhuang.com/images/120218/0144501296-.jpg",
    "http://blog.pic.xiaokui.io/3a8343fd852e6f88a0a13c1b6953bdc2",
"http://www.yadea.cn/UploadFiles/FCK/image/2016%E6%96%87%E7%AB%A0/10%E6%9C%88/%E7%8E%B0%E4%BB%A3%E5%AE%B6%E8%A3%85%E8%AE%BE%E8%AE%A1%E6%A1%88%E4%BE%8B1(4).png",
            "http://a2.att.hudong.com/65/36/14300001022490129344365509222_950.jpg",
            "http://pic.tugou.com/jingyan/20160217195430_70770.jpeg"
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        RetrofitHelper.getService().dynamicList()
//                .map(new HttpResultFunc<>())
//                .compose(RxUtils.rxSchedulerHelper())
//                .subscribe(new ProgressObserver<List<DynamicModel>>(this) {
//                    @Override
//                    public void onSuccess(List<DynamicModel> dynamicModels ) {
//                        initView(dynamicModels);
//                    }
//                });
        ArrayList<DynamicModel> dynamicModels = new ArrayList<>();
        for (int i = 0;i<titles.length;i++) {
            DynamicModel dynamicModel = new DynamicModel();
            dynamicModel.setLike(2600);
            dynamicModel.setComment(390);
            dynamicModel.setTitle(titles[i]);
            dynamicModel.setUrl("http://www.ecotechair.com.cn/index.php/archives/3653");
            dynamicModel.setCover(covers[i]);
            dynamicModels.add(dynamicModel);
        }
        initView(dynamicModels);
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
