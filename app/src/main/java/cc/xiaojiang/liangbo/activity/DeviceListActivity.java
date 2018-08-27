package cc.xiaojiang.liangbo.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import org.eclipse.paho.client.mqttv3.IMqttToken;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cc.xiaojiang.iotkit.bean.http.Device;
import cc.xiaojiang.iotkit.bean.http.DeviceNickRes;
import cc.xiaojiang.iotkit.bean.http.DeviceUnbindRes;
import cc.xiaojiang.iotkit.http.IotKitDeviceManager;
import cc.xiaojiang.iotkit.http.IotKitHttpCallback;
import cc.xiaojiang.iotkit.mqtt.IotKitActionCallback;
import cc.xiaojiang.iotkit.mqtt.IotKitMqttManager;
import cc.xiaojiang.iotkit.mqtt.IotKitReceivedCallback;
import cc.xiaojiang.liangbo.R;
import cc.xiaojiang.liangbo.adapter.DeviceAdapter;
import cc.xiaojiang.liangbo.base.BaseActivity;
import cc.xiaojiang.liangbo.iotkit.BaseDataModel;
import cc.xiaojiang.liangbo.iotkit.ProductKey;
import cc.xiaojiang.liangbo.utils.ToastUtils;

public class DeviceListActivity extends BaseActivity implements BaseQuickAdapter
        .OnItemChildClickListener, IotKitReceivedCallback, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.rv_device_list)
    RecyclerView rvDeviceList;
    @BindView(R.id.srl_refresh_device)
    SwipeRefreshLayout mSrlRefreshDevice;
    private DeviceAdapter mDeviceAdapter;
    private List<Device> mDevices;
    private AlertDialog mAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //开启mqtt
        IotKitMqttManager.getInstance().addDataCallback(this);
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
        mSrlRefreshDevice.setOnRefreshListener(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_device_list;
    }

    @Override
    protected void onDestroy() {
        IotKitMqttManager.getInstance().removeDataCallback(this);
        if (mAlertDialog != null && mAlertDialog.isShowing()) {
            mAlertDialog.dismiss();
        }
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_add) {
            startToActivity(ProductListActivity.class);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDevices();
    }

    private void getDevices() {
        mSrlRefreshDevice.setRefreshing(true);
        IotKitDeviceManager.getInstance().deviceList(new IotKitHttpCallback<List<Device>>() {
            @Override
            public void onSuccess(List<Device> data) {
                hideRefreshing();
                mDeviceAdapter.setNewData(data);
                queryDevices(data);
            }

            @Override
            public void onError(String code, String errorMsg) {
                hideRefreshing();
            }

        });
    }

    /**
     * 批量查询设备状态
     */
    private void queryDevices(List<Device> data) {
        if (!IotKitMqttManager.getInstance().isConnected()) {
            return;
        }
        for (int i = 0; i < data.size(); i++) {
            queryDevice(data.get(i));
        }
    }

    /**
     * 查询单个设备状态
     */
    public void queryDevice(Device device) {
        IotKitMqttManager.getInstance().queryStatus(device, new IotKitActionCallback() {
            @Override
            public void onSuccess() {
                Logger.d("查询设备成功，deviceId=" + device.getDeviceId());

            }

            @Override
            public void onFailure(String msg) {
                Logger.d("查询设备失败，deviceId=" + device.getDeviceId());
            }


        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void hideRefreshing() {
        if (mSrlRefreshDevice != null && mSrlRefreshDevice.isRefreshing()) {
            mSrlRefreshDevice.setRefreshing(false);
        }
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        Device device = (Device) adapter.getItem(position);
        Intent intent = null;
        switch (view.getId()) {

            case R.id.ll_device_content:
                if (ProductKey.KZZ.equals(device.getProductKey()) || ProductKey.LB.equals(device
                        .getProductKey())) {
                    intent = new Intent(this, KZZActivity.class);
                } else {
                    ToastUtils.show("暂不支持该设备！");
                    return;
                }
                if (mDeviceAdapter.getOnlineStatus(device.getDeviceId())) {
                    intent.putExtra("device_data", device);
                    startActivity(intent);
                } else {
                    ToastUtils.show("设备离线");
                }
//// TODO: 2018/8/13 test
//                intent = new Intent(this, KZZActivity.class);
//                intent.putExtra("device_data", device);
//                startActivity(intent);
                break;
            case R.id.tv_device_swipe_menu_modify:
                showModifyDialog(device.getDeviceId());
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

    private void showModifyDialog(String deviceId) {
        View view = getLayoutInflater().inflate(R.layout.layout_dialog_input, null);
        EditText editText = view.findViewById(R.id.edTxt_dialog_input_nick);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        mAlertDialog = builder.setTitle("修改设备昵称")
                .setView(view)
                .setNegativeButton("取消", null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String nick = editText.getText().toString();
                        if (TextUtils.isEmpty(nick)) {
                            ToastUtils.show("设备昵称不能为空！");
                            return;
                        }
                        if (nick.length() > 6) {
                            ToastUtils.show("输入设备昵称大于6位");
                            return;
                        }
                        modifyDevice(deviceId, nick);
                    }
                }).create();
        mAlertDialog.show();
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
    public void messageArrived(String deviceId, String productKey, String data) {
        BaseDataModel model = new Gson().fromJson(data, BaseDataModel.class);
        BaseDataModel.ParamsBean paramsBean = model.getParams();
        if (paramsBean == null || paramsBean.getOnlineStatus() == null) {
            Logger.e("error getParams!");
            return;
        }
        String onlineStatus = paramsBean.getOnlineStatus().getValue();
        if (onlineStatus.startsWith("online")) {
            mDeviceAdapter.setOnlineStatus(deviceId, "online");
        } else {
            mDeviceAdapter.setOnlineStatus(deviceId, "offline");
        }
    }

    @Override
    public boolean filter(String deviceId) {
        return false;
    }

    @Override
    public void onRefresh() {
        getDevices();
    }
}
