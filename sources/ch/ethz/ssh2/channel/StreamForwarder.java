package ch.ethz.ssh2.channel;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
/* loaded from: classes.dex */
public class StreamForwarder extends Thread {
    byte[] buffer = new byte[30000];
    Channel c;
    InputStream is;
    String mode;
    OutputStream os;
    Socket s;
    StreamForwarder sibling;

    /* JADX INFO: Access modifiers changed from: package-private */
    public StreamForwarder(Channel c, StreamForwarder sibling, Socket s, InputStream is, OutputStream os, String mode) throws IOException {
        this.is = is;
        this.os = os;
        this.mode = mode;
        this.c = c;
        this.sibling = sibling;
        this.s = s;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        while (true) {
            try {
                try {
                    int len = this.is.read(this.buffer);
                    if (len <= 0) {
                        break;
                    }
                    this.os.write(this.buffer, 0, len);
                    this.os.flush();
                } catch (IOException ignore) {
                    try {
                        ChannelManager channelManager = this.c.cm;
                        Channel channel = this.c;
                        StringBuffer stringBuffer = new StringBuffer("Closed due to exception in StreamForwarder (");
                        stringBuffer.append(this.mode);
                        stringBuffer.append("): ");
                        stringBuffer.append(ignore.getMessage());
                        channelManager.closeChannel(channel, stringBuffer.toString(), true);
                    } catch (IOException e) {
                    }
                }
            } catch (Throwable th) {
                try {
                    this.os.close();
                } catch (IOException e2) {
                }
                try {
                    this.is.close();
                } catch (IOException e3) {
                }
                if (this.sibling != null) {
                    while (this.sibling.isAlive()) {
                        try {
                            this.sibling.join();
                        } catch (InterruptedException e4) {
                        }
                    }
                    try {
                        ChannelManager channelManager2 = this.c.cm;
                        Channel channel2 = this.c;
                        StringBuffer stringBuffer2 = new StringBuffer("StreamForwarder (");
                        stringBuffer2.append(this.mode);
                        stringBuffer2.append(") is cleaning up the connection");
                        channelManager2.closeChannel(channel2, stringBuffer2.toString(), true);
                    } catch (IOException e5) {
                    }
                    try {
                        if (this.s != null) {
                            this.s.close();
                        }
                    } catch (IOException e6) {
                    }
                }
                throw th;
            }
        }
        try {
            this.os.close();
        } catch (IOException e7) {
        }
        try {
            this.is.close();
        } catch (IOException e8) {
        }
        if (this.sibling != null) {
            while (this.sibling.isAlive()) {
                try {
                    this.sibling.join();
                } catch (InterruptedException e9) {
                }
            }
            try {
                ChannelManager channelManager3 = this.c.cm;
                Channel channel3 = this.c;
                StringBuffer stringBuffer3 = new StringBuffer("StreamForwarder (");
                stringBuffer3.append(this.mode);
                stringBuffer3.append(") is cleaning up the connection");
                channelManager3.closeChannel(channel3, stringBuffer3.toString(), true);
            } catch (IOException e10) {
            }
            try {
                if (this.s != null) {
                    this.s.close();
                }
            } catch (IOException e11) {
            }
        }
    }
}
