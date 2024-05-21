package com.jcraft.jsch;
/* loaded from: classes.dex */
public interface HASH {
    byte[] digest() throws Exception;

    int getBlockSize();

    void init() throws Exception;

    void update(byte[] bArr, int i, int i2) throws Exception;
}
