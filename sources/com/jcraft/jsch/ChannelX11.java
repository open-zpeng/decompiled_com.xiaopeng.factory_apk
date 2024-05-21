package com.jcraft.jsch;

import androidx.core.view.MotionEventCompat;
import java.io.IOException;
import java.net.Socket;
import java.util.Hashtable;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class ChannelX11 extends Channel {
    private static final int LOCAL_MAXIMUM_PACKET_SIZE = 16384;
    private static final int LOCAL_WINDOW_SIZE_MAX = 131072;
    private static final int TIMEOUT = 10000;
    private static String host = "127.0.0.1";
    private static int port = 6000;
    static byte[] cookie = null;
    private static byte[] cookie_hex = null;
    private static Hashtable faked_cookie_pool = new Hashtable();
    private static Hashtable faked_cookie_hex_pool = new Hashtable();
    private static byte[] table = {48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 97, 98, 99, 100, 101, 102};
    private boolean init = true;
    private Socket socket = null;
    private byte[] cache = new byte[0];

    static int revtable(byte foo) {
        int i = 0;
        while (true) {
            byte[] bArr = table;
            if (i < bArr.length) {
                if (bArr[i] == foo) {
                    return i;
                }
                i++;
            } else {
                return 0;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void setCookie(String foo) {
        cookie_hex = Util.str2byte(foo);
        cookie = new byte[16];
        for (int i = 0; i < 16; i++) {
            cookie[i] = (byte) (((revtable(cookie_hex[i * 2]) << 4) & 240) | (revtable(cookie_hex[(i * 2) + 1]) & 15));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void setHost(String foo) {
        host = foo;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void setPort(int foo) {
        port = foo;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static byte[] getFakedCookie(Session session) {
        byte[] foo;
        synchronized (faked_cookie_hex_pool) {
            foo = (byte[]) faked_cookie_hex_pool.get(session);
            if (foo == null) {
                Random random = Session.random;
                byte[] foo2 = new byte[16];
                synchronized (random) {
                    random.fill(foo2, 0, 16);
                }
                faked_cookie_pool.put(session, foo2);
                byte[] bar = new byte[32];
                for (int i = 0; i < 16; i++) {
                    bar[i * 2] = table[(foo2[i] >>> 4) & 15];
                    bar[(i * 2) + 1] = table[foo2[i] & 15];
                }
                faked_cookie_hex_pool.put(session, bar);
                foo = bar;
            }
        }
        return foo;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void removeFakedCookie(Session session) {
        synchronized (faked_cookie_hex_pool) {
            faked_cookie_hex_pool.remove(session);
            faked_cookie_pool.remove(session);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public ChannelX11() {
        setLocalWindowSizeMax(131072);
        setLocalWindowSize(131072);
        setLocalPacketSize(16384);
        this.type = Util.str2byte("x11");
        this.connected = true;
    }

    /* JADX WARN: Code restructure failed: missing block: B:13:0x0069, code lost:
        eof();
     */
    @Override // com.jcraft.jsch.Channel, java.lang.Runnable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void run() {
        /*
            r7 = this;
            r0 = 1
            java.lang.String r1 = com.jcraft.jsch.ChannelX11.host     // Catch: java.lang.Exception -> L93
            int r2 = com.jcraft.jsch.ChannelX11.port     // Catch: java.lang.Exception -> L93
            r3 = 10000(0x2710, float:1.4013E-41)
            java.net.Socket r1 = com.jcraft.jsch.Util.createSocket(r1, r2, r3)     // Catch: java.lang.Exception -> L93
            r7.socket = r1     // Catch: java.lang.Exception -> L93
            java.net.Socket r1 = r7.socket     // Catch: java.lang.Exception -> L93
            r1.setTcpNoDelay(r0)     // Catch: java.lang.Exception -> L93
            com.jcraft.jsch.IO r1 = new com.jcraft.jsch.IO     // Catch: java.lang.Exception -> L93
            r1.<init>()     // Catch: java.lang.Exception -> L93
            r7.f211io = r1     // Catch: java.lang.Exception -> L93
            com.jcraft.jsch.IO r1 = r7.f211io     // Catch: java.lang.Exception -> L93
            java.net.Socket r2 = r7.socket     // Catch: java.lang.Exception -> L93
            java.io.InputStream r2 = r2.getInputStream()     // Catch: java.lang.Exception -> L93
            r1.setInputStream(r2)     // Catch: java.lang.Exception -> L93
            com.jcraft.jsch.IO r1 = r7.f211io     // Catch: java.lang.Exception -> L93
            java.net.Socket r2 = r7.socket     // Catch: java.lang.Exception -> L93
            java.io.OutputStream r2 = r2.getOutputStream()     // Catch: java.lang.Exception -> L93
            r1.setOutputStream(r2)     // Catch: java.lang.Exception -> L93
            r7.sendOpenConfirmation()     // Catch: java.lang.Exception -> L93
            java.lang.Thread r0 = java.lang.Thread.currentThread()
            r7.thread = r0
            com.jcraft.jsch.Buffer r0 = new com.jcraft.jsch.Buffer
            int r1 = r7.rmpsize
            r0.<init>(r1)
            com.jcraft.jsch.Packet r1 = new com.jcraft.jsch.Packet
            r1.<init>(r0)
            r2 = 0
        L46:
            java.lang.Thread r3 = r7.thread     // Catch: java.lang.Exception -> L8e
            if (r3 == 0) goto L8d
            com.jcraft.jsch.IO r3 = r7.f211io     // Catch: java.lang.Exception -> L8e
            if (r3 == 0) goto L8d
            com.jcraft.jsch.IO r3 = r7.f211io     // Catch: java.lang.Exception -> L8e
            java.io.InputStream r3 = r3.in     // Catch: java.lang.Exception -> L8e
            if (r3 == 0) goto L8d
            com.jcraft.jsch.IO r3 = r7.f211io     // Catch: java.lang.Exception -> L8e
            java.io.InputStream r3 = r3.in     // Catch: java.lang.Exception -> L8e
            byte[] r4 = r0.buffer     // Catch: java.lang.Exception -> L8e
            byte[] r5 = r0.buffer     // Catch: java.lang.Exception -> L8e
            int r5 = r5.length     // Catch: java.lang.Exception -> L8e
            r6 = 14
            int r5 = r5 - r6
            int r5 = r5 + (-128)
            int r3 = r3.read(r4, r6, r5)     // Catch: java.lang.Exception -> L8e
            r2 = r3
            if (r2 > 0) goto L6d
            r7.eof()     // Catch: java.lang.Exception -> L8e
            goto L8d
        L6d:
            boolean r3 = r7.close     // Catch: java.lang.Exception -> L8e
            if (r3 == 0) goto L72
            goto L8d
        L72:
            r1.reset()     // Catch: java.lang.Exception -> L8e
            r3 = 94
            r0.putByte(r3)     // Catch: java.lang.Exception -> L8e
            int r3 = r7.recipient     // Catch: java.lang.Exception -> L8e
            r0.putInt(r3)     // Catch: java.lang.Exception -> L8e
            r0.putInt(r2)     // Catch: java.lang.Exception -> L8e
            r0.skip(r2)     // Catch: java.lang.Exception -> L8e
            com.jcraft.jsch.Session r3 = r7.getSession()     // Catch: java.lang.Exception -> L8e
            r3.write(r1, r7, r2)     // Catch: java.lang.Exception -> L8e
            goto L46
        L8d:
            goto L8f
        L8e:
            r3 = move-exception
        L8f:
            r7.disconnect()
            return
        L93:
            r1 = move-exception
            r7.sendOpenFailure(r0)
            r7.close = r0
            r7.disconnect()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.jcraft.jsch.ChannelX11.run():void");
    }

    private byte[] addCache(byte[] foo, int s, int l) {
        byte[] bArr = this.cache;
        byte[] bar = new byte[bArr.length + l];
        System.arraycopy(foo, s, bar, bArr.length, l);
        byte[] bArr2 = this.cache;
        if (bArr2.length > 0) {
            System.arraycopy(bArr2, 0, bar, 0, bArr2.length);
        }
        this.cache = bar;
        return this.cache;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.jcraft.jsch.Channel
    public void write(byte[] foo, int s, int l) throws IOException {
        int plen;
        int dlen;
        byte[] faked_cookie;
        if (this.init) {
            try {
                Session _session = getSession();
                byte[] foo2 = addCache(foo, s, l);
                int l2 = foo2.length;
                if (l2 >= 9) {
                    int plen2 = ((foo2[0 + 6] & 255) * 256) + (foo2[0 + 7] & 255);
                    int dlen2 = ((foo2[0 + 8] & 255) * 256) + (foo2[0 + 9] & 255);
                    if ((foo2[0] & 255) != 66 && (foo2[0] & 255) == 108) {
                        plen = ((plen2 >>> 8) & 255) | ((plen2 << 8) & MotionEventCompat.ACTION_POINTER_INDEX_MASK);
                        dlen = ((dlen2 >>> 8) & 255) | ((dlen2 << 8) & MotionEventCompat.ACTION_POINTER_INDEX_MASK);
                    } else {
                        plen = plen2;
                        dlen = dlen2;
                    }
                    if (l2 < plen + 12 + ((-plen) & 3) + dlen) {
                        return;
                    }
                    byte[] bar = new byte[dlen];
                    System.arraycopy(foo2, 0 + 12 + plen + ((-plen) & 3), bar, 0, dlen);
                    synchronized (faked_cookie_pool) {
                        faked_cookie = (byte[]) faked_cookie_pool.get(_session);
                    }
                    if (equals(bar, faked_cookie)) {
                        byte[] bArr = cookie;
                        if (bArr != null) {
                            System.arraycopy(bArr, 0, foo2, 0 + 12 + plen + ((-plen) & 3), dlen);
                        }
                    } else {
                        this.thread = null;
                        eof();
                        this.f211io.close();
                        disconnect();
                    }
                    this.init = false;
                    this.f211io.put(foo2, 0, l2);
                    this.cache = null;
                    return;
                }
                return;
            } catch (JSchException e) {
                throw new IOException(e.toString());
            }
        }
        this.f211io.put(foo, s, l);
    }

    private static boolean equals(byte[] foo, byte[] bar) {
        if (foo.length != bar.length) {
            return false;
        }
        for (int i = 0; i < foo.length; i++) {
            if (foo[i] != bar[i]) {
                return false;
            }
        }
        return true;
    }
}
