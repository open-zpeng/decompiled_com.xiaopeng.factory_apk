package ch.ethz.ssh2;

import ch.ethz.ssh2.auth.AuthenticationManager;
import ch.ethz.ssh2.channel.ChannelManager;
import ch.ethz.ssh2.crypto.CryptoWishList;
import ch.ethz.ssh2.crypto.cipher.BlockCipherFactory;
import ch.ethz.ssh2.crypto.digest.MAC;
import ch.ethz.ssh2.transport.KexManager;
import ch.ethz.ssh2.transport.TransportManager;
import ch.ethz.ssh2.util.TimeoutService;
import java.io.CharArrayWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.security.SecureRandom;
import java.util.Vector;
/* loaded from: classes.dex */
public class Connection {
    public static final String identification = "Ganymed Build_210";
    private AuthenticationManager am;
    private boolean authenticated;
    private ChannelManager cm;
    private Vector connectionMonitors;
    private CryptoWishList cryptoWishList;
    private DHGexParameters dhgexpara;
    private SecureRandom generator;
    private final String hostname;
    private final int port;
    private ProxyData proxyData;
    private boolean tcpNoDelay;
    private TransportManager tm;

    public static synchronized String[] getAvailableCiphers() {
        String[] defaultCipherList;
        synchronized (Connection.class) {
            defaultCipherList = BlockCipherFactory.getDefaultCipherList();
        }
        return defaultCipherList;
    }

    public static synchronized String[] getAvailableMACs() {
        String[] macList;
        synchronized (Connection.class) {
            macList = MAC.getMacList();
        }
        return macList;
    }

    public static synchronized String[] getAvailableServerHostKeyAlgorithms() {
        String[] defaultServerHostkeyAlgorithmList;
        synchronized (Connection.class) {
            defaultServerHostkeyAlgorithmList = KexManager.getDefaultServerHostkeyAlgorithmList();
        }
        return defaultServerHostkeyAlgorithmList;
    }

    public Connection(String hostname) {
        this(hostname, 22);
    }

    public Connection(String hostname, int port) {
        this.authenticated = false;
        this.cryptoWishList = new CryptoWishList();
        this.dhgexpara = new DHGexParameters();
        this.tcpNoDelay = false;
        this.proxyData = null;
        this.connectionMonitors = new Vector();
        this.hostname = hostname;
        this.port = port;
    }

    public synchronized boolean authenticateWithDSA(String user, String pem, String password) throws IOException {
        if (this.tm == null) {
            throw new IllegalStateException("Connection is not established!");
        }
        if (this.authenticated) {
            throw new IllegalStateException("Connection is already authenticated!");
        }
        if (this.am == null) {
            this.am = new AuthenticationManager(this.tm);
        }
        if (this.cm == null) {
            this.cm = new ChannelManager(this.tm);
        }
        if (user == null) {
            throw new IllegalArgumentException("user argument is null");
        }
        if (pem == null) {
            throw new IllegalArgumentException("pem argument is null");
        }
        this.authenticated = this.am.authenticatePublicKey(user, pem.toCharArray(), password, getOrCreateSecureRND());
        return this.authenticated;
    }

    public synchronized boolean authenticateWithKeyboardInteractive(String user, InteractiveCallback cb) throws IOException {
        return authenticateWithKeyboardInteractive(user, null, cb);
    }

    public synchronized boolean authenticateWithKeyboardInteractive(String user, String[] submethods, InteractiveCallback cb) throws IOException {
        try {
            if (cb == null) {
                throw new IllegalArgumentException("Callback may not ne NULL!");
            }
            if (this.tm == null) {
                throw new IllegalStateException("Connection is not established!");
            }
            if (this.authenticated) {
                throw new IllegalStateException("Connection is already authenticated!");
            }
            if (this.am == null) {
                this.am = new AuthenticationManager(this.tm);
            }
            if (this.cm == null) {
                this.cm = new ChannelManager(this.tm);
            }
            if (user == null) {
                throw new IllegalArgumentException("user argument is null");
            }
            this.authenticated = this.am.authenticateInteractive(user, submethods, cb);
        } catch (Throwable th) {
            throw th;
        }
        return this.authenticated;
    }

