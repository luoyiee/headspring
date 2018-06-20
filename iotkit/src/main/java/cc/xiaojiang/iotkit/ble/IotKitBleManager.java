package cc.xiaojiang.iotkit.ble;

import android.Manifest;
import android.app.Application;
import android.bluetooth.BluetoothGatt;
import android.support.annotation.RequiresPermission;

import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.scan.BleScanRuleConfig;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;

import java.util.List;
import java.util.UUID;


public class IotKitBleManager {
    private static final String SERVICE_UUID = "9B74FEB7-D856-42DF-B78A-9E0932A04566";
    private static final String WRITE_CHARACTERISTICS_UUID = "9B74FEA7-D856-42DF-B78A-9E0932A04566";
    private static final String INDICATE_CHARACTERISTICS_UUID =
            "9B74FEA8-D856-42DF-B78A-9E0932A04566";
    private static final String READ_CHARACTERISTICS_UUID = "9B74FEA9-D856-42DF-B78A-9E0932A04566";
    private boolean authed = false;


    private static final IotKitBleManager ourInstance = new IotKitBleManager();

    public static IotKitBleManager getInstance() {
        return ourInstance;
    }

    private IotKitBleManager() {

    }

    public  void init(Application context) {
        BleManager.getInstance().init(context);
        BleManager.getInstance()
                .enableLog(true)
                .setReConnectCount(1, 5000)
                .setSplitWriteNum(20)
                .setConnectOverTime(10000)
                .setOperateTimeout(5000);
    }

    public void enableBluetooth() {
        BleManager.getInstance().enableBluetooth();
    }

    public void isSupportBle() {
        BleManager.getInstance().isSupportBle();
    }

    public void disableBluetooth() {
        BleManager.getInstance().disableBluetooth();
    }


    @RequiresPermission(Manifest.permission.BLUETOOTH_ADMIN)
    public void scan(final BleScanCallback bleScanCallback) {
        enableBluetooth();
        UUID[] serviceUuids = {UUID.fromString(SERVICE_UUID)};
        BleScanRuleConfig scanRuleConfig = new BleScanRuleConfig.Builder()
                .setServiceUuids(serviceUuids)
                .setScanTimeOut(10000)
                .build();
        BleManager.getInstance().initScanRule(scanRuleConfig);
        BleManager.getInstance().scan(new BleScanCallback() {
            @Override
            public void onScanFinished(List<BleDevice> scanResultList) {
                bleScanCallback.onScanFinished(scanResultList);
            }

            @Override
            public void onScanStarted(boolean success) {
                bleScanCallback.onScanStarted(success);
            }

            @Override
            public void onScanning(BleDevice bleDevice) {
                bleScanCallback.onScanning(bleDevice);
            }
        });
    }

    public void cancelScan() {
        BleManager.getInstance().cancelScan();
    }

    public void connect(BleDevice bleDevice, final IotBleCallback callback) {
        connect(bleDevice.getMac(), callback);
    }


