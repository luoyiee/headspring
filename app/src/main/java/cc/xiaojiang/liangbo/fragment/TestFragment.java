package cc.xiaojiang.liangbo.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.rd.PageIndicatorView;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cc.xiaojiang.iotkit.bean.http.Device;
import cc.xiaojiang.iotkit.http.IotKitDeviceManager;
import cc.xiaojiang.iotkit.http.IotKitHttpCallback;
import cc.xiaojiang.iotkit.mqtt.IotKitActionCallback;
import cc.xiaojiang.iotkit.mqtt.IotKitMqttManager;
import cc.xiaojiang.iotkit.mqtt.IotKitReceivedCallback;
import cc.xiaojiang.liangbo.R;
import cc.xiaojiang.liangbo.activity.wifiConfig.ProductListActivity;
import cc.xiaojiang.liangbo.adapter.DeviceStatusAdapter;
import cc.xiaojiang.liangbo.base.BaseFragment;
import cc.xiaojiang.liangbo.http.LoginInterceptor;
import cc.xiaojiang.liangbo.iotkit.BaseDataModel;
import cc.xiaojiang.liangbo.iotkit.IotKitUtils;
import cc.xiaojiang.liangbo.model.DeviceStatus;
import cc.xiaojiang.liangbo.model.event.LoginEvent;
import cc.xiaojiang.liangbo.utils.AccountUtils;


/**
 * A simple {@link Fragment} subclass.
 */
public class TestFragment extends BaseFragment implements IotKitReceivedCallback {

    public static final String DEFAULT_DATA = "-/-";
    @BindView(R.id.rv_air_new_device_status)
    RecyclerView mRvAirNewDeviceStatus;
    @BindView(R.id.iv_add_device)
    ImageView mIvAddDevice;
    @BindView(R.id.ll_device_add)
    LinearLayout mLlDeviceAdd;
    @BindView(R.id.view_air_new_status_indicator)
    PageIndicatorView mViewAirNewStatusIndicator;
    Unbinder unbinder;
    @BindView(R.id.ll_device_status)
    LinearLayout mLlDeviceStatus;


    private DeviceStatusAdapter mDeviceStatusAdapter;
    private List<DeviceStatus> mDeviceStatusList = new ArrayList<>();
    private List<Device> mDeviceList = new ArrayList<>();
    private Timer mTimer = new Timer();

    @Override
    public void onResume() {
        super.onResume();
        if (AccountUtils.isLogin()) {
            getDevices();
        } else {
            setAddDeviceViewVisibility(true);
        }
        IotKitMqttManager.getInstance().addDataCallback(this);
        startTimer();
    }

    @Override
    public void onPause() {
        mTimer.cancel();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        IotKitMqttManager.getInstance().removeDataCallback(this);
        super.onDestroy();
    }

    public TestFragment() {
        // Required empty public constructor
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_test;
    }

    @OnClick(R.id.iv_add_device)
    public void onViewClicked() {
        LoginInterceptor.interceptor(getContext(), ProductListActivity.class.getName(), null);
    }


    private void setAddDeviceViewVisibility(boolean showAddDeviceView) {
        mLlDeviceStatus.setVisibility(showAddDeviceView ? View.INVISIBLE : View.VISIBLE);
        mLlDeviceAdd.setVisibility(showAddDeviceView ? View.VISIBLE : View.INVISIBLE);
    }

