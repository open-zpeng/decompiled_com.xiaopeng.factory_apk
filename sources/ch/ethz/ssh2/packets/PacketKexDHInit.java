package ch.ethz.ssh2.packets;

import java.math.BigInteger;
/* loaded from: classes.dex */
public class PacketKexDHInit {
    BigInteger e;
    byte[] payload;

    public PacketKexDHInit(BigInteger e) {
        this.e = e;
    }

    public byte[] getPayload() {
        if (this.payload == null) {
            TypesWriter tw = new TypesWriter();
            tw.writeByte(30);
            tw.writeMPInt(this.e);
            this.payload = tw.getBytes();
        }
        return this.payload;
    }
}
