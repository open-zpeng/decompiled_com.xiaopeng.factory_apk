package cn.hutool.crypto.symmetric;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.CipherMode;
import cn.hutool.crypto.CryptoException;
import cn.hutool.crypto.KeyUtil;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.SecureUtil;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEParameterSpec;
/* loaded from: classes.dex */
public class SymmetricCrypto implements SymmetricEncryptor, SymmetricDecryptor, Serializable {
    private static final long serialVersionUID = 1;
    private Cipher cipher;
    private boolean isZeroPadding;
    private final Lock lock;
    private AlgorithmParameterSpec params;
    private SecretKey secretKey;

    public SymmetricCrypto(SymmetricAlgorithm algorithm) {
        this(algorithm, (byte[]) null);
    }

    public SymmetricCrypto(String algorithm) {
        this(algorithm, (byte[]) null);
    }

    public SymmetricCrypto(SymmetricAlgorithm algorithm, byte[] key) {
        this(algorithm.getValue(), key);
    }

    public SymmetricCrypto(SymmetricAlgorithm algorithm, SecretKey key) {
        this(algorithm.getValue(), key);
    }

    public SymmetricCrypto(String algorithm, byte[] key) {
        this(algorithm, KeyUtil.generateKey(algorithm, key));
    }

    public SymmetricCrypto(String algorithm, SecretKey key) {
        this(algorithm, key, null);
    }

    public SymmetricCrypto(String algorithm, SecretKey key, AlgorithmParameterSpec paramsSpec) {
        this.lock = new ReentrantLock();
        init(algorithm, key);
        initParams(algorithm, paramsSpec);
    }

    public SymmetricCrypto init(String algorithm, SecretKey key) {
        Assert.notBlank(algorithm, "'algorithm' must be not blank !", new Object[0]);
        this.secretKey = key;
        if (algorithm.contains(Padding.ZeroPadding.name())) {
            algorithm = StrUtil.replace(algorithm, Padding.ZeroPadding.name(), Padding.NoPadding.name());
            this.isZeroPadding = true;
        }
        this.cipher = SecureUtil.createCipher(algorithm);
        return this;
    }

    public SecretKey getSecretKey() {
        return this.secretKey;
    }

    public Cipher getCipher() {
        return this.cipher;
    }

    public SymmetricCrypto setParams(AlgorithmParameterSpec params) {
        this.params = params;
        return this;
    }

    public SymmetricCrypto setIv(IvParameterSpec iv) {
        setParams(iv);
        return this;
    }

    public SymmetricCrypto setIv(byte[] iv) {
        setIv(new IvParameterSpec(iv));
        return this;
    }

    public SymmetricCrypto setMode(CipherMode mode) {
        this.lock.lock();
        try {
            try {
                initMode(mode.getValue());
                return this;
            } catch (Exception e) {
                throw new CryptoException(e);
            }
        } finally {
            this.lock.unlock();
        }
    }

    public byte[] update(byte[] data) {
        this.lock.lock();
        try {
            try {
                return this.cipher.update(paddingDataWithZero(data, this.cipher.getBlockSize()));
            } catch (Exception e) {
                throw new CryptoException(e);
            }
        } finally {
            this.lock.unlock();
        }
    }

    public String updateHex(byte[] data) {
        return HexUtil.encodeHexStr(update(data));
    }

    @Override // cn.hutool.crypto.symmetric.SymmetricEncryptor
    public byte[] encrypt(byte[] data) {
        this.lock.lock();
        try {
            try {
                Cipher cipher = initMode(1);
                return cipher.doFinal(paddingDataWithZero(data, cipher.getBlockSize()));
            } catch (Exception e) {
                throw new CryptoException(e);
            }
        } finally {
            this.lock.unlock();
        }
    }

    @Override // cn.hutool.crypto.symmetric.SymmetricEncryptor
    public void encrypt(InputStream data, OutputStream out, boolean isClose) throws IORuntimeException {
        int blockSize;
        int remainLength;
        this.lock.lock();
        CipherOutputStream cipherOutputStream = null;
        try {
            try {
                Cipher cipher = initMode(1);
                cipherOutputStream = new CipherOutputStream(out, cipher);
                long length = IoUtil.copy(data, cipherOutputStream);
                if (this.isZeroPadding && (blockSize = cipher.getBlockSize()) > 0 && (remainLength = (int) (length % blockSize)) > 0) {
                    cipherOutputStream.write(new byte[blockSize - remainLength]);
                    cipherOutputStream.flush();
                }
            } catch (IORuntimeException e) {
                throw e;
            } catch (Exception e2) {
                throw new CryptoException(e2);
            }
        } finally {
            this.lock.unlock();
            if (isClose) {
                IoUtil.close((Closeable) data);
                IoUtil.close((Closeable) cipherOutputStream);
            }
        }
    }

