package com.jcraft.jsch;
/* loaded from: classes.dex */
class RequestShell extends Request {
    @Override // com.jcraft.jsch.Request
    public void request(Session session, Channel channel) throws Exception {
        super.request(session, channel);
        Buffer buf = new Buffer();
        Packet packet = new Packet(buf);
        packet.reset();
        buf.putByte((byte) 98);
        buf.putInt(channel.getRecipient());
        buf.putString(Util.str2byte("shell"));
        buf.putByte(waitForReply() ? (byte) 1 : (byte) 0);
        write(packet);
    }
}