    public synchronized boolean authenticateWithPassword(String user, String password) throws IOException {
        if (this.tm == null) {
            throw new IllegalStateException("Connection is not established!");
        }
        if (this.authenticated) {
            throw new IllegalStateException("Connection is already authenticated!");
        }
        if (this.am == null) {
            this.am = new AuthenticationManager(this.tm);
        }
        if (this.cm == null) {
            this.cm = new ChannelManager(this.tm);
        }
        if (user == null) {
            throw new IllegalArgumentException("user argument is null");
        }
        if (password == null) {
            throw new IllegalArgumentException("password argument is null");
        }
        this.authenticated = this.am.authenticatePassword(user, password);
        return this.authenticated;
    }

    public synchronized boolean authenticateWithPublicKey(String user, char[] pemPrivateKey, String password) throws IOException {
        if (this.tm == null) {
            throw new IllegalStateException("Connection is not established!");
        }
        if (this.authenticated) {
            throw new IllegalStateException("Connection is already authenticated!");
        }
        if (this.am == null) {
            this.am = new AuthenticationManager(this.tm);
        }
        if (this.cm == null) {
            this.cm = new ChannelManager(this.tm);
        }
        if (user == null) {
            throw new IllegalArgumentException("user argument is null");
        }
        if (pemPrivateKey == null) {
            throw new IllegalArgumentException("pemPrivateKey argument is null");
        }
        this.authenticated = this.am.authenticatePublicKey(user, pemPrivateKey, password, getOrCreateSecureRND());
        return this.authenticated;
    }

    public synchronized boolean authenticateWithPublicKey(String user, File pemFile, String password) throws IOException {
        CharArrayWriter cw;
        try {
            if (pemFile == null) {
                throw new IllegalArgumentException("pemFile argument is null");
            }
            char[] buff = new char[256];
            cw = new CharArrayWriter();
            FileReader fr = new FileReader(pemFile);
            while (true) {
                int len = fr.read(buff);
                if (len >= 0) {
                    cw.write(buff, 0, len);
                } else {
                    fr.close();
                }
            }
        } catch (Throwable th) {
            throw th;
        }
        return authenticateWithPublicKey(user, cw.toCharArray(), password);
    }

    public synchronized void addConnectionMonitor(ConnectionMonitor cmon) {
        if (cmon == null) {
            throw new IllegalArgumentException("cmon argument is null");
        }
        this.connectionMonitors.addElement(cmon);
        if (this.tm != null) {
            this.tm.setConnectionMonitors(this.connectionMonitors);
        }
    }

    public synchronized void close() {
        Throwable t = new Throwable("Closed due to user request.");
        close(t, false);
    }

    private void close(Throwable t, boolean hard) {
        ChannelManager channelManager = this.cm;
        if (channelManager != null) {
            channelManager.closeAllChannels();
        }
        TransportManager transportManager = this.tm;
        if (transportManager != null) {
            transportManager.close(t, !hard);
            this.tm = null;
        }
        this.am = null;
        this.cm = null;
        this.authenticated = false;
    }

    public synchronized ConnectionInfo connect() throws IOException {
        return connect(null, 0, 0);
    }

    public synchronized ConnectionInfo connect(ServerHostKeyVerifier verifier) throws IOException {
        return connect(verifier, 0, 0);
    }