    @Override // cn.hutool.crypto.symmetric.SymmetricDecryptor
    public byte[] decrypt(byte[] bytes) {
        this.lock.lock();
        try {
            try {
                Cipher cipher = initMode(2);
                int blockSize = cipher.getBlockSize();
                byte[] decryptData = cipher.doFinal(bytes);
                this.lock.unlock();
                return removePadding(decryptData, blockSize);
            } catch (Exception e) {
                throw new CryptoException(e);
            }
        } catch (Throwable th) {
            this.lock.unlock();
            throw th;
        }
    }

    @Override // cn.hutool.crypto.symmetric.SymmetricDecryptor
    public void decrypt(InputStream data, OutputStream out, boolean isClose) throws IORuntimeException {
        int blockSize;
        this.lock.lock();
        CipherInputStream cipherInputStream = null;
        try {
            try {
                Cipher cipher = initMode(2);
                cipherInputStream = new CipherInputStream(data, cipher);
                if (this.isZeroPadding && (blockSize = cipher.getBlockSize()) > 0) {
                    copyForZeroPadding(cipherInputStream, out, blockSize);
                    if (isClose) {
                        return;
                    }
                    return;
                }
                IoUtil.copy(cipherInputStream, out);
                this.lock.unlock();
                if (isClose) {
                    IoUtil.close((Closeable) data);
                    IoUtil.close((Closeable) cipherInputStream);
                }
            } catch (IORuntimeException e) {
                throw e;
            } catch (IOException e2) {
                throw new IORuntimeException(e2);
            } catch (Exception e3) {
                throw new CryptoException(e3);
            }
        } finally {
            this.lock.unlock();
            if (isClose) {
                IoUtil.close((Closeable) data);
                IoUtil.close((Closeable) cipherInputStream);
            }
        }
    }

    private SymmetricCrypto initParams(String algorithm, AlgorithmParameterSpec paramsSpec) {
        if (paramsSpec == null) {
            byte[] iv = null;
            Cipher cipher = this.cipher;
            if (cipher != null) {
                iv = cipher.getIV();
            }
            if (StrUtil.startWithIgnoreCase(algorithm, "PBE")) {
                if (iv == null) {
                    iv = RandomUtil.randomBytes(8);
                }
                paramsSpec = new PBEParameterSpec(iv, 100);
            } else if (StrUtil.startWithIgnoreCase(algorithm, "AES") && iv != null) {
                paramsSpec = new IvParameterSpec(iv);
            }
        }
        return setParams(paramsSpec);
    }

    private Cipher initMode(int mode) throws InvalidKeyException, InvalidAlgorithmParameterException {
        Cipher cipher = this.cipher;
        AlgorithmParameterSpec algorithmParameterSpec = this.params;
        if (algorithmParameterSpec == null) {
            cipher.init(mode, this.secretKey);
        } else {
            cipher.init(mode, this.secretKey, algorithmParameterSpec);
        }
        return cipher;
    }

    private byte[] paddingDataWithZero(byte[] data, int blockSize) {
        int length;
        int remainLength;
        if (this.isZeroPadding && (remainLength = (length = data.length) % blockSize) > 0) {
            return ArrayUtil.resize(data, (length + blockSize) - remainLength);
        }
        return data;
    }

    private byte[] removePadding(byte[] data, int blockSize) {
        if (this.isZeroPadding && blockSize > 0) {
            int length = data.length;
            int remainLength = length % blockSize;
            if (remainLength == 0) {
                int i = length - 1;
                while (i >= 0 && data[i] == 0) {
                    i--;
                }
                return ArrayUtil.resize(data, i + 1);
            }
        }
        return data;
    }

    private static void copyForZeroPadding(CipherInputStream in, OutputStream out, int blockSize) throws IOException {
        int n = 8192 > blockSize ? Math.max(1, 8192 / blockSize) : 1;
        int bufSize = blockSize * n;
        byte[] preBuffer = new byte[bufSize];
        byte[] buffer = new byte[bufSize];
        boolean isFirst = true;
        int preReadSize = 0;
        while (true) {
            int readSize = in.read(buffer);
            if (readSize == -1) {
                break;
            }
            if (isFirst) {
                isFirst = false;
            } else {
                out.write(preBuffer, 0, preReadSize);
            }
            ArrayUtil.copy(buffer, preBuffer, readSize);
            preReadSize = readSize;
        }
        int i = preReadSize - 1;
        while (i >= 0 && preBuffer[i] == 0) {
            i--;
        }
        out.write(preBuffer, 0, i + 1);
        out.flush();
    }
}
