package com.jcraft.jsch;

import com.jcraft.jsch.KeyPair;
import java.math.BigInteger;
import java.util.Vector;
/* loaded from: classes.dex */
public class KeyPairPKCS8 extends KeyPair {
    private KeyPair kpair;
    private static final byte[] rsaEncryption = {42, -122, 72, -122, -9, 13, 1, 1, 1};
    private static final byte[] dsaEncryption = {42, -122, 72, -50, 56, 4, 1};
    private static final byte[] pbes2 = {42, -122, 72, -122, -9, 13, 1, 5, 13};
    private static final byte[] pbkdf2 = {42, -122, 72, -122, -9, 13, 1, 5, 12};
    private static final byte[] aes128cbc = {96, -122, 72, 1, 101, 3, 4, 1, 2};
    private static final byte[] aes192cbc = {96, -122, 72, 1, 101, 3, 4, 1, 22};
    private static final byte[] aes256cbc = {96, -122, 72, 1, 101, 3, 4, 1, 42};
    private static final byte[] pbeWithMD5AndDESCBC = {42, -122, 72, -122, -9, 13, 1, 5, 3};
    private static final byte[] begin = Util.str2byte("-----BEGIN DSA PRIVATE KEY-----");
    private static final byte[] end = Util.str2byte("-----END DSA PRIVATE KEY-----");

    public KeyPairPKCS8(JSch jsch) {
        super(jsch);
        this.kpair = null;
    }

