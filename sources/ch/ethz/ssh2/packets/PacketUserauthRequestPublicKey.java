package ch.ethz.ssh2.packets;

import java.io.IOException;
/* loaded from: classes.dex */
public class PacketUserauthRequestPublicKey {
    String password;
    byte[] payload;
    byte[] pk;
    String pkAlgoName;
    String serviceName;
    byte[] sig;
    String userName;

    public PacketUserauthRequestPublicKey(String serviceName, String user, String pkAlgorithmName, byte[] pk, byte[] sig) {
        this.serviceName = serviceName;
        this.userName = user;
        this.pkAlgoName = pkAlgorithmName;
        this.pk = pk;
        this.sig = sig;
    }

    public PacketUserauthRequestPublicKey(byte[] payload, int off, int len) throws IOException {
        this.payload = new byte[len];
        System.arraycopy(payload, off, this.payload, 0, len);
        TypesReader tr = new TypesReader(payload, off, len);
        int packet_type = tr.readByte();
        if (packet_type != 50) {
            StringBuffer stringBuffer = new StringBuffer("This is not a SSH_MSG_USERAUTH_REQUEST! (");
            stringBuffer.append(packet_type);
            stringBuffer.append(")");
            throw new IOException(stringBuffer.toString());
        }
        throw new IOException("Not implemented!");
    }

    public byte[] getPayload() {
        if (this.payload == null) {
            TypesWriter tw = new TypesWriter();
            tw.writeByte(50);
            tw.writeString(this.userName);
            tw.writeString(this.serviceName);
            tw.writeString("publickey");
            tw.writeBoolean(true);
            tw.writeString(this.pkAlgoName);
            byte[] bArr = this.pk;
            tw.writeString(bArr, 0, bArr.length);
            byte[] bArr2 = this.sig;
            tw.writeString(bArr2, 0, bArr2.length);
            this.payload = tw.getBytes();
        }
        return this.payload;
    }
}
