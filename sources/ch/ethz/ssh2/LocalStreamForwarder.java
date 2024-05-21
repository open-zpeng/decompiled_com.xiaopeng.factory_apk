package ch.ethz.ssh2;

import ch.ethz.ssh2.channel.Channel;
import ch.ethz.ssh2.channel.ChannelManager;
import ch.ethz.ssh2.channel.LocalAcceptThread;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
/* loaded from: classes.dex */
public class LocalStreamForwarder {
    ChannelManager cm;

    /* renamed from: cn  reason: collision with root package name */
    Channel f208cn;
    String host_to_connect;
    LocalAcceptThread lat;
    int port_to_connect;

    /* JADX INFO: Access modifiers changed from: package-private */
    public LocalStreamForwarder(ChannelManager cm, String host_to_connect, int port_to_connect) throws IOException {
        this.cm = cm;
        this.host_to_connect = host_to_connect;
        this.port_to_connect = port_to_connect;
        this.f208cn = cm.openDirectTCPIPChannel(host_to_connect, port_to_connect, "127.0.0.1", 0);
    }

    public InputStream getInputStream() throws IOException {
        return this.f208cn.getStdoutStream();
    }

    public OutputStream getOutputStream() throws IOException {
        return this.f208cn.getStdinStream();
    }

    public void close() throws IOException {
        this.cm.closeChannel(this.f208cn, "Closed due to user request.", true);
    }
}