    @Override // com.jcraft.jsch.KeyPair
    void generate(int key_size) throws JSchException {
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
        return null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.jcraft.jsch.KeyPair
    public boolean parse(byte[] plain) {
        try {
            Vector values = new Vector();
            try {
                try {
                    KeyPair.ASN1[] contents = new KeyPair.ASN1(this, plain).getContents();
                    KeyPair.ASN1 privateKeyAlgorithm = contents[1];
                    KeyPair.ASN1 privateKey = contents[2];
                    KeyPair.ASN1[] contents2 = privateKeyAlgorithm.getContents();
                    byte[] privateKeyAlgorithmID = contents2[0].getContent();
                    KeyPair.ASN1[] contents3 = contents2[1].getContents();
                    if (contents3.length > 0) {
                        for (KeyPair.ASN1 asn1 : contents3) {
                            values.addElement(asn1.getContent());
                        }
                    }
                    byte[] _data = privateKey.getContent();
                    if (Util.array_equals(privateKeyAlgorithmID, rsaEncryption)) {
                        KeyPair _kpair = new KeyPairRSA(this.jsch);
                        _kpair.copy(this);
                        if (_kpair.parse(_data)) {
                            this.kpair = _kpair;
                        }
                    } else if (Util.array_equals(privateKeyAlgorithmID, dsaEncryption)) {
                        KeyPair.ASN1 asn12 = new KeyPair.ASN1(this, _data);
                        if (values.size() == 0) {
                            KeyPair.ASN1[] contents4 = asn12.getContents();
                            byte[] bar = contents4[1].getContent();
                            for (KeyPair.ASN1 asn13 : contents4[0].getContents()) {
                                values.addElement(asn13.getContent());
                            }
                            values.addElement(bar);
                        } else {
                            values.addElement(asn12.getContent());
                        }
                        byte[] P_array = (byte[]) values.elementAt(0);
                        byte[] Q_array = (byte[]) values.elementAt(1);
                        byte[] G_array = (byte[]) values.elementAt(2);
                        byte[] prv_array = (byte[]) values.elementAt(3);
                        try {
                            byte[] pub_array = new BigInteger(G_array).modPow(new BigInteger(prv_array), new BigInteger(P_array)).toByteArray();
                            KeyPairDSA _key = new KeyPairDSA(this.jsch, P_array, Q_array, G_array, pub_array, prv_array);
                            byte[] plain2 = _key.getPrivateKey();
                            try {
                                KeyPair _kpair2 = new KeyPairDSA(this.jsch);
                                _kpair2.copy(this);
                                if (_kpair2.parse(plain2)) {
                                    this.kpair = _kpair2;
                                }
                            } catch (KeyPair.ASN1Exception e) {
                                return false;
                            } catch (Exception e2) {
                                return false;
                            }
                        } catch (KeyPair.ASN1Exception e3) {
                            return false;
                        }
                    }
                    return this.kpair != null;
                } catch (KeyPair.ASN1Exception e4) {
                    return false;
                }
            } catch (Exception e5) {
                return false;
            }
        } catch (KeyPair.ASN1Exception e6) {
            return false;
        } catch (Exception e7) {
            return false;
        }
    }

    @Override // com.jcraft.jsch.KeyPair
    public byte[] getPublicKeyBlob() {
        return this.kpair.getPublicKeyBlob();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.jcraft.jsch.KeyPair
    public byte[] getKeyTypeName() {
        return this.kpair.getKeyTypeName();
    }

    @Override // com.jcraft.jsch.KeyPair
    public int getKeyType() {
        return this.kpair.getKeyType();
    }

    @Override // com.jcraft.jsch.KeyPair
    public int getKeySize() {
        return this.kpair.getKeySize();
    }

    @Override // com.jcraft.jsch.KeyPair
    public byte[] getSignature(byte[] data) {
        return this.kpair.getSignature(data);
    }

    @Override // com.jcraft.jsch.KeyPair
    public Signature getVerifier() {
        return this.kpair.getVerifier();
    }

    @Override // com.jcraft.jsch.KeyPair
    public byte[] forSSHAgent() throws JSchException {
        return this.kpair.forSSHAgent();
    }

    @Override // com.jcraft.jsch.KeyPair
    public boolean decrypt(byte[] _passphrase) {
        byte[] key;
        if (isEncrypted()) {
            if (_passphrase != null) {
                try {
                    KeyPair.ASN1 asn1 = new KeyPair.ASN1(this, this.data);
                    KeyPair.ASN1[] contents = asn1.getContents();
                    byte[] _data = contents[1].getContent();
                    KeyPair.ASN1 pbes = contents[0];
                    KeyPair.ASN1[] contents2 = pbes.getContents();
                    byte[] pbesid = contents2[0].getContent();
                    KeyPair.ASN1 pbesparam = contents2[1];
                    if (Util.array_equals(pbesid, pbes2)) {
                        KeyPair.ASN1[] contents3 = pbesparam.getContents();
                        KeyPair.ASN1 pbkdf = contents3[0];
                        KeyPair.ASN1 encryptfunc = contents3[1];
                        KeyPair.ASN1[] contents4 = pbkdf.getContents();
                        contents4[0].getContent();
                        KeyPair.ASN1 pbkdffunc = contents4[1];
                        KeyPair.ASN1[] contents5 = pbkdffunc.getContents();
                        byte[] salt = contents5[0].getContent();
                        int iterations = Integer.parseInt(new BigInteger(contents5[1].getContent()).toString());
                        KeyPair.ASN1[] contents6 = encryptfunc.getContents();
                        byte[] encryptfuncid = contents6[0].getContent();
                        byte[] iv = contents6[1].getContent();
                        Cipher cipher = getCipher(encryptfuncid);
                        if (cipher == null) {
                            return false;
                        }
                        try {
                            JSch jSch = this.jsch;
                            Class c = Class.forName(JSch.getConfig("pbkdf"));
                            PBKDF tmp = (PBKDF) c.newInstance();
                            key = tmp.getKey(_passphrase, salt, iterations, cipher.getBlockSize());
                        } catch (Exception e) {
                            key = null;
                        }
                        if (key == null) {
                            return false;
                        }
                        cipher.init(1, key, iv);
                        Util.bzero(key);
                        byte[] plain = new byte[_data.length];
                        cipher.update(_data, 0, _data.length, plain, 0);
                        if (parse(plain)) {
                            this.encrypted = false;
                            return true;
                        }
                        return false;
                    } else if (Util.array_equals(pbesid, pbeWithMD5AndDESCBC)) {
                        return false;
                    } else {
                        return false;
                    }
                } catch (KeyPair.ASN1Exception e2) {
                    return false;
                } catch (Exception e3) {
                    return false;
                }
            }
            return !isEncrypted();
        }
        return true;
    }

    Cipher getCipher(byte[] id) {
        String message;
        String name = null;
        try {
            if (Util.array_equals(id, aes128cbc)) {
                name = "aes128-cbc";
            } else if (Util.array_equals(id, aes192cbc)) {
                name = "aes192-cbc";
            } else if (Util.array_equals(id, aes256cbc)) {
                name = "aes256-cbc";
            }
            JSch jSch = this.jsch;
            Class c = Class.forName(JSch.getConfig(name));
            Cipher cipher = (Cipher) c.newInstance();
            return cipher;
        } catch (Exception e) {
            if (!JSch.getLogger().isEnabled(4)) {
                return null;
            }
            if (name == null) {
                message = "unknown oid: " + Util.toHex(id);
            } else {
                message = "function " + name + " is not supported";
            }
            JSch.getLogger().log(4, "PKCS8: " + message);
            return null;
        }
    }
}
