package com.xiaopeng.commonfunc.utils.tftp;

import java.net.InetAddress;
/* loaded from: classes.dex */
public class TFTPOptionsReadRequestPacket extends TFTPOptionsRequestPacket {
    public TFTPOptionsReadRequestPacket(InetAddress destination, int port, int segmentSize, String filename, long fileSize, int mode) {
        super(destination, port, 1, segmentSize, filename, fileSize, mode);
    }

    @Override // com.xiaopeng.commonfunc.utils.tftp.TFTPPacket
    public String toString() {
        return super.toString() + " RRQ " + getFilename() + " " + TFTP.getModeName(getMode());
    }
}
