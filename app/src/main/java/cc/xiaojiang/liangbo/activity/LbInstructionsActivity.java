package cc.xiaojiang.liangbo.activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cc.xiaojiang.liangbo.R;
import cc.xiaojiang.liangbo.adapter.LbInstructionsAdapter;
import cc.xiaojiang.liangbo.base.BaseActivity;

public class LbInstructionsActivity extends BaseActivity {
    @BindView(R.id.rv_lb_instructions)
    RecyclerView mRvLbInstructions;
    private LbInstructionsAdapter mLbInstructionsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLbInstructionsAdapter = new LbInstructionsAdapter(R.layout.item_lb_instructions, new
                ArrayList<>());
        mRvLbInstructions.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager
                .VERTICAL, false));
        mRvLbInstructions.setAdapter(mLbInstructionsAdapter);


        List<String> strings = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            strings.add("" + i);
        }
        mLbInstructionsAdapter.setNewData(strings);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_lb_instructions;
    }

}
