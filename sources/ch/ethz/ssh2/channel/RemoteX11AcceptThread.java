package ch.ethz.ssh2.channel;

import ch.ethz.ssh2.log.Logger;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
/* loaded from: classes.dex */
public class RemoteX11AcceptThread extends Thread {
    static /* synthetic */ Class class$0;
    private static final Logger log;
    Channel c;
    String remoteOriginatorAddress;
    int remoteOriginatorPort;
    Socket s;

    static {
        Class<?> cls = class$0;
        if (cls == null) {
            try {
                cls = Class.forName("ch.ethz.ssh2.channel.RemoteX11AcceptThread");
                class$0 = cls;
            } catch (ClassNotFoundException e) {
                throw new NoClassDefFoundError(e.getMessage());
            }
        }
        log = Logger.getLogger(cls);
    }

    public RemoteX11AcceptThread(Channel c, String remoteOriginatorAddress, int remoteOriginatorPort) {
        this.c = c;
        this.remoteOriginatorAddress = remoteOriginatorAddress;
        this.remoteOriginatorPort = remoteOriginatorPort;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        int i;
        String stringBuffer;
        try {
            this.c.cm.sendOpenConfirmation(this.c);
            OutputStream remote_os = this.c.getStdinStream();
            InputStream remote_is = this.c.getStdoutStream();
            byte[] header = new byte[6];
            if (remote_is.read(header) != 6) {
                throw new IOException("Unexpected EOF on X11 startup!");
            }
            if (header[0] != 66 && header[0] != 108) {
                throw new IOException("Unknown endian format in X11 message!");
            }
            if (header[0] != 66) {
                i = 1;
            } else {
                i = 0;
            }
            int idxMSB = i;
            byte[] auth_buff = new byte[6];
            if (remote_is.read(auth_buff) != 6) {
                throw new IOException("Unexpected EOF on X11 startup!");
            }
            int authProtocolNameLength = ((auth_buff[idxMSB] & 255) << 8) | (auth_buff[1 - idxMSB] & 255);
            int authProtocolDataLength = ((auth_buff[idxMSB + 2] & 255) << 8) | (auth_buff[3 - idxMSB] & 255);
            if (authProtocolNameLength > 256 || authProtocolDataLength > 256) {
                throw new IOException("Buggy X11 authorization data");
            }
            int authProtocolNamePadding = (4 - (authProtocolNameLength % 4)) % 4;
            int authProtocolDataPadding = (4 - (authProtocolDataLength % 4)) % 4;
            byte[] authProtocolName = new byte[authProtocolNameLength];
            byte[] authProtocolData = new byte[authProtocolDataLength];
            byte[] paddingBuffer = new byte[4];
            if (remote_is.read(authProtocolName) != authProtocolNameLength) {
                throw new IOException("Unexpected EOF on X11 startup! (authProtocolName)");
            }
            if (remote_is.read(paddingBuffer, 0, authProtocolNamePadding) != authProtocolNamePadding) {
                throw new IOException("Unexpected EOF on X11 startup! (authProtocolNamePadding)");
            }
            if (remote_is.read(authProtocolData) != authProtocolDataLength) {
                throw new IOException("Unexpected EOF on X11 startup! (authProtocolData)");
            }
            if (remote_is.read(paddingBuffer, 0, authProtocolDataPadding) != authProtocolDataPadding) {
                throw new IOException("Unexpected EOF on X11 startup! (authProtocolDataPadding)");
            }
            if (!"MIT-MAGIC-COOKIE-1".equals(new String(authProtocolName))) {
                throw new IOException("Unknown X11 authorization protocol!");
            }
            if (authProtocolDataLength != 16) {
                throw new IOException("Wrong data length for X11 authorization data!");
            }
            StringBuffer tmp = new StringBuffer(32);
            int i2 = 0;
            while (i2 < authProtocolData.length) {
                byte[] header2 = header;
                InputStream remote_is2 = remote_is;
                byte[] paddingBuffer2 = paddingBuffer;
                byte[] authProtocolData2 = authProtocolData;
                byte[] authProtocolName2 = authProtocolName;
                int authProtocolDataPadding2 = authProtocolDataPadding;
                int authProtocolNamePadding2 = authProtocolNamePadding;
                int authProtocolDataLength2 = authProtocolDataLength;
                int authProtocolNameLength2 = authProtocolNameLength;
                StringBuffer tmp2 = tmp;
                byte[] auth_buff2 = auth_buff;
                String digit2 = Integer.toHexString(authProtocolData2[i2] & 255);
                if (digit2.length() == 2) {
                    stringBuffer = digit2;
                } else {
                    StringBuffer stringBuffer2 = new StringBuffer("0");
                    stringBuffer2.append(digit2);
                    stringBuffer = stringBuffer2.toString();
                }
                tmp2.append(stringBuffer);
                i2++;
                tmp = tmp2;
                auth_buff = auth_buff2;
                authProtocolDataPadding = authProtocolDataPadding2;
                authProtocolNamePadding = authProtocolNamePadding2;
                header = header2;
                remote_is = remote_is2;
                paddingBuffer = paddingBuffer2;
                authProtocolData = authProtocolData2;
                authProtocolName = authProtocolName2;
                authProtocolDataLength = authProtocolDataLength2;
                authProtocolNameLength = authProtocolNameLength2;
            }
            String hexEncodedFakeCookie = tmp.toString();
            synchronized (this.c) {
                this.c.hexX11FakeCookie = hexEncodedFakeCookie;
            }
            X11ServerData sd = this.c.cm.checkX11Cookie(hexEncodedFakeCookie);
            if (sd == null) {
                throw new IOException("Invalid X11 cookie received.");
            }
            this.s = new Socket(sd.hostname, sd.port);
            OutputStream x11_os = this.s.getOutputStream();
            InputStream x11_is = this.s.getInputStream();
            x11_os.write(header);
            if (sd.x11_magic_cookie == null) {
                byte[] emptyAuthData = new byte[6];
                x11_os.write(emptyAuthData);
            } else {
                byte[] emptyAuthData2 = sd.x11_magic_cookie;
                if (emptyAuthData2.length != 16) {
                    throw new IOException("The real X11 cookie has an invalid length!");
                }
                x11_os.write(auth_buff);
                x11_os.write(authProtocolName);
                x11_os.write(paddingBuffer, 0, authProtocolNamePadding);
                x11_os.write(sd.x11_magic_cookie);
                x11_os.write(paddingBuffer, 0, authProtocolDataPadding);
            }
            x11_os.flush();
            StreamForwarder r2l = new StreamForwarder(this.c, null, null, remote_is, x11_os, "RemoteToX11");
            StreamForwarder l2r = new StreamForwarder(this.c, null, null, x11_is, remote_os, "X11ToRemote");
            r2l.setDaemon(true);
            r2l.start();
            l2r.run();
            while (r2l.isAlive()) {
                try {
                    r2l.join();
                } catch (InterruptedException e) {
                }
            }
            this.c.cm.closeChannel(this.c, "EOF on both X11 streams reached.", true);
            this.s.close();
        } catch (IOException e2) {
            Logger logger = log;
            StringBuffer stringBuffer3 = new StringBuffer("IOException in X11 proxy code: ");
            stringBuffer3.append(e2.getMessage());
            logger.log(50, stringBuffer3.toString());
            try {
                ChannelManager channelManager = this.c.cm;
                Channel channel = this.c;
                StringBuffer stringBuffer4 = new StringBuffer("IOException in X11 proxy code (");
                stringBuffer4.append(e2.getMessage());
                stringBuffer4.append(")");
                channelManager.closeChannel(channel, stringBuffer4.toString(), true);
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
