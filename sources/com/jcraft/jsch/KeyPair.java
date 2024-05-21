package com.jcraft.jsch;

import com.xiaopeng.commonfunc.Constant;
import com.xiaopeng.libconfig.ipc.AccountConfig;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Hashtable;
import java.util.Vector;
/* loaded from: classes.dex */
public abstract class KeyPair {
    public static final int DSA = 1;
    public static final int ECDSA = 3;
    public static final int ERROR = 0;
    public static final int RSA = 2;
    public static final int UNKNOWN = 4;
    static final int VENDOR_FSECURE = 1;
    static final int VENDOR_OPENSSH = 0;
    static final int VENDOR_PKCS8 = 3;
    static final int VENDOR_PUTTY = 2;
    private Cipher cipher;
    private HASH hash;
    JSch jsch;
    private byte[] passphrase;
    private Random random;
    private static final byte[] cr = Util.str2byte("\n");
    static byte[][] header = {Util.str2byte("Proc-Type: 4,ENCRYPTED"), Util.str2byte("DEK-Info: DES-EDE3-CBC,")};
    private static byte[] space = Util.str2byte(" ");
    private static final String[] header1 = {"PuTTY-User-Key-File-2: ", "Encryption: ", "Comment: ", "Public-Lines: "};
    private static final String[] header2 = {"Private-Lines: "};
    private static final String[] header3 = {"Private-MAC: "};
    int vendor = 0;
    protected String publicKeyComment = "no comment";
    protected boolean encrypted = false;
    protected byte[] data = null;
    private byte[] iv = null;
    private byte[] publickeyblob = null;

    public abstract byte[] forSSHAgent() throws JSchException;

    abstract void generate(int i) throws JSchException;

    abstract byte[] getBegin();

    abstract byte[] getEnd();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract int getKeySize();

    public abstract int getKeyType();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract byte[] getKeyTypeName();

    abstract byte[] getPrivateKey();

    public abstract byte[] getSignature(byte[] bArr);

    public abstract Signature getVerifier();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract boolean parse(byte[] bArr);

    public static KeyPair genKeyPair(JSch jsch, int type) throws JSchException {
        return genKeyPair(jsch, type, 1024);
    }

    public static KeyPair genKeyPair(JSch jsch, int type, int key_size) throws JSchException {
        KeyPair kpair = null;
        if (type == 1) {
            kpair = new KeyPairDSA(jsch);
        } else if (type == 2) {
            kpair = new KeyPairRSA(jsch);
        } else if (type == 3) {
            kpair = new KeyPairECDSA(jsch);
        }
        if (kpair != null) {
            kpair.generate(key_size);
        }
        return kpair;
    }

    public String getPublicKeyComment() {
        return this.publicKeyComment;
    }

    public void setPublicKeyComment(String publicKeyComment) {
        this.publicKeyComment = publicKeyComment;
    }

    public KeyPair(JSch jsch) {
        this.jsch = null;
        this.jsch = jsch;
    }

    public void writePrivateKey(OutputStream out) {
        writePrivateKey(out, (byte[]) null);
    }

    public void writePrivateKey(OutputStream out, byte[] passphrase) {
        if (passphrase == null) {
            passphrase = this.passphrase;
        }
        byte[] plain = getPrivateKey();
        byte[][] _iv = new byte[1];
        byte[] encoded = encrypt(plain, _iv, passphrase);
        if (encoded != plain) {
            Util.bzero(plain);
        }
        byte[] iv = _iv[0];
        byte[] prv = Util.toBase64(encoded, 0, encoded.length);
        try {
            out.write(getBegin());
            out.write(cr);
            if (passphrase != null) {
                out.write(header[0]);
                out.write(cr);
                out.write(header[1]);
                for (int i = 0; i < iv.length; i++) {
                    out.write(b2a((byte) ((iv[i] >>> 4) & 15)));
                    out.write(b2a((byte) (iv[i] & 15)));
                }
                out.write(cr);
                out.write(cr);
            }
            int i2 = 0;
            while (true) {
                if (i2 >= prv.length) {
                    break;
                } else if (i2 + 64 < prv.length) {
                    out.write(prv, i2, 64);
                    out.write(cr);
                    i2 += 64;
                } else {
                    out.write(prv, i2, prv.length - i2);
                    out.write(cr);
                    break;
                }
            }
            out.write(getEnd());
            out.write(cr);
        } catch (Exception e) {
        }
    }

    public byte[] getPublicKeyBlob() {
        return this.publickeyblob;
    }

    public void writePublicKey(OutputStream out, String comment) {
        byte[] pubblob = getPublicKeyBlob();
        byte[] pub = Util.toBase64(pubblob, 0, pubblob.length);
        try {
            out.write(getKeyTypeName());
            out.write(space);
            out.write(pub, 0, pub.length);
            out.write(space);
            out.write(Util.str2byte(comment));
            out.write(cr);
        } catch (Exception e) {
        }
    }

    public void writePublicKey(String name, String comment) throws FileNotFoundException, IOException {
        FileOutputStream fos = new FileOutputStream(name);
        writePublicKey(fos, comment);
        fos.close();
    }

    public void writeSECSHPublicKey(OutputStream out, String comment) {
        byte[] pubblob = getPublicKeyBlob();
        int index = 0;
        byte[] pub = Util.toBase64(pubblob, 0, pubblob.length);
        try {
            out.write(Util.str2byte("---- BEGIN SSH2 PUBLIC KEY ----"));
            out.write(cr);
            out.write(Util.str2byte("Comment: \"" + comment + Constant.DOUBLE_QUOTA));
            out.write(cr);
            while (index < pub.length) {
                int len = 70;
                if (pub.length - index < 70) {
                    len = pub.length - index;
                }
                out.write(pub, index, len);
                out.write(cr);
                index += len;
            }
            out.write(Util.str2byte("---- END SSH2 PUBLIC KEY ----"));
            out.write(cr);
        } catch (Exception e) {
        }
    }

    public void writeSECSHPublicKey(String name, String comment) throws FileNotFoundException, IOException {
        FileOutputStream fos = new FileOutputStream(name);
        writeSECSHPublicKey(fos, comment);
        fos.close();
    }

