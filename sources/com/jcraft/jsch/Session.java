package com.jcraft.jsch;

import com.jcraft.jsch.ConfigRepository;
import com.jcraft.jsch.IdentityRepository;
import com.xiaopeng.commonfunc.bean.factorytest.TestResultItem;
import com.xiaopeng.libconfig.ipc.AccountConfig;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;
import org.apache.commons.lang3.BooleanUtils;
/* loaded from: classes.dex */
public class Session implements Runnable {
    private static final int PACKET_MAX_SIZE = 262144;
    static final int SSH_MSG_CHANNEL_CLOSE = 97;
    static final int SSH_MSG_CHANNEL_DATA = 94;
    static final int SSH_MSG_CHANNEL_EOF = 96;
    static final int SSH_MSG_CHANNEL_EXTENDED_DATA = 95;
    static final int SSH_MSG_CHANNEL_FAILURE = 100;
    static final int SSH_MSG_CHANNEL_OPEN = 90;
    static final int SSH_MSG_CHANNEL_OPEN_CONFIRMATION = 91;
    static final int SSH_MSG_CHANNEL_OPEN_FAILURE = 92;
    static final int SSH_MSG_CHANNEL_REQUEST = 98;
    static final int SSH_MSG_CHANNEL_SUCCESS = 99;
    static final int SSH_MSG_CHANNEL_WINDOW_ADJUST = 93;
    static final int SSH_MSG_DEBUG = 4;
    static final int SSH_MSG_DISCONNECT = 1;
    static final int SSH_MSG_GLOBAL_REQUEST = 80;
    static final int SSH_MSG_IGNORE = 2;
    static final int SSH_MSG_KEXDH_INIT = 30;
    static final int SSH_MSG_KEXDH_REPLY = 31;
    static final int SSH_MSG_KEXINIT = 20;
    static final int SSH_MSG_KEX_DH_GEX_GROUP = 31;
    static final int SSH_MSG_KEX_DH_GEX_INIT = 32;
    static final int SSH_MSG_KEX_DH_GEX_REPLY = 33;
    static final int SSH_MSG_KEX_DH_GEX_REQUEST = 34;
    static final int SSH_MSG_NEWKEYS = 21;
    static final int SSH_MSG_REQUEST_FAILURE = 82;
    static final int SSH_MSG_REQUEST_SUCCESS = 81;
    static final int SSH_MSG_SERVICE_ACCEPT = 6;
    static final int SSH_MSG_SERVICE_REQUEST = 5;
    static final int SSH_MSG_UNIMPLEMENTED = 3;
    static final int buffer_margin = 128;
    private static final byte[] keepalivemsg = Util.str2byte("keepalive@jcraft.com");
    private static final byte[] nomoresessions = Util.str2byte("no-more-sessions@openssh.com");
    static Random random;
    private byte[] Ec2s;
    private byte[] Es2c;
    private byte[] IVc2s;
    private byte[] IVs2c;
    private byte[] I_C;
    private byte[] I_S;
    private byte[] K_S;
    private byte[] MACc2s;
    private byte[] MACs2c;
    private byte[] V_S;
    private Cipher c2scipher;
    private MAC c2smac;
    private Compression deflater;
    String host;
    private Compression inflater;

    /* renamed from: io  reason: collision with root package name */
    private IO f212io;
    JSch jsch;
    String org_host;
    int port;
    private Cipher s2ccipher;
    private MAC s2cmac;
    private byte[] s2cmac_result1;
    private byte[] s2cmac_result2;
    private byte[] session_id;
    private Socket socket;
    Runnable thread;
    private UserInfo userinfo;
    String username;
    private byte[] V_C = Util.str2byte("SSH-2.0-JSCH-0.1.54");
    private int seqi = 0;
    private int seqo = 0;
    String[] guess = null;
    private int timeout = 0;
    private volatile boolean isConnected = false;
    private boolean isAuthed = false;
    private Thread connectThread = null;
    private Object lock = new Object();
    boolean x11_forwarding = false;
    boolean agent_forwarding = false;
    InputStream in = null;
    OutputStream out = null;
    SocketFactory socket_factory = null;
    private Hashtable config = null;
    private Proxy proxy = null;
    private String hostKeyAlias = null;
    private int serverAliveInterval = 0;
    private int serverAliveCountMax = 1;
    private IdentityRepository identityRepository = null;
    private HostKeyRepository hostkeyRepository = null;
    protected boolean daemon_thread = false;
    private long kex_start_time = 0;
    int max_auth_tries = 6;
    int auth_failures = 0;
    byte[] password = null;
    private volatile boolean in_kex = false;
    private volatile boolean in_prompt = false;
    int[] uncompress_len = new int[1];
    int[] compress_len = new int[1];
    private int s2ccipher_size = 8;
    private int c2scipher_size = 8;
    private GlobalRequestReply grr = new GlobalRequestReply();
    private HostKey hostkey = null;
    Buffer buf = new Buffer();
    Packet packet = new Packet(this.buf);

    /* JADX INFO: Access modifiers changed from: package-private */
    public Session(JSch jsch, String username, String host, int port) throws JSchException {
        this.host = "127.0.0.1";
        this.org_host = "127.0.0.1";
        this.port = 22;
        this.username = null;
        this.jsch = jsch;
        this.username = username;
        this.host = host;
        this.org_host = host;
        this.port = port;
        applyConfig();
        if (this.username == null) {
            try {
                this.username = (String) System.getProperties().get("user.name");
            } catch (SecurityException e) {
            }
        }
        if (this.username == null) {
            throw new JSchException("username is not given.");
        }
    }

