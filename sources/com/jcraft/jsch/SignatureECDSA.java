package com.jcraft.jsch;
/* loaded from: classes.dex */
public interface SignatureECDSA extends Signature {
    void setPrvKey(byte[] bArr) throws Exception;

    void setPubKey(byte[] bArr, byte[] bArr2) throws Exception;
}
