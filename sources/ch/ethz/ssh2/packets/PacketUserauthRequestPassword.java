package ch.ethz.ssh2.packets;

import com.xiaopeng.commonfunc.Constant;
import java.io.IOException;
/* loaded from: classes.dex */
public class PacketUserauthRequestPassword {
    String password;
    byte[] payload;
    String serviceName;
    String userName;

    public PacketUserauthRequestPassword(String serviceName, String user, String pass) {
        this.serviceName = serviceName;
        this.userName = user;
        this.password = pass;
    }

    public PacketUserauthRequestPassword(byte[] payload, int off, int len) throws IOException {
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
        this.userName = tr.readString();
        this.serviceName = tr.readString();
        String method = tr.readString();
        if (!method.equals(Constant.HTTP_KEY_PASSWORD)) {
            throw new IOException("This is not a SSH_MSG_USERAUTH_REQUEST with type password!");
        }
        if (tr.remain() != 0) {
            throw new IOException("Padding in SSH_MSG_USERAUTH_REQUEST packet!");
        }
    }

    public byte[] getPayload() {
        if (this.payload == null) {
            TypesWriter tw = new TypesWriter();
            tw.writeByte(50);
            tw.writeString(this.userName);
            tw.writeString(this.serviceName);
            tw.writeString(Constant.HTTP_KEY_PASSWORD);
            tw.writeBoolean(false);
            tw.writeString(this.password);
            this.payload = tw.getBytes();
        }
        return this.payload;
    }
}
