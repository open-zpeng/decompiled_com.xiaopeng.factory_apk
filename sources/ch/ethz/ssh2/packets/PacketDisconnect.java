package ch.ethz.ssh2.packets;

import java.io.IOException;
/* loaded from: classes.dex */
public class PacketDisconnect {
    String desc;
    String lang;
    byte[] payload;
    int reason;

    public PacketDisconnect(byte[] payload, int off, int len) throws IOException {
        this.payload = new byte[len];
        System.arraycopy(payload, off, this.payload, 0, len);
        TypesReader tr = new TypesReader(payload, off, len);
        int packet_type = tr.readByte();
        if (packet_type != 1) {
            StringBuffer stringBuffer = new StringBuffer("This is not a Disconnect Packet! (");
            stringBuffer.append(packet_type);
            stringBuffer.append(")");
            throw new IOException(stringBuffer.toString());
        }
        this.reason = tr.readUINT32();
        this.desc = tr.readString();
        this.lang = tr.readString();
    }

    public PacketDisconnect(int reason, String desc, String lang) {
        this.reason = reason;
        this.desc = desc;
        this.lang = lang;
    }

    public byte[] getPayload() {
        if (this.payload == null) {
            TypesWriter tw = new TypesWriter();
            tw.writeByte(1);
            tw.writeUINT32(this.reason);
            tw.writeString(this.desc);
            tw.writeString(this.lang);
            this.payload = tw.getBytes();
        }
        return this.payload;
    }
}
