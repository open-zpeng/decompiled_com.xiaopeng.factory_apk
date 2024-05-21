package ch.ethz.ssh2.packets;

import java.io.IOException;
/* loaded from: classes.dex */
public class PacketIgnore {
    byte[] body;
    byte[] payload;

    public void setBody(byte[] body) {
        this.body = body;
        this.payload = null;
    }

    public PacketIgnore(byte[] payload, int off, int len) throws IOException {
        this.payload = new byte[len];
        System.arraycopy(payload, off, this.payload, 0, len);
        TypesReader tr = new TypesReader(payload, off, len);
        int packet_type = tr.readByte();
        if (packet_type != 2) {
            StringBuffer stringBuffer = new StringBuffer("This is not a SSH_MSG_IGNORE packet! (");
            stringBuffer.append(packet_type);
            stringBuffer.append(")");
            throw new IOException(stringBuffer.toString());
        }
    }

    public byte[] getPayload() {
        if (this.payload == null) {
            TypesWriter tw = new TypesWriter();
            tw.writeByte(2);
            byte[] bArr = this.body;
            tw.writeString(bArr, 0, bArr.length);
            this.payload = tw.getBytes();
        }
        return this.payload;
    }
}