    public synchronized ConnectionInfo connect(ServerHostKeyVerifier verifier, int connectTimeout, int kexTimeout) throws IOException {
        ConnectionInfo ci;
        if (this.tm != null) {
            StringBuffer stringBuffer = new StringBuffer("Connection to ");
            stringBuffer.append(this.hostname);
            stringBuffer.append(" is already in connected state!");
            throw new IOException(stringBuffer.toString());
        } else if (connectTimeout < 0) {
            throw new IllegalArgumentException("connectTimeout must be non-negative!");
        } else {
            if (kexTimeout < 0) {
                throw new IllegalArgumentException("kexTimeout must be non-negative!");
            }
            final AnonymousClass1.TimeoutState state = new AnonymousClass1.TimeoutState(this);
            this.tm = new TransportManager(this.hostname, this.port);
            this.tm.setConnectionMonitors(this.connectionMonitors);
            synchronized (this.tm) {
            }
            TimeoutService.TimeoutToken token = null;
            if (kexTimeout > 0) {
                try {
                    try {
                        Runnable timeoutHandler = new Runnable() { // from class: ch.ethz.ssh2.Connection.1

                            /* JADX INFO: Access modifiers changed from: package-private */
                            /* renamed from: ch.ethz.ssh2.Connection$1$TimeoutState */
                            /* loaded from: classes.dex */
                            public final class TimeoutState {
                                final /* synthetic */ Connection this$0;
                                boolean isCancelled = false;
                                boolean timeoutSocketClosed = false;

                                TimeoutState(Connection connection) {
                                    this.this$0 = connection;
                                }
                            }

                            @Override // java.lang.Runnable
                            public void run() {
                                synchronized (state) {
                                    if (state.isCancelled) {
                                        return;
                                    }
                                    state.timeoutSocketClosed = true;
                                    Connection.this.tm.close(new SocketTimeoutException("The connect timeout expired"), false);
                                }
                            }
                        };
                        long timeoutHorizont = System.currentTimeMillis() + kexTimeout;
                        token = TimeoutService.addTimeoutHandler(timeoutHorizont, timeoutHandler);
                    } catch (IOException e1) {
                        close(new Throwable("There was a problem during connect."), false);
                        try {
                            synchronized (state) {
                                try {
                                    if (state.timeoutSocketClosed) {
                                        StringBuffer stringBuffer2 = new StringBuffer("The kexTimeout (");
                                        stringBuffer2.append(kexTimeout);
                                        stringBuffer2.append(" ms) expired.");
                                        throw new SocketTimeoutException(stringBuffer2.toString());
                                    } else if (e1 instanceof HTTPProxyException) {
                                        throw e1;
                                    } else {
                                        StringBuffer stringBuffer3 = new StringBuffer("There was a problem while connecting to ");
                                        stringBuffer3.append(this.hostname);
                                        stringBuffer3.append(":");
                                        stringBuffer3.append(this.port);
                                        throw ((IOException) new IOException(stringBuffer3.toString()).initCause(e1));
                                    }
                                } catch (Throwable th) {
                                    th = th;
                                    throw th;
                                }
                            }
                        } catch (Throwable th2) {
                            th = th2;
                        }
                    }
                } catch (SocketTimeoutException ste) {
                    throw ste;
                }
            }
            try {
                this.tm.initialize(this.cryptoWishList, verifier, this.dhgexpara, connectTimeout, getOrCreateSecureRND(), this.proxyData);
                this.tm.setTcpNoDelay(this.tcpNoDelay);
                ci = this.tm.getConnectionInfo(1);
                if (token != null) {
                    TimeoutService.cancelTimeoutHandler(token);
                    try {
                        synchronized (state) {
                            try {
                                if (state.timeoutSocketClosed) {
                                    throw new IOException("This exception will be replaced by the one below =)");
                                }
                                state.isCancelled = true;
                            } catch (Throwable th3) {
                                th = th3;
                                throw th;
                            }
                        }
                    } catch (Throwable th4) {
                        th = th4;
                    }
                }
            } catch (SocketTimeoutException se) {
                throw ((SocketTimeoutException) new SocketTimeoutException("The connect() operation on the socket timed out.").initCause(se));
            }
        }
        return ci;
    }

