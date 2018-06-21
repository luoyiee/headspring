package cc.xiaojiang.iotkit.wifi;

import com.orhanobut.logger.Logger;

import java.nio.ByteBuffer;
import java.util.Arrays;

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
        Logger.d("PackData: " + Arrays.toString(bytes));
        return bytes;
    }

    public static String unpackData(byte[] received) {
        Logger.d( "unpackData: " + Arrays.toString(received));
        ByteBuffer byteBuffer = ByteBuffer.wrap(received);
        int head = byteBuffer.getShort() & 0xFFFF;
        if (head != 0xFFFF) {
            Logger.e( "data error!");
            return null;
        }
        int length = byteBuffer.getShort() & 0xFFFF;
        int flag = byteBuffer.get() & 0xFF;
        int sn = byteBuffer.getShort() & 0xFFFF;
        int reserved = byteBuffer.get() & 0xFF;
        Logger.d( "head: " + head);
        Logger.d( "length: " + length);
        Logger.d( "flag: " + flag);
        Logger.d( "sn: " + sn);
        Logger.d( "reserved: " + reserved);
        if (received.length != length) {
            Logger.e("error packet length!");
            return null;
        }
        byte[] data = Arrays.copyOfRange(received, byteBuffer.position(), received.length);
        return new String(data);
    }
}
