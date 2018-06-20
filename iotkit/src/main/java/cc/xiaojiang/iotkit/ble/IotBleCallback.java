package cc.xiaojiang.iotkit.ble;

import android.bluetooth.BluetoothGatt;

import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;

public interface IotBleCallback {
    void onStartConnect();

    void onConnectFail(BleDevice bleDevice, BleException exception);

    void onConnectSuccess(BleDevice bleDevice, BluetoothGatt gatt, int status);

    void onDisConnected(boolean isActiveDisConnected, BleDevice device, BluetoothGatt gatt, int
            status);

    void onIndicateSuccess();

    void onIndicateFailure(BleException exception);

    void onDataReceived(byte[] data);
}
