package com.xiaopeng.commonfunc.utils.tftp;

import java.net.DatagramPacket;
import java.net.InetAddress;
/* loaded from: classes.dex */
public final class TFTPReadRequestPacket extends TFTPRequestPacket {
    public TFTPReadRequestPacket(InetAddress destination, int port, int segmentSize, String filename, int mode) {
        super(destination, port, 1, segmentSize, filename, mode);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public TFTPReadRequestPacket(int segmentSize, DatagramPacket datagram) throws TFTPPacketException {
        super(1, segmentSize, datagram);
    }

    @Override // com.xiaopeng.commonfunc.utils.tftp.TFTPPacket
    public String toString() {
        return super.toString() + " RRQ " + getFilename() + " " + TFTP.getModeName(getMode());
    }
}
