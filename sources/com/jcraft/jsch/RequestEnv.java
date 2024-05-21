package com.jcraft.jsch;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class RequestEnv extends Request {
    byte[] name = new byte[0];
    byte[] value = new byte[0];

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setEnv(byte[] name, byte[] value) {
        this.name = name;
        this.value = value;
    }

    @Override // com.jcraft.jsch.Request
    public void request(Session session, Channel channel) throws Exception {
        super.request(session, channel);
        Buffer buf = new Buffer();
        Packet packet = new Packet(buf);
        packet.reset();
        buf.putByte((byte) 98);
        buf.putInt(channel.getRecipient());
        buf.putString(Util.str2byte("env"));
        buf.putByte(waitForReply() ? (byte) 1 : (byte) 0);
        buf.putString(this.name);
        buf.putString(this.value);
        write(packet);
    }
}
