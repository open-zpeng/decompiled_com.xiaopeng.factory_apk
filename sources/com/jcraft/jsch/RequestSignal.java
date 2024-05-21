package com.jcraft.jsch;
/* loaded from: classes.dex */
class RequestSignal extends Request {
    private String signal = "KILL";

    public void setSignal(String foo) {
        this.signal = foo;
    }

    @Override // com.jcraft.jsch.Request
    public void request(Session session, Channel channel) throws Exception {
        super.request(session, channel);
        Buffer buf = new Buffer();
        Packet packet = new Packet(buf);
        packet.reset();
        buf.putByte((byte) 98);
        buf.putInt(channel.getRecipient());
        buf.putString(Util.str2byte("signal"));
        buf.putByte(waitForReply() ? (byte) 1 : (byte) 0);
        buf.putString(Util.str2byte(this.signal));
        write(packet);
    }
}