    public void connect() throws JSchException {
        connect(this.timeout);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r12v27, types: [com.jcraft.jsch.Logger] */
    /* JADX WARN: Type inference failed for: r12v42, types: [com.jcraft.jsch.Logger] */
    /* JADX WARN: Type inference failed for: r4v24, types: [boolean, int] */
    /* JADX WARN: Type inference failed for: r4v72 */
    /* JADX WARN: Type inference failed for: r4v81 */
    /* JADX WARN: Type inference failed for: r8v39 */
    /* JADX WARN: Type inference failed for: r8v7, types: [int] */
    public void connect(int connectTimeout) throws JSchException {
        int i;
        String smethods;
        String[] smethoda;
        int methodi;
        UserAuth ua;
        int methodi2;
        InputStream in;
        OutputStream out;
        if (!this.isConnected) {
            this.f212io = new IO();
            if (random == null) {
                try {
                    Class c = Class.forName(getConfig("random"));
                    random = (Random) c.newInstance();
                } catch (Exception e) {
                    throw new JSchException(e.toString(), e);
                }
            }
            Packet.setRandom(random);
            boolean z = true;
            if (JSch.getLogger().isEnabled(1)) {
                JSch.getLogger().log(1, "Connecting to " + this.host + " port " + this.port);
            }
            byte b = 13;
            int i2 = 3;
            char c2 = 2;
            boolean z2 = false;
            try {
                try {
                    if (this.proxy == null) {
                        if (this.socket_factory == null) {
                            this.socket = Util.createSocket(this.host, this.port, connectTimeout);
                            in = this.socket.getInputStream();
                            out = this.socket.getOutputStream();
                        } else {
                            this.socket = this.socket_factory.createSocket(this.host, this.port);
                            in = this.socket_factory.getInputStream(this.socket);
                            out = this.socket_factory.getOutputStream(this.socket);
                        }
                        this.socket.setTcpNoDelay(true);
                        this.f212io.setInputStream(in);
                        this.f212io.setOutputStream(out);
                    } else {
                        synchronized (this.proxy) {
                            this.proxy.connect(this.socket_factory, this.host, this.port, connectTimeout);
                            this.f212io.setInputStream(this.proxy.getInputStream());
                            this.f212io.setOutputStream(this.proxy.getOutputStream());
                            this.socket = this.proxy.getSocket();
                        }
                    }
                    if (connectTimeout > 0 && this.socket != null) {
                        this.socket.setSoTimeout(connectTimeout);
                    }
                    this.isConnected = true;
                    if (JSch.getLogger().isEnabled(1)) {
                        JSch.getLogger().log(1, "Connection established");
                    }
                    this.jsch.addSession(this);
                    byte[] foo = new byte[this.V_C.length + 1];
                    System.arraycopy(this.V_C, 0, foo, 0, this.V_C.length);
                    foo[foo.length - 1] = 10;
                    this.f212io.put(foo, 0, foo.length);
                    while (true) {
                        i = 0;
                        int j = z2 ? 1 : 0;
                        while (i < this.buf.buffer.length && (j = this.f212io.getByte()) >= 0) {
                            this.buf.buffer[i] = (byte) j;
                            i++;
                            if (j == 10) {
                                break;
                            }
                        }
                        if (j < 0) {
                            throw new JSchException("connection is closed by foreign host");
                        }
                        if (this.buf.buffer[i - 1] == 10 && i - 1 > 0 && this.buf.buffer[i - 1] == b) {
                            i--;
                        }
                        if (i > i2) {
                            if (i == this.buf.buffer.length || (this.buf.buffer[z2 ? 1 : 0] == 83 && this.buf.buffer[z ? 1 : 0] == 83 && this.buf.buffer[c2] == 72 && this.buf.buffer[i2] == 45)) {
                                break;
                            }
                        } else {
                            b = 13;
                            z = true;
                            i2 = 3;
                            c2 = 2;
                            z2 = false;
                        }
                    }
                    if (i != this.buf.buffer.length && i >= 7 && (this.buf.buffer[4] != 49 || this.buf.buffer[6] == 57)) {
                        this.V_S = new byte[i];
                        System.arraycopy(this.buf.buffer, z2 ? 1 : 0, this.V_S, z2 ? 1 : 0, i);
                        if (JSch.getLogger().isEnabled(z ? 1 : 0)) {
                            JSch.getLogger().log(z ? 1 : 0, "Remote version string: " + Util.byte2str(this.V_S));
                            JSch.getLogger().log(z ? 1 : 0, "Local version string: " + Util.byte2str(this.V_C));
                        }
                        send_kexinit();
                        this.buf = read(this.buf);
                        if (this.buf.getCommand() != 20) {
                            this.in_kex = false;
                            throw new JSchException("invalid protocol: " + ((int) this.buf.getCommand()));
                        }
                        if (JSch.getLogger().isEnabled(z ? 1 : 0)) {
                            JSch.getLogger().log(z ? 1 : 0, "SSH_MSG_KEXINIT received");
                        }
                        KeyExchange kex = receive_kexinit(this.buf);
                        ?? r4 = z;
                        boolean z3 = z2;
                        while (true) {
                            this.buf = read(this.buf);
                            if (kex.getState() == this.buf.getCommand()) {
                                this.kex_start_time = System.currentTimeMillis();
                                boolean result = kex.next(this.buf);
                                if (!result) {
                                    this.in_kex = false;
                                    throw new JSchException("verify: " + result);
                                } else if (kex.getState() == 0) {
                                    try {
                                        long tmp = System.currentTimeMillis();
                                        this.in_prompt = r4;
                                        checkHost(this.host, this.port, kex);
                                        this.in_prompt = z3;
                                        this.kex_start_time += System.currentTimeMillis() - tmp;
                                        send_newkeys();
                                        this.buf = read(this.buf);
                                        if (this.buf.getCommand() != 21) {
                                            this.in_kex = false;
                                            throw new JSchException("invalid protocol(newkyes): " + ((int) this.buf.getCommand()));
                                        }
                                        if (JSch.getLogger().isEnabled(r4)) {
                                            JSch.getLogger().log(r4, "SSH_MSG_NEWKEYS received");
                                        }
                                        receive_newkeys(this.buf, kex);
                                        try {
                                            String s = getConfig("MaxAuthTries");
                                            if (s != null) {
                                                this.max_auth_tries = Integer.parseInt(s);
                                            }
                                            try {
                                                Class c3 = Class.forName(getConfig("userauth.none"));
                                                UserAuth ua2 = (UserAuth) c3.newInstance();
                                                boolean auth = ua2.start(this);
                                                String cmethods = getConfig("PreferredAuthentications");
                                                String[] cmethoda = Util.split(cmethods, ",");
                                                if (auth) {
                                                    smethods = null;
                                                } else {
                                                    String smethods2 = ((UserAuthNone) ua2).getMethods();
                                                    if (smethods2 != null) {
                                                        smethods = smethods2.toLowerCase();
                                                    } else {
                                                        smethods = cmethods;
                                                    }
                                                }
                                                String[] smethoda2 = Util.split(smethods, ",");
                                                String smethods3 = smethods;
                                                int methodi3 = 0;
                                                boolean auth_cancel = false;
                                                while (!auth && cmethoda != null && methodi3 < cmethoda.length) {
                                                    int methodi4 = methodi3 + 1;
                                                    String method = cmethoda[methodi3];
                                                    boolean acceptable = false;
                                                    int k = z3;
                                                    while (true) {
                                                        if (k >= smethoda2.length) {
                                                            break;
                                                        } else if (!smethoda2[k].equals(method)) {
                                                            k++;
                                                        } else {
                                                            acceptable = true;
                                                            break;
                                                        }
                                                    }
                                                    if (!acceptable) {
                                                        methodi3 = methodi4;
                                                        z3 = false;
                                                    } else {
                                                        if (!JSch.getLogger().isEnabled(1)) {
                                                            smethoda = smethoda2;
                                                            methodi = methodi4;
                                                        } else {
                                                            String str = "Authentications that can continue: ";
                                                            int k2 = methodi4 - 1;
                                                            while (true) {
                                                                smethoda = smethoda2;
                                                                if (k2 >= cmethoda.length) {
                                                                    break;
                                                                }
                                                                StringBuilder sb = new StringBuilder();
                                                                sb.append(str);
                                                                int methodi5 = methodi4;
                                                                sb.append(cmethoda[k2]);
                                                                str = sb.toString();
                                                                if (k2 + 1 < cmethoda.length) {
                                                                    str = str + ",";
                                                                }
                                                                k2++;
                                                                smethoda2 = smethoda;
                                                                methodi4 = methodi5;
                                                            }
                                                            methodi = methodi4;
                                                            JSch.getLogger().log(1, str);
                                                            JSch.getLogger().log(1, "Next authentication method: " + method);
                                                        }
                                                        UserAuth ua3 = null;
                                                        try {
                                                            if (getConfig("userauth." + method) != null) {
                                                                Class c4 = Class.forName(getConfig("userauth." + method));
                                                                ua3 = (UserAuth) c4.newInstance();
                                                            }
                                                            ua = ua3;
                                                        } catch (Exception e2) {
                                                            if (JSch.getLogger().isEnabled(2)) {
                                                                JSch.getLogger().log(2, "failed to load " + method + " method");
                                                            }
                                                            ua = null;
                                                        }
                                                        if (ua != null) {
                                                            auth_cancel = false;
                                                            try {
                                                                auth = ua.start(this);
                                                                if (auth && JSch.getLogger().isEnabled(1)) {
                                                                    JSch.getLogger().log(1, "Authentication succeeded (" + method + ").");
                                                                }
                                                            } catch (JSchAuthCancelException e3) {
                                                                auth_cancel = true;
                                                            } catch (JSchPartialAuthException ee) {
                                                                String tmp2 = smethods3;
                                                                smethods3 = ee.getMethods();
                                                                String[] smethoda3 = Util.split(smethods3, ",");
                                                                if (tmp2.equals(smethods3)) {
                                                                    methodi2 = methodi;
                                                                } else {
                                                                    methodi2 = 0;
                                                                }
                                                                auth_cancel = false;
                                                                smethoda2 = smethoda3;
                                                                methodi3 = methodi2;
                                                                z3 = false;
                                                            } catch (JSchException ee2) {
                                                                throw ee2;
                                                            } catch (RuntimeException ee3) {
                                                                throw ee3;
                                                            } catch (Exception ee4) {
                                                                if (JSch.getLogger().isEnabled(2)) {
                                                                    JSch.getLogger().log(2, "an exception during authentication\n" + ee4.toString());
                                                                }
                                                            }
                                                        }
                                                        smethoda2 = smethoda;
                                                        methodi3 = methodi;
                                                        z3 = false;
                                                    }
                                                }
                                                if (!auth) {
                                                    if (this.auth_failures >= this.max_auth_tries && JSch.getLogger().isEnabled(1)) {
                                                        JSch.getLogger().log(1, "Login trials exceeds " + this.max_auth_tries);
                                                    }
                                                    if (auth_cancel) {
                                                        throw new JSchException("Auth cancel");
                                                    }
                                                    throw new JSchException("Auth fail");
                                                }
                                                if (this.socket != null && (connectTimeout > 0 || this.timeout > 0)) {
                                                    this.socket.setSoTimeout(this.timeout);
                                                }
                                                this.isAuthed = true;
                                                synchronized (this.lock) {
                                                    if (this.isConnected) {
                                                        this.connectThread = new Thread(this);
                                                        this.connectThread.setName("Connect thread " + this.host + " session");
                                                        if (this.daemon_thread) {
                                                            this.connectThread.setDaemon(this.daemon_thread);
                                                        }
                                                        this.connectThread.start();
                                                        requestPortForwarding();
                                                    }
                                                }
                                                Util.bzero(this.password);
                                                this.password = null;
                                                return;
                                            } catch (Exception e4) {
                                                throw new JSchException(e4.toString(), e4);
                                            }
                                        } catch (NumberFormatException e5) {
                                            throw new JSchException("MaxAuthTries: " + getConfig("MaxAuthTries"), e5);
                                        }
                                    } catch (JSchException ee5) {
                                        this.in_kex = false;
                                        this.in_prompt = false;
                                        throw ee5;
                                    }
                                } else {
                                    r4 = 1;
                                    z3 = false;
                                }
                            } else {
                                this.in_kex = false;
                                throw new JSchException("invalid protocol(kex): " + ((int) this.buf.getCommand()));
                            }
                        }
                    } else {
                        throw new JSchException("invalid server's version string");
                    }
                } catch (Exception e6) {
                    this.in_kex = false;
                    try {
                        if (this.isConnected) {
                            String message = e6.toString();
                            this.packet.reset();
                            this.buf.checkFreeSize(message.length() + 13 + 2 + 128);
                            this.buf.putByte((byte) 1);
                            this.buf.putInt(3);
                            this.buf.putString(Util.str2byte(message));
                            this.buf.putString(Util.str2byte("en"));
                            write(this.packet);
                        }
                    } catch (Exception e7) {
                    }
                    try {
                        disconnect();
                    } catch (Exception e8) {
                    }
                    this.isConnected = false;
                    if (e6 instanceof RuntimeException) {
                        throw ((RuntimeException) e6);
                    }
                    if (e6 instanceof JSchException) {
                        throw ((JSchException) e6);
                    }
                    throw new JSchException("Session.connect: " + e6);
                }
            } catch (Throwable th) {
                Util.bzero(this.password);
                this.password = null;
                throw th;
            }
        } else {
            throw new JSchException("session is already connected");
        }
    }

    private KeyExchange receive_kexinit(Buffer buf) throws Exception {
        int j = buf.getInt();
        if (j != buf.getLength()) {
            buf.getByte();
            this.I_S = new byte[buf.index - 5];
        } else {
            this.I_S = new byte[(j - 1) - buf.getByte()];
        }
        byte[] bArr = buf.buffer;
        int i = buf.s;
        byte[] bArr2 = this.I_S;
        System.arraycopy(bArr, i, bArr2, 0, bArr2.length);
        if (!this.in_kex) {
            send_kexinit();
        }
        this.guess = KeyExchange.guess(this.I_S, this.I_C);
        String[] strArr = this.guess;
        if (strArr == null) {
            throw new JSchException("Algorithm negotiation fail");
        }
        if (!this.isAuthed && (strArr[2].equals(AccountConfig.FaceIDRegisterAction.ORIENTATION_NONE) || this.guess[3].equals(AccountConfig.FaceIDRegisterAction.ORIENTATION_NONE))) {
            throw new JSchException("NONE Cipher should not be chosen before authentification is successed.");
        }
        try {
            Class c = Class.forName(getConfig(this.guess[0]));
            KeyExchange kex = (KeyExchange) c.newInstance();
            kex.init(this, this.V_S, this.V_C, this.I_S, this.I_C);
            return kex;
        } catch (Exception e) {
            throw new JSchException(e.toString(), e);
        }
    }

    public void rekey() throws Exception {
        send_kexinit();
    }

    private void send_kexinit() throws Exception {
        if (this.in_kex) {
            return;
        }
        String cipherc2s = getConfig("cipher.c2s");
        String ciphers2c = getConfig("cipher.s2c");
        String[] not_available_ciphers = checkCiphers(getConfig("CheckCiphers"));
        if (not_available_ciphers != null && not_available_ciphers.length > 0) {
            cipherc2s = Util.diffString(cipherc2s, not_available_ciphers);
            ciphers2c = Util.diffString(ciphers2c, not_available_ciphers);
            if (cipherc2s == null || ciphers2c == null) {
                throw new JSchException("There are not any available ciphers.");
            }
        }
        String kex = getConfig("kex");
        String[] not_available_kexes = checkKexes(getConfig("CheckKexes"));
        if (not_available_kexes != null && not_available_kexes.length > 0 && (kex = Util.diffString(kex, not_available_kexes)) == null) {
            throw new JSchException("There are not any available kexes.");
        }
        String server_host_key = getConfig("server_host_key");
        String[] not_available_shks = checkSignatures(getConfig("CheckSignatures"));
        if (not_available_shks != null && not_available_shks.length > 0 && (server_host_key = Util.diffString(server_host_key, not_available_shks)) == null) {
            throw new JSchException("There are not any available sig algorithm.");
        }
        this.in_kex = true;
        this.kex_start_time = System.currentTimeMillis();
        Buffer buf = new Buffer();
        Packet packet = new Packet(buf);
        packet.reset();
        buf.putByte((byte) 20);
        synchronized (random) {
            random.fill(buf.buffer, buf.index, 16);
            buf.skip(16);
        }
        buf.putString(Util.str2byte(kex));
        buf.putString(Util.str2byte(server_host_key));
        buf.putString(Util.str2byte(cipherc2s));
        buf.putString(Util.str2byte(ciphers2c));
        buf.putString(Util.str2byte(getConfig("mac.c2s")));
        buf.putString(Util.str2byte(getConfig("mac.s2c")));
        buf.putString(Util.str2byte(getConfig("compression.c2s")));
        buf.putString(Util.str2byte(getConfig("compression.s2c")));
        buf.putString(Util.str2byte(getConfig("lang.c2s")));
        buf.putString(Util.str2byte(getConfig("lang.s2c")));
        buf.putByte((byte) 0);
        buf.putInt(0);
        buf.setOffSet(5);
        this.I_C = new byte[buf.getLength()];
        buf.getByte(this.I_C);
        write(packet);
        if (JSch.getLogger().isEnabled(1)) {
            JSch.getLogger().log(1, "SSH_MSG_KEXINIT sent");
        }
    }

    private void send_newkeys() throws Exception {
        this.packet.reset();
        this.buf.putByte((byte) 21);
        write(this.packet);
        if (JSch.getLogger().isEnabled(1)) {
            JSch.getLogger().log(1, "SSH_MSG_NEWKEYS sent");
        }
    }

    private void checkHost(String chost, int port, KeyExchange kex) throws JSchException {
        String chost2;
        int i;
        String file;
        boolean b;
        String shkc = getConfig("StrictHostKeyChecking");
        if (this.hostKeyAlias == null) {
            chost2 = chost;
        } else {
            chost2 = this.hostKeyAlias;
        }
        byte[] K_S = kex.getHostKey();
        String key_type = kex.getKeyType();
        String key_fprint = kex.getFingerPrint();
        String chost3 = (this.hostKeyAlias == null && port != 22) ? "[" + chost2 + "]:" + port : chost2;
        HostKeyRepository hkr = getHostKeyRepository();
        String hkh = getConfig("HashKnownHosts");
        if (!hkh.equals(BooleanUtils.YES) || !(hkr instanceof KnownHosts)) {
            this.hostkey = new HostKey(chost3, K_S);
        } else {
            this.hostkey = ((KnownHosts) hkr).createHashedHostKey(chost3, K_S);
        }
        synchronized (hkr) {
            i = hkr.check(chost3, K_S);
        }
        boolean insert = false;
        if ((shkc.equals("ask") || shkc.equals(BooleanUtils.YES)) && i == 2) {
            synchronized (hkr) {
                file = hkr.getKnownHostsRepositoryID();
            }
            if (file == null) {
                file = "known_hosts";
            }
            if (this.userinfo == null) {
                b = false;
            } else {
                String message = "WARNING: REMOTE HOST IDENTIFICATION HAS CHANGED!\nIT IS POSSIBLE THAT SOMEONE IS DOING SOMETHING NASTY!\nSomeone could be eavesdropping on you right now (man-in-the-middle attack)!\nIt is also possible that the " + key_type + " host key has just been changed.\nThe fingerprint for the " + key_type + " key sent by the remote host " + chost3 + " is\n" + key_fprint + ".\nPlease contact your system administrator.\nAdd correct host key in " + file + " to get rid of this message.";
                if (shkc.equals("ask")) {
                    b = this.userinfo.promptYesNo(message + "\nDo you want to delete the old key and insert the new key?");
                } else {
                    b = false;
                    this.userinfo.showMessage(message);
                }
            }
            if (!b) {
                throw new JSchException("HostKey has been changed: " + chost3);
            }
            synchronized (hkr) {
                hkr.remove(chost3, kex.getKeyAlgorithName(), null);
                insert = true;
            }
        }
        if ((shkc.equals("ask") || shkc.equals(BooleanUtils.YES)) && i != 0 && !insert) {
            if (shkc.equals(BooleanUtils.YES)) {
                throw new JSchException("reject HostKey: " + this.host);
            }
            UserInfo userInfo = this.userinfo;
            if (userInfo != null) {
                boolean foo = userInfo.promptYesNo("The authenticity of host '" + this.host + "' can't be established.\n" + key_type + " key fingerprint is " + key_fprint + ".\nAre you sure you want to continue connecting?");
                if (!foo) {
                    throw new JSchException("reject HostKey: " + this.host);
                }
                insert = true;
            } else if (i == 1) {
                throw new JSchException("UnknownHostKey: " + this.host + ". " + key_type + " key fingerprint is " + key_fprint);
            } else {
                throw new JSchException("HostKey has been changed: " + this.host);
            }
        }
        if (shkc.equals(BooleanUtils.NO) && 1 == i) {
            insert = true;
        }
        if (i == 0) {
            String _key = Util.byte2str(Util.toBase64(K_S, 0, K_S.length));
            int j = 0;
            for (HostKey[] keys = hkr.getHostKey(chost3, kex.getKeyAlgorithName()); j < keys.length; keys = keys) {
                if (!keys[i].getKey().equals(_key) || !keys[j].getMarker().equals("@revoked")) {
                    j++;
                } else {
                    UserInfo userInfo2 = this.userinfo;
                    if (userInfo2 != null) {
                        userInfo2.showMessage("The " + key_type + " host key for " + this.host + " is marked as revoked.\nThis could mean that a stolen key is being used to impersonate this host.");
                    }
                    if (JSch.getLogger().isEnabled(1)) {
                        JSch.getLogger().log(1, "Host '" + this.host + "' has provided revoked key.");
                    }
                    throw new JSchException("revoked HostKey: " + this.host);
                }
            }
        }
        if (i == 0 && JSch.getLogger().isEnabled(1)) {
            JSch.getLogger().log(1, "Host '" + this.host + "' is known and matches the " + key_type + " host key");
        }
        if (insert && JSch.getLogger().isEnabled(2)) {
            JSch.getLogger().log(2, "Permanently added '" + this.host + "' (" + key_type + ") to the list of known hosts.");
        }
        if (insert) {
            synchronized (hkr) {
                hkr.add(this.hostkey, this.userinfo);
            }
        }
    }

    public Channel openChannel(String type) throws JSchException {
        if (!this.isConnected) {
            throw new JSchException("session is down");
        }
        try {
            Channel channel = Channel.getChannel(type);
            addChannel(channel);
            channel.init();
            if (channel instanceof ChannelSession) {
                applyConfigChannel((ChannelSession) channel);
            }
            return channel;
        } catch (Exception e) {
            return null;
        }
    }

    public void encode(Packet packet) throws Exception {
        if (this.deflater != null) {
            this.compress_len[0] = packet.buffer.index;
            packet.buffer.buffer = this.deflater.compress(packet.buffer.buffer, 5, this.compress_len);
            packet.buffer.index = this.compress_len[0];
        }
        if (this.c2scipher != null) {
            packet.padding(this.c2scipher_size);
            int pad = packet.buffer.buffer[4];
            synchronized (random) {
                random.fill(packet.buffer.buffer, packet.buffer.index - pad, pad);
            }
        } else {
            packet.padding(8);
        }
        MAC mac = this.c2smac;
        if (mac != null) {
            mac.update(this.seqo);
            this.c2smac.update(packet.buffer.buffer, 0, packet.buffer.index);
            this.c2smac.doFinal(packet.buffer.buffer, packet.buffer.index);
        }
        if (this.c2scipher != null) {
            byte[] buf = packet.buffer.buffer;
            this.c2scipher.update(buf, 0, packet.buffer.index, buf, 0);
        }
        if (this.c2smac != null) {
            packet.buffer.skip(this.c2smac.getBlockSize());
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:70:0x01fd, code lost:
        r22.rewind();
     */
    /* JADX WARN: Code restructure failed: missing block: B:71:0x0200, code lost:
        return r22;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public com.jcraft.jsch.Buffer read(com.jcraft.jsch.Buffer r22) throws java.lang.Exception {
        /*
            Method dump skipped, instructions count: 579
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.jcraft.jsch.Session.read(com.jcraft.jsch.Buffer):com.jcraft.jsch.Buffer");
    }

    private void start_discard(Buffer buf, Cipher cipher, MAC mac, int packet_length, int discard) throws JSchException, IOException {
        MAC discard_mac = null;
        if (!cipher.isCBC()) {
            throw new JSchException("Packet corrupt");
        }
        if (packet_length != 262144 && mac != null) {
            discard_mac = mac;
        }
        int discard2 = discard - buf.index;
        while (discard2 > 0) {
            buf.reset();
            int len = discard2 > buf.buffer.length ? buf.buffer.length : discard2;
            this.f212io.getByte(buf.buffer, 0, len);
            if (discard_mac != null) {
                discard_mac.update(buf.buffer, 0, len);
            }
            discard2 -= len;
        }
        if (discard_mac != null) {
            discard_mac.doFinal(buf.buffer, 0);
        }
        throw new JSchException("Packet corrupt");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public byte[] getSessionId() {
        return this.session_id;
    }

    private void receive_newkeys(Buffer buf, KeyExchange kex) throws Exception {
        updateKeys(kex);
        this.in_kex = false;
    }

    private void updateKeys(KeyExchange kex) throws Exception {
        byte[] K = kex.getK();
        byte[] H = kex.getH();
        HASH hash = kex.getHash();
        if (this.session_id == null) {
            this.session_id = new byte[H.length];
            System.arraycopy(H, 0, this.session_id, 0, H.length);
        }
        this.buf.reset();
        this.buf.putMPInt(K);
        this.buf.putByte(H);
        this.buf.putByte((byte) 65);
        this.buf.putByte(this.session_id);
        hash.update(this.buf.buffer, 0, this.buf.index);
        this.IVc2s = hash.digest();
        int j = (this.buf.index - this.session_id.length) - 1;
        byte[] bArr = this.buf.buffer;
        bArr[j] = (byte) (bArr[j] + 1);
        hash.update(this.buf.buffer, 0, this.buf.index);
        this.IVs2c = hash.digest();
        byte[] bArr2 = this.buf.buffer;
        bArr2[j] = (byte) (bArr2[j] + 1);
        hash.update(this.buf.buffer, 0, this.buf.index);
        this.Ec2s = hash.digest();
        byte[] bArr3 = this.buf.buffer;
        bArr3[j] = (byte) (bArr3[j] + 1);
        hash.update(this.buf.buffer, 0, this.buf.index);
        this.Es2c = hash.digest();
        byte[] bArr4 = this.buf.buffer;
        bArr4[j] = (byte) (bArr4[j] + 1);
        hash.update(this.buf.buffer, 0, this.buf.index);
        this.MACc2s = hash.digest();
        byte[] bArr5 = this.buf.buffer;
        bArr5[j] = (byte) (bArr5[j] + 1);
        hash.update(this.buf.buffer, 0, this.buf.index);
        this.MACs2c = hash.digest();
        try {
            String method = this.guess[3];
            Class c = Class.forName(getConfig(method));
            this.s2ccipher = (Cipher) c.newInstance();
            while (this.s2ccipher.getBlockSize() > this.Es2c.length) {
                this.buf.reset();
                this.buf.putMPInt(K);
                this.buf.putByte(H);
                this.buf.putByte(this.Es2c);
                hash.update(this.buf.buffer, 0, this.buf.index);
                byte[] foo = hash.digest();
                byte[] bar = new byte[this.Es2c.length + foo.length];
                System.arraycopy(this.Es2c, 0, bar, 0, this.Es2c.length);
                System.arraycopy(foo, 0, bar, this.Es2c.length, foo.length);
                this.Es2c = bar;
            }
            this.s2ccipher.init(1, this.Es2c, this.IVs2c);
            this.s2ccipher_size = this.s2ccipher.getIVSize();
            String method2 = this.guess[5];
            Class c2 = Class.forName(getConfig(method2));
            this.s2cmac = (MAC) c2.newInstance();
            this.MACs2c = expandKey(this.buf, K, H, this.MACs2c, hash, this.s2cmac.getBlockSize());
            this.s2cmac.init(this.MACs2c);
            this.s2cmac_result1 = new byte[this.s2cmac.getBlockSize()];
            this.s2cmac_result2 = new byte[this.s2cmac.getBlockSize()];
            String method3 = this.guess[2];
            Class c3 = Class.forName(getConfig(method3));
            this.c2scipher = (Cipher) c3.newInstance();
            while (this.c2scipher.getBlockSize() > this.Ec2s.length) {
                this.buf.reset();
                this.buf.putMPInt(K);
                this.buf.putByte(H);
                this.buf.putByte(this.Ec2s);
                hash.update(this.buf.buffer, 0, this.buf.index);
                byte[] foo2 = hash.digest();
                byte[] bar2 = new byte[this.Ec2s.length + foo2.length];
                System.arraycopy(this.Ec2s, 0, bar2, 0, this.Ec2s.length);
                System.arraycopy(foo2, 0, bar2, this.Ec2s.length, foo2.length);
                this.Ec2s = bar2;
            }
            this.c2scipher.init(0, this.Ec2s, this.IVc2s);
            this.c2scipher_size = this.c2scipher.getIVSize();
            String method4 = this.guess[4];
            Class c4 = Class.forName(getConfig(method4));
            this.c2smac = (MAC) c4.newInstance();
            this.MACc2s = expandKey(this.buf, K, H, this.MACc2s, hash, this.c2smac.getBlockSize());
            this.c2smac.init(this.MACc2s);
            String method5 = this.guess[6];
            initDeflater(method5);
            String method6 = this.guess[7];
            initInflater(method6);
        } catch (Exception e) {
            if (e instanceof JSchException) {
                throw e;
            }
            throw new JSchException(e.toString(), e);
        }
    }

    private byte[] expandKey(Buffer buf, byte[] K, byte[] H, byte[] key, HASH hash, int required_length) throws Exception {
        byte[] result = key;
        int size = hash.getBlockSize();
        while (result.length < required_length) {
            buf.reset();
            buf.putMPInt(K);
            buf.putByte(H);
            buf.putByte(result);
            hash.update(buf.buffer, 0, buf.index);
            byte[] tmp = new byte[result.length + size];
            System.arraycopy(result, 0, tmp, 0, result.length);
            System.arraycopy(hash.digest(), 0, tmp, result.length, size);
            Util.bzero(result);
            result = tmp;
        }
        return result;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x005d, code lost:
        r13.rwsize -= r14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x0069, code lost:
        if (r13.close != false) goto L79;
     */
    /* JADX WARN: Code restructure failed: missing block: B:42:0x006f, code lost:
        if (r13.isConnected() == false) goto L76;
     */
    /* JADX WARN: Code restructure failed: missing block: B:43:0x0071, code lost:
        r2 = false;
        r5 = 0;
        r6 = 0;
        r7 = -1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:44:0x0075, code lost:
        monitor-enter(r13);
     */
    /* JADX WARN: Code restructure failed: missing block: B:46:0x007a, code lost:
        if (r13.rwsize <= 0) goto L40;
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x007c, code lost:
        r3 = r13.rwsize;
     */
    /* JADX WARN: Code restructure failed: missing block: B:48:0x0081, code lost:
        if (r3 <= r14) goto L28;
     */
    /* JADX WARN: Code restructure failed: missing block: B:49:0x0083, code lost:
        r3 = r14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x0087, code lost:
        if (r3 == r14) goto L39;
     */
    /* JADX WARN: Code restructure failed: missing block: B:52:0x0089, code lost:
        r8 = (int) r3;
     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x008c, code lost:
        if (r11.c2scipher == null) goto L38;
     */
    /* JADX WARN: Code restructure failed: missing block: B:54:0x008e, code lost:
        r9 = r11.c2scipher_size;
     */
    /* JADX WARN: Code restructure failed: missing block: B:55:0x0091, code lost:
        r9 = 8;
     */
    /* JADX WARN: Code restructure failed: missing block: B:57:0x0095, code lost:
        if (r11.c2smac == null) goto L37;
     */
    /* JADX WARN: Code restructure failed: missing block: B:58:0x0097, code lost:
        r10 = r11.c2smac.getBlockSize();
     */
    /* JADX WARN: Code restructure failed: missing block: B:59:0x009e, code lost:
        r10 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:60:0x009f, code lost:
        r5 = r12.shift(r8, r9, r10);
     */
    /* JADX WARN: Code restructure failed: missing block: B:61:0x00a4, code lost:
        r6 = r12.buffer.getCommand();
        r7 = r13.getRecipient();
        r14 = (int) (r14 - r3);
        r13.rwsize -= r3;
        r2 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:62:0x00b9, code lost:
        monitor-exit(r13);
     */
    /* JADX WARN: Code restructure failed: missing block: B:63:0x00ba, code lost:
        if (r2 == false) goto L48;
     */
    /* JADX WARN: Code restructure failed: missing block: B:64:0x00bc, code lost:
        _write(r12);
     */
    /* JADX WARN: Code restructure failed: missing block: B:65:0x00bf, code lost:
        if (r14 != 0) goto L44;
     */
    /* JADX WARN: Code restructure failed: missing block: B:66:0x00c1, code lost:
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:67:0x00c2, code lost:
        r12.unshift(r6, r7, r5, r14);
     */
    /* JADX WARN: Code restructure failed: missing block: B:68:0x00c5, code lost:
        monitor-enter(r13);
     */
    /* JADX WARN: Code restructure failed: missing block: B:70:0x00c8, code lost:
        if (r11.in_kex == false) goto L52;
     */
    /* JADX WARN: Code restructure failed: missing block: B:71:0x00ca, code lost:
        monitor-exit(r13);
     */
    /* JADX WARN: Code restructure failed: missing block: B:74:0x00d2, code lost:
        if (r13.rwsize < r14) goto L55;
     */
    /* JADX WARN: Code restructure failed: missing block: B:75:0x00d4, code lost:
        r13.rwsize -= r14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:76:0x00da, code lost:
        monitor-exit(r13);
     */
    /* JADX WARN: Code restructure failed: missing block: B:77:0x00db, code lost:
        _write(r12);
     */
    /* JADX WARN: Code restructure failed: missing block: B:78:0x00de, code lost:
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:79:0x00df, code lost:
        monitor-exit(r13);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void write(com.jcraft.jsch.Packet r12, com.jcraft.jsch.Channel r13, int r14) throws java.lang.Exception {
        /*
            Method dump skipped, instructions count: 243
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.jcraft.jsch.Session.write(com.jcraft.jsch.Packet, com.jcraft.jsch.Channel, int):void");
    }

    public void write(Packet packet) throws Exception {
        long t = getTimeout();
        while (this.in_kex) {
            if (t > 0 && System.currentTimeMillis() - this.kex_start_time > t && !this.in_prompt) {
                throw new JSchException("timeout in waiting for rekeying process.");
            }
            byte command = packet.buffer.getCommand();
            if (command == 20 || command == 21 || command == 30 || command == 31 || command == 31 || command == 32 || command == 33 || command == 34 || command == 1) {
                break;
            }
            try {
                Thread.sleep(10L);
            } catch (InterruptedException e) {
            }
        }
        _write(packet);
    }

    private void _write(Packet packet) throws Exception {
        synchronized (this.lock) {
            encode(packet);
            if (this.f212io != null) {
                this.f212io.put(packet);
                this.seqo++;
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:312:0x0666  */
    @Override // java.lang.Runnable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void run() {
        /*
            Method dump skipped, instructions count: 1716
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.jcraft.jsch.Session.run():void");
    }

    public void disconnect() {
        if (this.isConnected) {
            if (JSch.getLogger().isEnabled(1)) {
                Logger logger = JSch.getLogger();
                logger.log(1, "Disconnecting from " + this.host + " port " + this.port);
            }
            Channel.disconnect(this);
            this.isConnected = false;
            PortWatcher.delPort(this);
            ChannelForwardedTCPIP.delPort(this);
            ChannelX11.removeFakedCookie(this);
            synchronized (this.lock) {
                if (this.connectThread != null) {
                    Thread.yield();
                    this.connectThread.interrupt();
                    this.connectThread = null;
                }
            }
            this.thread = null;
            try {
                if (this.f212io != null) {
                    if (this.f212io.in != null) {
                        this.f212io.in.close();
                    }
                    if (this.f212io.out != null) {
                        this.f212io.out.close();
                    }
                    if (this.f212io.out_ext != null) {
                        this.f212io.out_ext.close();
                    }
                }
                if (this.proxy == null) {
                    if (this.socket != null) {
                        this.socket.close();
                    }
                } else {
                    synchronized (this.proxy) {
                        this.proxy.close();
                    }
                    this.proxy = null;
                }
            } catch (Exception e) {
            }
            this.f212io = null;
            this.socket = null;
            this.jsch.removeSession(this);
        }
    }

    public int setPortForwardingL(int lport, String host, int rport) throws JSchException {
        return setPortForwardingL("127.0.0.1", lport, host, rport);
    }

    public int setPortForwardingL(String bind_address, int lport, String host, int rport) throws JSchException {
        return setPortForwardingL(bind_address, lport, host, rport, null);
    }

    public int setPortForwardingL(String bind_address, int lport, String host, int rport, ServerSocketFactory ssf) throws JSchException {
        return setPortForwardingL(bind_address, lport, host, rport, ssf, 0);
    }

    public int setPortForwardingL(String bind_address, int lport, String host, int rport, ServerSocketFactory ssf, int connectTimeout) throws JSchException {
        PortWatcher pw = PortWatcher.addPort(this, bind_address, lport, host, rport, ssf);
        pw.setConnectTimeout(connectTimeout);
        Thread tmp = new Thread(pw);
        tmp.setName("PortWatcher Thread for " + host);
        boolean z = this.daemon_thread;
        if (z) {
            tmp.setDaemon(z);
        }
        tmp.start();
        return pw.lport;
    }

    public void delPortForwardingL(int lport) throws JSchException {
        delPortForwardingL("127.0.0.1", lport);
    }

    public void delPortForwardingL(String bind_address, int lport) throws JSchException {
        PortWatcher.delPort(this, bind_address, lport);
    }

    public String[] getPortForwardingL() throws JSchException {
        return PortWatcher.getPortForwarding(this);
    }

    public void setPortForwardingR(int rport, String host, int lport) throws JSchException {
        setPortForwardingR(null, rport, host, lport, null);
    }

    public void setPortForwardingR(String bind_address, int rport, String host, int lport) throws JSchException {
        setPortForwardingR(bind_address, rport, host, lport, null);
    }

    public void setPortForwardingR(int rport, String host, int lport, SocketFactory sf) throws JSchException {
        setPortForwardingR(null, rport, host, lport, sf);
    }

    public void setPortForwardingR(String bind_address, int rport, String host, int lport, SocketFactory sf) throws JSchException {
        int allocated = _setPortForwardingR(bind_address, rport);
        ChannelForwardedTCPIP.addPort(this, bind_address, rport, allocated, host, lport, sf);
    }

    public void setPortForwardingR(int rport, String daemon) throws JSchException {
        setPortForwardingR((String) null, rport, daemon, (Object[]) null);
    }

    public void setPortForwardingR(int rport, String daemon, Object[] arg) throws JSchException {
        setPortForwardingR((String) null, rport, daemon, arg);
    }

    public void setPortForwardingR(String bind_address, int rport, String daemon, Object[] arg) throws JSchException {
        int allocated = _setPortForwardingR(bind_address, rport);
        ChannelForwardedTCPIP.addPort(this, bind_address, rport, allocated, daemon, arg);
    }

    public String[] getPortForwardingR() throws JSchException {
        return ChannelForwardedTCPIP.getPortForwarding(this);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class Forwarding {
        String bind_address;
        String host;
        int hostport;
        int port;

        private Forwarding() {
            this.bind_address = null;
            this.port = -1;
            this.host = null;
            this.hostport = -1;
        }
    }

    private Forwarding parseForwarding(String conf) throws JSchException {
        String[] tmp = conf.split(" ");
        if (tmp.length > 1) {
            Vector foo = new Vector();
            for (int i = 0; i < tmp.length; i++) {
                if (tmp[i].length() != 0) {
                    foo.addElement(tmp[i].trim());
                }
            }
            StringBuffer sb = new StringBuffer();
            for (int i2 = 0; i2 < foo.size(); i2++) {
                sb.append((String) foo.elementAt(i2));
                if (i2 + 1 < foo.size()) {
                    sb.append(":");
                }
            }
            conf = sb.toString();
        }
        String org2 = conf;
        Forwarding f = new Forwarding();
        try {
            if (conf.lastIndexOf(":") == -1) {
                throw new JSchException("parseForwarding: " + org2);
            }
            f.hostport = Integer.parseInt(conf.substring(conf.lastIndexOf(":") + 1));
            String conf2 = conf.substring(0, conf.lastIndexOf(":"));
            if (conf2.lastIndexOf(":") == -1) {
                throw new JSchException("parseForwarding: " + org2);
            }
            f.host = conf2.substring(conf2.lastIndexOf(":") + 1);
            String conf3 = conf2.substring(0, conf2.lastIndexOf(":"));
            if (conf3.lastIndexOf(":") != -1) {
                f.port = Integer.parseInt(conf3.substring(conf3.lastIndexOf(":") + 1));
                String conf4 = conf3.substring(0, conf3.lastIndexOf(":"));
                conf4 = (conf4.length() == 0 || conf4.equals("*")) ? "0.0.0.0" : "0.0.0.0";
                if (conf4.equals("localhost")) {
                    conf4 = "127.0.0.1";
                }
                f.bind_address = conf4;
            } else {
                f.port = Integer.parseInt(conf3);
                f.bind_address = "127.0.0.1";
            }
            return f;
        } catch (NumberFormatException e) {
            throw new JSchException("parseForwarding: " + e.toString());
        }
    }

    public int setPortForwardingL(String conf) throws JSchException {
        Forwarding f = parseForwarding(conf);
        return setPortForwardingL(f.bind_address, f.port, f.host, f.hostport);
    }

    public int setPortForwardingR(String conf) throws JSchException {
        Forwarding f = parseForwarding(conf);
        int allocated = _setPortForwardingR(f.bind_address, f.port);
        ChannelForwardedTCPIP.addPort(this, f.bind_address, f.port, allocated, f.host, f.hostport, null);
        return allocated;
    }

    public Channel getStreamForwarder(String host, int port) throws JSchException {
        ChannelDirectTCPIP channel = new ChannelDirectTCPIP();
        channel.init();
        addChannel(channel);
        channel.setHost(host);
        channel.setPort(port);
        return channel;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class GlobalRequestReply {
        private int port;
        private int reply;
        private Thread thread;

        private GlobalRequestReply() {
            this.thread = null;
            this.reply = -1;
            this.port = 0;
        }

        void setThread(Thread thread) {
            this.thread = thread;
            this.reply = -1;
        }

        Thread getThread() {
            return this.thread;
        }

        void setReply(int reply) {
            this.reply = reply;
        }

        int getReply() {
            return this.reply;
        }

        int getPort() {
            return this.port;
        }

        void setPort(int port) {
            this.port = port;
        }
    }

    private int _setPortForwardingR(String bind_address, int rport) throws JSchException {
        int rport2;
        synchronized (this.grr) {
            Buffer buf = new Buffer(100);
            Packet packet = new Packet(buf);
            String address_to_bind = ChannelForwardedTCPIP.normalize(bind_address);
            this.grr.setThread(Thread.currentThread());
            this.grr.setPort(rport);
            try {
                packet.reset();
                buf.putByte(TestResultItem.RESULT_PASS);
                buf.putString(Util.str2byte("tcpip-forward"));
                buf.putByte((byte) 1);
                buf.putString(Util.str2byte(address_to_bind));
                buf.putInt(rport);
                write(packet);
                int count = 0;
                int reply = this.grr.getReply();
                while (count < 10 && reply == -1) {
                    try {
                        Thread.sleep(1000L);
                    } catch (Exception e) {
                    }
                    count++;
                    reply = this.grr.getReply();
                }
                this.grr.setThread(null);
                if (reply != 1) {
                    throw new JSchException("remote port forwarding failed for listen port " + rport);
                }
                rport2 = this.grr.getPort();
            } catch (Exception e2) {
                this.grr.setThread(null);
                if (e2 instanceof Throwable) {
                    throw new JSchException(e2.toString(), e2);
                }
                throw new JSchException(e2.toString());
            }
        }
        return rport2;
    }

    public void delPortForwardingR(int rport) throws JSchException {
        delPortForwardingR(null, rport);
    }

    public void delPortForwardingR(String bind_address, int rport) throws JSchException {
        ChannelForwardedTCPIP.delPort(this, bind_address, rport);
    }

    private void initDeflater(String method) throws JSchException {
        if (method.equals(AccountConfig.FaceIDRegisterAction.ORIENTATION_NONE)) {
            this.deflater = null;
            return;
        }
        String foo = getConfig(method);
        if (foo != null) {
            if (method.equals("zlib") || (this.isAuthed && method.equals("zlib@openssh.com"))) {
                try {
                    try {
                        Class c = Class.forName(foo);
                        this.deflater = (Compression) c.newInstance();
                        int level = 6;
                        try {
                            level = Integer.parseInt(getConfig("compression_level"));
                        } catch (Exception e) {
                        }
                        this.deflater.init(1, level);
                    } catch (NoClassDefFoundError ee) {
                        throw new JSchException(ee.toString(), ee);
                    }
                } catch (Exception ee2) {
                    throw new JSchException(ee2.toString(), ee2);
                }
            }
        }
    }

    private void initInflater(String method) throws JSchException {
        if (method.equals(AccountConfig.FaceIDRegisterAction.ORIENTATION_NONE)) {
            this.inflater = null;
            return;
        }
        String foo = getConfig(method);
        if (foo != null) {
            if (method.equals("zlib") || (this.isAuthed && method.equals("zlib@openssh.com"))) {
                try {
                    Class c = Class.forName(foo);
                    this.inflater = (Compression) c.newInstance();
                    this.inflater.init(0, 0);
                } catch (Exception ee) {
                    throw new JSchException(ee.toString(), ee);
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addChannel(Channel channel) {
        channel.setSession(this);
    }

    public void setProxy(Proxy proxy) {
        this.proxy = proxy;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    void setUserName(String username) {
        this.username = username;
    }

    public void setUserInfo(UserInfo userinfo) {
        this.userinfo = userinfo;
    }

    public UserInfo getUserInfo() {
        return this.userinfo;
    }

    public void setInputStream(InputStream in) {
        this.in = in;
    }

    public void setOutputStream(OutputStream out) {
        this.out = out;
    }

    public void setX11Host(String host) {
        ChannelX11.setHost(host);
    }

    public void setX11Port(int port) {
        ChannelX11.setPort(port);
    }

    public void setX11Cookie(String cookie) {
        ChannelX11.setCookie(cookie);
    }

    public void setPassword(String password) {
        if (password != null) {
            this.password = Util.str2byte(password);
        }
    }

    public void setPassword(byte[] password) {
        if (password != null) {
            this.password = new byte[password.length];
            System.arraycopy(password, 0, this.password, 0, password.length);
        }
    }

    public void setConfig(Properties newconf) {
        setConfig((Hashtable) newconf);
    }

    public void setConfig(Hashtable newconf) {
        synchronized (this.lock) {
            if (this.config == null) {
                this.config = new Hashtable();
            }
            Enumeration e = newconf.keys();
            while (e.hasMoreElements()) {
                String key = (String) e.nextElement();
                this.config.put(key, (String) newconf.get(key));
            }
        }
    }

    public void setConfig(String key, String value) {
        synchronized (this.lock) {
            if (this.config == null) {
                this.config = new Hashtable();
            }
            this.config.put(key, value);
        }
    }

    public String getConfig(String key) {
        Hashtable hashtable = this.config;
        if (hashtable != null) {
            Object foo = hashtable.get(key);
            if (foo instanceof String) {
                return (String) foo;
            }
        }
        JSch jSch = this.jsch;
        Object foo2 = JSch.getConfig(key);
        if (foo2 instanceof String) {
            return (String) foo2;
        }
        return null;
    }

    public void setSocketFactory(SocketFactory sfactory) {
        this.socket_factory = sfactory;
    }

    public boolean isConnected() {
        return this.isConnected;
    }

    public int getTimeout() {
        return this.timeout;
    }

    public void setTimeout(int timeout) throws JSchException {
        Socket socket = this.socket;
        if (socket == null) {
            if (timeout < 0) {
                throw new JSchException("invalid timeout value");
            }
            this.timeout = timeout;
            return;
        }
        try {
            socket.setSoTimeout(timeout);
            this.timeout = timeout;
        } catch (Exception e) {
            if (e instanceof Throwable) {
                throw new JSchException(e.toString(), e);
            }
            throw new JSchException(e.toString());
        }
    }

    public String getServerVersion() {
        return Util.byte2str(this.V_S);
    }

    public String getClientVersion() {
        return Util.byte2str(this.V_C);
    }

    public void setClientVersion(String cv) {
        this.V_C = Util.str2byte(cv);
    }

    public void sendIgnore() throws Exception {
        Buffer buf = new Buffer();
        Packet packet = new Packet(buf);
        packet.reset();
        buf.putByte((byte) 2);
        write(packet);
    }

    public void sendKeepAliveMsg() throws Exception {
        Buffer buf = new Buffer();
        Packet packet = new Packet(buf);
        packet.reset();
        buf.putByte(TestResultItem.RESULT_PASS);
        buf.putString(keepalivemsg);
        buf.putByte((byte) 1);
        write(packet);
    }

    public void noMoreSessionChannels() throws Exception {
        Buffer buf = new Buffer();
        Packet packet = new Packet(buf);
        packet.reset();
        buf.putByte(TestResultItem.RESULT_PASS);
        buf.putString(nomoresessions);
        buf.putByte((byte) 0);
        write(packet);
    }

    public HostKey getHostKey() {
        return this.hostkey;
    }

    public String getHost() {
        return this.host;
    }

    public String getUserName() {
        return this.username;
    }

    public int getPort() {
        return this.port;
    }

    public void setHostKeyAlias(String hostKeyAlias) {
        this.hostKeyAlias = hostKeyAlias;
    }

    public String getHostKeyAlias() {
        return this.hostKeyAlias;
    }

    public void setServerAliveInterval(int interval) throws JSchException {
        setTimeout(interval);
        this.serverAliveInterval = interval;
    }

    public int getServerAliveInterval() {
        return this.serverAliveInterval;
    }

    public void setServerAliveCountMax(int count) {
        this.serverAliveCountMax = count;
    }

    public int getServerAliveCountMax() {
        return this.serverAliveCountMax;
    }

    public void setDaemonThread(boolean enable) {
        this.daemon_thread = enable;
    }

    private String[] checkCiphers(String ciphers) {
        if (ciphers == null || ciphers.length() == 0) {
            return null;
        }
        if (JSch.getLogger().isEnabled(1)) {
            JSch.getLogger().log(1, "CheckCiphers: " + ciphers);
        }
        String cipherc2s = getConfig("cipher.c2s");
        String ciphers2c = getConfig("cipher.s2c");
        Vector result = new Vector();
        String[] _ciphers = Util.split(ciphers, ",");
        for (String cipher : _ciphers) {
            if ((ciphers2c.indexOf(cipher) != -1 || cipherc2s.indexOf(cipher) != -1) && !checkCipher(getConfig(cipher))) {
                result.addElement(cipher);
            }
        }
        int i = result.size();
        if (i == 0) {
            return null;
        }
        String[] foo = new String[result.size()];
        System.arraycopy(result.toArray(), 0, foo, 0, result.size());
        if (JSch.getLogger().isEnabled(1)) {
            for (int i2 = 0; i2 < foo.length; i2++) {
                JSch.getLogger().log(1, foo[i2] + " is not available.");
            }
        }
        return foo;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean checkCipher(String cipher) {
        try {
            Class c = Class.forName(cipher);
            Cipher _c = (Cipher) c.newInstance();
            _c.init(0, new byte[_c.getBlockSize()], new byte[_c.getIVSize()]);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private String[] checkKexes(String kexes) {
        if (kexes == null || kexes.length() == 0) {
            return null;
        }
        if (JSch.getLogger().isEnabled(1)) {
            Logger logger = JSch.getLogger();
            logger.log(1, "CheckKexes: " + kexes);
        }
        Vector result = new Vector();
        String[] _kexes = Util.split(kexes, ",");
        for (int i = 0; i < _kexes.length; i++) {
            if (!checkKex(this, getConfig(_kexes[i]))) {
                result.addElement(_kexes[i]);
            }
        }
        int i2 = result.size();
        if (i2 == 0) {
            return null;
        }
        String[] foo = new String[result.size()];
        System.arraycopy(result.toArray(), 0, foo, 0, result.size());
        if (JSch.getLogger().isEnabled(1)) {
            for (int i3 = 0; i3 < foo.length; i3++) {
                Logger logger2 = JSch.getLogger();
                logger2.log(1, foo[i3] + " is not available.");
            }
        }
        return foo;
    }

    static boolean checkKex(Session s, String kex) {
        try {
            Class c = Class.forName(kex);
            KeyExchange _c = (KeyExchange) c.newInstance();
            _c.init(s, null, null, null, null);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private String[] checkSignatures(String sigs) {
        if (sigs == null || sigs.length() == 0) {
            return null;
        }
        if (JSch.getLogger().isEnabled(1)) {
            Logger logger = JSch.getLogger();
            logger.log(1, "CheckSignatures: " + sigs);
        }
        Vector result = new Vector();
        String[] _sigs = Util.split(sigs, ",");
        for (int i = 0; i < _sigs.length; i++) {
            try {
                JSch jSch = this.jsch;
                Class c = Class.forName(JSch.getConfig(_sigs[i]));
                Signature sig = (Signature) c.newInstance();
                sig.init();
            } catch (Exception e) {
                result.addElement(_sigs[i]);
            }
        }
        int i2 = result.size();
        if (i2 == 0) {
            return null;
        }
        String[] foo = new String[result.size()];
        System.arraycopy(result.toArray(), 0, foo, 0, result.size());
        if (JSch.getLogger().isEnabled(1)) {
            for (int i3 = 0; i3 < foo.length; i3++) {
                Logger logger2 = JSch.getLogger();
                logger2.log(1, foo[i3] + " is not available.");
            }
        }
        return foo;
    }

    public void setIdentityRepository(IdentityRepository identityRepository) {
        this.identityRepository = identityRepository;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public IdentityRepository getIdentityRepository() {
        IdentityRepository identityRepository = this.identityRepository;
        if (identityRepository == null) {
            return this.jsch.getIdentityRepository();
        }
        return identityRepository;
    }

    public void setHostKeyRepository(HostKeyRepository hostkeyRepository) {
        this.hostkeyRepository = hostkeyRepository;
    }

    public HostKeyRepository getHostKeyRepository() {
        HostKeyRepository hostKeyRepository = this.hostkeyRepository;
        if (hostKeyRepository == null) {
            return this.jsch.getHostKeyRepository();
        }
        return hostKeyRepository;
    }

    private void applyConfig() throws JSchException {
        String value;
        ConfigRepository configRepository = this.jsch.getConfigRepository();
        if (configRepository == null) {
            return;
        }
        ConfigRepository.Config config = configRepository.getConfig(this.org_host);
        if (this.username == null && (value = config.getUser()) != null) {
            this.username = value;
        }
        String value2 = config.getHostname();
        if (value2 != null) {
            this.host = value2;
        }
        int port = config.getPort();
        if (port != -1) {
            this.port = port;
        }
        checkConfig(config, "kex");
        checkConfig(config, "server_host_key");
        checkConfig(config, "cipher.c2s");
        checkConfig(config, "cipher.s2c");
        checkConfig(config, "mac.c2s");
        checkConfig(config, "mac.s2c");
        checkConfig(config, "compression.c2s");
        checkConfig(config, "compression.s2c");
        checkConfig(config, "compression_level");
        checkConfig(config, "StrictHostKeyChecking");
        checkConfig(config, "HashKnownHosts");
        checkConfig(config, "PreferredAuthentications");
        checkConfig(config, "MaxAuthTries");
        checkConfig(config, "ClearAllForwardings");
        String value3 = config.getValue("HostKeyAlias");
        if (value3 != null) {
            setHostKeyAlias(value3);
        }
        String value4 = config.getValue("UserKnownHostsFile");
        if (value4 != null) {
            KnownHosts kh = new KnownHosts(this.jsch);
            kh.setKnownHosts(value4);
            setHostKeyRepository(kh);
        }
        String[] values = config.getValues("IdentityFile");
        if (values != null) {
            String[] global = configRepository.getConfig("").getValues("IdentityFile");
            if (global != null) {
                for (String str : global) {
                    this.jsch.addIdentity(str);
                }
            } else {
                global = new String[0];
            }
            if (values.length - global.length > 0) {
                IdentityRepository.Wrapper ir = new IdentityRepository.Wrapper(this.jsch.getIdentityRepository(), true);
                for (String ifile : values) {
                    int j = 0;
                    while (true) {
                        if (j >= global.length) {
                            break;
                        } else if (!ifile.equals(global[j])) {
                            j++;
                        } else {
                            ifile = null;
                            break;
                        }
                    }
                    if (ifile != null) {
                        Identity identity = IdentityFile.newInstance(ifile, null, this.jsch);
                        ir.add(identity);
                    }
                }
                setIdentityRepository(ir);
            }
        }
        String value5 = config.getValue("ServerAliveInterval");
        if (value5 != null) {
            try {
                setServerAliveInterval(Integer.parseInt(value5));
            } catch (NumberFormatException e) {
            }
        }
        String value6 = config.getValue("ConnectTimeout");
        if (value6 != null) {
            try {
                setTimeout(Integer.parseInt(value6));
            } catch (NumberFormatException e2) {
            }
        }
        String value7 = config.getValue("MaxAuthTries");
        if (value7 != null) {
            setConfig("MaxAuthTries", value7);
        }
        String value8 = config.getValue("ClearAllForwardings");
        if (value8 != null) {
            setConfig("ClearAllForwardings", value8);
        }
    }

    private void applyConfigChannel(ChannelSession channel) throws JSchException {
        ConfigRepository configRepository = this.jsch.getConfigRepository();
        if (configRepository == null) {
            return;
        }
        ConfigRepository.Config config = configRepository.getConfig(this.org_host);
        String value = config.getValue("ForwardAgent");
        if (value != null) {
            channel.setAgentForwarding(value.equals(BooleanUtils.YES));
        }
        String value2 = config.getValue("RequestTTY");
        if (value2 != null) {
            channel.setPty(value2.equals(BooleanUtils.YES));
        }
    }

    private void requestPortForwarding() throws JSchException {
        ConfigRepository configRepository;
        if (getConfig("ClearAllForwardings").equals(BooleanUtils.YES) || (configRepository = this.jsch.getConfigRepository()) == null) {
            return;
        }
        ConfigRepository.Config config = configRepository.getConfig(this.org_host);
        String[] values = config.getValues("LocalForward");
        if (values != null) {
            for (String str : values) {
                setPortForwardingL(str);
            }
        }
        String[] values2 = config.getValues("RemoteForward");
        if (values2 != null) {
            for (String str2 : values2) {
                setPortForwardingR(str2);
            }
        }
    }

    private void checkConfig(ConfigRepository.Config config, String key) {
        String value = config.getValue(key);
        if (value != null) {
            setConfig(key, value);
        }
    }
}
