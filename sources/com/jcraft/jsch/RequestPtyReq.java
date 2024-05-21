package com.jcraft.jsch;

import org.apache.commons.net.nntp.NNTPReply;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class RequestPtyReq extends Request {
    private String ttype = "vt100";
    private int tcol = 80;
    private int trow = 24;
    private int twp = 640;
    private int thp = NNTPReply.AUTHENTICATION_REQUIRED;
    private byte[] terminal_mode = Util.empty;

    void setCode(String cookie) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setTType(String ttype) {
        this.ttype = ttype;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setTerminalMode(byte[] terminal_mode) {
        this.terminal_mode = terminal_mode;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setTSize(int tcol, int trow, int twp, int thp) {
        this.tcol = tcol;
        this.trow = trow;
        this.twp = twp;
        this.thp = thp;
    }

    @Override // com.jcraft.jsch.Request
    public void request(Session session, Channel channel) throws Exception {
        super.request(session, channel);
        Buffer buf = new Buffer();
        Packet packet = new Packet(buf);
        packet.reset();
        buf.putByte((byte) 98);
        buf.putInt(channel.getRecipient());
        buf.putString(Util.str2byte("pty-req"));
        buf.putByte(waitForReply() ? (byte) 1 : (byte) 0);
        buf.putString(Util.str2byte(this.ttype));
        buf.putInt(this.tcol);
        buf.putInt(this.trow);
        buf.putInt(this.twp);
        buf.putInt(this.thp);
        buf.putString(this.terminal_mode);
        write(packet);
    }
}
