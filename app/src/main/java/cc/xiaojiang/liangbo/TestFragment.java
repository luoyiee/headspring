package cc.xiaojiang.liangbo;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cc.xiaojiang.iotkit.bean.http.Device;
import cc.xiaojiang.iotkit.http.IotKitDeviceManager;
import cc.xiaojiang.iotkit.http.IotKitHttpCallback;
import cc.xiaojiang.liangbo.adapter.DeviceStatusAdapter;
import cc.xiaojiang.liangbo.base.BaseFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class TestFragment extends BaseFragment {


    @BindView(R.id.rv_air_new_device_status)
    RecyclerView mRvAirNewDeviceStatus;
    Unbinder unbinder;


    private List<String> mIndoorPmList=new ArrayList<>();
    private DeviceStatusAdapter mDeviceStatusAdapter;

    public TestFragment() {
        // Required empty public constructor
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_test;
    }


    @Override
    protected void init() {
        mDeviceStatusAdapter = new DeviceStatusAdapter(R.layout.item_home_indoor_pm, mIndoorPmList);
        mRvAirNewDeviceStatus.setLayoutManager(new GridLayoutManager(getContext(), 1,
                LinearLayoutManager.HORIZONTAL, false));
        mRvAirNewDeviceStatus.setAdapter(mDeviceStatusAdapter);
        PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
        pagerSnapHelper.attachToRecyclerView(mRvAirNewDeviceStatus);
        getDevices();
    }

    private void getDevices() {
        IotKitDeviceManager.getInstance().deviceList(new IotKitHttpCallback<List<Device>>() {
            @Override
            public void onSuccess(List<Device> data) {
                if (data == null) {
                    return;
                }
                setDeviceStatus(data);
            }

            @Override
            public void onError(String code, String errorMsg) {

            }
        });
    }

    private void setDeviceStatus(List<Device> data) {
        for (int i = 0; i < data.size(); i++) {
            mIndoorPmList.add("------------");
        }
//        mDeviceStatusAdapter.setNewData(mIndoorPmList);
        mDeviceStatusAdapter.notifyDataSetChanged();
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
