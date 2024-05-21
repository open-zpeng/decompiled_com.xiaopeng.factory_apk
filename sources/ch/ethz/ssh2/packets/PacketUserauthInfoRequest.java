package ch.ethz.ssh2.packets;

import java.io.IOException;
/* loaded from: classes.dex */
public class PacketUserauthInfoRequest {
    boolean[] echo;
    String instruction;
    String languageTag;
    String name;
    int numPrompts;
    byte[] payload;
    String[] prompt;

    public PacketUserauthInfoRequest(byte[] payload, int off, int len) throws IOException {
        this.payload = new byte[len];
        System.arraycopy(payload, off, this.payload, 0, len);
        TypesReader tr = new TypesReader(payload, off, len);
        int packet_type = tr.readByte();
        if (packet_type != 60) {
            StringBuffer stringBuffer = new StringBuffer("This is not a SSH_MSG_USERAUTH_INFO_REQUEST! (");
            stringBuffer.append(packet_type);
            stringBuffer.append(")");
            throw new IOException(stringBuffer.toString());
        }
        this.name = tr.readString();
        this.instruction = tr.readString();
        this.languageTag = tr.readString();
        this.numPrompts = tr.readUINT32();
        int i = this.numPrompts;
        this.prompt = new String[i];
        this.echo = new boolean[i];
        for (int i2 = 0; i2 < this.numPrompts; i2++) {
            this.prompt[i2] = tr.readString();
            this.echo[i2] = tr.readBoolean();
        }
        int i3 = tr.remain();
        if (i3 != 0) {
            throw new IOException("Padding in SSH_MSG_USERAUTH_INFO_REQUEST packet!");
        }
    }

    public boolean[] getEcho() {
        return this.echo;
    }

    public String getInstruction() {
        return this.instruction;
    }

    public String getLanguageTag() {
        return this.languageTag;
    }

    public String getName() {
        return this.name;
    }

    public int getNumPrompts() {
        return this.numPrompts;
    }

    public String[] getPrompt() {
        return this.prompt;
    }
}
