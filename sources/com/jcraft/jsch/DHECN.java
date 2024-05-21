package com.jcraft.jsch;

import androidx.core.view.MotionEventCompat;
import androidx.core.view.ViewCompat;
import java.io.PrintStream;
/* loaded from: classes.dex */
public abstract class DHECN extends KeyExchange {
    private static final int SSH_MSG_KEX_ECDH_INIT = 30;
    private static final int SSH_MSG_KEX_ECDH_REPLY = 31;
    byte[] I_C;
    byte[] I_S;
    byte[] Q_C;
    byte[] V_C;
    byte[] V_S;
    private Buffer buf;
    byte[] e;
    private ECDH ecdh;
    protected int key_size;
    private Packet packet;
    protected String sha_name;
    private int state;

    @Override // com.jcraft.jsch.KeyExchange
    public void init(Session session, byte[] V_S, byte[] V_C, byte[] I_S, byte[] I_C) throws Exception {
        this.session = session;
        this.V_S = V_S;
        this.V_C = V_C;
        this.I_S = I_S;
        this.I_C = I_C;
        try {
            Class c = Class.forName(session.getConfig(this.sha_name));
            this.sha = (HASH) c.newInstance();
            this.sha.init();
        } catch (Exception e) {
            System.err.println(e);
        }
        this.buf = new Buffer();
        this.packet = new Packet(this.buf);
        this.packet.reset();
        this.buf.putByte((byte) 30);
        try {
            Class c2 = Class.forName(session.getConfig("ecdh-sha2-nistp"));
            this.ecdh = (ECDH) c2.newInstance();
            this.ecdh.init(this.key_size);
            this.Q_C = this.ecdh.getQ();
            this.buf.putString(this.Q_C);
            if (V_S == null) {
                return;
            }
            session.write(this.packet);
            if (JSch.getLogger().isEnabled(1)) {
                JSch.getLogger().log(1, "SSH_MSG_KEX_ECDH_INIT sent");
                JSch.getLogger().log(1, "expecting SSH_MSG_KEX_ECDH_REPLY");
            }
            this.state = 31;
        } catch (Exception e2) {
            if (e2 instanceof Throwable) {
                throw new JSchException(e2.toString(), e2);
            }
            throw new JSchException(e2.toString());
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
        byte[] Q_S = _buf.getString();
        byte[][] r_s = KeyPairECDSA.fromPoint(Q_S);
        if (this.ecdh.validate(r_s[0], r_s[1])) {
            this.K = this.ecdh.getSecret(r_s[0], r_s[1]);
            this.K = normalize(this.K);
            byte[] sig_of_H = _buf.getString();
            this.buf.reset();
            this.buf.putString(this.V_C);
            this.buf.putString(this.V_S);
            this.buf.putString(this.I_C);
            this.buf.putString(this.I_S);
            this.buf.putString(this.K_S);
            this.buf.putString(this.Q_C);
            this.buf.putString(Q_S);
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
        return false;
    }

    @Override // com.jcraft.jsch.KeyExchange
    public int getState() {
        return this.state;
    }
}