    public synchronized LocalPortForwarder createLocalPortForwarder(int local_port, String host_to_connect, int port_to_connect) throws IOException {
        if (this.tm == null) {
            throw new IllegalStateException("Cannot forward ports, you need to establish a connection first.");
        }
        if (!this.authenticated) {
            throw new IllegalStateException("Cannot forward ports, connection is not authenticated.");
        }
        return new LocalPortForwarder(this.cm, local_port, host_to_connect, port_to_connect);
    }

    public synchronized LocalStreamForwarder createLocalStreamForwarder(String host_to_connect, int port_to_connect) throws IOException {
        if (this.tm == null) {
            throw new IllegalStateException("Cannot forward, you need to establish a connection first.");
        }
        if (!this.authenticated) {
            throw new IllegalStateException("Cannot forward, connection is not authenticated.");
        }
        return new LocalStreamForwarder(this.cm, host_to_connect, port_to_connect);
    }

    public synchronized SCPClient createSCPClient() throws IOException {
        if (this.tm == null) {
            throw new IllegalStateException("Cannot create SCP client, you need to establish a connection first.");
        }
        if (!this.authenticated) {
            throw new IllegalStateException("Cannot create SCP client, connection is not authenticated.");
        }
        return new SCPClient(this);
    }

    public synchronized void forceKeyExchange() throws IOException {
        if (this.tm == null) {
            throw new IllegalStateException("You need to establish a connection first.");
        }
        this.tm.forceKeyExchange(this.cryptoWishList, this.dhgexpara);
    }

    public synchronized String getHostname() {
        return this.hostname;
    }

    public synchronized int getPort() {
        return this.port;
    }

    public synchronized ConnectionInfo getConnectionInfo() throws IOException {
        if (this.tm == null) {
            throw new IllegalStateException("Cannot get details of connection, you need to establish a connection first.");
        }
        return this.tm.getConnectionInfo(1);
    }

    public synchronized String[] getRemainingAuthMethods(String user) throws IOException {
        try {
            if (user == null) {
                throw new IllegalArgumentException("user argument may not be NULL!");
            }
            if (this.tm == null) {
                throw new IllegalStateException("Connection is not established!");
            }
            if (this.authenticated) {
                throw new IllegalStateException("Connection is already authenticated!");
            }
            if (this.am == null) {
                this.am = new AuthenticationManager(this.tm);
            }
            if (this.cm == null) {
                this.cm = new ChannelManager(this.tm);
            }
        } catch (Throwable th) {
            throw th;
        }
        return this.am.getRemainingMethods(user);
    }

    public synchronized boolean isAuthenticationComplete() {
        return this.authenticated;
    }

    public synchronized boolean isAuthenticationPartialSuccess() {
        if (this.am == null) {
            return false;
        }
        return this.am.getPartialSuccess();
    }

    public synchronized boolean isAuthMethodAvailable(String user, String method) throws IOException {
        if (method == null) {
            throw new IllegalArgumentException("method argument may not be NULL!");
        }
        String[] methods = getRemainingAuthMethods(user);
        for (String str : methods) {
            if (str.compareTo(method) == 0) {
                return true;
            }
        }
        return false;
    }

    private final SecureRandom getOrCreateSecureRND() {
        if (this.generator == null) {
            this.generator = new SecureRandom();
        }
        return this.generator;
    }

    public synchronized Session openSession() throws IOException {
        if (this.tm == null) {
            throw new IllegalStateException("Cannot open session, you need to establish a connection first.");
        }
        if (!this.authenticated) {
            throw new IllegalStateException("Cannot open session, connection is not authenticated.");
        }
        return new Session(this.cm, getOrCreateSecureRND());
    }

