package cc.xiaojiang.liangbo.activity;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cc.xiaojiang.iotkit.bean.http.Advert;
import cc.xiaojiang.iotkit.bean.http.Goods;
import cc.xiaojiang.iotkit.community.IotKitCommunityManager;
import cc.xiaojiang.iotkit.http.IotKitHttpCallback;
import cc.xiaojiang.liangbo.R;
import cc.xiaojiang.liangbo.adapter.GoodsAdapter;
import cc.xiaojiang.liangbo.base.BaseActivity;
import cc.xiaojiang.liangbo.utils.DbUtils;
import cc.xiaojiang.liangbo.utils.GlideImageLoader;

public class ShopActivity extends BaseActivity implements BaseQuickAdapter.OnItemClickListener {

    @BindView(R.id.rv_shop_goods)
    RecyclerView mRvShopGoods;
    private GoodsAdapter mGoodsAdapter;
    private Banner mBanner;
    private View mHeaderView;
    private List<String> mStrings = new ArrayList<>();
    private List<Goods> mListsBeans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initGoodsList();
        getAdvertList();
        getGoodsList();


    }

    private void initGoodsList() {
        initHeader();
        mGoodsAdapter = new GoodsAdapter(R.layout.item_shop_goods, mListsBeans);
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
    }

    private void getGoodsList() {
        IotKitCommunityManager.getInstance().getGoodsList(DbUtils.getUserId(), 1, 20, new
                IotKitHttpCallback<List<Goods>>() {
                    @Override
                    public void onSuccess(List<Goods> data) {
                        mGoodsAdapter.setNewData(data);
                    }

                    @Override
                    public void onError(String code, String errorMsg) {

                    }
                });
    }

    private void getAdvertList() {
        IotKitCommunityManager.getInstance().getAdvertList(IotKitCommunityManager
                .ADVERT_TYPE_GOODS, new IotKitHttpCallback<List<Advert>>() {
            @Override
            public void onSuccess(List<Advert> data) {
                mStrings.clear();
                for (Advert advert : data) {
                    mStrings.add(advert.getCover());
                }
                //设置图片集合
                mBanner.setImages(mStrings);
                //banner设置方法全部调用完毕时最后调用
                mBanner.start();

            }

            @Override
            public void onError(String code, String errorMsg) {

            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_shop;
    }


    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Goods goods = (Goods) adapter.getItem(position);
        BrowserActivity.actionStart(this, goods.getInfo_link(), goods.getTitle());
    }
}
