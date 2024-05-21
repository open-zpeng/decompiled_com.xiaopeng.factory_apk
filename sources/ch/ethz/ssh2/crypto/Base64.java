package ch.ethz.ssh2.crypto;

import java.io.CharArrayWriter;
import java.io.IOException;
/* loaded from: classes.dex */
public class Base64 {
    static final char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();

    public static char[] encode(byte[] content) {
        CharArrayWriter cw = new CharArrayWriter((content.length * 4) / 3);
        int idx = 0;
        int x = 0;
        for (int i = 0; i < content.length; i++) {
            if (idx == 0) {
                x = (content[i] & 255) << 16;
            } else if (idx == 1) {
                x |= (content[i] & 255) << 8;
            } else {
                x |= content[i] & 255;
            }
            idx++;
            if (idx == 3) {
                cw.write(alphabet[x >> 18]);
                cw.write(alphabet[(x >> 12) & 63]);
                cw.write(alphabet[(x >> 6) & 63]);
                cw.write(alphabet[x & 63]);
                idx = 0;
            }
        }
        if (idx == 1) {
            cw.write(alphabet[x >> 18]);
            cw.write(alphabet[(x >> 12) & 63]);
            cw.write(61);
            cw.write(61);
        }
        if (idx == 2) {
            cw.write(alphabet[x >> 18]);
            cw.write(alphabet[(x >> 12) & 63]);
            cw.write(alphabet[(x >> 6) & 63]);
            cw.write(61);
        }
        return cw.toCharArray();
    }

    public static byte[] decode(char[] message) throws IOException {
        int bpos;
        byte[] buff = new byte[4];
        byte[] dest = new byte[message.length];
        int bpos2 = 0;
        int destpos = 0;
        int i = 0;
        while (true) {
            if (i >= message.length) {
                break;
            }
            char c = message[i];
            if (c != '\n' && c != '\r' && c != ' ' && c != '\t') {
                if (c >= 'A' && c <= 'Z') {
                    bpos = bpos2 + 1;
                    buff[bpos2] = (byte) (c - 'A');
                } else if (c >= 'a' && c <= 'z') {
                    bpos = bpos2 + 1;
                    buff[bpos2] = (byte) ((c - 'a') + 26);
                } else if (c >= '0' && c <= '9') {
                    bpos = bpos2 + 1;
                    buff[bpos2] = (byte) ((c - '0') + 52);
                } else if (c == '+') {
                    bpos = bpos2 + 1;
                    buff[bpos2] = 62;
                } else if (c == '/') {
                    bpos = bpos2 + 1;
                    buff[bpos2] = 63;
                } else if (c == '=') {
                    bpos = bpos2 + 1;
                    buff[bpos2] = 64;
                } else {
                    throw new IOException("Illegal char in base64 code.");
                }
                if (bpos != 4) {
                    bpos2 = bpos;
                } else {
                    bpos2 = 0;
                    int bpos3 = buff[0];
                    if (bpos3 == 64) {
                        break;
                    } else if (buff[1] == 64) {
                        throw new IOException("Unexpected '=' in base64 code.");
                    } else {
                        if (buff[2] == 64) {
                            dest[destpos] = (byte) ((((buff[0] & 63) << 6) | (buff[1] & 63)) >> 4);
                            destpos++;
                            break;
                        } else if (buff[3] == 64) {
                            int v = ((buff[0] & 63) << 12) | ((buff[1] & 63) << 6) | (buff[2] & 63);
                            int destpos2 = destpos + 1;
                            dest[destpos] = (byte) (v >> 10);
                            destpos = destpos2 + 1;
                            dest[destpos2] = (byte) (v >> 2);
                            break;
                        } else {
                            int v2 = ((buff[0] & 63) << 18) | ((buff[1] & 63) << 12) | ((buff[2] & 63) << 6) | (buff[3] & 63);
                            int destpos3 = destpos + 1;
                            dest[destpos] = (byte) (v2 >> 16);
                            int destpos4 = destpos3 + 1;
                            dest[destpos3] = (byte) (v2 >> 8);
                            dest[destpos4] = (byte) v2;
                            destpos = destpos4 + 1;
                        }
                    }
                }
            }
            i++;
        }
        byte[] res = new byte[destpos];
        System.arraycopy(dest, 0, res, 0, destpos);
        return res;
    }
}
