package cc.xiaojiang.liangbo.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cc.xiaojiang.iotkit.bean.http.Product;
import cc.xiaojiang.liangbo.R;
import cc.xiaojiang.liangbo.model.http.CityQueryModel;
import cc.xiaojiang.liangbo.model.realm.WeatherCityCodeRealm;
import cc.xiaojiang.liangbo.utils.ImageLoader;

public class CitySearchAdapter extends BaseQuickAdapter<CityQueryModel, BaseViewHolder> {
    public CitySearchAdapter(int layoutResId, @Nullable List<CityQueryModel> data) {
        super(layoutResId, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, CityQueryModel item) {

        helper.setText(R.id.tv_item_city_search_city, item.getCountyName())
                .setText(R.id.tv_item_city_search_region, item.getCountyName() + ", " + item
                        .getCityName() + ", " + item.getProvinceName());
    }
}
