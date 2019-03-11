package com.xjiangiot.lib.common.utils;

import java.util.Arrays;

/**
 * Created by facexxyz on 2018/12/19
 */
public class ByteUtils {
    private static final String TAG = "ByteUtils";

    //将data字节型数据转换为0~255 (0xFF 即BYTE)。
    public static int getUnsignedByte(byte data) {
        return data & 0x0FF;
    }

    public static int getUnsignedShort(short data) {
        return data & 0xFFFF;
    }

    public static long getUnsignedInt(int data) {
        return data & 0xFFFFFF;
    }

    public static int[] bytesToInts(byte[] data) {
        int[] ints = new int[data.length];
        for (int i = 0; i < data.length; i++) {
            ints[i] = ByteUtils.getUnsignedByte(data[i]);
        }
        return ints;
    }

    public static byte[] longToBytes(long a) {
        int size = 8;
        byte[] bytes = new byte[size];
        for (int i = 0; i < size; i++) {
            bytes[size - 1 - i] = (byte) ((a >> 8 * i) & 0xFF);
        }
        return bytes;
    }

    public static byte[] intToBytes(int a) {
        int size = 4;
        byte[] bytes = new byte[size];
        for (int i = 0; i < size; i++) {
            bytes[size - 1 - i] = (byte) ((a >> 8 * i) & 0xFF);
        }
        return bytes;
    }

    public static byte[] shortToBytes(short a) {
        int size = 2;
        byte[] bytes = new byte[size];
        for (int i = 0; i < size; i++) {
            bytes[size - 1 - i] = (byte) ((a >> 8 * i) & 0xFF);
        }
        return bytes;
    }


    public static byte[] byteToBytes(byte a) {
        return new byte[]{a};
    }


    public static String bytesToHexString(byte[] bytes) {
        String divider = " ";
        StringBuilder builder = new StringBuilder();
        for (byte aByte : bytes) {
            builder.append(divider);
            builder.append(String.format("%02x", ByteUtils.getUnsignedByte(aByte)).toUpperCase());
        }
        return builder.toString().replaceFirst(divider, "");
    }

    public static String byteToHexString(byte b) {
        return String.format("0x%02x", ByteUtils.getUnsignedByte(b)).toUpperCase();
    }
    public static byte[] concat(byte[] first, byte[] second) {
        byte[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

}