    public void writePrivateKey(String name) throws FileNotFoundException, IOException {
        writePrivateKey(name, (byte[]) null);
    }

    public void writePrivateKey(String name, byte[] passphrase) throws FileNotFoundException, IOException {
        FileOutputStream fos = new FileOutputStream(name);
        writePrivateKey(fos, passphrase);
        fos.close();
    }

    public String getFingerPrint() {
        if (this.hash == null) {
            this.hash = genHash();
        }
        byte[] kblob = getPublicKeyBlob();
        if (kblob == null) {
            return null;
        }
        return Util.getFingerPrint(this.hash, kblob);
    }

    private byte[] encrypt(byte[] plain, byte[][] _iv, byte[] passphrase) {
        if (passphrase == null) {
            return plain;
        }
        if (this.cipher == null) {
            this.cipher = genCipher();
        }
        byte[] iv = new byte[this.cipher.getIVSize()];
        _iv[0] = iv;
        if (this.random == null) {
            this.random = genRandom();
        }
        this.random.fill(iv, 0, iv.length);
        byte[] key = genKey(passphrase, iv);
        int bsize = this.cipher.getIVSize();
        byte[] foo = new byte[((plain.length / bsize) + 1) * bsize];
        System.arraycopy(plain, 0, foo, 0, plain.length);
        int padding = bsize - (plain.length % bsize);
        int i = foo.length;
        while (true) {
            i--;
            if (foo.length - padding > i) {
                break;
            }
            foo[i] = (byte) padding;
        }
        try {
            this.cipher.init(0, key, iv);
            this.cipher.update(foo, 0, foo.length, foo, 0);
        } catch (Exception e) {
        }
        Util.bzero(key);
        return foo;
    }

