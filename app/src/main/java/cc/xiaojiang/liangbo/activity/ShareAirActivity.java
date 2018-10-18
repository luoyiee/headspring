package cc.xiaojiang.liangbo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import butterknife.BindView;
import cc.xiaojiang.liangbo.R;

public class ShareAirActivity extends ShareActivity {

    @BindView(R.id.tv_share_air_city)
    TextView mTvShareAirCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String city = getIntent().getStringExtra("city");
        mTvShareAirCity.setText(city);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_share_air;
    }

    public static void actionStart(Context context, String city) {
        Intent intent = new Intent(context, ShareAirActivity.class);
        intent.putExtra("city", city);
        context.startActivity(intent);
    }
}
