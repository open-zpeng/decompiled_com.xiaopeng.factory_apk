package com.jcraft.jsch;

import com.xiaopeng.factory.presenter.factorytest.hardwaretest.wlan.WlanPresenter;
import com.xiaopeng.lib.framework.moduleinterface.carcontroller.IInputController;
/* loaded from: classes.dex */
public class KeyPairECDSA extends KeyPair {
    private int key_size;
    private byte[] name;
    private byte[] prv_array;
    private byte[] r_array;
    private byte[] s_array;
    private static byte[][] oids = {new byte[]{6, 8, 42, -122, 72, -50, 61, 3, 1, 7}, new byte[]{6, 5, 43, -127, 4, 0, WlanPresenter.WL_TX_NF3219}, new byte[]{6, 5, 43, -127, 4, 0, WlanPresenter.WL_RX_NF3219}};
    private static String[] names = {"nistp256", "nistp384", "nistp521"};
    private static final byte[] begin = Util.str2byte("-----BEGIN EC PRIVATE KEY-----");
    private static final byte[] end = Util.str2byte("-----END EC PRIVATE KEY-----");

    public KeyPairECDSA(JSch jsch) {
        this(jsch, null, null, null, null);
    }

    public KeyPairECDSA(JSch jsch, byte[] pubkey) {
        this(jsch, null, null, null, null);
        if (pubkey != null) {
            byte[] name = new byte[8];
            System.arraycopy(pubkey, 11, name, 0, 8);
            if (Util.array_equals(name, Util.str2byte("nistp384"))) {
                this.key_size = 384;
                this.name = name;
            }
            if (Util.array_equals(name, Util.str2byte("nistp521"))) {
                this.key_size = IInputController.KEYCODE_KNOB_VOL_DOWN;
                this.name = name;
            }
        }
    }

    public KeyPairECDSA(JSch jsch, byte[] name, byte[] r_array, byte[] s_array, byte[] prv_array) {
        super(jsch);
        this.name = Util.str2byte(names[0]);
        int i = 256;
        this.key_size = 256;
        if (name != null) {
            this.name = name;
        }
        this.r_array = r_array;
        this.s_array = s_array;
        this.prv_array = prv_array;
        if (prv_array != null) {
            if (prv_array.length >= 64) {
                i = IInputController.KEYCODE_KNOB_VOL_DOWN;
            } else if (prv_array.length >= 48) {
                i = 384;
            }
            this.key_size = i;
        }
    }

    @Override // com.jcraft.jsch.KeyPair
    void generate(int key_size) throws JSchException {
        this.key_size = key_size;
        try {
            JSch jSch = this.jsch;
            Class c = Class.forName(JSch.getConfig("keypairgen.ecdsa"));
            KeyPairGenECDSA keypairgen = (KeyPairGenECDSA) c.newInstance();
            keypairgen.init(key_size);
            this.prv_array = keypairgen.getD();
            this.r_array = keypairgen.getR();
            this.s_array = keypairgen.getS();
            this.name = Util.str2byte(names[this.prv_array.length >= 64 ? (char) 2 : this.prv_array.length >= 48 ? (char) 1 : (char) 0]);
        } catch (Exception e) {
            if (e instanceof Throwable) {
                throw new JSchException(e.toString(), e);
            }
            throw new JSchException(e.toString());
        }
    }

    @Override // com.jcraft.jsch.KeyPair
    byte[] getBegin() {
        return begin;
    }

    @Override // com.jcraft.jsch.KeyPair
    byte[] getEnd() {
        return end;
    }

    @Override // com.jcraft.jsch.KeyPair
    byte[] getPrivateKey() {
        char c;
        byte[] tmp = {1};
        byte[][] bArr = oids;
        byte[] bArr2 = this.r_array;
        if (bArr2.length >= 64) {
            c = 2;
        } else {
            c = bArr2.length >= 48 ? (char) 1 : (char) 0;
        }
        byte[] oid = bArr[c];
        byte[] point = toPoint(this.r_array, this.s_array);
        int bar = ((point.length + 1) & 128) == 0 ? 3 : 4;
        byte[] foo = new byte[point.length + bar];
        System.arraycopy(point, 0, foo, bar, point.length);
        foo[0] = 3;
        if (bar == 3) {
            foo[1] = (byte) (point.length + 1);
        } else {
            foo[1] = -127;
            foo[2] = (byte) (point.length + 1);
        }
        int content = countLength(tmp.length) + 1 + tmp.length + 1 + countLength(this.prv_array.length) + this.prv_array.length + 1 + countLength(oid.length) + oid.length + 1 + countLength(foo.length) + foo.length;
        int total = countLength(content) + 1 + content;
        byte[] plain = new byte[total];
        int index = writeSEQUENCE(plain, 0, content);
        writeDATA(plain, (byte) -95, writeDATA(plain, (byte) -96, writeOCTETSTRING(plain, writeINTEGER(plain, index, tmp), this.prv_array), oid), foo);
        return plain;
    }

