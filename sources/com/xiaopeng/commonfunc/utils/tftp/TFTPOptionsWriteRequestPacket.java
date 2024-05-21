package com.xiaopeng.commonfunc.utils.tftp;

import java.net.DatagramPacket;
import java.net.InetAddress;
/* loaded from: classes.dex */
public final class TFTPOptionsWriteRequestPacket extends TFTPOptionsRequestPacket {
    public TFTPOptionsWriteRequestPacket(InetAddress destination, int port, int segmentSize, String filename, long fileSize, int mode) {
        super(destination, port, 2, segmentSize, filename, fileSize, mode);
    }

    TFTPOptionsWriteRequestPacket(int segmentSize, DatagramPacket datagram) throws TFTPPacketException {
        super(2, segmentSize, datagram);
    }

    @Override // com.xiaopeng.commonfunc.utils.tftp.TFTPPacket
    public String toString() {
        return super.toString() + " WRQ " + getFilename() + " " + TFTP.getModeName(getMode());
    }
}
