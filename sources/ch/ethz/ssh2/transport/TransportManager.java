package ch.ethz.ssh2.transport;

import ch.ethz.ssh2.ConnectionInfo;
import ch.ethz.ssh2.ConnectionMonitor;
import ch.ethz.ssh2.DHGexParameters;
import ch.ethz.ssh2.HTTPProxyData;
import ch.ethz.ssh2.HTTPProxyException;
import ch.ethz.ssh2.ProxyData;
import ch.ethz.ssh2.ServerHostKeyVerifier;
import ch.ethz.ssh2.crypto.Base64;
import ch.ethz.ssh2.crypto.CryptoWishList;
import ch.ethz.ssh2.crypto.cipher.BlockCipher;
import ch.ethz.ssh2.crypto.digest.MAC;
import ch.ethz.ssh2.log.Logger;
import ch.ethz.ssh2.packets.PacketDisconnect;
import ch.ethz.ssh2.packets.TypesReader;
import ch.ethz.ssh2.util.Tokenizer;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.SecureRandom;
import java.util.Vector;
import org.apache.commons.net.telnet.TelnetCommand;
/* loaded from: classes.dex */
public class TransportManager {
    static /* synthetic */ Class class$0;
    private static final Logger log;
    String hostname;
    KexManager km;
    int port;
    Thread receiveThread;
    TransportConnection tc;
    private final Vector asynchronousQueue = new Vector();
    private Thread asynchronousThread = null;
    final Socket sock = new Socket();
    Object connectionSemaphore = new Object();
    boolean flagKexOngoing = false;
    boolean connectionClosed = false;
    Throwable reasonClosedCause = null;
    Vector messageHandlers = new Vector();
    Vector connectionMonitors = new Vector();
    boolean monitorsWereInformed = false;