    @Override // com.jcraft.jsch.KeyPair
    boolean parse(byte[] plain) {
        try {
            if (this.vendor == 1 || this.vendor == 2 || plain[0] != 48) {
                return false;
            }
            int index = 0 + 1;
            int index2 = index + 1;
            int length = plain[index] & 255;
            if ((length & 128) != 0) {
                int foo = length & 127;
                int length2 = 0;
                while (true) {
                    int foo2 = foo - 1;
                    if (foo <= 0) {
                        break;
                    }
                    length2 = (length2 << 8) + (plain[index2] & 255);
                    foo = foo2;
                    index2++;
                }
            }
            if (plain[index2] != 2) {
                return false;
            }
            int index3 = index2 + 1;
            int index4 = index3 + 1;
            int length3 = plain[index3] & 255;
            if ((length3 & 128) != 0) {
                int foo3 = length3 & 127;
                length3 = 0;
                while (true) {
                    int foo4 = foo3 - 1;
                    if (foo3 <= 0) {
                        break;
                    }
                    length3 = (length3 << 8) + (plain[index4] & 255);
                    foo3 = foo4;
                    index4++;
                }
            }
            int index5 = index4 + length3 + 1;
            int index6 = index5 + 1;
            int length4 = plain[index5] & 255;
            if ((length4 & 128) != 0) {
                int foo5 = length4 & 127;
                length4 = 0;
                while (true) {
                    int foo6 = foo5 - 1;
                    if (foo5 <= 0) {
                        break;
                    }
                    length4 = (length4 << 8) + (plain[index6] & 255);
                    foo5 = foo6;
                    index6++;
                }
            }
            this.prv_array = new byte[length4];
            System.arraycopy(plain, index6, this.prv_array, 0, length4);
            int index7 = index6 + length4 + 1;
            int index8 = index7 + 1;
            int length5 = plain[index7] & 255;
            if ((length5 & 128) != 0) {
                int foo7 = length5 & 127;
                length5 = 0;
                while (true) {
                    int foo8 = foo7 - 1;
                    if (foo7 <= 0) {
                        break;
                    }
                    length5 = (length5 << 8) + (plain[index8] & 255);
                    foo7 = foo8;
                    index8++;
                }
            }
            byte[] oid_array = new byte[length5];
            System.arraycopy(plain, index8, oid_array, 0, length5);
            int index9 = index8 + length5;
            int i = 0;
            while (true) {
                if (i >= oids.length) {
                    break;
                } else if (!Util.array_equals(oids[i], oid_array)) {
                    i++;
                } else {
                    this.name = Util.str2byte(names[i]);
                    break;
                }
            }
            int index10 = index9 + 1;
            int index11 = index10 + 1;
            int length6 = plain[index10] & 255;
            if ((length6 & 128) != 0) {
                int foo9 = length6 & 127;
                length6 = 0;
                while (true) {
                    int foo10 = foo9 - 1;
                    if (foo9 <= 0) {
                        break;
                    }
                    length6 = (length6 << 8) + (plain[index11] & 255);
                    foo9 = foo10;
                    index11++;
                }
            }
            byte[] Q_array = new byte[length6];
            System.arraycopy(plain, index11, Q_array, 0, length6);
            int i2 = index11 + length6;
            byte[][] tmp = fromPoint(Q_array);
            this.r_array = tmp[0];
            this.s_array = tmp[1];
            if (this.prv_array != null) {
                this.key_size = this.prv_array.length >= 64 ? IInputController.KEYCODE_KNOB_VOL_DOWN : this.prv_array.length >= 48 ? 384 : 256;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override // com.jcraft.jsch.KeyPair
    public byte[] getPublicKeyBlob() {
        byte[] foo = super.getPublicKeyBlob();
        if (foo != null) {
            return foo;
        }
        if (this.r_array == null) {
            return null;
        }
        byte[] bArr = this.r_array;
        byte[][] tmp = {Util.str2byte("ecdsa-sha2-" + new String(this.name)), this.name, new byte[bArr.length + 1 + this.s_array.length]};
        tmp[2][0] = 4;
        System.arraycopy(bArr, 0, tmp[2], 1, bArr.length);
        byte[] bArr2 = this.s_array;
        System.arraycopy(bArr2, 0, tmp[2], this.r_array.length + 1, bArr2.length);
        return Buffer.fromBytes(tmp).buffer;
    }

    @Override // com.jcraft.jsch.KeyPair
    byte[] getKeyTypeName() {
        return Util.str2byte("ecdsa-sha2-" + new String(this.name));
    }

    @Override // com.jcraft.jsch.KeyPair
    public int getKeyType() {
        return 3;
    }

    @Override // com.jcraft.jsch.KeyPair
    public int getKeySize() {
        return this.key_size;
    }

    @Override // com.jcraft.jsch.KeyPair
    public byte[] getSignature(byte[] data) {
        try {
            JSch jSch = this.jsch;
            Class c = Class.forName(JSch.getConfig("ecdsa-sha2-" + new String(this.name)));
            SignatureECDSA ecdsa = (SignatureECDSA) c.newInstance();
            ecdsa.init();
            ecdsa.setPrvKey(this.prv_array);
            ecdsa.update(data);
            byte[] sig = ecdsa.sign();
            byte[][] tmp = {Util.str2byte("ecdsa-sha2-" + new String(this.name)), sig};
            return Buffer.fromBytes(tmp).buffer;
        } catch (Exception e) {
            return null;
        }
    }

    @Override // com.jcraft.jsch.KeyPair
    public Signature getVerifier() {
        try {
            JSch jSch = this.jsch;
            Class c = Class.forName(JSch.getConfig("ecdsa-sha2-" + new String(this.name)));
            SignatureECDSA ecdsa = (SignatureECDSA) c.newInstance();
            ecdsa.init();
            if (this.r_array == null && this.s_array == null && getPublicKeyBlob() != null) {
                Buffer buf = new Buffer(getPublicKeyBlob());
                buf.getString();
                buf.getString();
                byte[][] tmp = fromPoint(buf.getString());
                this.r_array = tmp[0];
                this.s_array = tmp[1];
            }
            ecdsa.setPubKey(this.r_array, this.s_array);
            return ecdsa;
        } catch (Exception e) {
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static KeyPair fromSSHAgent(JSch jsch, Buffer buf) throws JSchException {
        byte[][] tmp = buf.getBytes(5, "invalid key format");
        byte[] name = tmp[1];
        byte[][] foo = fromPoint(tmp[2]);
        byte[] r_array = foo[0];
        byte[] s_array = foo[1];
        byte[] prv_array = tmp[3];
        KeyPairECDSA kpair = new KeyPairECDSA(jsch, name, r_array, s_array, prv_array);
        kpair.publicKeyComment = new String(tmp[4]);
        kpair.vendor = 0;
        return kpair;
    }

    @Override // com.jcraft.jsch.KeyPair
    public byte[] forSSHAgent() throws JSchException {
        if (isEncrypted()) {
            throw new JSchException("key is encrypted.");
        }
        Buffer buf = new Buffer();
        buf.putString(Util.str2byte("ecdsa-sha2-" + new String(this.name)));
        buf.putString(this.name);
        buf.putString(toPoint(this.r_array, this.s_array));
        buf.putString(this.prv_array);
        buf.putString(Util.str2byte(this.publicKeyComment));
        byte[] result = new byte[buf.getLength()];
        buf.getByte(result, 0, result.length);
        return result;
    }

    static byte[] toPoint(byte[] r_array, byte[] s_array) {
        byte[] tmp = new byte[r_array.length + 1 + s_array.length];
        tmp[0] = 4;
        System.arraycopy(r_array, 0, tmp, 1, r_array.length);
        System.arraycopy(s_array, 0, tmp, r_array.length + 1, s_array.length);
        return tmp;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static byte[][] fromPoint(byte[] point) {
        int i = 0;
        while (point[i] != 4) {
            i++;
        }
        int i2 = i + 1;
        byte[] r_array = new byte[(point.length - i2) / 2];
        byte[] s_array = new byte[(point.length - i2) / 2];
        System.arraycopy(point, i2, r_array, 0, r_array.length);
        System.arraycopy(point, r_array.length + i2, s_array, 0, s_array.length);
        byte[][] tmp = {r_array, s_array};
        return tmp;
    }

    @Override // com.jcraft.jsch.KeyPair
    public void dispose() {
        super.dispose();
        Util.bzero(this.prv_array);
    }
}
