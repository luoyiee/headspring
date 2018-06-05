package cc.xiaojiang.headerspring.feature;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cc.xiaojiang.headerspring.R;
import cc.xiaojiang.headerspring.adapter.DeviceAdapter;
import cc.xiaojiang.headerspring.base.BaseActivity;
import cc.xiaojiang.headerspring.model.bean.Device;
import cc.xiaojiang.headerspring.model.bean.Product;

public class DeviceListActivity extends BaseActivity implements BaseQuickAdapter
        .OnItemClickListener {

    @BindView(R.id.rv_device_list)
    RecyclerView rvDeviceList;
    private DeviceAdapter mDeviceAdapter;
    private List<Device> mDevices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_device_list;
    }

    @Override
    protected void createInit() {
        mDevices = new ArrayList<>();
        mDeviceAdapter = new DeviceAdapter(R.layout.item_device, mDevices);
        mDeviceAdapter.setOnItemClickListener(this);
        rvDeviceList.setLayoutManager(new LinearLayoutManager(this));
        rvDeviceList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration
                .VERTICAL));
        rvDeviceList.setAdapter(mDeviceAdapter);
    }

    @Override
    protected void resumeInit() {
        getDevices();
    }

    private void getDevices() {
        for (int i = 0; i < 10; i++) {
            Device device = new Device();
            mDevices.add(device);
        }
        mDeviceAdapter.setNewData(mDevices);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

    }
}
