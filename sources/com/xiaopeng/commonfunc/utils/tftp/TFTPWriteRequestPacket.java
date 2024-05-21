package com.xiaopeng.commonfunc.utils.tftp;

import java.net.DatagramPacket;
import java.net.InetAddress;
/* loaded from: classes.dex */
public final class TFTPWriteRequestPacket extends TFTPRequestPacket {
    public TFTPWriteRequestPacket(InetAddress destination, int port, int segmentSize, String filename, int mode) {
        super(destination, port, 2, segmentSize, filename, mode);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public TFTPWriteRequestPacket(int segmentSize, DatagramPacket datagram) throws TFTPPacketException {
        super(2, segmentSize, datagram);
    }

    @Override // com.xiaopeng.commonfunc.utils.tftp.TFTPPacket
    public String toString() {
        return super.toString() + " WRQ " + getFilename() + " " + TFTP.getModeName(getMode());
    }
}
