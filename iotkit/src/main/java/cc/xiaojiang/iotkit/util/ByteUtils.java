package cc.xiaojiang.iotkit.util;

public class ByteUtils {
    public static int unsignedByte(byte b) {
        return b & 0xFF;
    }

    public static int unsignedShort(Short s) {
        return s & 0xFFFF;
    }
}
