package cc.xiaojiang.liangbo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cc.xiaojiang.liangbo.R;
import cc.xiaojiang.liangbo.adapter.LbInstructionsAdapter;
import cc.xiaojiang.liangbo.base.BaseActivity;

public class InstructionsActivity extends BaseActivity {
    @BindView(R.id.rv_lb_instructions)
    RecyclerView mRvLbInstructions;
    private LbInstructionsAdapter mLbInstructionsAdapter;


    public static void actionStart(Context context, int type) {
        Intent intent = new Intent(context, InstructionsActivity.class);
        intent.putExtra("type", type);
        context.startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLbInstructionsAdapter = new LbInstructionsAdapter(R.layout.item_lb_instructions, new
                ArrayList<>());
        mRvLbInstructions.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager
                .VERTICAL, false));
        mRvLbInstructions.setAdapter(mLbInstructionsAdapter);
        int type = getIntent().getIntExtra("type", 0);
        if (type == 1) {
            mLbInstructionsAdapter.setNewData(getDyInstructions());
            setTitle("空智子使用说明书");
        }
        if (type == 2) {
            mLbInstructionsAdapter.setNewData(getLbInstructions());
            setTitle("LB净化器使用说明书");
        }
    }

    private List<Integer> getLbInstructions() {
        List<Integer> integers = new ArrayList<>();
        integers.add(R.drawable.lb_instructions_1);
        integers.add(R.drawable.lb_instructions_2);
        integers.add(R.drawable.lb_instructions_3);
        integers.add(R.drawable.lb_instructions_4);
        integers.add(R.drawable.lb_instructions_5);
        integers.add(R.drawable.lb_instructions_6);
        integers.add(R.drawable.lb_instructions_7);
        integers.add(R.drawable.lb_instructions_8);
        integers.add(R.drawable.lb_instructions_9);
        integers.add(R.drawable.lb_instructions_10);
        integers.add(R.drawable.lb_instructions_11);
        integers.add(R.drawable.lb_instructions_12);
        integers.add(R.drawable.lb_instructions_13);
        integers.add(R.drawable.lb_instructions_14);
        integers.add(R.drawable.lb_instructions_15);
        integers.add(R.drawable.lb_instructions_16);
        integers.add(R.drawable.lb_instructions_17);
        integers.add(R.drawable.lb_instructions_18);
        integers.add(R.drawable.lb_instructions_19);
        integers.add(R.drawable.lb_instructions_20);
        return integers;
    }

    private List<Integer> getDyInstructions() {
        List<Integer> integers = new ArrayList<>();
        integers.add(R.drawable.dy_instructions_1);
        integers.add(R.drawable.dy_instructions_2);
        integers.add(R.drawable.dy_instructions_3);
        integers.add(R.drawable.dy_instructions_4);
        integers.add(R.drawable.dy_instructions_5);
        integers.add(R.drawable.dy_instructions_6);
        integers.add(R.drawable.dy_instructions_7);
        integers.add(R.drawable.dy_instructions_8);
        integers.add(R.drawable.dy_instructions_9);
        integers.add(R.drawable.dy_instructions_10);
        integers.add(R.drawable.dy_instructions_11);
        integers.add(R.drawable.dy_instructions_12);
        integers.add(R.drawable.dy_instructions_13);
        integers.add(R.drawable.dy_instructions_14);
        integers.add(R.drawable.dy_instructions_15);
        integers.add(R.drawable.dy_instructions_16);
        integers.add(R.drawable.dy_instructions_17);
        integers.add(R.drawable.dy_instructions_18);
        integers.add(R.drawable.dy_instructions_19);
        integers.add(R.drawable.dy_instructions_20);
        integers.add(R.drawable.dy_instructions_21);
        integers.add(R.drawable.dy_instructions_22);
        return integers;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_instructions;
    }

}
