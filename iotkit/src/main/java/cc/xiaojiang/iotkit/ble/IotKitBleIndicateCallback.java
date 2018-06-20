package cc.xiaojiang.iotkit.ble;

import com.clj.fastble.callback.BleIndicateCallback;
import com.orhanobut.logger.Logger;

import java.util.Arrays;

public abstract class IotKitBleIndicateCallback extends BleIndicateCallback {

    @Override
    public void onCharacteristicChanged(byte[] data) {
        // TODO: 2018/6/8 解析，子线程
        PacketHeader packetHeader = PacketHeader.unpackHeader(data);
        if (packetHeader.getLen() != data.length) {
            Logger.e("packet length error!");
            return;
        }
        byte[] body = Arrays.copyOfRange(data, PacketHeader.HEADER_LENGTH, packetHeader.getLen());
        onCharacteristicChanged(packetHeader, body);
    }

    abstract void onCharacteristicChanged(PacketHeader packetHeader, byte[] bytes);
}
