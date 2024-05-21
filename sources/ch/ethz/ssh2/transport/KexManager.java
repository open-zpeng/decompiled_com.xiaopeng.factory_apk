package ch.ethz.ssh2.transport;

import ch.ethz.ssh2.ConnectionInfo;
import ch.ethz.ssh2.DHGexParameters;
import ch.ethz.ssh2.ServerHostKeyVerifier;
import ch.ethz.ssh2.crypto.CryptoWishList;
import ch.ethz.ssh2.crypto.KeyMaterial;
import ch.ethz.ssh2.crypto.cipher.BlockCipher;
import ch.ethz.ssh2.crypto.cipher.BlockCipherFactory;
import ch.ethz.ssh2.crypto.dh.DhExchange;
import ch.ethz.ssh2.crypto.dh.DhGroupExchange;
import ch.ethz.ssh2.crypto.digest.MAC;
import ch.ethz.ssh2.log.Logger;
import ch.ethz.ssh2.packets.PacketKexDHInit;
import ch.ethz.ssh2.packets.PacketKexDHReply;
import ch.ethz.ssh2.packets.PacketKexDhGexGroup;
import ch.ethz.ssh2.packets.PacketKexDhGexInit;
import ch.ethz.ssh2.packets.PacketKexDhGexReply;
import ch.ethz.ssh2.packets.PacketKexDhGexRequest;
import ch.ethz.ssh2.packets.PacketKexDhGexRequestOld;
import ch.ethz.ssh2.packets.PacketKexInit;
import ch.ethz.ssh2.packets.PacketNewKeys;
import ch.ethz.ssh2.signature.DSAPublicKey;
import ch.ethz.ssh2.signature.DSASHA1Verify;
import ch.ethz.ssh2.signature.DSASignature;
import ch.ethz.ssh2.signature.RSAPublicKey;
import ch.ethz.ssh2.signature.RSASHA1Verify;
import ch.ethz.ssh2.signature.RSASignature;
import java.io.IOException;
import java.security.SecureRandom;
/* loaded from: classes.dex */
public class KexManager {
    static /* synthetic */ Class class$0;
    private static final Logger log;
    ClientServerHello csh;
    final String hostname;
    KeyMaterial km;
    KexState kxs;
    CryptoWishList nextKEXcryptoWishList;
    final int port;
    final SecureRandom rnd;
    byte[] sessionId;
    final TransportManager tm;
    ServerHostKeyVerifier verifier;
    int kexCount = 0;
    final Object accessLock = new Object();
    ConnectionInfo lastConnInfo = null;
    boolean connectionClosed = false;
    boolean ignore_next_kex_packet = false;
    DHGexParameters nextKEXdhgexParameters = new DHGexParameters();

    static {
        Class<?> cls = class$0;
        if (cls == null) {
            try {
                cls = Class.forName("ch.ethz.ssh2.transport.KexManager");
                class$0 = cls;
            } catch (ClassNotFoundException e) {
                throw new NoClassDefFoundError(e.getMessage());
            }
        }
        log = Logger.getLogger(cls);
    }

    public KexManager(TransportManager tm, ClientServerHello csh, CryptoWishList initialCwl, String hostname, int port, ServerHostKeyVerifier keyVerifier, SecureRandom rnd) {
        this.tm = tm;
        this.csh = csh;
        this.nextKEXcryptoWishList = initialCwl;
        this.hostname = hostname;
        this.port = port;
        this.verifier = keyVerifier;
        this.rnd = rnd;
    }

    public ConnectionInfo getOrWaitForConnectionInfo(int minKexCount) throws IOException {
        ConnectionInfo connectionInfo;
        synchronized (this.accessLock) {
            while (true) {
                if (this.lastConnInfo != null && this.lastConnInfo.keyExchangeCounter >= minKexCount) {
                    connectionInfo = this.lastConnInfo;
                } else if (this.connectionClosed) {
                    throw ((IOException) new IOException("Key exchange was not finished, connection is closed.").initCause(this.tm.getReasonClosedCause()));
                } else {
                    try {
                        this.accessLock.wait();
                    } catch (InterruptedException e) {
                    }
                }
            }
        }
        return connectionInfo;
    }

    private String getFirstMatch(String[] client, String[] server) throws NegotiateException {
        if (client == null || server == null) {
            throw new IllegalArgumentException();
        }
        if (client.length == 0) {
            return null;
        }
        for (int i = 0; i < client.length; i++) {
            for (String str : server) {
                if (client[i].equals(str)) {
                    return client[i];
                }
            }
        }
        throw new NegotiateException();
    }

