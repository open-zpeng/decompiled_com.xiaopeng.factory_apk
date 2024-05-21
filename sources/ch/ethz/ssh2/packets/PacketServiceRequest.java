package ch.ethz.ssh2.packets;

import java.io.IOException;
/* loaded from: classes.dex */
public class PacketServiceRequest {
    byte[] payload;
    String serviceName;

    public PacketServiceRequest(String serviceName) {
        this.serviceName = serviceName;
    }

    public PacketServiceRequest(byte[] payload, int off, int len) throws IOException {
        this.payload = new byte[len];
        System.arraycopy(payload, off, this.payload, 0, len);
        TypesReader tr = new TypesReader(payload, off, len);
        int packet_type = tr.readByte();
        if (packet_type != 5) {
            StringBuffer stringBuffer = new StringBuffer("This is not a SSH_MSG_SERVICE_REQUEST! (");
            stringBuffer.append(packet_type);
            stringBuffer.append(")");
            throw new IOException(stringBuffer.toString());
        }
        this.serviceName = tr.readString();
        if (tr.remain() != 0) {
            throw new IOException("Padding in SSH_MSG_SERVICE_REQUEST packet!");
        }
    }

    public byte[] getPayload() {
        if (this.payload == null) {
            TypesWriter tw = new TypesWriter();
            tw.writeByte(5);
            tw.writeString(this.serviceName);
            this.payload = tw.getBytes();
        }
        return this.payload;
    }
}
