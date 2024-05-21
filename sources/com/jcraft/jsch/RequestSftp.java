package com.jcraft.jsch;
/* loaded from: classes.dex */
public class RequestSftp extends Request {
    /* JADX INFO: Access modifiers changed from: package-private */
    public RequestSftp() {
        setReply(true);
    }

    @Override // com.jcraft.jsch.Request
    public void request(Session session, Channel channel) throws Exception {
        super.request(session, channel);
        Buffer buf = new Buffer();
        Packet packet = new Packet(buf);
        packet.reset();
        buf.putByte((byte) 98);
        buf.putInt(channel.getRecipient());
        buf.putString(Util.str2byte("subsystem"));
        buf.putByte(waitForReply() ? (byte) 1 : (byte) 0);
        buf.putString(Util.str2byte("sftp"));
        write(packet);
    }
}