    private boolean compareFirstOfNameList(String[] a, String[] b) {
        if (a == null || b == null) {
            throw new IllegalArgumentException();
        }
        if (a.length == 0 && b.length == 0) {
            return true;
        }
        if (a.length == 0 || b.length == 0) {
            return false;
        }
        return a[0].equals(b[0]);
    }

    private boolean isGuessOK(KexParameters cpar, KexParameters spar) {
        if (cpar == null || spar == null) {
            throw new IllegalArgumentException();
        }
        return compareFirstOfNameList(cpar.kex_algorithms, spar.kex_algorithms) && compareFirstOfNameList(cpar.server_host_key_algorithms, spar.server_host_key_algorithms);
    }

    private NegotiatedParameters mergeKexParameters(KexParameters client, KexParameters server) {
        NegotiatedParameters np = new NegotiatedParameters();
        try {
            np.kex_algo = getFirstMatch(client.kex_algorithms, server.kex_algorithms);
            Logger logger = log;
            StringBuffer stringBuffer = new StringBuffer("kex_algo=");
            stringBuffer.append(np.kex_algo);
            logger.log(20, stringBuffer.toString());
            np.server_host_key_algo = getFirstMatch(client.server_host_key_algorithms, server.server_host_key_algorithms);
            Logger logger2 = log;
            StringBuffer stringBuffer2 = new StringBuffer("server_host_key_algo=");
            stringBuffer2.append(np.server_host_key_algo);
            logger2.log(20, stringBuffer2.toString());
            np.enc_algo_client_to_server = getFirstMatch(client.encryption_algorithms_client_to_server, server.encryption_algorithms_client_to_server);
            np.enc_algo_server_to_client = getFirstMatch(client.encryption_algorithms_server_to_client, server.encryption_algorithms_server_to_client);
            Logger logger3 = log;
            StringBuffer stringBuffer3 = new StringBuffer("enc_algo_client_to_server=");
            stringBuffer3.append(np.enc_algo_client_to_server);
            logger3.log(20, stringBuffer3.toString());
            Logger logger4 = log;
            StringBuffer stringBuffer4 = new StringBuffer("enc_algo_server_to_client=");
            stringBuffer4.append(np.enc_algo_server_to_client);
            logger4.log(20, stringBuffer4.toString());
            np.mac_algo_client_to_server = getFirstMatch(client.mac_algorithms_client_to_server, server.mac_algorithms_client_to_server);
            np.mac_algo_server_to_client = getFirstMatch(client.mac_algorithms_server_to_client, server.mac_algorithms_server_to_client);
            Logger logger5 = log;
            StringBuffer stringBuffer5 = new StringBuffer("mac_algo_client_to_server=");
            stringBuffer5.append(np.mac_algo_client_to_server);
            logger5.log(20, stringBuffer5.toString());
            Logger logger6 = log;
            StringBuffer stringBuffer6 = new StringBuffer("mac_algo_server_to_client=");
            stringBuffer6.append(np.mac_algo_server_to_client);
            logger6.log(20, stringBuffer6.toString());
            np.comp_algo_client_to_server = getFirstMatch(client.compression_algorithms_client_to_server, server.compression_algorithms_client_to_server);
            np.comp_algo_server_to_client = getFirstMatch(client.compression_algorithms_server_to_client, server.compression_algorithms_server_to_client);
            Logger logger7 = log;
            StringBuffer stringBuffer7 = new StringBuffer("comp_algo_client_to_server=");
            stringBuffer7.append(np.comp_algo_client_to_server);
            logger7.log(20, stringBuffer7.toString());
            Logger logger8 = log;
            StringBuffer stringBuffer8 = new StringBuffer("comp_algo_server_to_client=");
            stringBuffer8.append(np.comp_algo_server_to_client);
            logger8.log(20, stringBuffer8.toString());
            try {
                np.lang_client_to_server = getFirstMatch(client.languages_client_to_server, server.languages_client_to_server);
            } catch (NegotiateException e) {
                np.lang_client_to_server = null;
            }
            try {
                np.lang_server_to_client = getFirstMatch(client.languages_server_to_client, server.languages_server_to_client);
            } catch (NegotiateException e2) {
                np.lang_server_to_client = null;
            }
            if (isGuessOK(client, server)) {
                np.guessOK = true;
            }
            return np;
        } catch (NegotiateException e3) {
            return null;
        }
    }

    public synchronized void initiateKEX(CryptoWishList cwl, DHGexParameters dhgex) throws IOException {
        this.nextKEXcryptoWishList = cwl;
        this.nextKEXdhgexParameters = dhgex;
        if (this.kxs == null) {
            this.kxs = new KexState();
            this.kxs.dhgexParameters = this.nextKEXdhgexParameters;
            PacketKexInit kp = new PacketKexInit(this.nextKEXcryptoWishList, this.rnd);
            this.kxs.localKEX = kp;
            this.tm.sendKexMessage(kp.getPayload());
        }
    }

