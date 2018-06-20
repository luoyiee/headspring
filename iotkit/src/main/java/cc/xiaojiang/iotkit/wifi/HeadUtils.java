package cc.xiaojiang.iotkit.wifi;

import java.nio.ByteBuffer;
import java.util.Arrays;

import cc.xiaojiang.iotkit.util.LogUtil;

public class HeadUtils {
    private static final String TAG = "HeadUtils";
    private static int sn = 0;

    // TODO: 18-4-9 封装

    public static byte[] PackData(int flag, byte[] data) {
        int length = 8 + data.length;
        ByteBuffer byteBuffer = ByteBuffer.allocate(length);
        byteBuffer.putShort((short) 0xFFFF);
        byteBuffer.putShort((short) length);
        byteBuffer.put((byte) flag);
        byteBuffer.putShort((short) ++sn);
        byteBuffer.put((byte) 0);
        byteBuffer.put(data);
        byte[] bytes = byteBuffer.array();
        LogUtil.d(TAG, "PackData: " + Arrays.toString(bytes));
        return bytes;
    }

    public static String unpackData(byte[] received) {
        LogUtil.d(TAG, "unpackData: " + Arrays.toString(received));
        ByteBuffer byteBuffer = ByteBuffer.wrap(received);
        int head = byteBuffer.getShort() & 0xFFFF;
        if (head != 0xFFFF) {
            LogUtil.e(TAG, "data error!");
            return null;
        }
        int length = byteBuffer.getShort() & 0xFFFF;
        int flag = byteBuffer.get() & 0xFF;
        int sn = byteBuffer.getShort() & 0xFFFF;
        int reserved = byteBuffer.get() & 0xFF;
        LogUtil.d(TAG, "head: " + head);
        LogUtil.d(TAG, "length: " + length);
        LogUtil.d(TAG, "flag: " + flag);
        LogUtil.d(TAG, "sn: " + sn);
        LogUtil.d(TAG, "reserved: " + reserved);
        if (received.length != length) {
            LogUtil.e(TAG, "error packet length!");
            return null;
        }
        byte[] data = Arrays.copyOfRange(received, byteBuffer.position(), received.length);
        return new String(data);
    }
}
