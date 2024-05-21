package ch.ethz.ssh2.packets;

import ch.ethz.ssh2.DHGexParameters;
/* loaded from: classes.dex */
public class PacketKexDhGexRequestOld {
    int n;
    byte[] payload;

    public PacketKexDhGexRequestOld(DHGexParameters para) {
        this.n = para.getPref_group_len();
    }

    public byte[] getPayload() {
        if (this.payload == null) {
            TypesWriter tw = new TypesWriter();
            tw.writeByte(30);
            tw.writeUINT32(this.n);
            this.payload = tw.getBytes();
        }
        return this.payload;
    }
}
