package cc.xiaojiang.liangbo.activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cc.xiaojiang.iotkit.bean.http.Advert;
import cc.xiaojiang.iotkit.bean.http.Goods;
import cc.xiaojiang.iotkit.community.IotKitCommunityManager;
import cc.xiaojiang.iotkit.http.IotKitHttpCallback;
import cc.xiaojiang.liangbo.R;
import cc.xiaojiang.liangbo.adapter.GoodsAdapter;
import cc.xiaojiang.liangbo.base.BaseActivity;
import cc.xiaojiang.liangbo.model.GoodsSection;
import cc.xiaojiang.liangbo.utils.DbUtils;
import cc.xiaojiang.liangbo.utils.GlideImageLoader;

public class ShopActivity extends BaseActivity implements BaseQuickAdapter.OnItemClickListener,
        OnBannerListener {

    @BindView(R.id.rv_shop_goods)
    RecyclerView mRvShopGoods;
    private GoodsAdapter mGoodsAdapter;
    private Banner mBanner;
    private View mHeaderView;
    private List<String> mUrls = new ArrayList<>();
    private List<GoodsSection> mGoodsSectionList = new ArrayList<>();
    private List<Advert> mAdverts = new ArrayList<>();
    private HashMap<String, List<Goods>> sectionMap = new HashMap<>();

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initGoodsList();
        getAdvertList();
        getGoodsList();


    }

    //如果你需要考虑更好的体验，可以这么操作
    @Override
    protected void onStart() {
        super.onStart();
        //开始轮播
        mBanner.startAutoPlay();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //结束轮播
        mBanner.stopAutoPlay();
    }

    private void initGoodsList() {
        initHeader();
        mGoodsAdapter = new GoodsAdapter(R.layout.item_shop_goods, R.layout
                .item_shop_goods_section_header, mGoodsSectionList);
        mRvShopGoods.setLayoutManager(new GridLayoutManager(this, 3));
        mRvShopGoods.setAdapter(mGoodsAdapter);
        mGoodsAdapter.addHeaderView(mHeaderView);
        mGoodsAdapter.setOnItemClickListener(this);
    }

    private void initHeader() {
        mHeaderView = getLayoutInflater().inflate(R.layout.layout_shop_banner, null);
        mBanner = mHeaderView.findViewById(R.id.banner_shop);
        //设置图片加载器
        mBanner.setImageLoader(new GlideImageLoader());
        mBanner.setOnBannerListener(this);
    }

    private void getGoodsList() {
        IotKitCommunityManager.getInstance().getGoodsList(DbUtils.getUserId(), new
                IotKitHttpCallback<List<Goods>>() {
                    @Override
                    public void onSuccess(List<Goods> data) {
                        formatSection(data);
//                        GoodsSection goodsSection = new GoodsSection();
                    }

                    @Override
                    public void onError(String code, String errorMsg) {
                        com.orhanobut.logger.Logger.e(errorMsg);
                    }
                });
    }

    private void formatSection(List<Goods> data) {
        for (Goods goods : data) {
            String cName = goods.getC_name();
            List<Goods> goods1 = sectionMap.get(cName);
            if (goods1 == null) {
                goods1 = new ArrayList<>();
            }
            goods1.add(goods);
            sectionMap.put(cName, goods1);
        }
        for (Map.Entry<String, List<Goods>> entry : sectionMap.entrySet()) {
            String key = entry.getKey();
            List<Goods> values = entry.getValue();
            mGoodsSectionList.add(new GoodsSection(true, key));
            for (Goods goods : values) {
                mGoodsSectionList.add(new GoodsSection(goods));
            }
        }
        mGoodsAdapter.setNewData(mGoodsSectionList);

    }

    private void getAdvertList() {
        IotKitCommunityManager.getInstance().getAdvertList(IotKitCommunityManager
                .ADVERT_TYPE_GOODS, new IotKitHttpCallback<List<Advert>>() {
            @Override
            public void onSuccess(List<Advert> data) {
                mAdverts.clear();
                mAdverts.addAll(data);
                mUrls.clear();
                for (Advert advert : data) {
                    mUrls.add(advert.getCover());
                }
                //设置图片集合
                mBanner.setImages(mUrls);
                //banner设置方法全部调用完毕时最后调用
                mBanner.start();

            }

            @Override
            public void onError(String code, String errorMsg) {
                com.orhanobut.logger.Logger.e(errorMsg);
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_shop;
    }


    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        GoodsSection goodsSection = (GoodsSection) adapter.getItem(position);
        if (goodsSection == null || goodsSection.isHeader) {
            return;
        }
        Goods goods = goodsSection.t;
        BrowserActivity.actionStart(this, goods.getInfo_link(), goods.getTitle(), goods.getTitle
                (), false);
    }

    @Override
    public void OnBannerClick(int position) {
        if (mAdverts.size() > position) {
            Advert advert = mAdverts.get(position);
            BrowserActivity.actionStart(this, advert.getLink_url(), advert.getTitle(), advert
                    .getTitle(), false);
        }

    }
}
