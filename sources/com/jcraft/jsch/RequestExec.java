package com.jcraft.jsch;
/* loaded from: classes.dex */
class RequestExec extends Request {
    private byte[] command;

    /* JADX INFO: Access modifiers changed from: package-private */
    public RequestExec(byte[] command) {
        this.command = new byte[0];
        this.command = command;
    }

    @Override // com.jcraft.jsch.Request
    public void request(Session session, Channel channel) throws Exception {
        super.request(session, channel);
        Buffer buf = new Buffer();
        Packet packet = new Packet(buf);
        packet.reset();
        buf.putByte((byte) 98);
        buf.putInt(channel.getRecipient());
        buf.putString(Util.str2byte("exec"));
        buf.putByte(waitForReply() ? (byte) 1 : (byte) 0);
        buf.checkFreeSize(this.command.length + 4);
        buf.putString(this.command);
        write(packet);
    }
}
