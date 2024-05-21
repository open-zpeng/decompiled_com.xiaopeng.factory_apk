package com.jcraft.jsch;

import java.io.InputStream;
import java.io.OutputStream;
/* loaded from: classes.dex */
public class ChannelDirectTCPIP extends Channel {
    private static final int LOCAL_MAXIMUM_PACKET_SIZE = 16384;
    private static final int LOCAL_WINDOW_SIZE_MAX = 131072;
    private static final byte[] _type = Util.str2byte("direct-tcpip");
    String host;
    String originator_IP_address = "127.0.0.1";
    int originator_port = 0;
    int port;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ChannelDirectTCPIP() {
        this.type = _type;
        setLocalWindowSizeMax(131072);
        setLocalWindowSize(131072);
        setLocalPacketSize(16384);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.jcraft.jsch.Channel
    public void init() {
        this.f211io = new IO();
    }

    @Override // com.jcraft.jsch.Channel
    public void connect(int connectTimeout) throws JSchException {
        this.connectTimeout = connectTimeout;
        try {
            Session _session = getSession();
            if (!_session.isConnected()) {
                throw new JSchException("session is down");
            }
            if (this.f211io.in != null) {
                this.thread = new Thread(this);
                Thread thread = this.thread;
                thread.setName("DirectTCPIP thread " + _session.getHost());
                if (_session.daemon_thread) {
                    this.thread.setDaemon(_session.daemon_thread);
                }
                this.thread.start();
            } else {
                sendChannelOpen();
            }
        } catch (Exception e) {
            this.f211io.close();
            this.f211io = null;
            Channel.del(this);
            if (e instanceof JSchException) {
                throw ((JSchException) e);
            }
        }
    }

    @Override // com.jcraft.jsch.Channel, java.lang.Runnable
    public void run() {
        try {
            sendChannelOpen();
            Buffer buf = new Buffer(this.rmpsize);
            Packet packet = new Packet(buf);
            Session _session = getSession();
            while (true) {
                if (isConnected() && this.thread != null && this.f211io != null && this.f211io.in != null) {
                    int i = this.f211io.in.read(buf.buffer, 14, (buf.buffer.length - 14) - 128);
                    if (i <= 0) {
                        eof();
                        break;
                    }
                    packet.reset();
                    buf.putByte((byte) 94);
                    buf.putInt(this.recipient);
                    buf.putInt(i);
                    buf.skip(i);
                    synchronized (this) {
                        if (!this.close) {
                            _session.write(packet, this, i);
                        }
                    }
                    break;
                }
                break;
            }
            eof();
            disconnect();
        } catch (Exception e) {
            if (!this.connected) {
                this.connected = true;
            }
            disconnect();
        }
    }

    @Override // com.jcraft.jsch.Channel
    public void setInputStream(InputStream in) {
        this.f211io.setInputStream(in);
    }

    @Override // com.jcraft.jsch.Channel
    public void setOutputStream(OutputStream out) {
        this.f211io.setOutputStream(out);
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setOrgIPAddress(String foo) {
        this.originator_IP_address = foo;
    }

    public void setOrgPort(int foo) {
        this.originator_port = foo;
    }

    @Override // com.jcraft.jsch.Channel
    protected Packet genChannelOpenPacket() {
        Buffer buf = new Buffer(this.host.length() + 50 + this.originator_IP_address.length() + 128);
        Packet packet = new Packet(buf);
        packet.reset();
        buf.putByte((byte) 90);
        buf.putString(this.type);
        buf.putInt(this.id);
        buf.putInt(this.lwsize);
        buf.putInt(this.lmpsize);
        buf.putString(Util.str2byte(this.host));
        buf.putInt(this.port);
        buf.putString(Util.str2byte(this.originator_IP_address));
        buf.putInt(this.originator_port);
        return packet;
    }
}