    private boolean establishKeyMaterial() {
        try {
            int mac_cs_key_len = MAC.getKeyLen(this.kxs.np.mac_algo_client_to_server);
            int enc_cs_key_len = BlockCipherFactory.getKeySize(this.kxs.np.enc_algo_client_to_server);
            int enc_cs_block_len = BlockCipherFactory.getBlockSize(this.kxs.np.enc_algo_client_to_server);
            int mac_sc_key_len = MAC.getKeyLen(this.kxs.np.mac_algo_server_to_client);
            int enc_sc_key_len = BlockCipherFactory.getKeySize(this.kxs.np.enc_algo_server_to_client);
            int enc_sc_block_len = BlockCipherFactory.getBlockSize(this.kxs.np.enc_algo_server_to_client);
            this.km = KeyMaterial.create("SHA1", this.kxs.H, this.kxs.K, this.sessionId, enc_cs_key_len, enc_cs_block_len, mac_cs_key_len, enc_sc_key_len, enc_sc_block_len, mac_sc_key_len);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private void finishKex() throws IOException {
        if (this.sessionId == null) {
            this.sessionId = this.kxs.H;
        }
        establishKeyMaterial();
        PacketNewKeys ign = new PacketNewKeys();
        this.tm.sendKexMessage(ign.getPayload());
        try {
            BlockCipher cbc = BlockCipherFactory.createCipher(this.kxs.np.enc_algo_client_to_server, true, this.km.enc_key_client_to_server, this.km.initial_iv_client_to_server);
            MAC mac = new MAC(this.kxs.np.mac_algo_client_to_server, this.km.integrity_key_client_to_server);
            this.tm.changeSendCipher(cbc, mac);
            this.tm.kexFinished();
        } catch (IllegalArgumentException e) {
            throw new IOException("Fatal error during MAC startup!");
        }
    }

    public static final String[] getDefaultServerHostkeyAlgorithmList() {
        return new String[]{"ssh-rsa", "ssh-dss"};
    }

    public static final void checkServerHostkeyAlgorithmsList(String[] algos) {
        for (int i = 0; i < algos.length; i++) {
            if (!"ssh-rsa".equals(algos[i]) && !"ssh-dss".equals(algos[i])) {
                StringBuffer stringBuffer = new StringBuffer("Unknown server host key algorithm '");
                stringBuffer.append(algos[i]);
                stringBuffer.append("'");
                throw new IllegalArgumentException(stringBuffer.toString());
            }
        }
    }

    public static final String[] getDefaultKexAlgorithmList() {
        return new String[]{"diffie-hellman-group-exchange-sha1", "diffie-hellman-group14-sha1", "diffie-hellman-group1-sha1"};
    }

    public static final void checkKexAlgorithmList(String[] algos) {
        for (int i = 0; i < algos.length; i++) {
            if (!"diffie-hellman-group-exchange-sha1".equals(algos[i]) && !"diffie-hellman-group14-sha1".equals(algos[i]) && !"diffie-hellman-group1-sha1".equals(algos[i])) {
                StringBuffer stringBuffer = new StringBuffer("Unknown kex algorithm '");
                stringBuffer.append(algos[i]);
                stringBuffer.append("'");
                throw new IllegalArgumentException(stringBuffer.toString());
            }
        }
    }

    private boolean verifySignature(byte[] sig, byte[] hostkey) throws IOException {
        if (this.kxs.np.server_host_key_algo.equals("ssh-rsa")) {
            RSASignature rs = RSASHA1Verify.decodeSSHRSASignature(sig);
            RSAPublicKey rpk = RSASHA1Verify.decodeSSHRSAPublicKey(hostkey);
            log.log(50, "Verifying ssh-rsa signature");
            return RSASHA1Verify.verifySignature(this.kxs.H, rs, rpk);
        } else if (this.kxs.np.server_host_key_algo.equals("ssh-dss")) {
            DSASignature ds = DSASHA1Verify.decodeSSHDSASignature(sig);
            DSAPublicKey dpk = DSASHA1Verify.decodeSSHDSAPublicKey(hostkey);
            log.log(50, "Verifying ssh-dss signature");
            return DSASHA1Verify.verifySignature(this.kxs.H, ds, dpk);
        } else {
            StringBuffer stringBuffer = new StringBuffer("Unknown server host key algorithm '");
            stringBuffer.append(this.kxs.np.server_host_key_algo);
            stringBuffer.append("'");
            throw new IOException(stringBuffer.toString());
        }
    }

    public synchronized void handleMessage(byte[] msg, int msglen) throws IOException {
        if (msg == null) {
            synchronized (this.accessLock) {
                this.connectionClosed = true;
                this.accessLock.notifyAll();
            }
            return;
        }
        if (this.kxs == null && msg[0] != 20) {
            StringBuffer stringBuffer = new StringBuffer("Unexpected KEX message (type ");
            stringBuffer.append((int) msg[0]);
            stringBuffer.append(")");
            throw new IOException(stringBuffer.toString());
        }
        if (this.ignore_next_kex_packet) {
            this.ignore_next_kex_packet = false;
        } else if (msg[0] == 20) {
            if (this.kxs != null && this.kxs.state != 0) {
                throw new IOException("Unexpected SSH_MSG_KEXINIT message during on-going kex exchange!");
            }
            if (this.kxs == null) {
                this.kxs = new KexState();
                this.kxs.dhgexParameters = this.nextKEXdhgexParameters;
                PacketKexInit kip = new PacketKexInit(this.nextKEXcryptoWishList, this.rnd);
                this.kxs.localKEX = kip;
                this.tm.sendKexMessage(kip.getPayload());
            }
            this.kxs.remoteKEX = new PacketKexInit(msg, 0, msglen);
            this.kxs.np = mergeKexParameters(this.kxs.localKEX.getKexParameters(), this.kxs.remoteKEX.getKexParameters());
            if (this.kxs.np == null) {
                throw new IOException("Cannot negotiate, proposals do not match.");
            }
            if (this.kxs.remoteKEX.isFirst_kex_packet_follows() && !this.kxs.np.guessOK) {
                this.ignore_next_kex_packet = true;
            }
            if (this.kxs.np.kex_algo.equals("diffie-hellman-group-exchange-sha1")) {
                if (this.kxs.dhgexParameters.getMin_group_len() == 0) {
                    PacketKexDhGexRequestOld dhgexreq = new PacketKexDhGexRequestOld(this.kxs.dhgexParameters);
                    this.tm.sendKexMessage(dhgexreq.getPayload());
                } else {
                    PacketKexDhGexRequest dhgexreq2 = new PacketKexDhGexRequest(this.kxs.dhgexParameters);
                    this.tm.sendKexMessage(dhgexreq2.getPayload());
                }
                this.kxs.state = 1;
                return;
            }
            if (!this.kxs.np.kex_algo.equals("diffie-hellman-group1-sha1") && !this.kxs.np.kex_algo.equals("diffie-hellman-group14-sha1")) {
                throw new IllegalStateException("Unkown KEX method!");
            }
            this.kxs.dhx = new DhExchange();
            if (this.kxs.np.kex_algo.equals("diffie-hellman-group1-sha1")) {
                this.kxs.dhx.init(1, this.rnd);
            } else {
                this.kxs.dhx.init(14, this.rnd);
            }
            PacketKexDHInit kp = new PacketKexDHInit(this.kxs.dhx.getE());
            this.tm.sendKexMessage(kp.getPayload());
            this.kxs.state = 1;
        } else {
            if (msg[0] == 21) {
                if (this.km == null) {
                    throw new IOException("Peer sent SSH_MSG_NEWKEYS, but I have no key material ready!");
                }
                try {
                    BlockCipher cbc = BlockCipherFactory.createCipher(this.kxs.np.enc_algo_server_to_client, false, this.km.enc_key_server_to_client, this.km.initial_iv_server_to_client);
                    MAC mac = new MAC(this.kxs.np.mac_algo_server_to_client, this.km.integrity_key_server_to_client);
                    this.tm.changeRecvCipher(cbc, mac);
                    ConnectionInfo sci = new ConnectionInfo();
                    this.kexCount++;
                    sci.keyExchangeAlgorithm = this.kxs.np.kex_algo;
                    sci.keyExchangeCounter = this.kexCount;
                    sci.clientToServerCryptoAlgorithm = this.kxs.np.enc_algo_client_to_server;
                    sci.serverToClientCryptoAlgorithm = this.kxs.np.enc_algo_server_to_client;
                    sci.clientToServerMACAlgorithm = this.kxs.np.mac_algo_client_to_server;
                    sci.serverToClientMACAlgorithm = this.kxs.np.mac_algo_server_to_client;
                    sci.serverHostKeyAlgorithm = this.kxs.np.server_host_key_algo;
                    sci.serverHostKey = this.kxs.hostkey;
                    synchronized (this.accessLock) {
                        this.lastConnInfo = sci;
                        this.accessLock.notifyAll();
                    }
                    this.kxs = null;
                    return;
                } catch (IllegalArgumentException e) {
                    throw new IOException("Fatal error during MAC startup!");
                }
            } else if (this.kxs == null || this.kxs.state == 0) {
                throw new IOException("Unexpected Kex submessage!");
            } else {
                if (this.kxs.np.kex_algo.equals("diffie-hellman-group-exchange-sha1")) {
                    if (this.kxs.state == 1) {
                        PacketKexDhGexGroup dhgexgrp = new PacketKexDhGexGroup(msg, 0, msglen);
                        this.kxs.dhgx = new DhGroupExchange(dhgexgrp.getP(), dhgexgrp.getG());
                        this.kxs.dhgx.init(this.rnd);
                        PacketKexDhGexInit dhgexinit = new PacketKexDhGexInit(this.kxs.dhgx.getE());
                        this.tm.sendKexMessage(dhgexinit.getPayload());
                        this.kxs.state = 2;
                        return;
                    } else if (this.kxs.state == 2) {
                        PacketKexDhGexReply dhgexrpl = new PacketKexDhGexReply(msg, 0, msglen);
                        this.kxs.hostkey = dhgexrpl.getHostKey();
                        if (this.verifier != null) {
                            try {
                                boolean vres = this.verifier.verifyServerHostKey(this.hostname, this.port, this.kxs.np.server_host_key_algo, this.kxs.hostkey);
                                if (!vres) {
                                    throw new IOException("The server hostkey was not accepted by the verifier callback");
                                }
                            } catch (Exception e2) {
                                throw ((IOException) new IOException("The server hostkey was not accepted by the verifier callback.").initCause(e2));
                            }
                        }
                        this.kxs.dhgx.setF(dhgexrpl.getF());
                        try {
                            this.kxs.H = this.kxs.dhgx.calculateH(this.csh.getClientString(), this.csh.getServerString(), this.kxs.localKEX.getPayload(), this.kxs.remoteKEX.getPayload(), dhgexrpl.getHostKey(), this.kxs.dhgexParameters);
                            boolean res = verifySignature(dhgexrpl.getSignature(), this.kxs.hostkey);
                            if (!res) {
                                throw new IOException("Hostkey signature sent by remote is wrong!");
                            }
                            this.kxs.K = this.kxs.dhgx.getK();
                            finishKex();
                            this.kxs.state = -1;
                            return;
                        } catch (IllegalArgumentException e3) {
                            throw ((IOException) new IOException("KEX error.").initCause(e3));
                        }
                    } else {
                        throw new IllegalStateException("Illegal State in KEX Exchange!");
                    }
                } else if ((this.kxs.np.kex_algo.equals("diffie-hellman-group1-sha1") || this.kxs.np.kex_algo.equals("diffie-hellman-group14-sha1")) && this.kxs.state == 1) {
                    PacketKexDHReply dhr = new PacketKexDHReply(msg, 0, msglen);
                    this.kxs.hostkey = dhr.getHostKey();
                    if (this.verifier != null) {
                        try {
                            boolean vres2 = this.verifier.verifyServerHostKey(this.hostname, this.port, this.kxs.np.server_host_key_algo, this.kxs.hostkey);
                            if (!vres2) {
                                throw new IOException("The server hostkey was not accepted by the verifier callback");
                            }
                        } catch (Exception e4) {
                            throw ((IOException) new IOException("The server hostkey was not accepted by the verifier callback.").initCause(e4));
                        }
                    }
                    this.kxs.dhx.setF(dhr.getF());
                    try {
                        this.kxs.H = this.kxs.dhx.calculateH(this.csh.getClientString(), this.csh.getServerString(), this.kxs.localKEX.getPayload(), this.kxs.remoteKEX.getPayload(), dhr.getHostKey());
                        boolean res2 = verifySignature(dhr.getSignature(), this.kxs.hostkey);
                        if (!res2) {
                            throw new IOException("Hostkey signature sent by remote is wrong!");
                        }
                        this.kxs.K = this.kxs.dhx.getK();
                        finishKex();
                        this.kxs.state = -1;
                        return;
                    } catch (IllegalArgumentException e5) {
                        throw ((IOException) new IOException("KEX error.").initCause(e5));
                    }
                } else {
                    StringBuffer stringBuffer2 = new StringBuffer("Unkown KEX method! (");
                    stringBuffer2.append(this.kxs.np.kex_algo);
                    stringBuffer2.append(")");
                    throw new IllegalStateException(stringBuffer2.toString());
                }
            }
        }
    }
}
