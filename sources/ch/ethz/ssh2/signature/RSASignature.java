package ch.ethz.ssh2.signature;

import java.math.BigInteger;
/* loaded from: classes.dex */
public class RSASignature {
    BigInteger s;

    public BigInteger getS() {
        return this.s;
    }

    public RSASignature(BigInteger s) {
        this.s = s;
    }
}
