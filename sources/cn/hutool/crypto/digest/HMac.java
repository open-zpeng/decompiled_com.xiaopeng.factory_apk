package cn.hutool.crypto.digest;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.CryptoException;
import cn.hutool.crypto.digest.mac.MacEngine;
import cn.hutool.crypto.digest.mac.MacEngineFactory;
import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.security.Key;
import java.security.MessageDigest;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.spec.SecretKeySpec;
/* loaded from: classes.dex */
public class HMac implements Serializable {
    private static final long serialVersionUID = 1;
    private final MacEngine engine;

    public HMac(HmacAlgorithm algorithm) {
        this(algorithm, (Key) null);
    }

    public HMac(HmacAlgorithm algorithm, byte[] key) {
        this(algorithm.getValue(), key);
    }

    public HMac(HmacAlgorithm algorithm, Key key) {
        this(algorithm.getValue(), key);
    }

    public HMac(String algorithm, byte[] key) {
        this(algorithm, new SecretKeySpec(key, algorithm));
    }

    public HMac(String algorithm, Key key) {
        this(algorithm, key, null);
    }

    public HMac(String algorithm, Key key, AlgorithmParameterSpec spec) {
        this(MacEngineFactory.createEngine(algorithm, key, spec));
    }

    public HMac(MacEngine engine) {
        this.engine = engine;
    }

    public MacEngine getEngine() {
        return this.engine;
    }

    public byte[] digest(String data, Charset charset) {
        return digest(StrUtil.bytes(data, charset));
    }

    public byte[] digest(String data) {
        return digest(data, CharsetUtil.CHARSET_UTF_8);
    }

    public String digestBase64(String data, boolean isUrlSafe) {
        return digestBase64(data, CharsetUtil.CHARSET_UTF_8, isUrlSafe);
    }

    public String digestBase64(String data, Charset charset, boolean isUrlSafe) {
        return Base64.encodeStr(digest(data, charset), false, isUrlSafe);
    }

    public String digestHex(String data, Charset charset) {
        return HexUtil.encodeHexStr(digest(data, charset));
    }

    public String digestHex(String data) {
        return digestHex(data, CharsetUtil.CHARSET_UTF_8);
    }

    public byte[] digest(File file) throws CryptoException {
        InputStream in = null;
        try {
            in = FileUtil.getInputStream(file);
            return digest(in);
        } finally {
            IoUtil.close((Closeable) in);
        }
    }

    public String digestHex(File file) {
        return HexUtil.encodeHexStr(digest(file));
    }

    public byte[] digest(byte[] data) {
        return digest(new ByteArrayInputStream(data), -1);
    }

    public String digestHex(byte[] data) {
        return HexUtil.encodeHexStr(digest(data));
    }

    public byte[] digest(InputStream data) {
        return digest(data, 8192);
    }

    public String digestHex(InputStream data) {
        return HexUtil.encodeHexStr(digest(data));
    }

    public byte[] digest(InputStream data, int bufferLength) {
        return this.engine.digest(data, bufferLength);
    }

    public String digestHex(InputStream data, int bufferLength) {
        return HexUtil.encodeHexStr(digest(data, bufferLength));
    }

    public boolean verify(byte[] digest, byte[] digestToCompare) {
        return MessageDigest.isEqual(digest, digestToCompare);
    }

    public int getMacLength() {
        return this.engine.getMacLength();
    }

    public String getAlgorithm() {
        return this.engine.getAlgorithm();
    }
}
