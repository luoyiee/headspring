package cc.xiaojiang.liangbo.model;

import com.chad.library.adapter.base.entity.SectionEntity;

import cc.xiaojiang.iotkit.bean.http.Goods;

public class GoodsSection extends SectionEntity<Goods> {
    public GoodsSection(boolean isHeader, String header) {
        super(isHeader, header);
    }

    public GoodsSection(Goods goods) {
        super(goods);
    }
}
