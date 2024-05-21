package ch.ethz.ssh2.packets;

import java.io.IOException;
import java.math.BigInteger;
/* loaded from: classes.dex */
public class PacketKexDhGexGroup {
    BigInteger g;
    BigInteger p;
    byte[] payload;

    public PacketKexDhGexGroup(byte[] payload, int off, int len) throws IOException {
        this.payload = new byte[len];
        System.arraycopy(payload, off, this.payload, 0, len);
        TypesReader tr = new TypesReader(payload, off, len);
        int packet_type = tr.readByte();
        if (packet_type != 31) {
            StringBuffer stringBuffer = new StringBuffer("This is not a SSH_MSG_KEX_DH_GEX_GROUP! (");
            stringBuffer.append(packet_type);
            stringBuffer.append(")");
            throw new IllegalArgumentException(stringBuffer.toString());
        }
        this.p = tr.readMPINT();
        this.g = tr.readMPINT();
        if (tr.remain() != 0) {
            throw new IOException("PADDING IN SSH_MSG_KEX_DH_GEX_GROUP!");
        }
    }

    public BigInteger getG() {
        return this.g;
    }

    public BigInteger getP() {
        return this.p;
    }
}
