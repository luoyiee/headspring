package cc.xiaojiang.headspring.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cc.xiaojiang.headspring.R;
import cc.xiaojiang.headspring.adapter.ProductAdapter;
import cc.xiaojiang.headspring.base.BaseActivity;
import cc.xiaojiang.headspring.model.bean.ProductResp;
import cc.xiaojiang.headspring.utils.ToastUtils;
import cc.xiaojiang.iotkit.bean.http.Product;
import cc.xiaojiang.iotkit.http.IotKitCallBack;
import cc.xiaojiang.iotkit.http.IotKitDeviceManager;
import cc.xiaojiang.iotkit.http.IotKitHttpCallback2;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class ProductListActivity extends BaseActivity implements BaseQuickAdapter
        .OnItemClickListener {

    @BindView(R.id.ll_product_list_scan)
    LinearLayout llProductListScan;
    @BindView(R.id.rv_product)
    RecyclerView rvProduct;


    private static final int REQUEST_CODE = 1;

    private ProductAdapter mProductAdapter;
    private List<Product> mProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        getProducts();
    }

    private void initView() {
        mProducts = new ArrayList<>();
        mProductAdapter = new ProductAdapter(R.layout.item_product, mProducts);
        mProductAdapter.setOnItemClickListener(this);
        rvProduct.setLayoutManager(new LinearLayoutManager(this));
        rvProduct.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration
                .VERTICAL));
        rvProduct.setAdapter(mProductAdapter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_product_list;
    }


    private void getProducts() {
        IotKitDeviceManager.getInstance().productList(new IotKitHttpCallback2<List<Product>>() {
            @Override
            public void onSuccess(List<Product> data) {
                mProductAdapter.setNewData(data);
            }

            @Override
            public void onError(String code, String errorMsg) {

            }
        });

    }

    @OnClick(R.id.ll_product_list_scan)
    public void onViewClicked() {
        ProductListActivityPermissionsDispatcher.startToScanCodeActivityWithPermissionCheck(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ProductListActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode,
                grantResults);
    }

    @NeedsPermission({Manifest.permission.CAMERA})
    void startToScanCodeActivity() {
        Intent intent = new Intent(this, ScanCodeActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @OnPermissionDenied(Manifest.permission.CAMERA)
    void onCameraDenied() {
        ToastUtils.show(R.string.scan_code_permission_tip);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        Product product = (Product) adapter.getItem(position);
        startToConfigInfoActivity(product.getProductKey());
    }

    public void startToConfigInfoActivity(String productKey) {
        Intent intent = new Intent(this, ConfigInfoActivity.class);
        intent.putExtra("product_key", productKey);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        /**
         * 处理二维码扫描结果
         */
        //处理扫描结果（在界面上显示）
        if (requestCode == REQUEST_CODE) {
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    if (result == null) {
                        ToastUtils.show("解析错误！");
                        return;
                    }
                    Logger.d("解析成功，result=" + result);
                    // TODO: 2018/7/14 解析productKey
                    startToConfigInfoActivity(result);
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    ToastUtils.show("解析失败 请重新尝试!");
                }
            }
        }

    }

}
