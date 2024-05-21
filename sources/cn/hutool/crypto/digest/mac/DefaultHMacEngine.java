package cn.hutool.crypto.digest.mac;

import cn.hutool.crypto.CryptoException;
import cn.hutool.crypto.SecureUtil;
import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
/* loaded from: classes.dex */
public class DefaultHMacEngine implements MacEngine {
    private Mac mac;

    public DefaultHMacEngine(String algorithm, byte[] key) {
        this(algorithm, key == null ? null : new SecretKeySpec(key, algorithm));
    }

    public DefaultHMacEngine(String algorithm, Key key) {
        this(algorithm, key, null);
    }

    public DefaultHMacEngine(String algorithm, Key key, AlgorithmParameterSpec spec) {
        init(algorithm, key, spec);
    }

    public DefaultHMacEngine init(String algorithm, byte[] key) {
        return init(algorithm, key == null ? null : new SecretKeySpec(key, algorithm));
    }

    public DefaultHMacEngine init(String algorithm, Key key) {
        return init(algorithm, key, null);
    }

    public DefaultHMacEngine init(String algorithm, Key key, AlgorithmParameterSpec spec) {
        try {
            this.mac = SecureUtil.createMac(algorithm);
            if (key == null) {
                key = SecureUtil.generateKey(algorithm);
            }
            if (spec != null) {
                this.mac.init(key, spec);
            } else {
                this.mac.init(key);
            }
            return this;
        } catch (Exception e) {
            throw new CryptoException(e);
        }
    }

    public Mac getMac() {
        return this.mac;
    }

    @Override // cn.hutool.crypto.digest.mac.MacEngine
    public void update(byte[] in) {
        this.mac.update(in);
    }

    @Override // cn.hutool.crypto.digest.mac.MacEngine
    public void update(byte[] in, int inOff, int len) {
        this.mac.update(in, inOff, len);
    }

    @Override // cn.hutool.crypto.digest.mac.MacEngine
    public byte[] doFinal() {
        return this.mac.doFinal();
    }

    @Override // cn.hutool.crypto.digest.mac.MacEngine
    public void reset() {
        this.mac.reset();
    }

    @Override // cn.hutool.crypto.digest.mac.MacEngine
    public int getMacLength() {
        return this.mac.getMacLength();
    }

    @Override // cn.hutool.crypto.digest.mac.MacEngine
    public String getAlgorithm() {
        return this.mac.getAlgorithm();
    }
}
