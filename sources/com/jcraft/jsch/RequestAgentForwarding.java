package com.jcraft.jsch;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class RequestAgentForwarding extends Request {
    @Override // com.jcraft.jsch.Request
    public void request(Session session, Channel channel) throws Exception {
        super.request(session, channel);
        setReply(false);
        Buffer buf = new Buffer();
        Packet packet = new Packet(buf);
        packet.reset();
        buf.putByte((byte) 98);
        buf.putInt(channel.getRecipient());
        buf.putString(Util.str2byte("auth-agent-req@openssh.com"));
        buf.putByte(waitForReply() ? (byte) 1 : (byte) 0);
        write(packet);
        session.agent_forwarding = true;
    }
}
