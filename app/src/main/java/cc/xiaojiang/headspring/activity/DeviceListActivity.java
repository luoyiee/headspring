package cc.xiaojiang.headspring.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.orhanobut.logger.Logger;

import org.eclipse.paho.client.mqttv3.IMqttToken;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cc.xiaojiang.headspring.R;
import cc.xiaojiang.headspring.adapter.DeviceAdapter;
import cc.xiaojiang.headspring.base.BaseActivity;
import cc.xiaojiang.iotkit.bean.http.Device;
import cc.xiaojiang.iotkit.bean.http.DeviceNickRes;
import cc.xiaojiang.iotkit.bean.http.DeviceUnbindRes;
import cc.xiaojiang.iotkit.http.IotKitDeviceManager;
import cc.xiaojiang.iotkit.http.IotKitHttpCallback;
import cc.xiaojiang.iotkit.mqtt.IotKitActionCallback;
import cc.xiaojiang.iotkit.mqtt.IotKitConnectionManager;
import cc.xiaojiang.iotkit.mqtt.IotKitReceivedCallback;

public class DeviceListActivity extends BaseActivity implements BaseQuickAdapter
        .OnItemChildClickListener, IotKitReceivedCallback {

    @BindView(R.id.rv_device_list)
    RecyclerView rvDeviceList;
    private DeviceAdapter mDeviceAdapter;
    private List<Device> mDevices;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IotKitConnectionManager.getInstance().addDataCallback(this);
        initView();
    }

    private void initView() {
        mDevices = new ArrayList<>();
        mDeviceAdapter = new DeviceAdapter(R.layout.item_device, mDevices);
        mDeviceAdapter.setOnItemChildClickListener(this);
        rvDeviceList.setLayoutManager(new LinearLayoutManager(this));
        rvDeviceList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration
                .VERTICAL));
        rvDeviceList.setAdapter(mDeviceAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDevices();

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_device_list;
    }

    @Override
    protected void onDestroy() {
        IotKitConnectionManager.getInstance().removeDataCallback(this);
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_add) {
            startToActivity(ProductListActivity.class);
        }
        return super.onOptionsItemSelected(item);
    }

    private void getDevices() {
        IotKitDeviceManager.getInstance().deviceList(new IotKitHttpCallback<List<Device>>() {
            @Override
            public void onSuccess(List<Device> data) {
                mDeviceAdapter.setNewData(data);
                queryDevices(data);
            }

            @Override
            public void onError(String code, String errorMsg) {

            }

        });
    }


    /**
     * 批量查询设备状态
     */
    private void queryDevices(List<Device> data) {
        for (int i = 0; i < data.size(); i++) {
            queryDevice(data.get(i));
        }
    }


    /**
     * 查询单个设备状态
     */
    public void queryDevice(Device device) {
        IotKitConnectionManager.getInstance().queryStatus(device.getProductKey(),
                device.getDeviceId(), new IotKitActionCallback() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        Logger.d("查询设备成功，deviceId=" + device.getDeviceId());

                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable
                            exception) {
                        Logger.d("查询设备失败，deviceId=" + device.getDeviceId());
                    }
                });
    }


    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        Device device = (Device) adapter.getItem(position);
        Intent intent;
        switch (view.getId()) {
            case R.id.ll_device_content:
                intent = new Intent(this, DeviceControlActivity.class);
                intent.putExtra("device_data", device);
                startActivity(intent);
                break;
            case R.id.tv_device_swipe_menu_modify:
                modifyDevice(device.getDeviceId(), "test");
                break;
            case R.id.tv_device_swipe_menu_share:
                intent = new Intent(this, ShareQrCodeActivity.class);
                intent.putExtra("device_data", device);
                startActivity(intent);
                break;
            case R.id.tv_device_swipe_menu_delete:
                deleteDevice(device.getProductKey(), device.getDeviceId());
                break;

        }
    }

    private void modifyDevice(String deviceId, String test) {
        IotKitDeviceManager.getInstance().deviceNick(deviceId, test, new
                IotKitHttpCallback<DeviceNickRes>() {
                    @Override
                    public void onSuccess(DeviceNickRes data) {
                        getDevices();

                    }

                    @Override
                    public void onError(String code, String errorMsg) {

                    }


                });
    }


    private void deleteDevice(String productKey, String deviceId) {
        IotKitDeviceManager.getInstance().deviceUnbind(productKey, deviceId, new
                IotKitHttpCallback<DeviceUnbindRes>() {
                    @Override
                    public void onSuccess(DeviceUnbindRes data) {
                        getDevices();
                    }

                    @Override
                    public void onError(String code, String errorMsg) {

                    }

                });
    }


    @Override
    public void messageArrived(String deviceId, String onlineStatus, String data) {
        if (onlineStatus.startsWith("online")) {
            mDeviceAdapter.updateOnlineStatus(deviceId, "online");
        } else {
            mDeviceAdapter.updateOnlineStatus(deviceId, "offline");
        }
    }

    @Override
    public boolean filter(String deviceId) {
        return false;
    }
}