    private String[] removeDuplicates(String[] list) {
        if (list == 0 || list.length < 2) {
            return list;
        }
        String[] list2 = new String[list.length];
        int count = 0;
        for (int i = 0; i < list.length; i++) {
            boolean duplicate = false;
            String element = list[i];
            for (int j = 0; j < count; j++) {
                if ((element == null && list2[j] == null) || (element != null && element.equals(list2[j]))) {
                    duplicate = true;
                    break;
                }
            }
            if (!duplicate) {
                list2[count] = list[i];
                count++;
            }
        }
        int i2 = list2.length;
        if (count == i2) {
            return list2;
        }
        String[] tmp = new String[count];
        System.arraycopy(list2, 0, tmp, 0, count);
        return tmp;
    }

    public synchronized void setClient2ServerCiphers(String[] ciphers) {
        if (ciphers != null) {
            if (ciphers.length != 0) {
                String[] ciphers2 = removeDuplicates(ciphers);
                BlockCipherFactory.checkCipherList(ciphers2);
                this.cryptoWishList.c2s_enc_algos = ciphers2;
            }
        }
        throw new IllegalArgumentException();
    }

    public synchronized void setClient2ServerMACs(String[] macs) {
        if (macs != null) {
            if (macs.length != 0) {
                String[] macs2 = removeDuplicates(macs);
                MAC.checkMacList(macs2);
                this.cryptoWishList.c2s_mac_algos = macs2;
            }
        }
        throw new IllegalArgumentException();
    }

    public synchronized void setDHGexParameters(DHGexParameters dgp) {
        if (dgp == null) {
            throw new IllegalArgumentException();
        }
        this.dhgexpara = dgp;
    }

    public synchronized void setServer2ClientCiphers(String[] ciphers) {
        if (ciphers != null) {
            if (ciphers.length != 0) {
                String[] ciphers2 = removeDuplicates(ciphers);
                BlockCipherFactory.checkCipherList(ciphers2);
                this.cryptoWishList.s2c_enc_algos = ciphers2;
            }
        }
        throw new IllegalArgumentException();
    }

    public synchronized void setServer2ClientMACs(String[] macs) {
        if (macs != null) {
            if (macs.length != 0) {
                String[] macs2 = removeDuplicates(macs);
                MAC.checkMacList(macs2);
                this.cryptoWishList.s2c_mac_algos = macs2;
            }
        }
        throw new IllegalArgumentException();
    }

    public synchronized void setServerHostKeyAlgorithms(String[] algos) {
        if (algos != null) {
            if (algos.length != 0) {
                String[] algos2 = removeDuplicates(algos);
                KexManager.checkServerHostkeyAlgorithmsList(algos2);
                this.cryptoWishList.serverHostKeyAlgorithms = algos2;
            }
        }
        throw new IllegalArgumentException();
    }

    public synchronized void setTCPNoDelay(boolean enable) throws IOException {
        this.tcpNoDelay = enable;
        if (this.tm != null) {
            this.tm.setTcpNoDelay(enable);
        }
    }

    public synchronized void setProxyData(ProxyData proxyData) {
        this.proxyData = proxyData;
    }

    public synchronized void requestRemotePortForwarding(String bindAddress, int bindPort, String targetAddress, int targetPort) throws IOException {
        if (this.tm == null) {
            throw new IllegalStateException("You need to establish a connection first.");
        }
        if (!this.authenticated) {
            throw new IllegalStateException("The connection is not authenticated.");
        }
        if (bindAddress == null || targetAddress == null || bindPort <= 0 || targetPort <= 0) {
            throw new IllegalArgumentException();
        }
        this.cm.requestGlobalForward(bindAddress, bindPort, targetAddress, targetPort);
    }

    public synchronized void cancelRemotePortForwarding(int bindPort) throws IOException {
        if (this.tm == null) {
            throw new IllegalStateException("You need to establish a connection first.");
        }
        if (!this.authenticated) {
            throw new IllegalStateException("The connection is not authenticated.");
        }
        this.cm.requestCancelGlobalForward(bindPort);
    }

    public synchronized void setSecureRandom(SecureRandom rnd) {
        if (rnd == null) {
            throw new IllegalArgumentException();
        }
        this.generator = rnd;
    }
}
