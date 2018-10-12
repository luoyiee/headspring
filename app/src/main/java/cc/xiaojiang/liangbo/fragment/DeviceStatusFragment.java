package cc.xiaojiang.liangbo.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnPageChangeListener;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
import cc.xiaojiang.liangbo.activity.ProductListActivity;
import cc.xiaojiang.liangbo.adapter.DeviceStatusAdapter;
import cc.xiaojiang.liangbo.adapter.HomeIndoorPmHolder;
import cc.xiaojiang.liangbo.http.LoginInterceptor;
import cc.xiaojiang.liangbo.iotkit.BaseDataModel;
import cc.xiaojiang.liangbo.iotkit.IotKitUtils;
import cc.xiaojiang.liangbo.model.event.LoginEvent;
import cc.xiaojiang.liangbo.utils.AccountUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class DeviceStatusFragment extends Fragment implements IotKitReceivedCallback,
        OnPageChangeListener {
    public static final String DEFAULT_DATA = "-/-";
    @BindView(R.id.tv_select_device_name)
    TextView mTvSelectDeviceName;
    @BindView(R.id.ll_selected_devices)
    LinearLayout mLlSelectedDevices;
    @BindView(R.id.iv_add_device)
    ImageView mIvAddDevice;
    @BindView(R.id.ll_device_add)
    LinearLayout mLlDeviceAdd;
    @BindView(R.id.textView14)
    TextView mTextView14;
    Unbinder unbinder;
    @BindView(R.id.convenientBanner)
    ConvenientBanner<String> mConvenientBanner;

    private Timer mTimer = new Timer();
    private LinkedHashMap<String, String> mIndoorMap = new LinkedHashMap<>();
    private CBViewHolderCreator mHolderCreator;
    private ArrayList<String> mIndoorPmList = new ArrayList<>();
    private int mDeviceSize;
    private List<Device> mDeviceList = new ArrayList<>();


    public DeviceStatusFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_device_status, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();

    }

    private void init() {
//        mIndoorPmList.add(DEFAULT_DATA);
        mHolderCreator = new CBViewHolderCreator() {
            @Override
            public HomeIndoorPmHolder createHolder(View itemView) {
                return new HomeIndoorPmHolder(itemView);
            }

            @Override
            public int getLayoutId() {
                return R.layout.item_home_indoor_pm;
            }
        };
        mConvenientBanner.setPages(mHolderCreator, mIndoorPmList);
        mConvenientBanner.setOnPageChangeListener(this);
    }


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

    private void setAddDeviceViewVisibility(boolean showAddDeviceView) {
        mLlSelectedDevices.setVisibility(showAddDeviceView ? View.INVISIBLE : View.VISIBLE);
        mLlDeviceAdd.setVisibility(showAddDeviceView ? View.VISIBLE : View.INVISIBLE);
    }

    private void getDevices() {
        if (!IotKitMqttManager.getInstance().isConnected()) {
            return;
        }
        IotKitDeviceManager.getInstance().deviceList(new IotKitHttpCallback<List<Device>>() {
            @Override
            public void onSuccess(List<Device> data) {
                mDeviceSize = data.size();
                mDeviceList = data;
                if (mDeviceSize == 0) {
                    setAddDeviceViewVisibility(true);
                    return;
                }
                if (mDeviceSize > 1) {
                    mConvenientBanner.setPageIndicator(new int[]{R.drawable.dot_default_indicator,
                            R.drawable.dot_select_indicator});
                }
                for (Device datum : data) {
                    mIndoorMap.put(datum.getDeviceId(), "");
                }
                mTvSelectDeviceName.setText(IotKitUtils.getDeviceName(data.get(0)));
                setAddDeviceViewVisibility(false);
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
     * 当用户登录或退出,会执行
     *
     * @param loginEvent 登录或退出事件
     */
    @Subscribe
    public void onLoginEvent(LoginEvent loginEvent) {
        if (LoginEvent.CODE_LOGIN == loginEvent.getCode()) {
            mIndoorMap.clear();
        }
    }

    @OnClick(R.id.iv_add_device)
    public void onViewClicked() {
        LoginInterceptor.interceptor(getContext(), ProductListActivity.class.getName(), null);
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
            if (onlineStatus.startsWith("online")) {
                mIndoorMap.put(deviceId, pm205);
            } else {
                mIndoorMap.put(deviceId, DEFAULT_DATA);
            }
            if (mIndoorMap.size() == mDeviceSize && mDeviceSize > 0) {
                mIndoorPmList.clear();
                // 遍历 ArrayMap
                mIndoorPmList.addAll(mIndoorMap.values());
                int currentItem = mConvenientBanner.getCurrentItem();
                Logger.d("notifyData " + mIndoorPmList.toString());
                mConvenientBanner.setCurrentItem(currentItem, false);
                mConvenientBanner.setFirstItemPos(currentItem);
                mConvenientBanner.notifyDataSetChanged();
                if (mIndoorMap.size() > 1) {
                    mConvenientBanner.setPageIndicator(new int[]{R.drawable.dot_default_indicator,
                            R.drawable.dot_select_indicator});
                }
            } else {
                Logger.e("data error");
            }
        }
    }


    @Override
    public boolean filter(String deviceId) {
        for (Device device : mDeviceList) {
            if (deviceId.equals(device.getDeviceId())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

    }

    @Override
    public void onPageSelected(int index) {
        final Device device = mDeviceList.get(index);
        if (mTvSelectDeviceName != null) {
            mTvSelectDeviceName.setText(IotKitUtils.getDeviceName(device));
            Logger.d("set name: " + IotKitUtils.getDeviceName(device));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
