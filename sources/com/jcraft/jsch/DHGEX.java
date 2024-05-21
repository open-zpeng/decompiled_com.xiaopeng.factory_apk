package com.jcraft.jsch;

import androidx.core.view.MotionEventCompat;
import androidx.core.view.ViewCompat;
import com.xiaopeng.factory.presenter.factorytest.hardwaretest.wlan.WlanPresenter;
import java.io.PrintStream;
/* loaded from: classes.dex */
public class DHGEX extends KeyExchange {
    private static final int SSH_MSG_KEX_DH_GEX_GROUP = 31;
    private static final int SSH_MSG_KEX_DH_GEX_INIT = 32;
    private static final int SSH_MSG_KEX_DH_GEX_REPLY = 33;
    private static final int SSH_MSG_KEX_DH_GEX_REQUEST = 34;
    static int min = 1024;
    static int preferred = 1024;
    byte[] I_C;
    byte[] I_S;
    byte[] V_C;
    byte[] V_S;
    private Buffer buf;
    DH dh;
    private byte[] e;
    private byte[] g;
    private byte[] p;
    private Packet packet;
    private int state;
    int max = 1024;
    protected String hash = "sha-1";

    @Override // com.jcraft.jsch.KeyExchange
    public void init(Session session, byte[] V_S, byte[] V_C, byte[] I_S, byte[] I_C) throws Exception {
        this.session = session;
        this.V_S = V_S;
        this.V_C = V_C;
        this.I_S = I_S;
        this.I_C = I_C;
        try {
            this.sha = (HASH) Class.forName(session.getConfig(this.hash)).newInstance();
            this.sha.init();
        } catch (Exception e) {
            System.err.println(e);
        }
        this.buf = new Buffer();
        this.packet = new Packet(this.buf);
        try {
            Class c = Class.forName(session.getConfig("dh"));
            int check2048 = check2048(c, this.max);
            this.max = check2048;
            preferred = check2048;
            this.dh = (DH) c.newInstance();
            this.dh.init();
            this.packet.reset();
            this.buf.putByte(WlanPresenter.WL_TX_NF3219);
            this.buf.putInt(min);
            this.buf.putInt(preferred);
            this.buf.putInt(this.max);
            session.write(this.packet);
            if (JSch.getLogger().isEnabled(1)) {
                Logger logger = JSch.getLogger();
                logger.log(1, "SSH_MSG_KEX_DH_GEX_REQUEST(" + min + "<" + preferred + "<" + this.max + ") sent");
                JSch.getLogger().log(1, "expecting SSH_MSG_KEX_DH_GEX_GROUP");
            }
            this.state = 31;
        } catch (Exception e2) {
            throw e2;
        }
    }

    @Override // com.jcraft.jsch.KeyExchange
    public boolean next(Buffer _buf) throws Exception {
        int i = this.state;
        if (i == 31) {
            _buf.getInt();
            _buf.getByte();
            int j = _buf.getByte();
            if (j != 31) {
                PrintStream printStream = System.err;
                printStream.println("type: must be SSH_MSG_KEX_DH_GEX_GROUP " + j);
                return false;
            }
            this.p = _buf.getMPInt();
            this.g = _buf.getMPInt();
            this.dh.setP(this.p);
            this.dh.setG(this.g);
            this.e = this.dh.getE();
            this.packet.reset();
            this.buf.putByte(WlanPresenter.WL_RMMOD_INSMOD);
            this.buf.putMPInt(this.e);
            this.session.write(this.packet);
            if (JSch.getLogger().isEnabled(1)) {
                JSch.getLogger().log(1, "SSH_MSG_KEX_DH_GEX_INIT sent");
                JSch.getLogger().log(1, "expecting SSH_MSG_KEX_DH_GEX_REPLY");
            }
            this.state = 33;
            return true;
        } else if (i != 33) {
            return false;
        } else {
            _buf.getInt();
            _buf.getByte();
            int j2 = _buf.getByte();
            if (j2 != 33) {
                PrintStream printStream2 = System.err;
                printStream2.println("type: must be SSH_MSG_KEX_DH_GEX_REPLY " + j2);
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
            this.buf.putInt(min);
            this.buf.putInt(preferred);
            this.buf.putInt(this.max);
            this.buf.putMPInt(this.p);
            this.buf.putMPInt(this.g);
            this.buf.putMPInt(this.e);
            this.buf.putMPInt(f);
            this.buf.putMPInt(this.K);
            byte[] foo = new byte[this.buf.getLength()];
            this.buf.getByte(foo);
            this.sha.update(foo, 0, foo.length);
            this.H = this.sha.digest();
            int i2 = 0 + 1;
            int i3 = i2 + 1;
            int i4 = i3 + 1;
            int i5 = i4 + 1;
            int j3 = ((this.K_S[0] << 24) & ViewCompat.MEASURED_STATE_MASK) | ((this.K_S[i2] << 16) & 16711680) | ((this.K_S[i3] << 8) & MotionEventCompat.ACTION_POINTER_INDEX_MASK) | (this.K_S[i4] & 255);
            String alg = Util.byte2str(this.K_S, i5, j3);
            boolean result = verify(alg, this.K_S, i5 + j3, sig_of_H);
            this.state = 0;
            return result;
        }
    }

    @Override // com.jcraft.jsch.KeyExchange
    public int getState() {
        return this.state;
    }

    protected int check2048(Class c, int _max) throws Exception {
        DH dh = (DH) c.newInstance();
        dh.init();
        byte[] foo = new byte[257];
        foo[1] = -35;
        foo[256] = 115;
        dh.setP(foo);
        byte[] bar = {2};
        dh.setG(bar);
        try {
            dh.getE();
            return 2048;
        } catch (Exception e) {
            return _max;
        }
    }
}
