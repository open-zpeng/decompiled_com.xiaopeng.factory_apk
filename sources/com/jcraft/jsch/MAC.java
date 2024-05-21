package com.jcraft.jsch;
/* loaded from: classes.dex */
public interface MAC {
    void doFinal(byte[] bArr, int i);

    int getBlockSize();

    String getName();

    void init(byte[] bArr) throws Exception;

    void update(int i);

    void update(byte[] bArr, int i, int i2);
}
