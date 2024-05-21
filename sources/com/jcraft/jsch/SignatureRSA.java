package com.jcraft.jsch;
/* loaded from: classes.dex */
public interface SignatureRSA extends Signature {
    void setPrvKey(byte[] bArr, byte[] bArr2) throws Exception;

    void setPubKey(byte[] bArr, byte[] bArr2) throws Exception;
}
