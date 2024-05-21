package com.jcraft.jsch;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class RequestX11 extends Request {
    public void setCookie(String cookie) {
        ChannelX11.cookie = Util.str2byte(cookie);
    }

    @Override // com.jcraft.jsch.Request
    public void request(Session session, Channel channel) throws Exception {
        super.request(session, channel);
        Buffer buf = new Buffer();
        Packet packet = new Packet(buf);
        packet.reset();
        buf.putByte((byte) 98);
        buf.putInt(channel.getRecipient());
        buf.putString(Util.str2byte("x11-req"));
        buf.putByte(waitForReply() ? (byte) 1 : (byte) 0);
        buf.putByte((byte) 0);
        buf.putString(Util.str2byte("MIT-MAGIC-COOKIE-1"));
        buf.putString(ChannelX11.getFakedCookie(session));
        buf.putInt(0);
        write(packet);
        session.x11_forwarding = true;
    }
}