    /**
     * 连接蓝牙设备
     */
    public void connect(String mac, final IotBleCallback callback) {
        BleManager.getInstance().connect(mac, new BleGattCallback() {
            @Override
            public void onStartConnect() {
                callback.onStartConnect();
            }

            @Override
            public void onConnectFail(BleDevice bleDevice, BleException exception) {
                callback.onConnectFail(bleDevice, exception);
            }

            @Override
            public void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status) {
                callback.onConnectSuccess(bleDevice, gatt, status);
                bleIndicate(bleDevice, callback);

            }

            @Override
            public void onDisConnected(boolean isActiveDisConnected, BleDevice device,
                                       BluetoothGatt gatt, int status) {
                callback.onDisConnected(isActiveDisConnected, device, gatt, status);
            }
        });
    }

    /**
     * 接收蓝牙设备数据
     */
    private void bleIndicate(final BleDevice bleDevice, final IotBleCallback callback) {
        BleManager.getInstance().indicate(bleDevice, SERVICE_UUID, "ss", new
                IotKitBleIndicateCallback() {
                    @Override
                    void onCharacteristicChanged(PacketHeader packetHeader, byte[] bytes) {
                        handle(bleDevice, packetHeader, bytes, callback);
                    }

                    @Override
                    public void onIndicateSuccess() {
                        callback.onIndicateSuccess();
                    }

                    @Override
                    public void onIndicateFailure(BleException exception) {
                        callback.onIndicateFailure(exception);
                    }
                });
    }

    /**
     * 处理接收到的数据
     */
    private void handle(BleDevice bleDevice, PacketHeader packetHeader, byte[] body,
                        IotBleCallback callback) {
        switch (packetHeader.getCmd()) {
            case BleProto.EmCmdId.ECI_req_auth_VALUE:
                handleAuth(bleDevice, packetHeader, body);
                break;
            case BleProto.EmCmdId.ECI_req_init_VALUE:
                handleInit(bleDevice, packetHeader, body);
                break;
            case BleProto.EmCmdId.ECI_req_sendData_VALUE:
                handleData(bleDevice, packetHeader, body, callback);
                break;
        }
    }

    /**
     * 处理开蓝牙设备业务数据
     */
    private void handleData(BleDevice bleDevice, PacketHeader packetHeader, byte[] body,
                            IotBleCallback callback) {
        try {
            BleProto.SendDataRequest sendDataRequest = BleProto.SendDataRequest.parseFrom(body);
            //调用回调
            callback.onDataReceived(sendDataRequest.getSendData().toByteArray());
            //回复设备
            BleProto.BaseResponse baseResponse;
            if (!authed) {
                baseResponse = BleProto.BaseResponse.newBuilder().setErrCode(BleProto
                        .EmErrCode.ERR_needAuth_VALUE).build();
            } else {
                baseResponse = BleProto.BaseResponse.newBuilder().setErrCode(BleProto
                        .EmErrCode.NO_ERR_VALUE).build();
            }
            BleProto.SendDataResponse sendDataResponse = BleProto.SendDataResponse.newBuilder()
                    .setBaseResponse(baseResponse)
                    .build();
            bleWrite(bleDevice, packetHeader.getCmd(), packetHeader.getSeq(), sendDataResponse
                    .toByteArray());
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化
     */
    private void handleInit(BleDevice bleDevice, PacketHeader packetHeader, byte[] body) {
        try {
            BleProto.InitRequest initRequest = BleProto.InitRequest.parseFrom(body);
            // TODO: 2018/6/8 request判断
//            if (authRequest.getProtoVersion() < 2) {
//                authBuilder.setBaseResponse(StandardBleProto.BaseResponse.newBuilder().setErrCode
//                        (StandardBleProto.EmErrCode.ERR_deviceProtoVersionNeedUpdate_VALUE));
//            }
            BleProto.BaseResponse baseResponse;
            if (!authed) {
                baseResponse = BleProto.BaseResponse.newBuilder().setErrCode(BleProto
                        .EmErrCode.ERR_needAuth_VALUE).build();
            } else {
                baseResponse = BleProto.BaseResponse.newBuilder().setErrCode(BleProto
                        .EmErrCode.NO_ERR_VALUE).build();
            }
            BleProto.InitResponse initResponse = BleProto.InitResponse.newBuilder()
                    .setBaseResponse(baseResponse)
                    // TODO: 2018/6/8 getUserId
                    .setUserIdHigh(1)
                    .setUserIdLow(1)
                    .build();
            bleWrite(bleDevice, packetHeader.getCmd(), packetHeader.getSeq(), initResponse
                    .toByteArray());
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
    }

    /**
     * 授权
     */
    private void handleAuth(BleDevice bleDevice, PacketHeader packetHeader, byte[] body) {
        try {
            BleProto.AuthRequest authRequest = BleProto.AuthRequest.parseFrom(body);
            // TODO: 2018/6/8 request判断
//            if (authRequest.getProtoVersion() < 2) {
//                authBuilder.setBaseResponse(StandardBleProto.BaseResponse.newBuilder().setErrCode
//                        (StandardBleProto.EmErrCode.ERR_deviceProtoVersionNeedUpdate_VALUE));
//            }
            authed = true;
            BleProto.AuthResponse authResponse = BleProto.AuthResponse.newBuilder()
                    .setBaseResponse(BleProto.BaseResponse.newBuilder().setErrCode
                            (BleProto.EmErrCode.NO_ERR_VALUE))
                    .build();
            bleWrite(bleDevice, packetHeader.getCmd(), packetHeader.getSeq(), authResponse
                    .toByteArray());

        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
    }

    /**
     * APP发送数据到蓝牙设备
     */
    public void sendData(BleDevice bleDevice, byte[] sendBody) {
        BleProto.RecvControlData recvControlData = BleProto.RecvControlData.newBuilder()
                .setControlData(ByteString.copyFrom(sendBody))
                .build();
        bleWrite(bleDevice, BleProto.EmCmdId.ECI_push_controlData_VALUE, recvControlData
                .toByteArray());
    }

    /**
     * APP发送查询数据到蓝牙设备
     */
    public void queryData(BleDevice bleDevice, byte[] queryBody) {
        BleProto.RecvQueryData recvQueryData = BleProto.RecvQueryData.newBuilder()
                .setQueryData(ByteString.copyFrom(queryBody))
                .build();
        bleWrite(bleDevice, BleProto.EmCmdId.ECI_push_queryData_VALUE, recvQueryData
                .toByteArray());
    }


    private void bleWrite(BleDevice bleDevice, int cmd, byte[] bytes) {
        bleWrite(bleDevice, cmd, 0, bytes);
    }

    /**
     * 数据发送方法,封装包头
     */
    private void bleWrite(BleDevice bleDevice, int cmd, int seq, byte[] reqBody) {
        BleManager.getInstance().write(bleDevice, SERVICE_UUID, WRITE_CHARACTERISTICS_UUID,
                PacketHeader.packHeader(cmd, seq, reqBody), new BleWriteCallback() {
                    @Override
                    public void onWriteSuccess(int current, int total, byte[] justWrite) {
                    }

                    @Override
                    public void onWriteFailure(BleException exception) {

                    }
                });
    }

}
