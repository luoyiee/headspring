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
import cc.xiaojiang.baselibrary.util.ToastUtils;
import cc.xiaojiang.headerspring.R;
import cc.xiaojiang.headerspring.adapter.DeviceAdapter;
import cc.xiaojiang.headerspring.base.BaseActivity;
import cc.xiaojiang.headerspring.model.bean.DeviceResp;
import cc.xiaojiang.iotkit.http.IotKitCallBack;
import cc.xiaojiang.iotkit.http.IotKitDeviceManager;

public class DeviceListActivity extends BaseActivity implements BaseQuickAdapter
        .OnItemClickListener, BaseQuickAdapter.OnItemChildClickListener {

    @BindView(R.id.rv_device_list)
    RecyclerView rvDeviceList;
    private DeviceAdapter mDeviceAdapter;
    private List<DeviceResp.DataBean> mDevices;

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
        mDeviceAdapter.setOnItemChildClickListener(this);
        rvDeviceList.setLayoutManager(new LinearLayoutManager(this));
        rvDeviceList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration
                .VERTICAL));
        rvDeviceList.setAdapter(mDeviceAdapter);
    }

    @Override
    protected void resumeInit() {
//        getDevices();
        for (int i = 0; i < 10; i++) {
            mDevices.add(new DeviceResp.DataBean());
        }
        mDeviceAdapter.setNewData(mDevices);
    }

    private void getDevices() {
        IotKitDeviceManager.getInstance().deviceList(new IotKitCallBack() {
            @Override
            public void onSuccess(String response) {
                for (int i = 0; i < 10; i++) {
                    mDevices.add(new DeviceResp.DataBean());
                }
                mDeviceAdapter.setNewData(mDevices);
            }

            @Override
            public void onError(int code, String errorMsg) {

            }
        });
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        DeviceResp.DataBean dataBean = (DeviceResp.DataBean) adapter.getItem(position);
        switch (view.getId()) {
            case R.id.tv_device_swipe_menu_modify:
                modifyDevice(dataBean.getDeviceId(), "test");
                break;
            case R.id.tv_device_swipe_menu_share:
                shareDevice(dataBean.getDeviceId());
                break;
            case R.id.tv_device_swipe_menu_delete:
                deleteDevice(dataBean.getDeviceId());
                break;

        }
    }

    private void modifyDevice(String deviceId, String test) {
        IotKitDeviceManager.getInstance().deviceNick(deviceId, test, new IotKitCallBack() {
            @Override
            public void onSuccess(String response) {

            }

            @Override
            public void onError(int code, String errorMsg) {

            }
        });
    }

    private void shareDevice(String deviceId) {
        IotKitDeviceManager.getInstance().deviceShare(deviceId, new
                IotKitCallBack() {
                    @Override
                    public void onSuccess(String response) {

                    }

                    @Override
                    public void onError(int code, String errorMsg) {

                    }
                });
    }

    private void deleteDevice(String deviceId) {
        IotKitDeviceManager.getInstance().deviceUnbind(deviceId, new IotKitCallBack() {
            @Override
            public void onSuccess(String response) {

            }

            @Override
            public void onError(int code, String errorMsg) {

            }
        });
        ToastUtils.show("删除");
    }
}
