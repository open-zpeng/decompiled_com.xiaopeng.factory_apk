package cn.hutool.crypto.digest.mac;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.Mac;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
/* loaded from: classes.dex */
public class BCHMacEngine implements MacEngine {
    private Mac mac;

    public BCHMacEngine(Digest digest, byte[] key, byte[] iv) {
        this(digest, (CipherParameters) new ParametersWithIV(new KeyParameter(key), iv));
    }

    public BCHMacEngine(Digest digest, byte[] key) {
        this(digest, (CipherParameters) new KeyParameter(key));
    }

    public BCHMacEngine(Digest digest, CipherParameters params) {
        init(digest, params);
    }

    public BCHMacEngine init(Digest digest, CipherParameters params) {
        this.mac = new HMac(digest);
        this.mac.init(params);
        return this;
    }

    public Mac getMac() {
        return this.mac;
    }

    @Override // cn.hutool.crypto.digest.mac.MacEngine
    public void update(byte[] in, int inOff, int len) {
        this.mac.update(in, inOff, len);
    }

    @Override // cn.hutool.crypto.digest.mac.MacEngine
    public byte[] doFinal() {
        byte[] result = new byte[getMacLength()];
        this.mac.doFinal(result, 0);
        return result;
    }

    @Override // cn.hutool.crypto.digest.mac.MacEngine
    public void reset() {
        this.mac.reset();
    }

    @Override // cn.hutool.crypto.digest.mac.MacEngine
    public int getMacLength() {
        return this.mac.getMacSize();
    }

    @Override // cn.hutool.crypto.digest.mac.MacEngine
    public String getAlgorithm() {
        return this.mac.getAlgorithmName();
    }
}
