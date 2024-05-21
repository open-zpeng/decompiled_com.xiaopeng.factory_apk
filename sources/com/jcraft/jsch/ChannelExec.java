package com.jcraft.jsch;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Hashtable;
/* loaded from: classes.dex */
public class ChannelExec extends ChannelSession {
    byte[] command = new byte[0];

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
    public /* bridge */ /* synthetic */ void setPty(boolean x0) {
        super.setPty(x0);
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
    public /* bridge */ /* synthetic */ void setXForwarding(boolean x0) {
        super.setXForwarding(x0);
    }

    @Override // com.jcraft.jsch.Channel
    public void start() throws JSchException {
        Session _session = getSession();
        try {
            sendRequests();
            Request request = new RequestExec(this.command);
            request.request(_session, this);
            if (this.f211io.in != null) {
                this.thread = new Thread(this);
                Thread thread = this.thread;
                thread.setName("Exec thread " + _session.getHost());
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
                throw new JSchException("ChannelExec", e);
            }
            throw new JSchException("ChannelExec");
        }
    }

    public void setCommand(String command) {
        this.command = Util.str2byte(command);
    }

    public void setCommand(byte[] command) {
        this.command = command;
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

    public void setErrStream(OutputStream out, boolean dontclose) {
        setExtOutputStream(out, dontclose);
    }

    public InputStream getErrStream() throws IOException {
        return getExtInputStream();
    }
}
