package com.jcraft.jsch;

import androidx.core.view.MotionEventCompat;
import androidx.core.view.ViewCompat;
import com.xiaopeng.commonfunc.bean.factorytest.TestResultItem;
import com.xiaopeng.factory.presenter.factorytest.hardwaretest.wlan.WlanPresenter;
import java.io.PrintStream;
/* loaded from: classes.dex */
public class DHG1 extends KeyExchange {
    private static final int SSH_MSG_KEXDH_INIT = 30;
    private static final int SSH_MSG_KEXDH_REPLY = 31;
    static final byte[] g = {2};
    static final byte[] p = {0, -1, -1, -1, -1, -1, -1, -1, -1, -55, 15, -38, -94, WlanPresenter.WL_START_RF, 104, -62, 52, -60, -58, 98, -117, Byte.MIN_VALUE, -36, 28, -47, 41, 2, TestResultItem.RESULT_NOTEST, 8, -118, 103, -52, 116, 2, 11, -66, -90, 59, 19, -101, WlanPresenter.WL_TX_NF3219, 81, 74, 8, 121, -114, 52, 4, -35, -17, -107, 25, -77, -51, 58, 67, 27, 48, 43, 10, 109, -14, 95, 20, 55, 79, -31, 53, 109, 109, 81, -62, TestResultItem.RESULT_ENTER, -28, -123, -75, 118, 98, 94, 126, -58, -12, 76, 66, -23, -90, 55, -19, 107, 11, -1, 92, -74, -12, 6, -73, -19, -18, 56, 107, -5, 90, -119, -97, -91, -82, -97, 36, 17, 124, 75, 31, -26, 73, 40, 102, 81, -20, -26, 83, -127, -1, -1, -1, -1, -1, -1, -1, -1};
    byte[] I_C;
    byte[] I_S;
    byte[] V_C;
    byte[] V_S;
    private Buffer buf;
    DH dh;
    byte[] e;
    private Packet packet;
    private int state;

    @Override // com.jcraft.jsch.KeyExchange
    public void init(Session session, byte[] V_S, byte[] V_C, byte[] I_S, byte[] I_C) throws Exception {
        this.session = session;
        this.V_S = V_S;
        this.V_C = V_C;
        this.I_S = I_S;
        this.I_C = I_C;
        try {
            Class c = Class.forName(session.getConfig("sha-1"));
            this.sha = (HASH) c.newInstance();
            this.sha.init();
        } catch (Exception e) {
            System.err.println(e);
        }
        this.buf = new Buffer();
        this.packet = new Packet(this.buf);
        try {
            Class c2 = Class.forName(session.getConfig("dh"));
            this.dh = (DH) c2.newInstance();
            this.dh.init();
            this.dh.setP(p);
            this.dh.setG(g);
            this.e = this.dh.getE();
            this.packet.reset();
            this.buf.putByte((byte) 30);
            this.buf.putMPInt(this.e);
            session.write(this.packet);
            if (JSch.getLogger().isEnabled(1)) {
                JSch.getLogger().log(1, "SSH_MSG_KEXDH_INIT sent");
                JSch.getLogger().log(1, "expecting SSH_MSG_KEXDH_REPLY");
            }
            this.state = 31;
        } catch (Exception e2) {
            throw e2;
        }
    }

    @Override // com.jcraft.jsch.KeyExchange
    public boolean next(Buffer _buf) throws Exception {
        if (this.state != 31) {
            return false;
        }
        _buf.getInt();
        _buf.getByte();
        int j = _buf.getByte();
        if (j != 31) {
            PrintStream printStream = System.err;
            printStream.println("type: must be 31 " + j);
            return false;
        }
        this.K_S = _buf.getString();
        byte[] f = _buf.getMPInt();
        byte[] sig_of_H = _buf.getString();
        this.dh.setF(f);
        this.dh.checkRange();
        this.K = normalize(this.dh.getK());
        this.buf.reset();
        this.buf.putString(this.V_C);
        this.buf.putString(this.V_S);
        this.buf.putString(this.I_C);
        this.buf.putString(this.I_S);
        this.buf.putString(this.K_S);
        this.buf.putMPInt(this.e);
        this.buf.putMPInt(f);
        this.buf.putMPInt(this.K);
        byte[] foo = new byte[this.buf.getLength()];
        this.buf.getByte(foo);
        this.sha.update(foo, 0, foo.length);
        this.H = this.sha.digest();
        int i = 0 + 1;
        int i2 = i + 1;
        int i3 = i2 + 1;
        int i4 = i3 + 1;
        int j2 = ((this.K_S[0] << 24) & ViewCompat.MEASURED_STATE_MASK) | ((this.K_S[i] << 16) & 16711680) | ((this.K_S[i2] << 8) & MotionEventCompat.ACTION_POINTER_INDEX_MASK) | (this.K_S[i3] & 255);
        String alg = Util.byte2str(this.K_S, i4, j2);
        boolean result = verify(alg, this.K_S, i4 + j2, sig_of_H);
        this.state = 0;
        return result;
    }

    @Override // com.jcraft.jsch.KeyExchange
    public int getState() {
        return this.state;
    }
}
