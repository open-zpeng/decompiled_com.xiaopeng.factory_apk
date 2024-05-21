package ch.ethz.ssh2.channel;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
/* loaded from: classes.dex */
public class LocalAcceptThread extends Thread implements IChannelWorkerThread {
    ChannelManager cm;
    String host_to_connect;
    int local_port;
    int port_to_connect;
    final ServerSocket ss;

    public LocalAcceptThread(ChannelManager cm, int local_port, String host_to_connect, int port_to_connect) throws IOException {
        this.cm = cm;
        this.local_port = local_port;
        this.host_to_connect = host_to_connect;
        this.port_to_connect = port_to_connect;
        this.ss = new ServerSocket(local_port);
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        boolean z;
        try {
            this.cm.registerThread(this);
            while (true) {
                try {
                    Socket s = this.ss.accept();
                    try {
                        Channel cn2 = this.cm.openDirectTCPIPChannel(this.host_to_connect, this.port_to_connect, s.getInetAddress().getHostAddress(), s.getPort());
                        try {
                            StreamForwarder r2l = new StreamForwarder(cn2, null, null, cn2.stdoutStream, s.getOutputStream(), "RemoteToLocal");
                            try {
                                z = true;
                                try {
                                    StreamForwarder l2r = new StreamForwarder(cn2, r2l, s, s.getInputStream(), cn2.stdinStream, "LocalToRemote");
                                    r2l.setDaemon(true);
                                    l2r.setDaemon(true);
                                    r2l.start();
                                    l2r.start();
                                } catch (IOException e) {
                                    e = e;
                                    IOException e2 = e;
                                    try {
                                        ChannelManager channelManager = cn2.cm;
                                        StringBuffer stringBuffer = new StringBuffer("Weird error during creation of StreamForwarder (");
                                        stringBuffer.append(e2.getMessage());
                                        stringBuffer.append(")");
                                        channelManager.closeChannel(cn2, stringBuffer.toString(), z);
                                    } catch (IOException e3) {
                                    }
                                }
                            } catch (IOException e4) {
                                e = e4;
                                z = true;
                            }
                        } catch (IOException e5) {
                            e = e5;
                            z = true;
                        }
                    } catch (IOException e6) {
                        try {
                            s.close();
                        } catch (IOException e7) {
                        }
                    }
                } catch (IOException e8) {
                    stopWorking();
                    return;
                }
            }
        } catch (IOException e9) {
            stopWorking();
        }
    }

    @Override // ch.ethz.ssh2.channel.IChannelWorkerThread
    public void stopWorking() {
        try {
            this.ss.close();
        } catch (IOException e) {
        }
    }
}