    private void startTimer() {
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                for (Device device : mDeviceList) {
                    openRealReporting(device, 1);
                }
            }
        }, 3000, 60 * 1000);
    }

    private void openRealReporting(Device device, int isOpen) {
        HashMap<String, String> hashMap1 = new HashMap<>();
        hashMap1.put("RealReportingSwitch", String.valueOf(isOpen));
        sendCmd(device, hashMap1);
    }

    private void sendCmd(Device device, HashMap<String, String> hashMap) {
        IotKitMqttManager.getInstance().sendCmd(device, hashMap, new IotKitActionCallback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure(String msg) {

            }
        });
    }

    /**
     * 批量查询设备状态
     */
    private void queryDevices(List<Device> data) {
        for (int i = 0; i < data.size(); i++) {
            queryDevice(data.get(i));
            openRealReporting(data.get(i), 1);
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


    /**
     * 当用户登录或退出,会执行
     *
     * @param loginEvent 登录或退出事件
     */
    @Subscribe
    public void onLoginEvent(LoginEvent loginEvent) {
        if (LoginEvent.CODE_LOGIN == loginEvent.getCode()) {
            clearDevice();
        }
    }

    private void clearDevice() {
        mDeviceList.clear();
        mDeviceStatusList.clear();
        mDeviceStatusAdapter.notifyDataSetChanged();
        mViewAirNewStatusIndicator.setCount(0);
        setAddDeviceViewVisibility(true);
    }


    @Override
    protected void init() {
        mDeviceStatusAdapter = new DeviceStatusAdapter(R.layout.item_device_status,
                mDeviceStatusList);
        mRvAirNewDeviceStatus.setLayoutManager(new GridLayoutManager(getContext(), 1,
                LinearLayoutManager.HORIZONTAL, false));
        mRvAirNewDeviceStatus.setAdapter(mDeviceStatusAdapter);
        PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
        pagerSnapHelper.attachToRecyclerView(mRvAirNewDeviceStatus);
        mRvAirNewDeviceStatus.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int position;
                GridLayoutManager l = (GridLayoutManager) recyclerView.getLayoutManager();
                position = l.findFirstVisibleItemPosition();
                mViewAirNewStatusIndicator.setSelection(position);
            }
        });
    }

    private void getDevices() {
        IotKitDeviceManager.getInstance().deviceList(new IotKitHttpCallback<List<Device>>() {
            @Override
            public void onSuccess(List<Device> data) {
                if (data == null || data.isEmpty()) {
                    clearDevice();
                    return;
                }
                mViewAirNewStatusIndicator.setCount(data.size());
                mDeviceList = new ArrayList<>(data);
                setDeviceStatus(data);
                queryDevices(data);
                setAddDeviceViewVisibility(false);
            }

            @Override
            public void onError(String code, String errorMsg) {

            }
        });
    }

    private void setDeviceStatus(List<Device> data) {
        mDeviceStatusList.clear();
        for (Device device : data) {
            DeviceStatus status = new DeviceStatus();
            status.setDeviceId(device.getDeviceId());
            status.setName(IotKitUtils.getDeviceName(device));
            status.setPm25(DEFAULT_DATA);
            mDeviceStatusList.add(status);
        }
        mDeviceStatusAdapter.notifyDataSetChanged();
    }


    @Override
    public void messageArrived(String type, String deviceId, String productKey, String data) {
        if ("get".equals(type)) {
            BaseDataModel model = new Gson().fromJson(data, BaseDataModel.class);
            BaseDataModel.ParamsBean paramsBean = model.getParams();
            if (paramsBean == null || paramsBean.getPM205() == null || paramsBean.getOnlineStatus
                    () == null) {
                Logger.e("error getParams!");
                return;
            }
            String pm205 = paramsBean.getPM205().getValue();
            String onlineStatus = paramsBean.getOnlineStatus().getValue();
            for (int i = 0; i < mDeviceStatusList.size(); i++) {
                DeviceStatus status = mDeviceStatusList.get(i);
                if (status.getDeviceId().equals(deviceId)) {
                    if (onlineStatus.startsWith("online")) {
                        status.setPm25(pm205);
                    } else {
                        status.setPm25(DEFAULT_DATA);
                    }
                    mDeviceStatusAdapter.notifyItemChanged(i);
                }
            }
        }
    }

    @Override
    public boolean filter(String deviceId) {
        for (DeviceStatus status : mDeviceStatusList) {
            if (deviceId.equals(status.getDeviceId())) {
                return false;
            }
        }
        return true;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
