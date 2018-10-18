package cc.xiaojiang.liangbo.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cc.xiaojiang.iotkit.bean.http.Product;
import cc.xiaojiang.liangbo.R;
import cc.xiaojiang.liangbo.model.http.WeatherCityModel;
import cc.xiaojiang.liangbo.utils.ImageLoader;

public class WeatherCityManagerAdapter extends BaseQuickAdapter<WeatherCityModel, BaseViewHolder> {
    private String city;

    public WeatherCityManagerAdapter(int layoutResId, @Nullable List<WeatherCityModel> data,
                                     String city) {
        super(layoutResId, data);
        this.city = city;
    }


    @Override
    protected void convert(BaseViewHolder helper, WeatherCityModel item) {
        String weather = item.getTemperature() + "℃ | " + item.getText();
        helper.setText(R.id.tv_item_weather_city_manager_city, item.getCountyName())
                .setText(R.id.tv_item_weather_city_manager_weather, weather)
                .addOnClickListener(R.id.tv_city_manager_swipe_menu_delete)
                .addOnClickListener(R.id.cl_city_manager_content)
                .setVisible(R.id.iv_item_weather_city_manager_my_location, item.getCountyName()
                        .equals(city));

    }
}
