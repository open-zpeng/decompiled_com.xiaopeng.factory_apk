package com.jcraft.jsch;
/* loaded from: classes.dex */
public interface KeyPairGenRSA {
    byte[] getC();

    byte[] getD();

    byte[] getE();

    byte[] getEP();

    byte[] getEQ();

    byte[] getN();

    byte[] getP();

    byte[] getQ();

    void init(int i) throws Exception;
}
