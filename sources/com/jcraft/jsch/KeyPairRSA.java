package com.jcraft.jsch;

import java.math.BigInteger;
/* loaded from: classes.dex */
public class KeyPairRSA extends KeyPair {
    private static final byte[] begin = Util.str2byte("-----BEGIN RSA PRIVATE KEY-----");
    private static final byte[] end = Util.str2byte("-----END RSA PRIVATE KEY-----");
    private static final byte[] sshrsa = Util.str2byte("ssh-rsa");
    private byte[] c_array;
    private byte[] ep_array;
    private byte[] eq_array;
    private int key_size;
    private byte[] n_array;
    private byte[] p_array;
    private byte[] prv_array;
    private byte[] pub_array;
    private byte[] q_array;

    public KeyPairRSA(JSch jsch) {
        this(jsch, null, null, null);
    }

    public KeyPairRSA(JSch jsch, byte[] n_array, byte[] pub_array, byte[] prv_array) {
        super(jsch);
        this.key_size = 1024;
        this.n_array = n_array;
        this.pub_array = pub_array;
        this.prv_array = prv_array;
        if (n_array != null) {
            this.key_size = new BigInteger(n_array).bitLength();
        }
    }

    @Override // com.jcraft.jsch.KeyPair
    void generate(int key_size) throws JSchException {
        this.key_size = key_size;
        try {
            JSch jSch = this.jsch;
            Class c = Class.forName(JSch.getConfig("keypairgen.rsa"));
            KeyPairGenRSA keypairgen = (KeyPairGenRSA) c.newInstance();
            keypairgen.init(key_size);
            this.pub_array = keypairgen.getE();
            this.prv_array = keypairgen.getD();
            this.n_array = keypairgen.getN();
            this.p_array = keypairgen.getP();
            this.q_array = keypairgen.getQ();
            this.ep_array = keypairgen.getEP();
            this.eq_array = keypairgen.getEQ();
            this.c_array = keypairgen.getC();
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
        int content = countLength(1) + 1 + 1 + 1 + countLength(this.n_array.length) + this.n_array.length + 1 + countLength(this.pub_array.length) + this.pub_array.length + 1 + countLength(this.prv_array.length) + this.prv_array.length + 1 + countLength(this.p_array.length) + this.p_array.length + 1 + countLength(this.q_array.length) + this.q_array.length + 1 + countLength(this.ep_array.length) + this.ep_array.length + 1 + countLength(this.eq_array.length) + this.eq_array.length + 1 + countLength(this.c_array.length) + this.c_array.length;
        int total = countLength(content) + 1 + content;
        byte[] plain = new byte[total];
        int index = writeSEQUENCE(plain, 0, content);
        writeINTEGER(plain, writeINTEGER(plain, writeINTEGER(plain, writeINTEGER(plain, writeINTEGER(plain, writeINTEGER(plain, writeINTEGER(plain, writeINTEGER(plain, writeINTEGER(plain, index, new byte[1]), this.n_array), this.pub_array), this.prv_array), this.p_array), this.q_array), this.ep_array), this.eq_array), this.c_array);
        return plain;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.jcraft.jsch.KeyPair
    public boolean parse(byte[] plain) {
        try {
            if (this.vendor == 2) {
                Buffer buf = new Buffer(plain);
                buf.skip(plain.length);
                try {
                    byte[][] tmp = buf.getBytes(4, "");
                    this.prv_array = tmp[0];
                    this.p_array = tmp[1];
                    this.q_array = tmp[2];
                    this.c_array = tmp[3];
                    getEPArray();
                    getEQArray();
                    return true;
                } catch (JSchException e) {
                    return false;
                }
            } else if (this.vendor == 1) {
                if (plain[0] == 48) {
                    return false;
                }
                Buffer buf2 = new Buffer(plain);
                this.pub_array = buf2.getMPIntBits();
                this.prv_array = buf2.getMPIntBits();
                this.n_array = buf2.getMPIntBits();
                buf2.getMPIntBits();
                this.p_array = buf2.getMPIntBits();
                this.q_array = buf2.getMPIntBits();
                if (this.n_array != null) {
                    this.key_size = new BigInteger(this.n_array).bitLength();
                }
                getEPArray();
                getEQArray();
                getCArray();
                return true;
            } else {
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
                this.n_array = new byte[length4];
                System.arraycopy(plain, index6, this.n_array, 0, length4);
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
                this.pub_array = new byte[length5];
                System.arraycopy(plain, index8, this.pub_array, 0, length5);
                int index9 = index8 + length5 + 1;
                int index10 = index9 + 1;
                int length6 = plain[index9] & 255;
                if ((length6 & 128) != 0) {
                    int foo9 = length6 & 127;
                    length6 = 0;
                    while (true) {
                        int foo10 = foo9 - 1;
                        if (foo9 <= 0) {
                            break;
                        }
                        length6 = (length6 << 8) + (plain[index10] & 255);
                        foo9 = foo10;
                        index10++;
                    }
                }
                this.prv_array = new byte[length6];
                System.arraycopy(plain, index10, this.prv_array, 0, length6);
                int index11 = index10 + length6 + 1;
                int index12 = index11 + 1;
                int length7 = plain[index11] & 255;
                if ((length7 & 128) != 0) {
                    int foo11 = length7 & 127;
                    length7 = 0;
                    while (true) {
                        int foo12 = foo11 - 1;
                        if (foo11 <= 0) {
                            break;
                        }
                        length7 = (length7 << 8) + (plain[index12] & 255);
                        foo11 = foo12;
                        index12++;
                    }
                }
                this.p_array = new byte[length7];
                System.arraycopy(plain, index12, this.p_array, 0, length7);
                int index13 = index12 + length7 + 1;
                int index14 = index13 + 1;
                int length8 = plain[index13] & 255;
                if ((length8 & 128) != 0) {
                    int foo13 = length8 & 127;
                    length8 = 0;
                    while (true) {
                        int foo14 = foo13 - 1;
                        if (foo13 <= 0) {
                            break;
                        }
                        length8 = (length8 << 8) + (plain[index14] & 255);
                        foo13 = foo14;
                        index14++;
                    }
                }
                this.q_array = new byte[length8];
                System.arraycopy(plain, index14, this.q_array, 0, length8);
                int index15 = index14 + length8 + 1;
                int index16 = index15 + 1;
                int length9 = plain[index15] & 255;
                if ((length9 & 128) != 0) {
                    int foo15 = length9 & 127;
                    length9 = 0;
                    while (true) {
                        int foo16 = foo15 - 1;
                        if (foo15 <= 0) {
                            break;
                        }
                        length9 = (length9 << 8) + (plain[index16] & 255);
                        foo15 = foo16;
                        index16++;
                    }
                }
                this.ep_array = new byte[length9];
                System.arraycopy(plain, index16, this.ep_array, 0, length9);
                int index17 = index16 + length9 + 1;
                int index18 = index17 + 1;
                int length10 = plain[index17] & 255;
                if ((length10 & 128) != 0) {
                    int foo17 = length10 & 127;
                    length10 = 0;
                    while (true) {
                        int foo18 = foo17 - 1;
                        if (foo17 <= 0) {
                            break;
                        }
                        length10 = (length10 << 8) + (plain[index18] & 255);
                        foo17 = foo18;
                        index18++;
                    }
                }
                this.eq_array = new byte[length10];
                System.arraycopy(plain, index18, this.eq_array, 0, length10);
                int index19 = index18 + length10 + 1;
                int index20 = index19 + 1;
                int length11 = plain[index19] & 255;
                if ((length11 & 128) != 0) {
                    int foo19 = length11 & 127;
                    length11 = 0;
                    while (true) {
                        int foo20 = foo19 - 1;
                        if (foo19 <= 0) {
                            break;
                        }
                        length11 = (length11 << 8) + (plain[index20] & 255);
                        foo19 = foo20;
                        index20++;
                    }
                }
                this.c_array = new byte[length11];
                System.arraycopy(plain, index20, this.c_array, 0, length11);
                int i = index20 + length11;
                if (this.n_array != null) {
                    this.key_size = new BigInteger(this.n_array).bitLength();
                }
                return true;
            }
        } catch (Exception e2) {
            return false;
        }
    }

    @Override // com.jcraft.jsch.KeyPair
    public byte[] getPublicKeyBlob() {
        byte[] foo = super.getPublicKeyBlob();
        if (foo != null) {
            return foo;
        }
        byte[] bArr = this.pub_array;
        if (bArr == null) {
            return null;
        }
        byte[][] tmp = {sshrsa, bArr, this.n_array};
        return Buffer.fromBytes(tmp).buffer;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.jcraft.jsch.KeyPair
    public byte[] getKeyTypeName() {
        return sshrsa;
    }

    @Override // com.jcraft.jsch.KeyPair
    public int getKeyType() {
        return 2;
    }

    @Override // com.jcraft.jsch.KeyPair
    public int getKeySize() {
        return this.key_size;
    }

    @Override // com.jcraft.jsch.KeyPair
    public byte[] getSignature(byte[] data) {
        try {
            JSch jSch = this.jsch;
            Class c = Class.forName(JSch.getConfig("signature.rsa"));
            SignatureRSA rsa = (SignatureRSA) c.newInstance();
            rsa.init();
            rsa.setPrvKey(this.prv_array, this.n_array);
            rsa.update(data);
            byte[] sig = rsa.sign();
            byte[][] tmp = {sshrsa, sig};
            return Buffer.fromBytes(tmp).buffer;
        } catch (Exception e) {
            return null;
        }
    }

    @Override // com.jcraft.jsch.KeyPair
    public Signature getVerifier() {
        try {
            JSch jSch = this.jsch;
            Class c = Class.forName(JSch.getConfig("signature.rsa"));
            SignatureRSA rsa = (SignatureRSA) c.newInstance();
            rsa.init();
            if (this.pub_array == null && this.n_array == null && getPublicKeyBlob() != null) {
                Buffer buf = new Buffer(getPublicKeyBlob());
                buf.getString();
                this.pub_array = buf.getString();
                this.n_array = buf.getString();
            }
            rsa.setPubKey(this.pub_array, this.n_array);
            return rsa;
        } catch (Exception e) {
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static KeyPair fromSSHAgent(JSch jsch, Buffer buf) throws JSchException {
        byte[][] tmp = buf.getBytes(8, "invalid key format");
        byte[] n_array = tmp[1];
        byte[] pub_array = tmp[2];
        byte[] prv_array = tmp[3];
        KeyPairRSA kpair = new KeyPairRSA(jsch, n_array, pub_array, prv_array);
        kpair.c_array = tmp[4];
        kpair.p_array = tmp[5];
        kpair.q_array = tmp[6];
        kpair.publicKeyComment = new String(tmp[7]);
        kpair.vendor = 0;
        return kpair;
    }

    @Override // com.jcraft.jsch.KeyPair
    public byte[] forSSHAgent() throws JSchException {
        if (isEncrypted()) {
            throw new JSchException("key is encrypted.");
        }
        Buffer buf = new Buffer();
        buf.putString(sshrsa);
        buf.putString(this.n_array);
        buf.putString(this.pub_array);
        buf.putString(this.prv_array);
        buf.putString(getCArray());
        buf.putString(this.p_array);
        buf.putString(this.q_array);
        buf.putString(Util.str2byte(this.publicKeyComment));
        byte[] result = new byte[buf.getLength()];
        buf.getByte(result, 0, result.length);
        return result;
    }

    private byte[] getEPArray() {
        if (this.ep_array == null) {
            this.ep_array = new BigInteger(this.prv_array).mod(new BigInteger(this.p_array).subtract(BigInteger.ONE)).toByteArray();
        }
        return this.ep_array;
    }

    private byte[] getEQArray() {
        if (this.eq_array == null) {
            this.eq_array = new BigInteger(this.prv_array).mod(new BigInteger(this.q_array).subtract(BigInteger.ONE)).toByteArray();
        }
        return this.eq_array;
    }

    private byte[] getCArray() {
        if (this.c_array == null) {
            this.c_array = new BigInteger(this.q_array).modInverse(new BigInteger(this.p_array)).toByteArray();
        }
        return this.c_array;
    }

    @Override // com.jcraft.jsch.KeyPair
    public void dispose() {
        super.dispose();
        Util.bzero(this.prv_array);
    }
}
