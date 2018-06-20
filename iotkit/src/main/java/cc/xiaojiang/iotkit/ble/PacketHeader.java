package cc.xiaojiang.iotkit.ble;

import java.nio.ByteBuffer;

import cc.xiaojiang.iotkit.util.ByteUtils;

public class PacketHeader {
    public static final int HEADER_LENGTH = 8;
    private int magic;
    private int ver;
    private int len;
    private int cmd;
    private int seq;

    public int getMagic() {
        return magic;
    }

    public void setMagic(int magic) {
        this.magic = magic;
    }

    public int getVer() {
        return ver;
    }

    public void setVer(int ver) {
        this.ver = ver;
    }

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }

    public int getCmd() {
        return cmd;
    }

    public void setCmd(int cmd) {
        this.cmd = cmd;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public static byte[] packHeader(int cmd, int seq, byte[] reqBody) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(8 + reqBody.length);
        byteBuffer.put((byte) 0xFA);
        byteBuffer.put((byte) 0x01);
        byteBuffer.putShort((short) (HEADER_LENGTH + reqBody.length));
        byteBuffer.putShort((short) cmd);
        byteBuffer.putShort((short) seq);
        byteBuffer.put(reqBody);
        return byteBuffer.array();

    }

    public static PacketHeader unpackHeader(byte[] packet) {
        PacketHeader header = new PacketHeader();
        ByteBuffer byteBuffer = ByteBuffer.wrap(packet, 0, HEADER_LENGTH);
        header.setMagic(ByteUtils.unsignedByte(byteBuffer.get()));
        header.setVer(ByteUtils.unsignedByte(byteBuffer.get()));
        header.setLen(ByteUtils.unsignedShort(byteBuffer.getShort()));
        header.setCmd(ByteUtils.unsignedShort(byteBuffer.getShort()));
        header.setSeq(ByteUtils.unsignedShort(byteBuffer.getShort()));
        return header;
    }
}
