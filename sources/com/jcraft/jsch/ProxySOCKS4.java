package com.jcraft.jsch;

import com.xiaopeng.commonfunc.utils.CameraUtils;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
/* loaded from: classes.dex */
public class ProxySOCKS4 implements Proxy {
    private static int DEFAULTPORT = CameraUtils.HIGH_HEIGHT;
    private InputStream in;
    private OutputStream out;
    private String passwd;
    private String proxy_host;
    private int proxy_port;
    private Socket socket;
    private String user;

    public ProxySOCKS4(String proxy_host) {
        int port = DEFAULTPORT;
        String host = proxy_host;
        if (proxy_host.indexOf(58) != -1) {
            try {
                host = proxy_host.substring(0, proxy_host.indexOf(58));
                port = Integer.parseInt(proxy_host.substring(proxy_host.indexOf(58) + 1));
            } catch (Exception e) {
            }
        }
        this.proxy_host = host;
        this.proxy_port = port;
    }

    public ProxySOCKS4(String proxy_host, int proxy_port) {
        this.proxy_host = proxy_host;
        this.proxy_port = proxy_port;
    }

    public void setUserPasswd(String user, String passwd) {
        this.user = user;
        this.passwd = passwd;
    }

    @Override // com.jcraft.jsch.Proxy
    public void connect(SocketFactory socket_factory, String host, int port, int timeout) throws JSchException {
        try {
            if (socket_factory == null) {
                this.socket = Util.createSocket(this.proxy_host, this.proxy_port, timeout);
                this.in = this.socket.getInputStream();
                this.out = this.socket.getOutputStream();
            } else {
                this.socket = socket_factory.createSocket(this.proxy_host, this.proxy_port);
                this.in = socket_factory.getInputStream(this.socket);
                this.out = socket_factory.getOutputStream(this.socket);
            }
            if (timeout > 0) {
                this.socket.setSoTimeout(timeout);
            }
            this.socket.setTcpNoDelay(true);
            byte[] buf = new byte[1024];
            int index = 0 + 1;
            buf[0] = 4;
            int index2 = index + 1;
            buf[index] = 1;
            int index3 = index2 + 1;
            buf[index2] = (byte) (port >>> 8);
            int index4 = index3 + 1;
            buf[index3] = (byte) (port & 255);
            try {
                InetAddress addr = InetAddress.getByName(host);
                byte[] byteAddress = addr.getAddress();
                int i = 0;
                while (i < byteAddress.length) {
                    int index5 = index4 + 1;
                    try {
                        buf[index4] = byteAddress[i];
                        i++;
                        index4 = index5;
                    } catch (UnknownHostException e) {
                        uhe = e;
                        throw new JSchException("ProxySOCKS4: " + uhe.toString(), uhe);
                    }
                }
                if (this.user != null) {
                    System.arraycopy(Util.str2byte(this.user), 0, buf, index4, this.user.length());
                    index4 += this.user.length();
                }
                buf[index4] = 0;
                this.out.write(buf, 0, index4 + 1);
                int s = 0;
                while (s < 8) {
                    int i2 = this.in.read(buf, s, 8 - s);
                    if (i2 <= 0) {
                        throw new JSchException("ProxySOCKS4: stream is closed");
                    }
                    s += i2;
                }
                int i3 = buf[0];
                if (i3 != 0) {
                    throw new JSchException("ProxySOCKS4: server returns VN " + ((int) buf[0]));
                } else if (buf[1] != 90) {
                    try {
                        this.socket.close();
                    } catch (Exception e2) {
                    }
                    String message = "ProxySOCKS4: server returns CD " + ((int) buf[1]);
                    throw new JSchException(message);
                }
            } catch (UnknownHostException e3) {
                uhe = e3;
            }
        } catch (RuntimeException e4) {
            throw e4;
        } catch (Exception e5) {
            try {
                if (this.socket != null) {
                    this.socket.close();
                }
            } catch (Exception e6) {
            }
            throw new JSchException("ProxySOCKS4: " + e5.toString());
        }
    }

    @Override // com.jcraft.jsch.Proxy
    public InputStream getInputStream() {
        return this.in;
    }

    @Override // com.jcraft.jsch.Proxy
    public OutputStream getOutputStream() {
        return this.out;
    }

    @Override // com.jcraft.jsch.Proxy
    public Socket getSocket() {
        return this.socket;
    }

    @Override // com.jcraft.jsch.Proxy
    public void close() {
        try {
            if (this.in != null) {
                this.in.close();
            }
            if (this.out != null) {
                this.out.close();
            }
            if (this.socket != null) {
                this.socket.close();
            }
        } catch (Exception e) {
        }
        this.in = null;
        this.out = null;
        this.socket = null;
    }

    public static int getDefaultPort() {
        return DEFAULTPORT;
    }
}
