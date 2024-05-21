package com.jcraft.jsch;
/* loaded from: classes.dex */
public interface KeyPairGenECDSA {
    byte[] getD();

    byte[] getR();

    byte[] getS();

    void init(int i) throws Exception;
}
