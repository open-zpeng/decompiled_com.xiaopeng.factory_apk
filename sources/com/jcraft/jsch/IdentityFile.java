package com.jcraft.jsch;

import java.io.UnsupportedEncodingException;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class IdentityFile implements Identity {
    private String identity;
    private JSch jsch;
    private KeyPair kpair;

    /* JADX INFO: Access modifiers changed from: package-private */
    public static IdentityFile newInstance(String prvfile, String pubfile, JSch jsch) throws JSchException {
        KeyPair kpair = KeyPair.load(jsch, prvfile, pubfile);
        return new IdentityFile(jsch, prvfile, kpair);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static IdentityFile newInstance(String name, byte[] prvkey, byte[] pubkey, JSch jsch) throws JSchException {
        KeyPair kpair = KeyPair.load(jsch, prvkey, pubkey);
        return new IdentityFile(jsch, name, kpair);
    }

    private IdentityFile(JSch jsch, String name, KeyPair kpair) throws JSchException {
        this.jsch = jsch;
        this.identity = name;
        this.kpair = kpair;
    }

    @Override // com.jcraft.jsch.Identity
    public boolean setPassphrase(byte[] passphrase) throws JSchException {
        return this.kpair.decrypt(passphrase);
    }

    @Override // com.jcraft.jsch.Identity
    public byte[] getPublicKeyBlob() {
        return this.kpair.getPublicKeyBlob();
    }

    @Override // com.jcraft.jsch.Identity
    public byte[] getSignature(byte[] data) {
        return this.kpair.getSignature(data);
    }

    @Override // com.jcraft.jsch.Identity
    public boolean decrypt() {
        throw new RuntimeException("not implemented");
    }

    @Override // com.jcraft.jsch.Identity
    public String getAlgName() {
        byte[] name = this.kpair.getKeyTypeName();
        try {
            return new String(name, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return new String(name);
        }
    }

    @Override // com.jcraft.jsch.Identity
    public String getName() {
        return this.identity;
    }

    @Override // com.jcraft.jsch.Identity
    public boolean isEncrypted() {
        return this.kpair.isEncrypted();
    }

    @Override // com.jcraft.jsch.Identity
    public void clear() {
        this.kpair.dispose();
        this.kpair = null;
    }

    public KeyPair getKeyPair() {
        return this.kpair;
    }
}
