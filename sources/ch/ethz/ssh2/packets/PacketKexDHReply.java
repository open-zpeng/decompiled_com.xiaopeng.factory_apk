package ch.ethz.ssh2.packets;

import java.io.IOException;
import java.math.BigInteger;
/* loaded from: classes.dex */
public class PacketKexDHReply {
    BigInteger f;
    byte[] hostKey;
    byte[] payload;
    byte[] signature;

    public PacketKexDHReply(byte[] payload, int off, int len) throws IOException {
        this.payload = new byte[len];
        System.arraycopy(payload, off, this.payload, 0, len);
        TypesReader tr = new TypesReader(payload, off, len);
        int packet_type = tr.readByte();
        if (packet_type != 31) {
            StringBuffer stringBuffer = new StringBuffer("This is not a SSH_MSG_KEXDH_REPLY! (");
            stringBuffer.append(packet_type);
            stringBuffer.append(")");
            throw new IOException(stringBuffer.toString());
        }
        this.hostKey = tr.readByteString();
        this.f = tr.readMPINT();
        this.signature = tr.readByteString();
        if (tr.remain() != 0) {
            throw new IOException("PADDING IN SSH_MSG_KEXDH_REPLY!");
        }
    }

    public BigInteger getF() {
        return this.f;
    }

    public byte[] getHostKey() {
        return this.hostKey;
    }

    public byte[] getSignature() {
        return this.signature;
    }
}
