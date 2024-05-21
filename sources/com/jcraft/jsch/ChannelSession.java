package com.jcraft.jsch;

import io.sentry.cache.EnvelopeCache;
import java.util.Enumeration;
import java.util.Hashtable;
import org.apache.commons.net.nntp.NNTPReply;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class ChannelSession extends Channel {
    private static byte[] _session = Util.str2byte(EnvelopeCache.PREFIX_CURRENT_SESSION_FILE);
    protected boolean agent_forwarding = false;
    protected boolean xforwading = false;
    protected Hashtable env = null;
    protected boolean pty = false;
    protected String ttype = "vt100";
    protected int tcol = 80;
    protected int trow = 24;
    protected int twp = 640;
    protected int thp = NNTPReply.AUTHENTICATION_REQUIRED;
    protected byte[] terminal_mode = null;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ChannelSession() {
        this.type = _session;
        this.f211io = new IO();
    }

    public void setAgentForwarding(boolean enable) {
        this.agent_forwarding = enable;
    }

    @Override // com.jcraft.jsch.Channel
    public void setXForwarding(boolean enable) {
        this.xforwading = enable;
    }

    public void setEnv(Hashtable env) {
        synchronized (this) {
            this.env = env;
        }
    }

    public void setEnv(String name, String value) {
        setEnv(Util.str2byte(name), Util.str2byte(value));
    }

    public void setEnv(byte[] name, byte[] value) {
        synchronized (this) {
            getEnv().put(name, value);
        }
    }

    private Hashtable getEnv() {
        if (this.env == null) {
            this.env = new Hashtable();
        }
        return this.env;
    }

    public void setPty(boolean enable) {
        this.pty = enable;
    }

    public void setTerminalMode(byte[] terminal_mode) {
        this.terminal_mode = terminal_mode;
    }

    public void setPtySize(int col, int row, int wp, int hp) {
        setPtyType(this.ttype, col, row, wp, hp);
        if (!this.pty || !isConnected()) {
            return;
        }
        try {
            RequestWindowChange request = new RequestWindowChange();
            request.setSize(col, row, wp, hp);
            request.request(getSession(), this);
        } catch (Exception e) {
        }
    }

    public void setPtyType(String ttype) {
        setPtyType(ttype, 80, 24, 640, NNTPReply.AUTHENTICATION_REQUIRED);
    }

    public void setPtyType(String ttype, int col, int row, int wp, int hp) {
        this.ttype = ttype;
        this.tcol = col;
        this.trow = row;
        this.twp = wp;
        this.thp = hp;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void sendRequests() throws Exception {
        Session _session2 = getSession();
        if (this.agent_forwarding) {
            new RequestAgentForwarding().request(_session2, this);
        }
        if (this.xforwading) {
            new RequestX11().request(_session2, this);
        }
        if (this.pty) {
            Request request = new RequestPtyReq();
            ((RequestPtyReq) request).setTType(this.ttype);
            ((RequestPtyReq) request).setTSize(this.tcol, this.trow, this.twp, this.thp);
            byte[] bArr = this.terminal_mode;
            if (bArr != null) {
                ((RequestPtyReq) request).setTerminalMode(bArr);
            }
            request.request(_session2, this);
        }
        Hashtable hashtable = this.env;
        if (hashtable != null) {
            Enumeration _env = hashtable.keys();
            while (_env.hasMoreElements()) {
                Object name = _env.nextElement();
                Object value = this.env.get(name);
                Request request2 = new RequestEnv();
                ((RequestEnv) request2).setEnv(toByteArray(name), toByteArray(value));
                request2.request(_session2, this);
            }
        }
    }

    private byte[] toByteArray(Object o) {
        if (o instanceof String) {
            return Util.str2byte((String) o);
        }
        return (byte[]) o;
    }

    /* JADX WARN: Code restructure failed: missing block: B:16:0x003a, code lost:
        eof();
     */
    @Override // com.jcraft.jsch.Channel, java.lang.Runnable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void run() {
        /*
            r7 = this;
            com.jcraft.jsch.Buffer r0 = new com.jcraft.jsch.Buffer
            int r1 = r7.rmpsize
            r0.<init>(r1)
            com.jcraft.jsch.Packet r1 = new com.jcraft.jsch.Packet
            r1.<init>(r0)
            r2 = -1
        Ld:
            boolean r3 = r7.isConnected()     // Catch: java.lang.Exception -> L5f
            if (r3 == 0) goto L5e
            java.lang.Thread r3 = r7.thread     // Catch: java.lang.Exception -> L5f
            if (r3 == 0) goto L5e
            com.jcraft.jsch.IO r3 = r7.f211io     // Catch: java.lang.Exception -> L5f
            if (r3 == 0) goto L5e
            com.jcraft.jsch.IO r3 = r7.f211io     // Catch: java.lang.Exception -> L5f
            java.io.InputStream r3 = r3.in     // Catch: java.lang.Exception -> L5f
            if (r3 == 0) goto L5e
            com.jcraft.jsch.IO r3 = r7.f211io     // Catch: java.lang.Exception -> L5f
            java.io.InputStream r3 = r3.in     // Catch: java.lang.Exception -> L5f
            byte[] r4 = r0.buffer     // Catch: java.lang.Exception -> L5f
            byte[] r5 = r0.buffer     // Catch: java.lang.Exception -> L5f
            int r5 = r5.length     // Catch: java.lang.Exception -> L5f
            r6 = 14
            int r5 = r5 - r6
            int r5 = r5 + (-128)
            int r3 = r3.read(r4, r6, r5)     // Catch: java.lang.Exception -> L5f
            r2 = r3
            if (r2 != 0) goto L37
            goto Ld
        L37:
            r3 = -1
            if (r2 != r3) goto L3e
            r7.eof()     // Catch: java.lang.Exception -> L5f
            goto L5e
        L3e:
            boolean r3 = r7.close     // Catch: java.lang.Exception -> L5f
            if (r3 == 0) goto L43
            goto L5e
        L43:
            r1.reset()     // Catch: java.lang.Exception -> L5f
            r3 = 94
            r0.putByte(r3)     // Catch: java.lang.Exception -> L5f
            int r3 = r7.recipient     // Catch: java.lang.Exception -> L5f
            r0.putInt(r3)     // Catch: java.lang.Exception -> L5f
            r0.putInt(r2)     // Catch: java.lang.Exception -> L5f
            r0.skip(r2)     // Catch: java.lang.Exception -> L5f
            com.jcraft.jsch.Session r3 = r7.getSession()     // Catch: java.lang.Exception -> L5f
            r3.write(r1, r7, r2)     // Catch: java.lang.Exception -> L5f
            goto Ld
        L5e:
            goto L60
        L5f:
            r3 = move-exception
        L60:
            java.lang.Thread r3 = r7.thread
            if (r3 == 0) goto L6d
            monitor-enter(r3)
            r3.notifyAll()     // Catch: java.lang.Throwable -> L6a
            monitor-exit(r3)     // Catch: java.lang.Throwable -> L6a
            goto L6d
        L6a:
            r4 = move-exception
            monitor-exit(r3)     // Catch: java.lang.Throwable -> L6a
            throw r4
        L6d:
            r4 = 0
            r7.thread = r4
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.jcraft.jsch.ChannelSession.run():void");
    }
}
