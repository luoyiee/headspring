package cc.xiaojiang.headerspring.feature;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cc.xiaojiang.headerspring.R;
import cc.xiaojiang.headerspring.adapter.ProductAdapter;
import cc.xiaojiang.headerspring.base.BaseActivity;
import cc.xiaojiang.headerspring.model.bean.ProductResp;
import cc.xiaojiang.headerspring.utils.ToastUtils;
import cc.xiaojiang.iotkit.http.IotKitCallBack;
import cc.xiaojiang.iotkit.http.IotKitDeviceManager;

public class ProductListActivity extends BaseActivity implements BaseQuickAdapter
        .OnItemClickListener {

    @BindView(R.id.ll_product_list_scan)
    LinearLayout llProductListScan;
    @BindView(R.id.rv_product)
    RecyclerView rvProduct;

    private ProductAdapter mProductAdapter;
    private List<ProductResp.DataBean> mProducts;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_product_list;
    }

    @Override
    protected void createInit() {
        mProducts = new ArrayList<>();
        mProductAdapter = new ProductAdapter(R.layout.item_product, mProducts);
        mProductAdapter.setOnItemClickListener(this);
        rvProduct.setLayoutManager(new LinearLayoutManager(this));
        rvProduct.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration
                .VERTICAL));
        rvProduct.setAdapter(mProductAdapter);

    }

    @Override
    protected void resumeInit() {
        getProducts();
    }

    private void getProducts() {
        IotKitDeviceManager.getInstance().productList(new IotKitCallBack() {
            @Override
            public void onSuccess(String response) {
                ProductResp productResp = new Gson().fromJson(response, ProductResp.class);
                mProductAdapter.setNewData(productResp.getData());
            }

            @Override
            public void onError(int code, String errorMsg) {

            }
        });

    }

    @OnClick(R.id.ll_product_list_scan)
    public void onViewClicked() {
        ToastUtils.show("scan");

    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        startToActivity(ConfigInfoActivity.class);
    }
}
