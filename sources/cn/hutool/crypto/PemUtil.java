package cn.hutool.crypto;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import com.xpeng.upso.aesserver.AesConstants;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemObjectGenerator;
import org.bouncycastle.util.io.pem.PemReader;
import org.bouncycastle.util.io.pem.PemWriter;
/* loaded from: classes.dex */
public class PemUtil {
    public static PrivateKey readPemPrivateKey(InputStream pemStream) {
        return (PrivateKey) readPemKey(pemStream);
    }

    public static PublicKey readPemPublicKey(InputStream pemStream) {
        return (PublicKey) readPemKey(pemStream);
    }

    public static Key readPemKey(InputStream keyStream) {
        PemObject object = readPemObject(keyStream);
        String type = object.getType();
        if (StrUtil.isNotBlank(type)) {
            if (type.endsWith("EC PRIVATE KEY")) {
                return KeyUtil.generatePrivateKey(AesConstants.SERVER_MAGIC_CDU, object.getContent());
            }
            if (type.endsWith("PRIVATE KEY")) {
                return KeyUtil.generateRSAPrivateKey(object.getContent());
            }
            if (type.endsWith("EC PUBLIC KEY")) {
                return KeyUtil.generatePublicKey(AesConstants.SERVER_MAGIC_CDU, object.getContent());
            }
            if (type.endsWith("PUBLIC KEY")) {
                return KeyUtil.generateRSAPublicKey(object.getContent());
            }
            if (type.endsWith("CERTIFICATE")) {
                return KeyUtil.readPublicKeyFromCert(IoUtil.toStream(object.getContent()));
            }
            return null;
        }
        return null;
    }

    public static byte[] readPem(InputStream keyStream) {
        PemObject pemObject = readPemObject(keyStream);
        if (pemObject != null) {
            return pemObject.getContent();
        }
        return null;
    }

    public static PemObject readPemObject(InputStream keyStream) {
        return readPemObject(IoUtil.getUtf8Reader(keyStream));
    }

    public static PemObject readPemObject(Reader reader) {
        PemReader pemReader = null;
        try {
            try {
                pemReader = new PemReader(reader);
                return pemReader.readPemObject();
            } catch (IOException e) {
                throw new IORuntimeException(e);
            }
        } finally {
            IoUtil.close((Closeable) pemReader);
        }
    }

    public static PrivateKey readSm2PemPrivateKey(InputStream keyStream) {
        try {
            return KeyUtil.generatePrivateKey("sm2", ECKeyUtil.createOpenSSHPrivateKeySpec(readPem(keyStream)));
        } finally {
            IoUtil.close((Closeable) keyStream);
        }
    }

    public static String toPem(String type, byte[] content) {
        StringWriter stringWriter = new StringWriter();
        writePemObject(type, content, stringWriter);
        return stringWriter.toString();
    }

    public static void writePemObject(String type, byte[] content, OutputStream keyStream) {
        writePemObject((PemObjectGenerator) new PemObject(type, content), keyStream);
    }

    public static void writePemObject(String type, byte[] content, Writer writer) {
        writePemObject((PemObjectGenerator) new PemObject(type, content), writer);
    }

    public static void writePemObject(PemObjectGenerator pemObject, OutputStream keyStream) {
        writePemObject(pemObject, IoUtil.getUtf8Writer(keyStream));
    }

    public static void writePemObject(PemObjectGenerator pemObject, Writer writer) {
        PemWriter pemWriter = new PemWriter(writer);
        try {
            try {
                pemWriter.writeObject(pemObject);
            } catch (IOException e) {
                throw new IORuntimeException(e);
            }
        } finally {
            IoUtil.close((Closeable) pemWriter);
        }
    }
}
