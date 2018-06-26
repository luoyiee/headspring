package cc.xiaojiang.headerspring.feature;

import android.content.Intent;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cc.xiaojiang.headerspring.R;
import cc.xiaojiang.headerspring.adapter.DeviceAdapter;
import cc.xiaojiang.headerspring.base.BaseActivity;
import cc.xiaojiang.headerspring.model.bean.DeviceResponse;
import cc.xiaojiang.iotkit.http.IotKitCallBack;
import cc.xiaojiang.iotkit.http.IotKitDeviceManager;
import cc.xiaojiang.iotkit.mqtt.IotKitConnectionManager;
import cc.xiaojiang.iotkit.mqtt.IotKitReceivedCallback;

public class DeviceListActivity extends BaseActivity implements BaseQuickAdapter
        .OnItemChildClickListener {

    @BindView(R.id.rv_device_list)
    RecyclerView rvDeviceList;
    private DeviceAdapter mDeviceAdapter;
    private List<DeviceResponse.DataBean> mDevices;
//    private IotKitReceivedCallback mIotKitReceivedCallback = new IotKitReceivedCallback() {
//        @Override
//        public void messageArrived(String deviceId, String data) {
////            com.orhanobut.logger.Logger.d("deviceId:" + deviceId + ", data: " + data);
//        }
//
//        @Override
//        public boolean filter(String deviceId) {
//            return false;
//        }
//    };


    @Override
    protected int getLayoutId() {
        return R.layout.activity_device_list;
    }

    @Override
    protected void createInit() {
        mDevices = new ArrayList<>();
        mDeviceAdapter = new DeviceAdapter(R.layout.item_device, mDevices);
        mDeviceAdapter.setOnItemChildClickListener(this);
        rvDeviceList.setLayoutManager(new LinearLayoutManager(this));
        rvDeviceList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration
                .VERTICAL));
        rvDeviceList.setAdapter(mDeviceAdapter);
    }

    @Override
    protected void resumeInit() {
//        IotKitConnectionManager.getInstance().addDataCallback(mIotKitReceivedCallback);
        getDevices();
    }

    @Override
    protected void onPause() {
//        IotKitConnectionManager.getInstance().removeDataCallback(mIotKitReceivedCallback);
        super.onPause();

    }

    private void getDevices() {
        IotKitDeviceManager.getInstance().deviceList(new IotKitCallBack() {
            @Override
            public void onSuccess(String response) {
                DeviceResponse deviceResponse = new Gson().fromJson(response, DeviceResponse.class);
                mDeviceAdapter.setNewData(deviceResponse.getData());

            }

            @Override
            public void onError(int code, String errorMsg) {

            }
        });
    }


    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        DeviceResponse.DataBean dataBean = (DeviceResponse.DataBean) adapter.getItem(position);
        switch (view.getId()) {
            case R.id.ll_device_content:
                Intent intent = new Intent(this, DeviceControlActivity.class);
                intent.putExtra("device_data", dataBean);
                startActivity(intent);
                break;
            case R.id.tv_device_swipe_menu_modify:
                modifyDevice(dataBean.getDeviceId(), "test");
                break;
            case R.id.tv_device_swipe_menu_share:
                shareDevice(dataBean.getProductKey(), dataBean.getDeviceId());
                break;
            case R.id.tv_device_swipe_menu_delete:
                deleteDevice(dataBean.getProductKey(), dataBean.getDeviceId());
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

    private void shareDevice(String productKey, String deviceId) {
        IotKitDeviceManager.getInstance().deviceShare(productKey, deviceId, new IotKitCallBack() {
            @Override
            public void onSuccess(String response) {

            }

            @Override
            public void onError(int code, String errorMsg) {

            }
        });
    }

    private void deleteDevice(String productKey, String deviceId) {
        IotKitDeviceManager.getInstance().deviceUnbind(productKey, deviceId, new IotKitCallBack() {
            @Override
            public void onSuccess(String response) {

            }

            @Override
            public void onError(int code, String errorMsg) {

            }
        });
    }
}
