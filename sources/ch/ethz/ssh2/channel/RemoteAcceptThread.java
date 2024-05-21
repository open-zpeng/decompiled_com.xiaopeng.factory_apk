package ch.ethz.ssh2.channel;

import ch.ethz.ssh2.log.Logger;
import java.io.IOException;
import java.net.Socket;
/* loaded from: classes.dex */
public class RemoteAcceptThread extends Thread {
    static /* synthetic */ Class class$0;
    private static final Logger log;
    Channel c;
    String remoteConnectedAddress;
    int remoteConnectedPort;
    String remoteOriginatorAddress;
    int remoteOriginatorPort;
    Socket s;
    String targetAddress;
    int targetPort;

    static {
        Class<?> cls = class$0;
        if (cls == null) {
            try {
                cls = Class.forName("ch.ethz.ssh2.channel.RemoteAcceptThread");
                class$0 = cls;
            } catch (ClassNotFoundException e) {
                throw new NoClassDefFoundError(e.getMessage());
            }
        }
        log = Logger.getLogger(cls);
    }

    public RemoteAcceptThread(Channel c, String remoteConnectedAddress, int remoteConnectedPort, String remoteOriginatorAddress, int remoteOriginatorPort, String targetAddress, int targetPort) {
        this.c = c;
        this.remoteConnectedAddress = remoteConnectedAddress;
        this.remoteConnectedPort = remoteConnectedPort;
        this.remoteOriginatorAddress = remoteOriginatorAddress;
        this.remoteOriginatorPort = remoteOriginatorPort;
        this.targetAddress = targetAddress;
        this.targetPort = targetPort;
        if (log.isEnabled()) {
            Logger logger = log;
            StringBuffer stringBuffer = new StringBuffer("RemoteAcceptThread: ");
            stringBuffer.append(remoteConnectedAddress);
            stringBuffer.append("/");
            stringBuffer.append(remoteConnectedPort);
            stringBuffer.append(", R: ");
            stringBuffer.append(remoteOriginatorAddress);
            stringBuffer.append("/");
            stringBuffer.append(remoteOriginatorPort);
            logger.log(20, stringBuffer.toString());
        }
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        try {
            this.c.cm.sendOpenConfirmation(this.c);
            this.s = new Socket(this.targetAddress, this.targetPort);
            StreamForwarder r2l = new StreamForwarder(this.c, null, null, this.c.getStdoutStream(), this.s.getOutputStream(), "RemoteToLocal");
            StreamForwarder l2r = new StreamForwarder(this.c, null, null, this.s.getInputStream(), this.c.getStdinStream(), "LocalToRemote");
            r2l.setDaemon(true);
            r2l.start();
            l2r.run();
            while (r2l.isAlive()) {
                try {
                    r2l.join();
                } catch (InterruptedException e) {
                }
            }
            this.c.cm.closeChannel(this.c, "EOF on both streams reached.", true);
            this.s.close();
        } catch (IOException e2) {
            Logger logger = log;
            StringBuffer stringBuffer = new StringBuffer("IOException in proxy code: ");
            stringBuffer.append(e2.getMessage());
            logger.log(50, stringBuffer.toString());
            try {
                ChannelManager channelManager = this.c.cm;
                Channel channel = this.c;
                StringBuffer stringBuffer2 = new StringBuffer("IOException in proxy code (");
                stringBuffer2.append(e2.getMessage());
                stringBuffer2.append(")");
                channelManager.closeChannel(channel, stringBuffer2.toString(), true);
            } catch (IOException e3) {
            }
            try {
                if (this.s != null) {
                    this.s.close();
                }
            } catch (IOException e4) {
            }
        }
    }
}