    private byte[] decrypt(byte[] data, byte[] passphrase, byte[] iv) {
        try {
            byte[] key = genKey(passphrase, iv);
            this.cipher.init(1, key, iv);
            Util.bzero(key);
            byte[] plain = new byte[data.length];
            this.cipher.update(data, 0, data.length, plain, 0);
            return plain;
        } catch (Exception e) {
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int writeSEQUENCE(byte[] buf, int index, int len) {
        buf[index] = 48;
        return writeLength(buf, index + 1, len);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int writeINTEGER(byte[] buf, int index, byte[] data) {
        buf[index] = 2;
        int index2 = writeLength(buf, index + 1, data.length);
        System.arraycopy(data, 0, buf, index2, data.length);
        return index2 + data.length;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int writeOCTETSTRING(byte[] buf, int index, byte[] data) {
        buf[index] = 4;
        int index2 = writeLength(buf, index + 1, data.length);
        System.arraycopy(data, 0, buf, index2, data.length);
        return index2 + data.length;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int writeDATA(byte[] buf, byte n, int index, byte[] data) {
        buf[index] = n;
        int index2 = writeLength(buf, index + 1, data.length);
        System.arraycopy(data, 0, buf, index2, data.length);
        return index2 + data.length;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int countLength(int len) {
        int i = 1;
        if (len <= 127) {
            return 1;
        }
        while (len > 0) {
            len >>>= 8;
            i++;
        }
        return i;
    }

    int writeLength(byte[] data, int index, int len) {
        int index2;
        int i = countLength(len) - 1;
        if (i == 0) {
            int index3 = index + 1;
            data[index] = (byte) len;
            return index3;
        }
        data[index] = (byte) (i | 128);
        int j = index + 1 + i;
        while (i > 0) {
            data[(index2 + i) - 1] = (byte) (len & 255);
            len >>>= 8;
            i--;
        }
        return j;
    }

    private Random genRandom() {
        if (this.random == null) {
            try {
                JSch jSch = this.jsch;
                Class c = Class.forName(JSch.getConfig("random"));
                this.random = (Random) c.newInstance();
            } catch (Exception e) {
                PrintStream printStream = System.err;
                printStream.println("connect: random " + e);
            }
        }
        return this.random;
    }

    private HASH genHash() {
        try {
            JSch jSch = this.jsch;
            Class c = Class.forName(JSch.getConfig("md5"));
            this.hash = (HASH) c.newInstance();
            this.hash.init();
        } catch (Exception e) {
        }
        return this.hash;
    }

    private Cipher genCipher() {
        try {
            JSch jSch = this.jsch;
            Class c = Class.forName(JSch.getConfig("3des-cbc"));
            this.cipher = (Cipher) c.newInstance();
        } catch (Exception e) {
        }
        return this.cipher;
    }

    synchronized byte[] genKey(byte[] passphrase, byte[] iv) {
        byte[] key;
        if (this.cipher == null) {
            this.cipher = genCipher();
        }
        if (this.hash == null) {
            this.hash = genHash();
        }
        key = new byte[this.cipher.getBlockSize()];
        int hsize = this.hash.getBlockSize();
        byte[] hn = new byte[((key.length / hsize) * hsize) + (key.length % hsize == 0 ? 0 : hsize)];
        byte[] tmp = null;
        try {
            if (this.vendor == 0) {
                int index = 0;
                while (index + hsize <= hn.length) {
                    if (tmp != null) {
                        this.hash.update(tmp, 0, tmp.length);
                    }
                    this.hash.update(passphrase, 0, passphrase.length);
                    HASH hash = this.hash;
                    int i = 8;
                    if (iv.length <= 8) {
                        i = iv.length;
                    }
                    hash.update(iv, 0, i);
                    tmp = this.hash.digest();
                    System.arraycopy(tmp, 0, hn, index, tmp.length);
                    index += tmp.length;
                }
                int index2 = key.length;
                System.arraycopy(hn, 0, key, 0, index2);
            } else if (this.vendor == 1) {
                int index3 = 0;
                while (index3 + hsize <= hn.length) {
                    if (tmp != null) {
                        this.hash.update(tmp, 0, tmp.length);
                    }
                    this.hash.update(passphrase, 0, passphrase.length);
                    tmp = this.hash.digest();
                    System.arraycopy(tmp, 0, hn, index3, tmp.length);
                    index3 += tmp.length;
                }
                int index4 = key.length;
                System.arraycopy(hn, 0, key, 0, index4);
            } else if (this.vendor == 2) {
                JSch jSch = this.jsch;
                Class c = Class.forName(JSch.getConfig("sha-1"));
                HASH sha1 = (HASH) c.newInstance();
                byte[] tmp2 = new byte[4];
                key = new byte[40];
                for (int i2 = 0; i2 < 2; i2++) {
                    sha1.init();
                    tmp2[3] = (byte) i2;
                    sha1.update(tmp2, 0, tmp2.length);
                    sha1.update(passphrase, 0, passphrase.length);
                    System.arraycopy(sha1.digest(), 0, key, i2 * 20, 20);
                }
            }
        } catch (Exception e) {
            System.err.println(e);
        }
        return key;
    }

    public void setPassphrase(String passphrase) {
        if (passphrase == null || passphrase.length() == 0) {
            setPassphrase((byte[]) null);
        } else {
            setPassphrase(Util.str2byte(passphrase));
        }
    }

    public void setPassphrase(byte[] passphrase) {
        if (passphrase != null && passphrase.length == 0) {
            passphrase = null;
        }
        this.passphrase = passphrase;
    }

    public boolean isEncrypted() {
        return this.encrypted;
    }

    public boolean decrypt(String _passphrase) {
        if (_passphrase == null || _passphrase.length() == 0) {
            return !this.encrypted;
        }
        return decrypt(Util.str2byte(_passphrase));
    }

    public boolean decrypt(byte[] _passphrase) {
        boolean z = this.encrypted;
        if (z) {
            if (_passphrase == null) {
                return !z;
            }
            byte[] bar = new byte[_passphrase.length];
            System.arraycopy(_passphrase, 0, bar, 0, bar.length);
            byte[] foo = decrypt(this.data, bar, this.iv);
            Util.bzero(bar);
            if (parse(foo)) {
                this.encrypted = false;
            }
            return true ^ this.encrypted;
        }
        return true;
    }

    public static KeyPair load(JSch jsch, String prvkey) throws JSchException {
        String pubkey = prvkey + ".pub";
        if (!new File(pubkey).exists()) {
            pubkey = null;
        }
        return load(jsch, prvkey, pubkey);
    }

    public static KeyPair load(JSch jsch, String prvfile, String pubfile) throws JSchException {
        byte[] pubkey = null;
        try {
            byte[] prvkey = Util.fromFile(prvfile);
            String _pubfile = pubfile;
            if (pubfile == null) {
                _pubfile = prvfile + ".pub";
            }
            try {
                pubkey = Util.fromFile(_pubfile);
            } catch (IOException e) {
                if (pubfile != null) {
                    throw new JSchException(e.toString(), e);
                }
            }
            try {
                return load(jsch, prvkey, pubkey);
            } finally {
                Util.bzero(prvkey);
            }
        } catch (IOException e2) {
            throw new JSchException(e2.toString(), e2);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:273:0x04be, code lost:
        if (r0 == null) goto L142;
     */
    /* JADX WARN: Code restructure failed: missing block: B:274:0x04c0, code lost:
        if (r8 == 0) goto L493;
     */
    /* JADX WARN: Code restructure failed: missing block: B:275:0x04c2, code lost:
        r6 = r5;
     */
    /* JADX WARN: Code restructure failed: missing block: B:276:0x04c3, code lost:
        if (r5 >= r4) goto L492;
     */
    /* JADX WARN: Code restructure failed: missing block: B:279:0x04c9, code lost:
        if (r0[r5] != 45) goto L439;
     */
    /* JADX WARN: Code restructure failed: missing block: B:281:0x04cc, code lost:
        r5 = r5 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:283:0x04d1, code lost:
        if ((r4 - r5) == 0) goto L490;
     */
    /* JADX WARN: Code restructure failed: missing block: B:285:0x04d5, code lost:
        if ((r5 - r6) == 0) goto L490;
     */
    /* JADX WARN: Code restructure failed: missing block: B:287:0x04d9, code lost:
        r12 = new byte[r5 - r6];
        r21 = r4;
        java.lang.System.arraycopy(r0, r6, r12, 0, r12.length);
        r5 = 0;
        r13 = r12.length;
     */
    /* JADX WARN: Code restructure failed: missing block: B:288:0x04e6, code lost:
        if (r5 >= r13) goto L486;
     */
    /* JADX WARN: Code restructure failed: missing block: B:289:0x04e8, code lost:
        r22 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:290:0x04ec, code lost:
        r23 = r7;
     */
    /* JADX WARN: Code restructure failed: missing block: B:291:0x04f0, code lost:
        if (r12[r5] != 10) goto L452;
     */
    /* JADX WARN: Code restructure failed: missing block: B:295:0x04f8, code lost:
        if (r12[r5 - 1] != 13) goto L481;
     */
    /* JADX WARN: Code restructure failed: missing block: B:296:0x04fa, code lost:
        r0 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:297:0x04fc, code lost:
        r0 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:298:0x04fd, code lost:
        r7 = r5 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:299:0x04ff, code lost:
        if (r0 == false) goto L480;
     */
    /* JADX WARN: Code restructure failed: missing block: B:300:0x0501, code lost:
        r24 = 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:301:0x0504, code lost:
        r24 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:302:0x0506, code lost:
        r25 = r10;
        r26 = r12;
     */
    /* JADX WARN: Code restructure failed: missing block: B:303:0x0510, code lost:
        java.lang.System.arraycopy(r12, r7, r12, r5 - r24, r13 - (r5 + 1));
     */
    /* JADX WARN: Code restructure failed: missing block: B:304:0x0513, code lost:
        if (r0 == false) goto L478;
     */
    /* JADX WARN: Code restructure failed: missing block: B:305:0x0515, code lost:
        r13 = r13 - 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:306:0x0517, code lost:
        r13 = r13 - 1;
        r0 = r22;
        r7 = r23;
        r10 = r25;
        r12 = r26;
     */
    /* JADX WARN: Code restructure failed: missing block: B:307:0x0522, code lost:
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:309:0x0527, code lost:
        r25 = r10;
        r26 = r12;
     */
    /* JADX WARN: Code restructure failed: missing block: B:310:0x052f, code lost:
        if (r12[r5] != 45) goto L455;
     */
    /* JADX WARN: Code restructure failed: missing block: B:312:0x0532, code lost:
        r5 = r5 + 1;
        r0 = r22;
        r7 = r23;
        r10 = r25;
        r12 = r26;
     */
    /* JADX WARN: Code restructure failed: missing block: B:313:0x053d, code lost:
        r23 = r7;
        r25 = r10;
     */
    /* JADX WARN: Code restructure failed: missing block: B:315:0x0547, code lost:
        if ((r5 - 0) <= 0) goto L463;
     */
    /* JADX WARN: Code restructure failed: missing block: B:316:0x0549, code lost:
        r20 = com.jcraft.jsch.Util.fromBase64(r12, 0, r5 - 0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:317:0x0551, code lost:
        com.jcraft.jsch.Util.bzero(r12);
        r6 = r20;
     */
    /* JADX WARN: Code restructure failed: missing block: B:318:0x0557, code lost:
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:321:0x057a, code lost:
        throw new com.jcraft.jsch.JSchException("invalid privatekey: " + r28);
     */
    /* JADX WARN: Code restructure failed: missing block: B:323:0x0597, code lost:
        throw new com.jcraft.jsch.JSchException("invalid privatekey: " + r28);
     */
    /* JADX WARN: Code restructure failed: missing block: B:324:0x0598, code lost:
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:326:0x059b, code lost:
        r21 = r4;
        r23 = null;
        r25 = "";
        r6 = r20;
     */
    /* JADX WARN: Code restructure failed: missing block: B:327:0x05a5, code lost:
        if (r6 == null) goto L144;
     */
    /* JADX WARN: Code restructure failed: missing block: B:329:0x05a9, code lost:
        if (r6.length <= 4) goto L144;
     */
    /* JADX WARN: Code restructure failed: missing block: B:331:0x05b0, code lost:
        if (r6[0] != 63) goto L144;
     */
    /* JADX WARN: Code restructure failed: missing block: B:333:0x05b7, code lost:
        if (r6[1] != 111) goto L144;
     */
    /* JADX WARN: Code restructure failed: missing block: B:335:0x05bd, code lost:
        if (r6[2] != (-7)) goto L144;
     */
    /* JADX WARN: Code restructure failed: missing block: B:337:0x05c4, code lost:
        if (r6[3] != (-21)) goto L144;
     */
    /* JADX WARN: Code restructure failed: missing block: B:338:0x05c6, code lost:
        r0 = new com.jcraft.jsch.Buffer(r6);
        r0.getInt();
        r0.getInt();
        r0.getString();
        r7 = com.jcraft.jsch.Util.byte2str(r0.getString());
     */
    /* JADX WARN: Code restructure failed: missing block: B:339:0x05e3, code lost:
        if (r7.equals("3des-cbc") != false) goto L422;
     */
    /* JADX WARN: Code restructure failed: missing block: B:341:0x05eb, code lost:
        if (r7.equals(com.xiaopeng.libconfig.ipc.AccountConfig.FaceIDRegisterAction.ORIENTATION_NONE) == false) goto L144;
     */
    /* JADX WARN: Code restructure failed: missing block: B:342:0x05ed, code lost:
        r0.getInt();
        r0.getInt();
     */
    /* JADX WARN: Code restructure failed: missing block: B:343:0x05f3, code lost:
        r10 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:344:0x05f4, code lost:
        r12 = new byte[r6.length - r0.getOffSet()];
        r0.getByte(r12);
     */
    /* JADX WARN: Code restructure failed: missing block: B:345:0x05ff, code lost:
        r6 = r12;
     */
    /* JADX WARN: Code restructure failed: missing block: B:346:0x0601, code lost:
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:348:0x0607, code lost:
        r0.getInt();
        r10 = new byte[r6.length - r0.getOffSet()];
        r0.getByte(r10);
     */
    /* JADX WARN: Code restructure failed: missing block: B:349:0x0615, code lost:
        r6 = r10;
     */
    /* JADX WARN: Code restructure failed: missing block: B:351:0x062f, code lost:
        throw new com.jcraft.jsch.JSchException("unknown privatekey format: " + r28);
     */
    /* JADX WARN: Code restructure failed: missing block: B:352:0x0630, code lost:
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:354:0x0636, code lost:
        r0 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:359:0x063f, code lost:
        if ((r0 instanceof java.lang.Throwable) != false) goto L415;
     */
    /* JADX WARN: Code restructure failed: missing block: B:361:0x064a, code lost:
        throw new com.jcraft.jsch.JSchException(r0.toString(), r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:363:0x0654, code lost:
        throw new com.jcraft.jsch.JSchException(r0.toString());
     */
    /* JADX WARN: Code restructure failed: missing block: B:365:0x0658, code lost:
        throw ((com.jcraft.jsch.JSchException) r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:366:0x0659, code lost:
        r10 = r19;
     */
    /* JADX WARN: Code restructure failed: missing block: B:367:0x065b, code lost:
        if (r29 == null) goto L386;
     */
    /* JADX WARN: Code restructure failed: missing block: B:370:0x0660, code lost:
        r7 = r29.length;
     */
    /* JADX WARN: Code restructure failed: missing block: B:372:0x0663, code lost:
        if (r29.length <= 4) goto L269;
     */
    /* JADX WARN: Code restructure failed: missing block: B:374:0x066a, code lost:
        if (r29[0] != 45) goto L269;
     */
    /* JADX WARN: Code restructure failed: missing block: B:376:0x066f, code lost:
        if (r29[1] != 45) goto L269;
     */
    /* JADX WARN: Code restructure failed: missing block: B:378:0x0674, code lost:
        if (r29[2] != 45) goto L269;
     */
    /* JADX WARN: Code restructure failed: missing block: B:380:0x0679, code lost:
        if (r29[3] != 45) goto L269;
     */
    /* JADX WARN: Code restructure failed: missing block: B:381:0x067b, code lost:
        r0 = true;
        r5 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:382:0x067d, code lost:
        r5 = r5 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:383:0x0680, code lost:
        if (r29.length <= r5) goto L268;
     */
    /* JADX WARN: Code restructure failed: missing block: B:385:0x0686, code lost:
        if (r29[r5] != 10) goto L163;
     */
    /* JADX WARN: Code restructure failed: missing block: B:387:0x0689, code lost:
        if (r29.length > r5) goto L171;
     */
    /* JADX WARN: Code restructure failed: missing block: B:388:0x068b, code lost:
        r0 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:389:0x068c, code lost:
        if (r0 == false) goto L266;
     */
    /* JADX WARN: Code restructure failed: missing block: B:391:0x0692, code lost:
        if (r29[r5] != 10) goto L264;
     */
    /* JADX WARN: Code restructure failed: missing block: B:392:0x0694, code lost:
        r12 = false;
        r13 = r5 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:393:0x0697, code lost:
        r17 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:394:0x069a, code lost:
        if (r13 >= r29.length) goto L262;
     */
    /* JADX WARN: Code restructure failed: missing block: B:395:0x069c, code lost:
        r19 = r12;
     */
    /* JADX WARN: Code restructure failed: missing block: B:396:0x06a2, code lost:
        if (r29[r13] != 10) goto L180;
     */
    /* JADX WARN: Code restructure failed: missing block: B:399:0x06a9, code lost:
        if (r29[r13] != 58) goto L182;
     */
    /* JADX WARN: Code restructure failed: missing block: B:400:0x06ab, code lost:
        r19 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:401:0x06af, code lost:
        r13 = r13 + 1;
        r0 = r17;
        r12 = r19;
     */
    /* JADX WARN: Code restructure failed: missing block: B:402:0x06b6, code lost:
        r19 = r12;
     */
    /* JADX WARN: Code restructure failed: missing block: B:403:0x06b8, code lost:
        if (r19 != false) goto L258;
     */
    /* JADX WARN: Code restructure failed: missing block: B:404:0x06ba, code lost:
        r5 = r5 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:405:0x06bd, code lost:
        r17 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:406:0x06bf, code lost:
        r5 = r5 + 1;
        r0 = r17;
     */
    /* JADX WARN: Code restructure failed: missing block: B:407:0x06c4, code lost:
        r17 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:409:0x06c7, code lost:
        if (r29.length > r5) goto L257;
     */
    /* JADX WARN: Code restructure failed: missing block: B:410:0x06c9, code lost:
        r0 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:411:0x06cb, code lost:
        r0 = r17;
     */
    /* JADX WARN: Code restructure failed: missing block: B:412:0x06cd, code lost:
        r12 = r5;
     */
    /* JADX WARN: Code restructure failed: missing block: B:413:0x06ce, code lost:
        if (r0 == false) goto L256;
     */
    /* JADX WARN: Code restructure failed: missing block: B:414:0x06d0, code lost:
        if (r5 >= r7) goto L254;
     */
    /* JADX WARN: Code restructure failed: missing block: B:416:0x06d4, code lost:
        r19 = r15;
     */
    /* JADX WARN: Code restructure failed: missing block: B:417:0x06d8, code lost:
        if (r29[r5] != 10) goto L197;
     */
    /* JADX WARN: Code restructure failed: missing block: B:419:0x06e2, code lost:
        java.lang.System.arraycopy(r29, r5 + 1, r29, r5, (r7 - r5) - 1);
        r7 = r7 - 1;
        r15 = r19;
     */
    /* JADX WARN: Code restructure failed: missing block: B:421:0x06ee, code lost:
        if (r29[r5] != 45) goto L200;
     */
    /* JADX WARN: Code restructure failed: missing block: B:423:0x06f1, code lost:
        r5 = r5 + 1;
        r15 = r19;
     */
    /* JADX WARN: Code restructure failed: missing block: B:424:0x06f6, code lost:
        r19 = r15;
     */
    /* JADX WARN: Code restructure failed: missing block: B:425:0x06f8, code lost:
        if (r0 == false) goto L248;
     */
    /* JADX WARN: Code restructure failed: missing block: B:426:0x06fa, code lost:
        r13 = com.jcraft.jsch.Util.fromBase64(r29, r12, r5 - r12);
     */
    /* JADX WARN: Code restructure failed: missing block: B:427:0x0700, code lost:
        if (r28 == null) goto L235;
     */
    /* JADX WARN: Code restructure failed: missing block: B:429:0x0703, code lost:
        if (r8 != 4) goto L210;
     */
    /* JADX WARN: Code restructure failed: missing block: B:432:0x070d, code lost:
        if (r13[8] != 100) goto L240;
     */
    /* JADX WARN: Code restructure failed: missing block: B:433:0x070f, code lost:
        r8 = 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:436:0x0717, code lost:
        if (r13[8] != 114) goto L210;
     */
    /* JADX WARN: Code restructure failed: missing block: B:437:0x0719, code lost:
        r8 = 2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:439:0x071c, code lost:
        r23 = r13;
     */
    /* JADX WARN: Code restructure failed: missing block: B:440:0x0720, code lost:
        r13 = r23;
     */
    /* JADX WARN: Code restructure failed: missing block: B:441:0x0724, code lost:
        r23 = r13;
     */
    /* JADX WARN: Code restructure failed: missing block: B:442:0x0729, code lost:
        r19 = "invalid privatekey: ";
     */
    /* JADX WARN: Code restructure failed: missing block: B:444:0x0730, code lost:
        if (r29[0] != 115) goto L326;
     */
    /* JADX WARN: Code restructure failed: missing block: B:446:0x0737, code lost:
        if (r29[1] != 115) goto L326;
     */
    /* JADX WARN: Code restructure failed: missing block: B:448:0x073e, code lost:
        if (r29[2] != 104) goto L326;
     */
    /* JADX WARN: Code restructure failed: missing block: B:450:0x0745, code lost:
        if (r29[3] != 45) goto L326;
     */
    /* JADX WARN: Code restructure failed: missing block: B:451:0x0747, code lost:
        if (r28 != null) goto L288;
     */
    /* JADX WARN: Code restructure failed: missing block: B:453:0x074b, code lost:
        if (r29.length <= 7) goto L288;
     */
    /* JADX WARN: Code restructure failed: missing block: B:455:0x0752, code lost:
        if (r29[4] != 100) goto L285;
     */
    /* JADX WARN: Code restructure failed: missing block: B:456:0x0754, code lost:
        r8 = 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:458:0x075a, code lost:
        if (r29[4] != 114) goto L288;
     */
    /* JADX WARN: Code restructure failed: missing block: B:459:0x075c, code lost:
        r8 = 2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:460:0x075d, code lost:
        r5 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:461:0x075f, code lost:
        if (r5 >= r7) goto L325;
     */
    /* JADX WARN: Code restructure failed: missing block: B:463:0x0765, code lost:
        if (r29[r5] != 32) goto L292;
     */
    /* JADX WARN: Code restructure failed: missing block: B:465:0x0768, code lost:
        r5 = r5 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:466:0x076b, code lost:
        r5 = r5 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:467:0x076d, code lost:
        if (r5 >= r7) goto L307;
     */
    /* JADX WARN: Code restructure failed: missing block: B:469:0x0770, code lost:
        if (r5 >= r7) goto L306;
     */
    /* JADX WARN: Code restructure failed: missing block: B:471:0x0776, code lost:
        if (r29[r5] != 32) goto L301;
     */
    /* JADX WARN: Code restructure failed: missing block: B:473:0x0779, code lost:
        r5 = r5 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:475:0x0782, code lost:
        r23 = com.jcraft.jsch.Util.fromBase64(r29, r5, r5 - r5);
     */
    /* JADX WARN: Code restructure failed: missing block: B:476:0x0784, code lost:
        r0 = r5 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:477:0x0786, code lost:
        if (r5 >= r7) goto L324;
     */
    /* JADX WARN: Code restructure failed: missing block: B:478:0x0788, code lost:
        r12 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:479:0x078a, code lost:
        if (r12 >= r7) goto L323;
     */
    /* JADX WARN: Code restructure failed: missing block: B:481:0x0790, code lost:
        if (r29[r12] != 10) goto L313;
     */
    /* JADX WARN: Code restructure failed: missing block: B:483:0x0793, code lost:
        r12 = r12 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:486:0x079a, code lost:
        if (r12 <= 0) goto L320;
     */
    /* JADX WARN: Code restructure failed: missing block: B:488:0x07a2, code lost:
        if (r29[r12 - 1] != 13) goto L320;
     */
    /* JADX WARN: Code restructure failed: missing block: B:489:0x07a4, code lost:
        r12 = r12 - 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:490:0x07a6, code lost:
        if (r0 >= r12) goto L211;
     */
    /* JADX WARN: Code restructure failed: missing block: B:492:0x07af, code lost:
        r25 = new java.lang.String(r29, r0, r12 - r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:497:0x07bb, code lost:
        if (r29[0] != 101) goto L377;
     */
    /* JADX WARN: Code restructure failed: missing block: B:499:0x07c2, code lost:
        if (r29[1] != 99) goto L377;
     */
    /* JADX WARN: Code restructure failed: missing block: B:501:0x07c9, code lost:
        if (r29[2] != 100) goto L377;
     */
    /* JADX WARN: Code restructure failed: missing block: B:503:0x07d0, code lost:
        if (r29[3] != 115) goto L377;
     */
    /* JADX WARN: Code restructure failed: missing block: B:504:0x07d2, code lost:
        if (r28 != null) goto L339;
     */
    /* JADX WARN: Code restructure failed: missing block: B:506:0x07d6, code lost:
        if (r29.length <= 7) goto L339;
     */
    /* JADX WARN: Code restructure failed: missing block: B:507:0x07d8, code lost:
        r8 = 3;
     */
    /* JADX WARN: Code restructure failed: missing block: B:508:0x07d9, code lost:
        r5 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:509:0x07db, code lost:
        if (r5 >= r7) goto L376;
     */
    /* JADX WARN: Code restructure failed: missing block: B:511:0x07e1, code lost:
        if (r29[r5] != 32) goto L343;
     */
    /* JADX WARN: Code restructure failed: missing block: B:513:0x07e4, code lost:
        r5 = r5 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:514:0x07e7, code lost:
        r5 = r5 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:515:0x07e9, code lost:
        if (r5 >= r7) goto L358;
     */
    /* JADX WARN: Code restructure failed: missing block: B:517:0x07ec, code lost:
        if (r5 >= r7) goto L357;
     */
    /* JADX WARN: Code restructure failed: missing block: B:519:0x07f2, code lost:
        if (r29[r5] != 32) goto L352;
     */
    /* JADX WARN: Code restructure failed: missing block: B:521:0x07f5, code lost:
        r5 = r5 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:523:0x07fe, code lost:
        r23 = com.jcraft.jsch.Util.fromBase64(r29, r5, r5 - r5);
     */
    /* JADX WARN: Code restructure failed: missing block: B:524:0x0800, code lost:
        r0 = r5 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:525:0x0802, code lost:
        if (r5 >= r7) goto L375;
     */
    /* JADX WARN: Code restructure failed: missing block: B:526:0x0804, code lost:
        r12 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:527:0x0806, code lost:
        if (r12 >= r7) goto L374;
     */
    /* JADX WARN: Code restructure failed: missing block: B:529:0x080c, code lost:
        if (r29[r12] != 10) goto L364;
     */
    /* JADX WARN: Code restructure failed: missing block: B:531:0x080f, code lost:
        r12 = r12 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:532:0x0812, code lost:
        if (r12 <= 0) goto L371;
     */
    /* JADX WARN: Code restructure failed: missing block: B:534:0x081a, code lost:
        if (r29[r12 - 1] != 13) goto L371;
     */
    /* JADX WARN: Code restructure failed: missing block: B:535:0x081c, code lost:
        r12 = r12 - 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:536:0x081e, code lost:
        if (r0 >= r12) goto L211;
     */
    /* JADX WARN: Code restructure failed: missing block: B:538:0x0827, code lost:
        r25 = new java.lang.String(r29, r0, r12 - r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:541:0x082d, code lost:
        r0 = r23;
        r4 = r25;
     */
    /* JADX WARN: Code restructure failed: missing block: B:545:0x0835, code lost:
        r19 = r15;
     */
    /* JADX WARN: Code restructure failed: missing block: B:547:0x0839, code lost:
        r19 = "invalid privatekey: ";
     */
    /* JADX WARN: Code restructure failed: missing block: B:548:0x083d, code lost:
        r0 = r23;
        r4 = r25;
     */
    /* JADX WARN: Code restructure failed: missing block: B:549:0x0842, code lost:
        r19 = "invalid privatekey: ";
        r0 = r23;
        r4 = r25;
     */
    /* JADX WARN: Code restructure failed: missing block: B:550:0x0848, code lost:
        r5 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:551:0x084b, code lost:
        if (r8 != 1) goto L227;
     */
    /* JADX WARN: Code restructure failed: missing block: B:552:0x084d, code lost:
        r5 = new com.jcraft.jsch.KeyPairDSA(r27);
     */
    /* JADX WARN: Code restructure failed: missing block: B:554:0x0855, code lost:
        if (r8 == 2) goto L229;
     */
    /* JADX WARN: Code restructure failed: missing block: B:555:0x0857, code lost:
        r5 = new com.jcraft.jsch.KeyPairRSA(r27);
     */
    /* JADX WARN: Code restructure failed: missing block: B:557:0x085f, code lost:
        if (r8 == 3) goto L232;
     */
    /* JADX WARN: Code restructure failed: missing block: B:558:0x0861, code lost:
        r5 = new com.jcraft.jsch.KeyPairECDSA(r27, r29);
     */
    /* JADX WARN: Code restructure failed: missing block: B:559:0x0868, code lost:
        if (r9 == 3) goto L234;
     */
    /* JADX WARN: Code restructure failed: missing block: B:560:0x086a, code lost:
        r5 = new com.jcraft.jsch.KeyPairPKCS8(r27);
     */
    /* JADX WARN: Code restructure failed: missing block: B:561:0x0870, code lost:
        if (r5 != null) goto L216;
     */
    /* JADX WARN: Code restructure failed: missing block: B:562:0x0872, code lost:
        r5.encrypted = r10;
        r5.publickeyblob = r0;
        r5.vendor = r9;
        r5.publicKeyComment = r4;
        r5.cipher = r14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:563:0x087c, code lost:
        if (r10 != 0) goto L218;
     */
    /* JADX WARN: Code restructure failed: missing block: B:564:0x087e, code lost:
        r5.encrypted = true;
        r5.iv = r11;
        r5.data = r6;
     */
    /* JADX WARN: Code restructure failed: missing block: B:566:0x088a, code lost:
        if (r5.parse(r6) != false) goto L221;
     */
    /* JADX WARN: Code restructure failed: missing block: B:567:0x088c, code lost:
        r5.encrypted = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:568:0x088f, code lost:
        return r5;
     */
    /* JADX WARN: Code restructure failed: missing block: B:570:0x08a6, code lost:
        throw new com.jcraft.jsch.JSchException(r19 + r28);
     */
    /* JADX WARN: Code restructure failed: missing block: B:571:0x08a7, code lost:
        return r5;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:358:0x063d  */
    /* JADX WARN: Removed duplicated region for block: B:364:0x0655  */
    /* JADX WARN: Removed duplicated region for block: B:552:0x084d  */
    /* JADX WARN: Removed duplicated region for block: B:553:0x0854  */
    /* JADX WARN: Removed duplicated region for block: B:562:0x0872  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static com.jcraft.jsch.KeyPair load(com.jcraft.jsch.JSch r27, byte[] r28, byte[] r29) throws com.jcraft.jsch.JSchException {
        /*
            Method dump skipped, instructions count: 2216
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.jcraft.jsch.KeyPair.load(com.jcraft.jsch.JSch, byte[], byte[]):com.jcraft.jsch.KeyPair");
    }

    private static byte a2b(byte c) {
        return (48 > c || c > 57) ? (byte) ((c - 97) + 10) : (byte) (c - 48);
    }

    private static byte b2a(byte c) {
        return (c < 0 || c > 9) ? (byte) ((c - 10) + 65) : (byte) (c + 48);
    }

    public void dispose() {
        Util.bzero(this.passphrase);
    }

    public void finalize() {
        dispose();
    }

    static KeyPair loadPPK(JSch jsch, byte[] buf) throws JSchException {
        KeyPair kpair;
        Buffer buffer = new Buffer(buf);
        Hashtable v = new Hashtable();
        do {
        } while (parseHeader(buffer, v));
        String typ = (String) v.get("PuTTY-User-Key-File-2");
        if (typ == null) {
            return null;
        }
        int lines = Integer.parseInt((String) v.get("Public-Lines"));
        byte[] pubkey = parseLines(buffer, lines);
        do {
        } while (parseHeader(buffer, v));
        int lines2 = Integer.parseInt((String) v.get("Private-Lines"));
        byte[] prvkey = parseLines(buffer, lines2);
        do {
        } while (parseHeader(buffer, v));
        byte[] prvkey2 = Util.fromBase64(prvkey, 0, prvkey.length);
        byte[] pubkey2 = Util.fromBase64(pubkey, 0, pubkey.length);
        if (typ.equals("ssh-rsa")) {
            Buffer _buf = new Buffer(pubkey2);
            _buf.skip(pubkey2.length);
            int len = _buf.getInt();
            _buf.getByte(new byte[len]);
            byte[] pub_array = new byte[_buf.getInt()];
            _buf.getByte(pub_array);
            byte[] n_array = new byte[_buf.getInt()];
            _buf.getByte(n_array);
            KeyPair kpair2 = new KeyPairRSA(jsch, n_array, pub_array, null);
            kpair = kpair2;
        } else if (!typ.equals("ssh-dss")) {
            return null;
        } else {
            Buffer _buf2 = new Buffer(pubkey2);
            _buf2.skip(pubkey2.length);
            int len2 = _buf2.getInt();
            _buf2.getByte(new byte[len2]);
            byte[] p_array = new byte[_buf2.getInt()];
            _buf2.getByte(p_array);
            byte[] q_array = new byte[_buf2.getInt()];
            _buf2.getByte(q_array);
            byte[] g_array = new byte[_buf2.getInt()];
            _buf2.getByte(g_array);
            byte[] y_array = new byte[_buf2.getInt()];
            _buf2.getByte(y_array);
            KeyPair kpair3 = new KeyPairDSA(jsch, p_array, q_array, g_array, y_array, null);
            kpair = kpair3;
        }
        kpair.encrypted = !v.get("Encryption").equals(AccountConfig.FaceIDRegisterAction.ORIENTATION_NONE);
        kpair.vendor = 2;
        kpair.publicKeyComment = (String) v.get("Comment");
        if (kpair.encrypted) {
            if (Session.checkCipher(JSch.getConfig("aes256-cbc"))) {
                try {
                    Class c = Class.forName(JSch.getConfig("aes256-cbc"));
                    kpair.cipher = (Cipher) c.newInstance();
                    kpair.iv = new byte[kpair.cipher.getIVSize()];
                    kpair.data = prvkey2;
                } catch (Exception e) {
                    throw new JSchException("The cipher 'aes256-cbc' is required, but it is not available.");
                }
            } else {
                throw new JSchException("The cipher 'aes256-cbc' is required, but it is not available.");
            }
        } else {
            kpair.data = prvkey2;
            kpair.parse(prvkey2);
        }
        return kpair;
    }

    private static byte[] parseLines(Buffer buffer, int lines) {
        int i;
        byte[] buf = buffer.buffer;
        int index = buffer.index;
        byte[] data = null;
        int i2 = index;
        while (true) {
            int lines2 = lines - 1;
            if (lines <= 0) {
                break;
            }
            while (true) {
                if (buf.length <= i2) {
                    i = i2;
                    break;
                }
                i = i2 + 1;
                if (buf[i2] != 13) {
                    i2 = i;
                } else if (data == null) {
                    data = new byte[(i - index) - 1];
                    System.arraycopy(buf, index, data, 0, (i - index) - 1);
                } else {
                    byte[] tmp = new byte[((data.length + i) - index) - 1];
                    System.arraycopy(data, 0, tmp, 0, data.length);
                    System.arraycopy(buf, index, tmp, data.length, (i - index) - 1);
                    for (int j = 0; j < data.length; j++) {
                        data[j] = 0;
                    }
                    data = tmp;
                }
            }
            if (buf[i] == 10) {
                i++;
            }
            i2 = i;
            index = i2;
            lines = lines2;
        }
        if (data != null) {
            buffer.index = index;
        }
        return data;
    }

    private static boolean parseHeader(Buffer buffer, Hashtable v) {
        byte[] buf = buffer.buffer;
        int index = buffer.index;
        String key = null;
        String value = null;
        int i = index;
        while (true) {
            if (i >= buf.length || buf[i] == 13) {
                break;
            } else if (buf[i] != 58) {
                i++;
            } else {
                key = new String(buf, index, i - index);
                int i2 = i + 1;
                if (i2 < buf.length && buf[i2] == 32) {
                    i2++;
                }
                index = i2;
            }
        }
        if (key == null) {
            return false;
        }
        int i3 = index;
        while (true) {
            if (i3 >= buf.length) {
                break;
            } else if (buf[i3] != 13) {
                i3++;
            } else {
                value = new String(buf, index, i3 - index);
                int i4 = i3 + 1;
                if (i4 < buf.length && buf[i4] == 10) {
                    i4++;
                }
                index = i4;
            }
        }
        if (value != null) {
            v.put(key, value);
            buffer.index = index;
        }
        return value != null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void copy(KeyPair kpair) {
        this.publickeyblob = kpair.publickeyblob;
        this.vendor = kpair.vendor;
        this.publicKeyComment = kpair.publicKeyComment;
        this.cipher = kpair.cipher;
    }

    /* loaded from: classes.dex */
    class ASN1Exception extends Exception {
        ASN1Exception() {
        }
    }

    /* loaded from: classes.dex */
    class ASN1 {
        byte[] buf;
        int length;
        int start;

        /* JADX INFO: Access modifiers changed from: package-private */
        public ASN1(KeyPair keyPair, byte[] buf) throws ASN1Exception {
            this(buf, 0, buf.length);
        }

        ASN1(byte[] buf, int start, int length) throws ASN1Exception {
            this.buf = buf;
            this.start = start;
            this.length = length;
            if (start + length > buf.length) {
                throw new ASN1Exception();
            }
        }

        int getType() {
            return this.buf[this.start] & 255;
        }

        boolean isSEQUENCE() {
            return getType() == 48;
        }

        boolean isINTEGER() {
            return getType() == 2;
        }

        boolean isOBJECT() {
            return getType() == 6;
        }

        boolean isOCTETSTRING() {
            return getType() == 4;
        }

        private int getLength(int[] indexp) {
            int index = indexp[0];
            int index2 = index + 1;
            int length = this.buf[index] & 255;
            if ((length & 128) != 0) {
                int foo = length & 127;
                length = 0;
                while (true) {
                    int foo2 = foo - 1;
                    if (foo <= 0) {
                        break;
                    }
                    length = (length << 8) + (this.buf[index2] & 255);
                    foo = foo2;
                    index2++;
                }
            }
            indexp[0] = index2;
            return length;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public byte[] getContent() {
            int[] indexp = {this.start + 1};
            int length = getLength(indexp);
            int index = indexp[0];
            byte[] tmp = new byte[length];
            System.arraycopy(this.buf, index, tmp, 0, tmp.length);
            return tmp;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public ASN1[] getContents() throws ASN1Exception {
            byte[] bArr = this.buf;
            int i = this.start;
            int typ = bArr[i];
            int[] indexp = {i + 1};
            int length = getLength(indexp);
            if (typ == 5) {
                return new ASN1[0];
            }
            int index = indexp[0];
            Vector values = new Vector();
            while (length > 0) {
                int index2 = index + 1;
                indexp[0] = index2;
                int l = getLength(indexp);
                int index3 = indexp[0];
                int length2 = (length - 1) - (index3 - index2);
                values.addElement(new ASN1(this.buf, index2 - 1, (index3 - index2) + 1 + l));
                index = index3 + l;
                length = length2 - l;
            }
            ASN1[] result = new ASN1[values.size()];
            for (int i2 = 0; i2 < values.size(); i2++) {
                result[i2] = (ASN1) values.elementAt(i2);
            }
            return result;
        }
    }
}
