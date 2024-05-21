package cn.hutool.crypto;

import cn.hutool.core.io.FastByteArrayOutputStream;
import cn.hutool.core.io.IORuntimeException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.BERSequence;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DLSequence;
import org.bouncycastle.asn1.util.ASN1Dump;
/* loaded from: classes.dex */
public class ASN1Util {
    public static byte[] encodeDer(ASN1Encodable... elements) {
        return encode("DER", elements);
    }

    public static byte[] encode(String asn1Encoding, ASN1Encodable... elements) {
        FastByteArrayOutputStream out = new FastByteArrayOutputStream();
        encodeTo(asn1Encoding, out, elements);
        return out.toByteArray();
    }

    public static void encodeTo(String asn1Encoding, OutputStream out, ASN1Encodable... elements) {
        char c;
        DLSequence dERSequence;
        int hashCode = asn1Encoding.hashCode();
        if (hashCode == 2184) {
            if (asn1Encoding.equals("DL")) {
                c = 2;
            }
            c = 65535;
        } else if (hashCode != 65647) {
            if (hashCode == 67569 && asn1Encoding.equals("DER")) {
                c = 0;
            }
            c = 65535;
        } else {
            if (asn1Encoding.equals("BER")) {
                c = 1;
            }
            c = 65535;
        }
        if (c == 0) {
            dERSequence = new DERSequence(elements);
        } else if (c == 1) {
            dERSequence = new BERSequence(elements);
        } else if (c == 2) {
            dERSequence = new DLSequence(elements);
        } else {
            throw new CryptoException("Unsupported ASN1 encoding: {}", asn1Encoding);
        }
        try {
            dERSequence.encodeTo(out);
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    public static ASN1Object decode(InputStream in) {
        ASN1InputStream asn1In = new ASN1InputStream(in);
        try {
            return asn1In.readObject();
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    public static String getDumpStr(InputStream in) {
        return ASN1Dump.dumpAsString(decode(in));
    }
}
