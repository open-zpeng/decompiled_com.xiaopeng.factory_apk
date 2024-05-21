package com.jcraft.jsch;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Hashtable;
/* loaded from: classes.dex */
public class ChannelSubsystem extends ChannelSession {
    boolean xforwading = false;
    boolean pty = false;
    boolean want_reply = true;
    String subsystem = "";

    @Override // com.jcraft.jsch.ChannelSession, com.jcraft.jsch.Channel, java.lang.Runnable
    public /* bridge */ /* synthetic */ void run() {
        super.run();
    }

    @Override // com.jcraft.jsch.ChannelSession
    public /* bridge */ /* synthetic */ void setAgentForwarding(boolean x0) {
        super.setAgentForwarding(x0);
    }

    @Override // com.jcraft.jsch.ChannelSession
    public /* bridge */ /* synthetic */ void setEnv(String x0, String x1) {
        super.setEnv(x0, x1);
    }

    @Override // com.jcraft.jsch.ChannelSession
    public /* bridge */ /* synthetic */ void setEnv(Hashtable x0) {
        super.setEnv(x0);
    }

    @Override // com.jcraft.jsch.ChannelSession
    public /* bridge */ /* synthetic */ void setEnv(byte[] x0, byte[] x1) {
        super.setEnv(x0, x1);
    }

    @Override // com.jcraft.jsch.ChannelSession
    public /* bridge */ /* synthetic */ void setPtySize(int x0, int x1, int x2, int x3) {
        super.setPtySize(x0, x1, x2, x3);
    }

    @Override // com.jcraft.jsch.ChannelSession
    public /* bridge */ /* synthetic */ void setPtyType(String x0) {
        super.setPtyType(x0);
    }

    @Override // com.jcraft.jsch.ChannelSession
    public /* bridge */ /* synthetic */ void setPtyType(String x0, int x1, int x2, int x3, int x4) {
        super.setPtyType(x0, x1, x2, x3, x4);
    }

    @Override // com.jcraft.jsch.ChannelSession
    public /* bridge */ /* synthetic */ void setTerminalMode(byte[] x0) {
        super.setTerminalMode(x0);
    }

    @Override // com.jcraft.jsch.ChannelSession, com.jcraft.jsch.Channel
    public void setXForwarding(boolean foo) {
        this.xforwading = foo;
    }

    @Override // com.jcraft.jsch.ChannelSession
    public void setPty(boolean foo) {
        this.pty = foo;
    }

    public void setWantReply(boolean foo) {
        this.want_reply = foo;
    }

    public void setSubsystem(String foo) {
        this.subsystem = foo;
    }

    @Override // com.jcraft.jsch.Channel
    public void start() throws JSchException {
        Session _session = getSession();
        try {
            if (this.xforwading) {
                Request request = new RequestX11();
                request.request(_session, this);
            }
            if (this.pty) {
                Request request2 = new RequestPtyReq();
                request2.request(_session, this);
            }
            Request request3 = new RequestSubsystem();
            ((RequestSubsystem) request3).request(_session, this, this.subsystem, this.want_reply);
            if (this.f211io.in != null) {
                this.thread = new Thread(this);
                Thread thread = this.thread;
                thread.setName("Subsystem for " + _session.host);
                if (_session.daemon_thread) {
                    this.thread.setDaemon(_session.daemon_thread);
                }
                this.thread.start();
            }
        } catch (Exception e) {
            if (e instanceof JSchException) {
                throw ((JSchException) e);
            }
            if (e instanceof Throwable) {
                throw new JSchException("ChannelSubsystem", e);
            }
            throw new JSchException("ChannelSubsystem");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.jcraft.jsch.Channel
    public void init() throws JSchException {
        this.f211io.setInputStream(getSession().in);
        this.f211io.setOutputStream(getSession().out);
    }

    public void setErrStream(OutputStream out) {
        setExtOutputStream(out);
    }

    public InputStream getErrStream() throws IOException {
        return getExtInputStream();
    }
}
