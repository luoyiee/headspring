package cc.xiaojiang.liangbo.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cc.xiaojiang.liangbo.R;
import cc.xiaojiang.liangbo.model.http.WeatherCityModel;

public class WeatherRecommendCityAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public WeatherRecommendCityAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tv_weather_recommend_city, item);
    }
}
