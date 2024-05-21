package com.jcraft.jsch;
/* loaded from: classes.dex */
public interface Signature {
    void init() throws Exception;

    byte[] sign() throws Exception;

    void update(byte[] bArr) throws Exception;

    boolean verify(byte[] bArr) throws Exception;
}
