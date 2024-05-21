package com.jcraft.jsch;

import com.xiaopeng.commonfunc.utils.CameraUtils;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
/* loaded from: classes.dex */
public class ProxySOCKS5 implements Proxy {
    private static int DEFAULTPORT = CameraUtils.HIGH_HEIGHT;
    private InputStream in;
    private OutputStream out;
    private String passwd;
    private String proxy_host;
    private int proxy_port;
    private Socket socket;
    private String user;

    public ProxySOCKS5(String proxy_host) {
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

    public ProxySOCKS5(String proxy_host, int proxy_port) {
        this.proxy_host = proxy_host;
        this.proxy_port = proxy_port;
    }

    public void setUserPasswd(String user, String passwd) {
        this.user = user;
        this.passwd = passwd;
    }

    /* JADX WARN: Code restructure failed: missing block: B:21:0x00d4, code lost:
        r7 = 0 + 1;
        r0[0] = 5;
        r2 = r7 + 1;
        r0[r7] = 1;
        r4 = r2 + 1;
        r0[r2] = 0;
        r2 = com.jcraft.jsch.Util.str2byte(r13);
        r7 = r2.length;
        r8 = r4 + 1;
        r0[r4] = 3;
        r4 = r8 + 1;
        r0[r8] = (byte) r7;
        java.lang.System.arraycopy(r2, 0, r0, r4, r7);
        r4 = r4 + r7;
        r8 = r4 + 1;
        r0[r4] = (byte) (r14 >>> 8);
        r0[r8] = (byte) (r14 & 255);
        r11.out.write(r0, 0, r8 + 1);
        fill(r11.in, r0, 4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x010f, code lost:
        if (r0[1] != 0) goto L26;
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x0111, code lost:
        r8 = r0[3] & 255;
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x0115, code lost:
        if (r8 == 1) goto L24;
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x0117, code lost:
        if (r8 == 3) goto L22;
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x0119, code lost:
        if (r8 == 4) goto L20;
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x011c, code lost:
        fill(r11.in, r0, 18);
     */
    /* JADX WARN: Code restructure failed: missing block: B:29:0x0124, code lost:
        fill(r11.in, r0, 1);
        fill(r11.in, r0, (r0[0] & 255) + 2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x0134, code lost:
        fill(r11.in, r0, 6);
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x013c, code lost:
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x013d, code lost:
        r11.socket.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:64:?, code lost:
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:65:?, code lost:
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:66:?, code lost:
        return;
     */
    @Override // com.jcraft.jsch.Proxy
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void connect(com.jcraft.jsch.SocketFactory r12, java.lang.String r13, int r14, int r15) throws com.jcraft.jsch.JSchException {
        /*
            Method dump skipped, instructions count: 415
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.jcraft.jsch.ProxySOCKS5.connect(com.jcraft.jsch.SocketFactory, java.lang.String, int, int):void");
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

    private void fill(InputStream in, byte[] buf, int len) throws JSchException, IOException {
        int s = 0;
        while (s < len) {
            int i = in.read(buf, s, len - s);
            if (i <= 0) {
                throw new JSchException("ProxySOCKS5: stream is closed");
            }
            s += i;
        }
    }
}
