package cn.hutool.crypto.digest;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.CryptoException;
import cn.hutool.crypto.SecureUtil;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
/* loaded from: classes.dex */
public class Digester implements Serializable {
    private static final long serialVersionUID = 1;
    private MessageDigest digest;
    protected int digestCount;
    protected byte[] salt;
    protected int saltPosition;

    public Digester(DigestAlgorithm algorithm) {
        this(algorithm.getValue());
    }

    public Digester(String algorithm) {
        this(algorithm, (Provider) null);
    }

    public Digester(DigestAlgorithm algorithm, Provider provider) {
        init(algorithm.getValue(), provider);
    }

    public Digester(String algorithm, Provider provider) {
        init(algorithm, provider);
    }

    public Digester init(String algorithm, Provider provider) {
        if (provider == null) {
            this.digest = SecureUtil.createMessageDigest(algorithm);
        } else {
            try {
                this.digest = MessageDigest.getInstance(algorithm, provider);
            } catch (NoSuchAlgorithmException e) {
                throw new CryptoException(e);
            }
        }
        return this;
    }

    public Digester setSalt(byte[] salt) {
        this.salt = salt;
        return this;
    }

    public Digester setSaltPosition(int saltPosition) {
        this.saltPosition = saltPosition;
        return this;
    }

    public Digester setDigestCount(int digestCount) {
        this.digestCount = digestCount;
        return this;
    }

    public Digester reset() {
        this.digest.reset();
        return this;
    }

    public byte[] digest(String data, String charsetName) {
        return digest(data, CharsetUtil.charset(charsetName));
    }

    public byte[] digest(String data, Charset charset) {
        return digest(StrUtil.bytes(data, charset));
    }

    public byte[] digest(String data) {
        return digest(data, CharsetUtil.CHARSET_UTF_8);
    }

    public String digestHex(String data, String charsetName) {
        return digestHex(data, CharsetUtil.charset(charsetName));
    }

    public String digestHex(String data, Charset charset) {
        return HexUtil.encodeHexStr(digest(data, charset));
    }

    public String digestHex(String data) {
        return digestHex(data, "UTF-8");
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
        byte[] result;
        int i = this.saltPosition;
        if (i <= 0) {
            result = doDigest(this.salt, data);
        } else if (i >= data.length) {
            result = doDigest(data, this.salt);
        } else {
            byte[] result2 = this.salt;
            if (ArrayUtil.isNotEmpty(result2)) {
                this.digest.update(data, 0, this.saltPosition);
                this.digest.update(this.salt);
                MessageDigest messageDigest = this.digest;
                int i2 = this.saltPosition;
                messageDigest.update(data, i2, data.length - i2);
                result = this.digest.digest();
            } else {
                result = doDigest(data);
            }
        }
        return resetAndRepeatDigest(result);
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

    public byte[] digest(InputStream data, int bufferLength) throws IORuntimeException {
        byte[] result;
        if (bufferLength < 1) {
            bufferLength = 8192;
        }
        try {
            if (ArrayUtil.isEmpty(this.salt)) {
                result = digestWithoutSalt(data, bufferLength);
            } else {
                result = digestWithSalt(data, bufferLength);
            }
            return resetAndRepeatDigest(result);
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    public String digestHex(InputStream data, int bufferLength) {
        return HexUtil.encodeHexStr(digest(data, bufferLength));
    }

    public MessageDigest getDigest() {
        return this.digest;
    }

    public int getDigestLength() {
        return this.digest.getDigestLength();
    }

    private byte[] digestWithoutSalt(InputStream data, int bufferLength) throws IOException {
        byte[] buffer = new byte[bufferLength];
        while (true) {
            int read = data.read(buffer, 0, bufferLength);
            if (read > -1) {
                this.digest.update(buffer, 0, read);
            } else {
                return this.digest.digest();
            }
        }
    }

    private byte[] digestWithSalt(InputStream data, int bufferLength) throws IOException {
        if (this.saltPosition <= 0) {
            this.digest.update(this.salt);
        }
        byte[] buffer = new byte[bufferLength];
        int total = 0;
        while (true) {
            int read = data.read(buffer, 0, bufferLength);
            if (read <= -1) {
                break;
            }
            total += read;
            int i = this.saltPosition;
            if (i <= 0 || total < i) {
                this.digest.update(buffer, 0, read);
            } else {
                if (total != i) {
                    this.digest.update(buffer, 0, total - i);
                }
                this.digest.update(this.salt);
                this.digest.update(buffer, total - this.saltPosition, read);
            }
        }
        if (total < this.saltPosition) {
            this.digest.update(this.salt);
        }
        return this.digest.digest();
    }

    private byte[] doDigest(byte[]... datas) {
        for (byte[] data : datas) {
            if (data != null) {
                this.digest.update(data);
            }
        }
        return this.digest.digest();
    }

    private byte[] resetAndRepeatDigest(byte[] digestData) {
        int digestCount = Math.max(1, this.digestCount);
        reset();
        for (int i = 0; i < digestCount - 1; i++) {
            digestData = doDigest(digestData);
            reset();
        }
        return digestData;
    }
}
