package com.jcraft.jsch;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Vector;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class PortWatcher implements Runnable {
    private static InetAddress anyLocalAddress;
    private static Vector pool = new Vector();
    InetAddress boundaddress;
    int connectTimeout = 0;
    String host;
    int lport;
    int rport;
    Session session;
    ServerSocket ss;
    Runnable thread;

    static {
        anyLocalAddress = null;
        try {
            anyLocalAddress = InetAddress.getByName("0.0.0.0");
        } catch (UnknownHostException e) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String[] getPortForwarding(Session session) {
        Vector foo = new Vector();
        synchronized (pool) {
            for (int i = 0; i < pool.size(); i++) {
                PortWatcher p = (PortWatcher) pool.elementAt(i);
                if (p.session == session) {
                    foo.addElement(p.lport + ":" + p.host + ":" + p.rport);
                }
            }
        }
        String[] bar = new String[foo.size()];
        for (int i2 = 0; i2 < foo.size(); i2++) {
            bar[i2] = (String) foo.elementAt(i2);
        }
        return bar;
    }

    static PortWatcher getPort(Session session, String address, int lport) throws JSchException {
        try {
            InetAddress addr = InetAddress.getByName(address);
            synchronized (pool) {
                for (int i = 0; i < pool.size(); i++) {
                    PortWatcher p = (PortWatcher) pool.elementAt(i);
                    if (p.session == session && p.lport == lport && ((anyLocalAddress != null && p.boundaddress.equals(anyLocalAddress)) || p.boundaddress.equals(addr))) {
                        return p;
                    }
                }
                return null;
            }
        } catch (UnknownHostException uhe) {
            throw new JSchException("PortForwardingL: invalid address " + address + " specified.", uhe);
        }
    }

    private static String normalize(String address) {
        if (address != null) {
            if (address.length() == 0 || address.equals("*")) {
                return "0.0.0.0";
            }
            if (address.equals("localhost")) {
                return "127.0.0.1";
            }
            return address;
        }
        return address;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static PortWatcher addPort(Session session, String address, int lport, String host, int rport, ServerSocketFactory ssf) throws JSchException {
        String address2 = normalize(address);
        if (getPort(session, address2, lport) != null) {
            throw new JSchException("PortForwardingL: local port " + address2 + ":" + lport + " is already registered.");
        }
        PortWatcher pw = new PortWatcher(session, address2, lport, host, rport, ssf);
        pool.addElement(pw);
        return pw;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void delPort(Session session, String address, int lport) throws JSchException {
        String address2 = normalize(address);
        PortWatcher pw = getPort(session, address2, lport);
        if (pw == null) {
            throw new JSchException("PortForwardingL: local port " + address2 + ":" + lport + " is not registered.");
        }
        pw.delete();
        pool.removeElement(pw);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void delPort(Session session) {
        synchronized (pool) {
            PortWatcher[] foo = new PortWatcher[pool.size()];
            int count = 0;
            for (int i = 0; i < pool.size(); i++) {
                PortWatcher p = (PortWatcher) pool.elementAt(i);
                if (p.session == session) {
                    p.delete();
                    foo[count] = p;
                    count++;
                }
            }
            for (int i2 = 0; i2 < count; i2++) {
                pool.removeElement(foo[i2]);
            }
        }
    }

    PortWatcher(Session session, String address, int lport, String host, int rport, ServerSocketFactory factory) throws JSchException {
        int assigned;
        this.session = session;
        this.lport = lport;
        this.host = host;
        this.rport = rport;
        try {
            this.boundaddress = InetAddress.getByName(address);
            this.ss = factory == null ? new ServerSocket(lport, 0, this.boundaddress) : factory.createServerSocket(lport, 0, this.boundaddress);
            if (lport == 0 && (assigned = this.ss.getLocalPort()) != -1) {
                this.lport = assigned;
            }
        } catch (Exception e) {
            String message = "PortForwardingL: local port " + address + ":" + lport + " cannot be bound.";
            if (e instanceof Throwable) {
                throw new JSchException(message, e);
            }
            throw new JSchException(message);
        }
    }

    @Override // java.lang.Runnable
    public void run() {
        this.thread = this;
        while (this.thread != null) {
            try {
                Socket socket = this.ss.accept();
                socket.setTcpNoDelay(true);
                InputStream in = socket.getInputStream();
                OutputStream out = socket.getOutputStream();
                ChannelDirectTCPIP channel = new ChannelDirectTCPIP();
                channel.init();
                channel.setInputStream(in);
                channel.setOutputStream(out);
                this.session.addChannel(channel);
                channel.setHost(this.host);
                channel.setPort(this.rport);
                channel.setOrgIPAddress(socket.getInetAddress().getHostAddress());
                channel.setOrgPort(socket.getPort());
                channel.connect(this.connectTimeout);
                int i = channel.exitstatus;
            } catch (Exception e) {
            }
        }
        delete();
    }

    void delete() {
        this.thread = null;
        try {
            if (this.ss != null) {
                this.ss.close();
            }
            this.ss = null;
        } catch (Exception e) {
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }
}