    static {
        Class<?> cls = class$0;
        if (cls == null) {
            try {
                cls = Class.forName("ch.ethz.ssh2.transport.TransportManager");
                class$0 = cls;
            } catch (ClassNotFoundException e) {
                throw new NoClassDefFoundError(e.getMessage());
            }
        }
        log = Logger.getLogger(cls);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class HandlerEntry {
        int high;
        int low;
        MessageHandler mh;

        HandlerEntry() {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class AsynchronousWorker extends Thread {
        AsynchronousWorker() {
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            byte[] msg;
            while (true) {
                synchronized (TransportManager.this.asynchronousQueue) {
                    if (TransportManager.this.asynchronousQueue.size() == 0) {
                        try {
                            TransportManager.this.asynchronousQueue.wait(2000L);
                        } catch (InterruptedException e) {
                        }
                        if (TransportManager.this.asynchronousQueue.size() == 0) {
                            TransportManager.this.asynchronousThread = null;
                            return;
                        }
                    }
                    msg = (byte[]) TransportManager.this.asynchronousQueue.remove(0);
                }
                try {
                    TransportManager.this.sendMessage(msg);
                } catch (IOException e2) {
                    return;
                }
            }
        }
    }

    private InetAddress createInetAddress(String host) throws UnknownHostException {
        InetAddress addr = parseIPv4Address(host);
        if (addr != null) {
            return addr;
        }
        return InetAddress.getByName(host);
    }

    private InetAddress parseIPv4Address(String host) throws UnknownHostException {
        String[] quad;
        if (host == null || (quad = Tokenizer.parseTokens(host, '.')) == null || quad.length != 4) {
            return null;
        }
        byte[] addr = new byte[4];
        for (int i = 0; i < 4; i++) {
            int part = 0;
            if (quad[i].length() == 0 || quad[i].length() > 3) {
                return null;
            }
            for (int k = 0; k < quad[i].length(); k++) {
                char c = quad[i].charAt(k);
                if (c < '0' || c > '9') {
                    return null;
                }
                part = (part * 10) + (c - '0');
            }
            if (part > 255) {
                return null;
            }
            addr[i] = (byte) part;
        }
        return InetAddress.getByAddress(host, addr);
    }

    public TransportManager(String host, int port) throws IOException {
        this.hostname = host;
        this.port = port;
    }

    public int getPacketOverheadEstimate() {
        return this.tc.getPacketOverheadEstimate();
    }

    public void setTcpNoDelay(boolean state) throws IOException {
        this.sock.setTcpNoDelay(state);
    }

    public void setSoTimeout(int timeout) throws IOException {
        this.sock.setSoTimeout(timeout);
    }

    public ConnectionInfo getConnectionInfo(int kexNumber) throws IOException {
        return this.km.getOrWaitForConnectionInfo(kexNumber);
    }

    public Throwable getReasonClosedCause() {
        Throwable th;
        synchronized (this.connectionSemaphore) {
            th = this.reasonClosedCause;
        }
        return th;
    }

    public byte[] getSessionIdentifier() {
        return this.km.sessionId;
    }

    public void close(Throwable cause, boolean useDisconnectPacket) {
        if (!useDisconnectPacket) {
            try {
                this.sock.close();
            } catch (IOException e) {
            }
        }
        synchronized (this.connectionSemaphore) {
            if (!this.connectionClosed) {
                if (useDisconnectPacket) {
                    try {
                        byte[] msg = new PacketDisconnect(11, cause.getMessage(), "").getPayload();
                        if (this.tc != null) {
                            this.tc.sendMessage(msg);
                        }
                    } catch (IOException e2) {
                    }
                    try {
                        this.sock.close();
                    } catch (IOException e3) {
                    }
                }
                this.connectionClosed = true;
                this.reasonClosedCause = cause;
            }
            this.connectionSemaphore.notifyAll();
        }
        Vector monitors = null;
        synchronized (this) {
            if (!this.monitorsWereInformed) {
                this.monitorsWereInformed = true;
                monitors = (Vector) this.connectionMonitors.clone();
            }
        }
        if (monitors != null) {
            for (int i = 0; i < monitors.size(); i++) {
                try {
                    ConnectionMonitor cmon = (ConnectionMonitor) monitors.elementAt(i);
                    cmon.connectionLost(this.reasonClosedCause);
                } catch (Exception e4) {
                }
            }
        }
    }

    private void establishConnection(ProxyData proxyData, int connectTimeout) throws IOException {
        int len;
        if (proxyData == null) {
            InetAddress addr = createInetAddress(this.hostname);
            this.sock.connect(new InetSocketAddress(addr, this.port), connectTimeout);
            this.sock.setSoTimeout(0);
        } else if (proxyData instanceof HTTPProxyData) {
            HTTPProxyData pd = (HTTPProxyData) proxyData;
            InetAddress addr2 = createInetAddress(pd.proxyHost);
            this.sock.connect(new InetSocketAddress(addr2, pd.proxyPort), connectTimeout);
            this.sock.setSoTimeout(0);
            StringBuffer sb = new StringBuffer();
            sb.append("CONNECT ");
            sb.append(this.hostname);
            sb.append(':');
            sb.append(this.port);
            sb.append(" HTTP/1.0\r\n");
            if (pd.proxyUser != null && pd.proxyPass != null) {
                StringBuffer stringBuffer = new StringBuffer(String.valueOf(pd.proxyUser));
                stringBuffer.append(":");
                stringBuffer.append(pd.proxyPass);
                String credentials = stringBuffer.toString();
                char[] encoded = Base64.encode(credentials.getBytes());
                sb.append("Proxy-Authorization: Basic ");
                sb.append(encoded);
                sb.append("\r\n");
            }
            if (pd.requestHeaderLines != null) {
                for (int i = 0; i < pd.requestHeaderLines.length; i++) {
                    if (pd.requestHeaderLines[i] != null) {
                        sb.append(pd.requestHeaderLines[i]);
                        sb.append("\r\n");
                    }
                }
            }
            sb.append("\r\n");
            OutputStream out = this.sock.getOutputStream();
            out.write(sb.toString().getBytes());
            out.flush();
            byte[] buffer = new byte[1024];
            InputStream in = this.sock.getInputStream();
            int len2 = ClientServerHello.readLineRN(in, buffer);
            String httpReponse = new String(buffer, 0, len2);
            if (!httpReponse.startsWith("HTTP/")) {
                throw new IOException("The proxy did not send back a valid HTTP response.");
            }
            if (httpReponse.length() < 14 || httpReponse.charAt(8) != ' ' || httpReponse.charAt(12) != ' ') {
                throw new IOException("The proxy did not send back a valid HTTP response.");
            }
            try {
                int errorCode = Integer.parseInt(httpReponse.substring(9, 12));
                if (errorCode < 0 || errorCode > 999) {
                    throw new IOException("The proxy did not send back a valid HTTP response.");
                }
                if (errorCode != 200) {
                    throw new HTTPProxyException(httpReponse.substring(13), errorCode);
                }
                do {
                    len = ClientServerHello.readLineRN(in, buffer);
                } while (len != 0);
            } catch (NumberFormatException e) {
                throw new IOException("The proxy did not send back a valid HTTP response.");
            }
        } else {
            throw new IOException("Unsupported ProxyData");
        }
    }

    public void initialize(CryptoWishList cwl, ServerHostKeyVerifier verifier, DHGexParameters dhgex, int connectTimeout, SecureRandom rnd, ProxyData proxyData) throws IOException {
        establishConnection(proxyData, connectTimeout);
        ClientServerHello csh = new ClientServerHello(this.sock.getInputStream(), this.sock.getOutputStream());
        this.tc = new TransportConnection(this.sock.getInputStream(), this.sock.getOutputStream(), rnd);
        this.km = new KexManager(this, csh, cwl, this.hostname, this.port, verifier, rnd);
        this.km.initiateKEX(cwl, dhgex);
        this.receiveThread = new Thread(new Runnable() { // from class: ch.ethz.ssh2.transport.TransportManager.1
            @Override // java.lang.Runnable
            public void run() {
                try {
                    TransportManager.this.receiveLoop();
                } catch (IOException e) {
                    TransportManager.this.close(e, false);
                    if (TransportManager.log.isEnabled()) {
                        Logger logger = TransportManager.log;
                        StringBuffer stringBuffer = new StringBuffer("Receive thread: error in receiveLoop: ");
                        stringBuffer.append(e.getMessage());
                        logger.log(10, stringBuffer.toString());
                    }
                }
                if (TransportManager.log.isEnabled()) {
                    TransportManager.log.log(50, "Receive thread: back from receiveLoop");
                }
                if (TransportManager.this.km != null) {
                    try {
                        TransportManager.this.km.handleMessage(null, 0);
                    } catch (IOException e2) {
                    }
                }
                for (int i = 0; i < TransportManager.this.messageHandlers.size(); i++) {
                    HandlerEntry he = (HandlerEntry) TransportManager.this.messageHandlers.elementAt(i);
                    try {
                        he.mh.handleMessage(null, 0);
                    } catch (Exception e3) {
                    }
                }
            }
        });
        this.receiveThread.setDaemon(true);
        this.receiveThread.start();
    }

    public void registerMessageHandler(MessageHandler mh, int low, int high) {
        HandlerEntry he = new HandlerEntry();
        he.mh = mh;
        he.low = low;
        he.high = high;
        synchronized (this.messageHandlers) {
            this.messageHandlers.addElement(he);
        }
    }

    public void removeMessageHandler(MessageHandler mh, int low, int high) {
        synchronized (this.messageHandlers) {
            int i = 0;
            while (true) {
                if (i < this.messageHandlers.size()) {
                    HandlerEntry he = (HandlerEntry) this.messageHandlers.elementAt(i);
                    if (he.mh != mh || he.low != low || he.high != high) {
                        i++;
                    } else {
                        this.messageHandlers.removeElementAt(i);
                        break;
                    }
                } else {
                    break;
                }
            }
        }
    }

    public void sendKexMessage(byte[] msg) throws IOException {
        synchronized (this.connectionSemaphore) {
            if (this.connectionClosed) {
                throw ((IOException) new IOException("Sorry, this connection is closed.").initCause(this.reasonClosedCause));
            }
            this.flagKexOngoing = true;
            try {
                this.tc.sendMessage(msg);
            } catch (IOException e) {
                close(e, false);
                throw e;
            }
        }
    }

    public void kexFinished() throws IOException {
        synchronized (this.connectionSemaphore) {
            this.flagKexOngoing = false;
            this.connectionSemaphore.notifyAll();
        }
    }

    public void forceKeyExchange(CryptoWishList cwl, DHGexParameters dhgex) throws IOException {
        this.km.initiateKEX(cwl, dhgex);
    }

    public void changeRecvCipher(BlockCipher bc, MAC mac) {
        this.tc.changeRecvCipher(bc, mac);
    }

    public void changeSendCipher(BlockCipher bc, MAC mac) {
        this.tc.changeSendCipher(bc, mac);
    }

    public void sendAsynchronousMessage(byte[] msg) throws IOException {
        synchronized (this.asynchronousQueue) {
            this.asynchronousQueue.addElement(msg);
            if (this.asynchronousQueue.size() > 100) {
                throw new IOException("Error: the peer is not consuming our asynchronous replies.");
            }
            if (this.asynchronousThread == null) {
                this.asynchronousThread = new AsynchronousWorker();
                this.asynchronousThread.setDaemon(true);
                this.asynchronousThread.start();
            }
        }
    }

    public void setConnectionMonitors(Vector monitors) {
        synchronized (this) {
            this.connectionMonitors = (Vector) monitors.clone();
        }
    }

    public void sendMessage(byte[] msg) throws IOException {
        if (Thread.currentThread() == this.receiveThread) {
            throw new IOException("Assertion error: sendMessage may never be invoked by the receiver thread!");
        }
        synchronized (this.connectionSemaphore) {
            while (!this.connectionClosed) {
                if (this.flagKexOngoing) {
                    try {
                        this.connectionSemaphore.wait();
                    } catch (InterruptedException e) {
                    }
                } else {
                    try {
                        this.tc.sendMessage(msg);
                    } catch (IOException e2) {
                        close(e2, false);
                        throw e2;
                    }
                }
            }
            throw ((IOException) new IOException("Sorry, this connection is closed.").initCause(this.reasonClosedCause));
        }
    }

    public void receiveLoop() throws IOException {
        byte[] msg = new byte[35000];
        while (true) {
            int msglen = this.tc.receiveMessage(msg, 0, msg.length);
            int type = msg[0] & 255;
            if (type != 2) {
                if (type == 4) {
                    if (log.isEnabled()) {
                        TypesReader tr = new TypesReader(msg, 0, msglen);
                        tr.readByte();
                        tr.readBoolean();
                        StringBuffer debugMessageBuffer = new StringBuffer();
                        debugMessageBuffer.append(tr.readString("UTF-8"));
                        for (int i = 0; i < debugMessageBuffer.length(); i++) {
                            char c = debugMessageBuffer.charAt(i);
                            if (c < ' ' || c > '~') {
                                debugMessageBuffer.setCharAt(i, (char) 65533);
                            }
                        }
                        Logger logger = log;
                        StringBuffer stringBuffer = new StringBuffer("DEBUG Message from remote: '");
                        stringBuffer.append(debugMessageBuffer.toString());
                        stringBuffer.append("'");
                        logger.log(50, stringBuffer.toString());
                    }
                } else if (type == 3) {
                    throw new IOException("Peer sent UNIMPLEMENTED message, that should not happen.");
                } else {
                    if (type == 1) {
                        TypesReader tr2 = new TypesReader(msg, 0, msglen);
                        tr2.readByte();
                        int reason_code = tr2.readUINT32();
                        StringBuffer reasonBuffer = new StringBuffer();
                        reasonBuffer.append(tr2.readString("UTF-8"));
                        if (reasonBuffer.length() > 255) {
                            reasonBuffer.setLength(255);
                            reasonBuffer.setCharAt(TelnetCommand.DONT, '.');
                            reasonBuffer.setCharAt(TelnetCommand.DO, '.');
                            reasonBuffer.setCharAt(TelnetCommand.WONT, '.');
                        }
                        for (int i2 = 0; i2 < reasonBuffer.length(); i2++) {
                            char c2 = reasonBuffer.charAt(i2);
                            if (c2 < ' ' || c2 > '~') {
                                reasonBuffer.setCharAt(i2, (char) 65533);
                            }
                        }
                        StringBuffer stringBuffer2 = new StringBuffer("Peer sent DISCONNECT message (reason code ");
                        stringBuffer2.append(reason_code);
                        stringBuffer2.append("): ");
                        stringBuffer2.append(reasonBuffer.toString());
                        throw new IOException(stringBuffer2.toString());
                    } else if (type == 20 || type == 21 || (type >= 30 && type <= 49)) {
                        this.km.handleMessage(msg, msglen);
                    } else {
                        MessageHandler mh = null;
                        int i3 = 0;
                        while (true) {
                            if (i3 >= this.messageHandlers.size()) {
                                break;
                            }
                            HandlerEntry he = (HandlerEntry) this.messageHandlers.elementAt(i3);
                            if (he.low > type || type > he.high) {
                                i3++;
                            } else {
                                mh = he.mh;
                                break;
                            }
                        }
                        if (mh == null) {
                            StringBuffer stringBuffer3 = new StringBuffer("Unexpected SSH message (type ");
                            stringBuffer3.append(type);
                            stringBuffer3.append(")");
                            throw new IOException(stringBuffer3.toString());
                        }
                        mh.handleMessage(msg, msglen);
                    }
                }
            }
        }
    }
}
