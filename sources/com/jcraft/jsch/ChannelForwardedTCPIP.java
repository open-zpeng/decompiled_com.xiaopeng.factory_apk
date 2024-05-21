package com.jcraft.jsch;

import com.jcraft.jsch.Channel;
import com.xiaopeng.commonfunc.bean.factorytest.TestResultItem;
import java.io.PipedOutputStream;
import java.net.Socket;
import java.util.Vector;
/* loaded from: classes.dex */
public class ChannelForwardedTCPIP extends Channel {
    private static final int LOCAL_MAXIMUM_PACKET_SIZE = 16384;
    private static final int LOCAL_WINDOW_SIZE_MAX = 131072;
    private static final int TIMEOUT = 10000;
    private static Vector pool = new Vector();
    private Socket socket = null;
    private ForwardedTCPIPDaemon daemon = null;
    private Config config = null;

    /* JADX INFO: Access modifiers changed from: package-private */
    public ChannelForwardedTCPIP() {
        setLocalWindowSizeMax(131072);
        setLocalWindowSize(131072);
        setLocalPacketSize(16384);
        this.f211io = new IO();
        this.connected = true;
    }

    @Override // com.jcraft.jsch.Channel, java.lang.Runnable
    public void run() {
        try {
            if (this.config instanceof ConfigDaemon) {
                ConfigDaemon _config = (ConfigDaemon) this.config;
                Class c = Class.forName(_config.target);
                this.daemon = (ForwardedTCPIPDaemon) c.newInstance();
                PipedOutputStream out = new PipedOutputStream();
                this.f211io.setInputStream(new Channel.PassiveInputStream(out, 32768), false);
                this.daemon.setChannel(this, getInputStream(), out);
                this.daemon.setArg(_config.arg);
                new Thread(this.daemon).start();
            } else {
                ConfigLHost _config2 = (ConfigLHost) this.config;
                this.socket = _config2.factory == null ? Util.createSocket(_config2.target, _config2.lport, 10000) : _config2.factory.createSocket(_config2.target, _config2.lport);
                this.socket.setTcpNoDelay(true);
                this.f211io.setInputStream(this.socket.getInputStream());
                this.f211io.setOutputStream(this.socket.getOutputStream());
            }
            sendOpenConfirmation();
            this.thread = Thread.currentThread();
            Buffer buf = new Buffer(this.rmpsize);
            Packet packet = new Packet(buf);
            try {
                Session _session = getSession();
                while (true) {
                    if (this.thread != null && this.f211io != null && this.f211io.in != null) {
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
            } catch (Exception e) {
            }
            disconnect();
        } catch (Exception e2) {
            sendOpenFailure(1);
            this.close = true;
            disconnect();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.jcraft.jsch.Channel
    public void getData(Buffer buf) {
        setRecipient(buf.getInt());
        setRemoteWindowSize(buf.getUInt());
        setRemotePacketSize(buf.getInt());
        byte[] addr = buf.getString();
        int port = buf.getInt();
        buf.getString();
        buf.getInt();
        Session _session = null;
        try {
            _session = getSession();
        } catch (JSchException e) {
        }
        this.config = getPort(_session, Util.byte2str(addr), port);
        if (this.config == null) {
            this.config = getPort(_session, null, port);
        }
        if (this.config == null && JSch.getLogger().isEnabled(3)) {
            Logger logger = JSch.getLogger();
            logger.log(3, "ChannelForwardedTCPIP: " + Util.byte2str(addr) + ":" + port + " is not registered.");
        }
    }

    private static Config getPort(Session session, String address_to_bind, int rport) {
        synchronized (pool) {
            for (int i = 0; i < pool.size(); i++) {
                Config bar = (Config) pool.elementAt(i);
                if (bar.session == session && ((bar.rport == rport || (bar.rport == 0 && bar.allocated_rport == rport)) && (address_to_bind == null || bar.address_to_bind.equals(address_to_bind)))) {
                    return bar;
                }
            }
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String[] getPortForwarding(Session session) {
        Vector foo = new Vector();
        synchronized (pool) {
            for (int i = 0; i < pool.size(); i++) {
                Config config = (Config) pool.elementAt(i);
                if (config instanceof ConfigDaemon) {
                    foo.addElement(config.allocated_rport + ":" + config.target + ":");
                } else {
                    foo.addElement(config.allocated_rport + ":" + config.target + ":" + ((ConfigLHost) config).lport);
                }
            }
        }
        String[] bar = new String[foo.size()];
        for (int i2 = 0; i2 < foo.size(); i2++) {
            bar[i2] = (String) foo.elementAt(i2);
        }
        return bar;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String normalize(String address) {
        if (address == null) {
            return "localhost";
        }
        if (address.length() == 0 || address.equals("*")) {
            return "";
        }
        return address;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void addPort(Session session, String _address_to_bind, int port, int allocated_port, String target, int lport, SocketFactory factory) throws JSchException {
        String address_to_bind = normalize(_address_to_bind);
        synchronized (pool) {
            if (getPort(session, address_to_bind, port) != null) {
                throw new JSchException("PortForwardingR: remote port " + port + " is already registered.");
            }
            ConfigLHost config = new ConfigLHost();
            config.session = session;
            config.rport = port;
            config.allocated_rport = allocated_port;
            config.target = target;
            config.lport = lport;
            config.address_to_bind = address_to_bind;
            config.factory = factory;
            pool.addElement(config);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void addPort(Session session, String _address_to_bind, int port, int allocated_port, String daemon, Object[] arg) throws JSchException {
        String address_to_bind = normalize(_address_to_bind);
        synchronized (pool) {
            if (getPort(session, address_to_bind, port) != null) {
                throw new JSchException("PortForwardingR: remote port " + port + " is already registered.");
            }
            ConfigDaemon config = new ConfigDaemon();
            config.session = session;
            config.rport = port;
            config.allocated_rport = port;
            config.target = daemon;
            config.arg = arg;
            config.address_to_bind = address_to_bind;
            pool.addElement(config);
        }
    }

    static void delPort(ChannelForwardedTCPIP c) {
        Config config;
        Session _session = null;
        try {
            _session = c.getSession();
        } catch (JSchException e) {
        }
        if (_session != null && (config = c.config) != null) {
            delPort(_session, config.rport);
        }
    }

    static void delPort(Session session, int rport) {
        delPort(session, null, rport);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void delPort(Session session, String address_to_bind, int rport) {
        synchronized (pool) {
            Config foo = getPort(session, normalize(address_to_bind), rport);
            if (foo == null) {
                foo = getPort(session, null, rport);
            }
            if (foo == null) {
                return;
            }
            pool.removeElement(foo);
            if (address_to_bind == null) {
                address_to_bind = foo.address_to_bind;
            }
            if (address_to_bind == null) {
                address_to_bind = "0.0.0.0";
            }
            Buffer buf = new Buffer(100);
            Packet packet = new Packet(buf);
            try {
                packet.reset();
                buf.putByte(TestResultItem.RESULT_PASS);
                buf.putString(Util.str2byte("cancel-tcpip-forward"));
                buf.putByte((byte) 0);
                buf.putString(Util.str2byte(address_to_bind));
                buf.putInt(rport);
                session.write(packet);
            } catch (Exception e) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void delPort(Session session) {
        int count = 0;
        synchronized (pool) {
            try {
                int[] rport = new int[pool.size()];
                for (int i = 0; i < pool.size(); i++) {
                    Config config = (Config) pool.elementAt(i);
                    if (config.session == session) {
                        int count2 = count + 1;
                        try {
                            rport[count] = config.rport;
                            count = count2;
                        } catch (Throwable th) {
                            th = th;
                            throw th;
                        }
                    }
                }
                for (int i2 = 0; i2 < count; i2++) {
                    delPort(session, rport[i2]);
                }
            } catch (Throwable th2) {
                th = th2;
            }
        }
    }

    public int getRemotePort() {
        Config config = this.config;
        if (config != null) {
            return config.rport;
        }
        return 0;
    }

    private void setSocketFactory(SocketFactory factory) {
        Config config = this.config;
        if (config != null && (config instanceof ConfigLHost)) {
            ((ConfigLHost) config).factory = factory;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static abstract class Config {
        String address_to_bind;
        int allocated_rport;
        int rport;
        Session session;
        String target;

        Config() {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class ConfigDaemon extends Config {
        Object[] arg;

        ConfigDaemon() {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class ConfigLHost extends Config {
        SocketFactory factory;
        int lport;

        ConfigLHost() {
        }
    }
}
