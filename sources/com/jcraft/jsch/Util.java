package com.jcraft.jsch;

import com.xiaopeng.commonfunc.Constant;
import com.xiaopeng.lib.utils.info.BuildInfoUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.Vector;
/* loaded from: classes.dex */
class Util {
    private static final byte[] b64 = str2byte("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=");
    private static String[] chars = {"0", "1", "2", "3", BuildInfoUtils.BID_LAN, BuildInfoUtils.BID_PT_SPECIAL_1, BuildInfoUtils.BID_PT_SPECIAL_2, "7", "8", Constant.ALPHA_9, Constant.ALPHA_LOWCASE_A, "b", Constant.ALPHA_LOWCASE_C, Constant.ALPHA_LOWCASE_D, Constant.ALPHA_LOWCASE_E, Constant.ALPHA_LOWCASE_F};
    static final byte[] empty = str2byte("");

    Util() {
    }

    private static byte val(byte foo) {
        if (foo == 61) {
            return (byte) 0;
        }
        int j = 0;
        while (true) {
            byte[] bArr = b64;
            if (j >= bArr.length) {
                return (byte) 0;
            }
            if (foo == bArr[j]) {
                return (byte) j;
            }
            j++;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static byte[] fromBase64(byte[] buf, int start, int length) throws JSchException {
        try {
            byte[] foo = new byte[length];
            int j = 0;
            int i = start;
            while (true) {
                if (i >= start + length) {
                    break;
                }
                foo[j] = (byte) ((val(buf[i]) << 2) | ((val(buf[i + 1]) & 48) >>> 4));
                if (buf[i + 2] == 61) {
                    j++;
                    break;
                }
                foo[j + 1] = (byte) (((val(buf[i + 1]) & 15) << 4) | ((val(buf[i + 2]) & 60) >>> 2));
                if (buf[i + 3] == 61) {
                    j += 2;
                    break;
                }
                foo[j + 2] = (byte) (((val(buf[i + 2]) & 3) << 6) | (val(buf[i + 3]) & 63));
                j += 3;
                i += 4;
            }
            byte[] bar = new byte[j];
            System.arraycopy(foo, 0, bar, 0, j);
            return bar;
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new JSchException("fromBase64: invalid base64 data", e);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static byte[] toBase64(byte[] buf, int start, int length) {
        byte[] tmp = new byte[length * 2];
        int foo = ((length / 3) * 3) + start;
        int k = 0;
        int j = start;
        while (j < foo) {
            int k2 = (buf[j] >>> 2) & 63;
            int i = k + 1;
            byte[] bArr = b64;
            tmp[k] = bArr[k2];
            int k3 = ((buf[j] & 3) << 4) | ((buf[j + 1] >>> 4) & 15);
            int k4 = i + 1;
            tmp[i] = bArr[k3];
            int k5 = ((buf[j + 1] & 15) << 2) | ((buf[j + 2] >>> 6) & 3);
            int i2 = k4 + 1;
            tmp[k4] = bArr[k5];
            int k6 = buf[j + 2] & 63;
            tmp[i2] = bArr[k6];
            j += 3;
            k = i2 + 1;
        }
        int foo2 = (start + length) - foo;
        if (foo2 == 1) {
            int k7 = (buf[j] >>> 2) & 63;
            int i3 = k + 1;
            byte[] bArr2 = b64;
            tmp[k] = bArr2[k7];
            int k8 = ((buf[j] & 3) << 4) & 63;
            int k9 = i3 + 1;
            tmp[i3] = bArr2[k8];
            int i4 = k9 + 1;
            tmp[k9] = 61;
            tmp[i4] = 61;
            k = i4 + 1;
        } else if (foo2 == 2) {
            int k10 = (buf[j] >>> 2) & 63;
            int i5 = k + 1;
            byte[] bArr3 = b64;
            tmp[k] = bArr3[k10];
            int k11 = ((buf[j] & 3) << 4) | ((buf[j + 1] >>> 4) & 15);
            int k12 = i5 + 1;
            tmp[i5] = bArr3[k11];
            int k13 = ((buf[j + 1] & 15) << 2) & 63;
            int i6 = k12 + 1;
            tmp[k12] = bArr3[k13];
            tmp[i6] = 61;
            k = i6 + 1;
        }
        byte[] bar = new byte[k];
        System.arraycopy(tmp, 0, bar, 0, k);
        return bar;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String[] split(String foo, String split) {
        if (foo == null) {
            return null;
        }
        byte[] buf = str2byte(foo);
        Vector bar = new Vector();
        int start = 0;
        while (true) {
            int index = foo.indexOf(split, start);
            if (index < 0) {
                break;
            }
            bar.addElement(byte2str(buf, start, index - start));
            start = index + 1;
        }
        bar.addElement(byte2str(buf, start, buf.length - start));
        String[] result = new String[bar.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = (String) bar.elementAt(i);
        }
        return result;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean glob(byte[] pattern, byte[] name) {
        return glob0(pattern, 0, name, 0);
    }

    private static boolean glob0(byte[] pattern, int pattern_index, byte[] name, int name_index) {
        if (name.length > 0 && name[0] == 46) {
            if (pattern.length <= 0 || pattern[0] != 46) {
                return false;
            }
            if (pattern.length == 2 && pattern[1] == 42) {
                return true;
            }
            return glob(pattern, pattern_index + 1, name, name_index + 1);
        }
        return glob(pattern, pattern_index, name, name_index);
    }

    private static boolean glob(byte[] pattern, int pattern_index, byte[] name, int name_index) {
        int patternlen = pattern.length;
        if (patternlen == 0) {
            return false;
        }
        int namelen = name.length;
        int i = pattern_index;
        int j = name_index;
        while (i < patternlen && j < namelen) {
            if (pattern[i] != 92) {
                if (pattern[i] == 42) {
                    while (i < patternlen && pattern[i] == 42) {
                        i++;
                    }
                    if (patternlen == i) {
                        return true;
                    }
                    byte foo = pattern[i];
                    if (foo == 63) {
                        while (j < namelen) {
                            if (glob(pattern, i, name, j)) {
                                return true;
                            }
                            j += skipUTF8Char(name[j]);
                        }
                        return false;
                    } else if (foo == 92) {
                        if (i + 1 == patternlen) {
                            return false;
                        }
                        int i2 = i + 1;
                        byte foo2 = pattern[i2];
                        while (j < namelen) {
                            if (foo2 == name[j] && glob(pattern, skipUTF8Char(foo2) + i2, name, skipUTF8Char(name[j]) + j)) {
                                return true;
                            }
                            j += skipUTF8Char(name[j]);
                        }
                        return false;
                    } else {
                        while (j < namelen) {
                            if (foo == name[j] && glob(pattern, i, name, j)) {
                                return true;
                            }
                            j += skipUTF8Char(name[j]);
                        }
                        return false;
                    }
                } else if (pattern[i] == 63) {
                    i++;
                    j += skipUTF8Char(name[j]);
                } else if (pattern[i] != name[j]) {
                    return false;
                } else {
                    i += skipUTF8Char(pattern[i]);
                    j += skipUTF8Char(name[j]);
                    if (j < namelen) {
                        continue;
                    } else if (i >= patternlen) {
                        return true;
                    } else {
                        if (pattern[i] == 42) {
                            break;
                        }
                    }
                }
            } else if (i + 1 == patternlen) {
                return false;
            } else {
                int i3 = i + 1;
                if (pattern[i3] != name[j]) {
                    return false;
                }
                i = i3 + skipUTF8Char(pattern[i3]);
                j += skipUTF8Char(name[j]);
            }
        }
        if (i == patternlen && j == namelen) {
            return true;
        }
        if (j < namelen || pattern[i] != 42) {
            return false;
        }
        while (i < patternlen) {
            int i4 = i + 1;
            if (pattern[i] == 42) {
                i = i4;
            } else {
                return false;
            }
        }
        return true;
    }

    static String quote(String path) {
        byte[] _path = str2byte(path);
        int count = 0;
        for (byte b : _path) {
            if (b == 92 || b == 63 || b == 42) {
                count++;
            }
        }
        if (count == 0) {
            return path;
        }
        byte[] _path2 = new byte[_path.length + count];
        int i = 0;
        int j = 0;
        while (i < _path.length) {
            byte b2 = _path[i];
            if (b2 == 92 || b2 == 63 || b2 == 42) {
                _path2[j] = 92;
                j++;
            }
            _path2[j] = b2;
            i++;
            j++;
        }
        return byte2str(_path2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String unquote(String path) {
        byte[] foo = str2byte(path);
        byte[] bar = unquote(foo);
        if (foo.length == bar.length) {
            return path;
        }
        return byte2str(bar);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static byte[] unquote(byte[] path) {
        int pathlen = path.length;
        int i = 0;
        while (i < pathlen) {
            if (path[i] == 92) {
                if (i + 1 == pathlen) {
                    break;
                }
                System.arraycopy(path, i + 1, path, i, path.length - (i + 1));
                pathlen--;
                i++;
            } else {
                i++;
            }
        }
        if (pathlen == path.length) {
            return path;
        }
        byte[] foo = new byte[pathlen];
        System.arraycopy(path, 0, foo, 0, pathlen);
        return foo;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String getFingerPrint(HASH hash, byte[] data) {
        try {
            hash.init();
            hash.update(data, 0, data.length);
            byte[] foo = hash.digest();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < foo.length; i++) {
                int bar = foo[i] & 255;
                sb.append(chars[(bar >>> 4) & 15]);
                sb.append(chars[bar & 15]);
                if (i + 1 < foo.length) {
                    sb.append(":");
                }
            }
            return sb.toString();
        } catch (Exception e) {
            return "???";
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean array_equals(byte[] foo, byte[] bar) {
        int i = foo.length;
        if (i != bar.length) {
            return false;
        }
        for (int j = 0; j < i; j++) {
            if (foo[j] != bar[j]) {
                return false;
            }
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Socket createSocket(final String host, final int port, int timeout) throws JSchException {
        if (timeout == 0) {
            try {
                Socket socket = new Socket(host, port);
                return socket;
            } catch (Exception e) {
                String message = e.toString();
                if (e instanceof Throwable) {
                    throw new JSchException(message, e);
                }
                throw new JSchException(message);
            }
        }
        final Socket[] sockp = new Socket[1];
        final Exception[] ee = new Exception[1];
        String message2 = "";
        Thread tmp = new Thread(new Runnable() { // from class: com.jcraft.jsch.Util.1
            @Override // java.lang.Runnable
            public void run() {
                Socket[] socketArr = sockp;
                socketArr[0] = null;
                try {
                    socketArr[0] = new Socket(host, port);
                } catch (Exception e2) {
                    ee[0] = e2;
                    Socket[] socketArr2 = sockp;
                    if (socketArr2[0] != null && socketArr2[0].isConnected()) {
                        try {
                            sockp[0].close();
                        } catch (Exception e3) {
                        }
                    }
                    sockp[0] = null;
                }
            }
        });
        tmp.setName("Opening Socket " + host);
        tmp.start();
        try {
            tmp.join(timeout);
            message2 = "timeout: ";
        } catch (InterruptedException e2) {
        }
        if (sockp[0] != null && sockp[0].isConnected()) {
            Socket socket2 = sockp[0];
            return socket2;
        }
        String message3 = message2 + "socket is not established";
        if (ee[0] != null) {
            message3 = ee[0].toString();
        }
        tmp.interrupt();
        throw new JSchException(message3, ee[0]);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static byte[] str2byte(String str, String encoding) {
        if (str == null) {
            return null;
        }
        try {
            return str.getBytes(encoding);
        } catch (UnsupportedEncodingException e) {
            return str.getBytes();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static byte[] str2byte(String str) {
        return str2byte(str, "UTF-8");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String byte2str(byte[] str, String encoding) {
        return byte2str(str, 0, str.length, encoding);
    }

    static String byte2str(byte[] str, int s, int l, String encoding) {
        try {
            return new String(str, s, l, encoding);
        } catch (UnsupportedEncodingException e) {
            return new String(str, s, l);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String byte2str(byte[] str) {
        return byte2str(str, 0, str.length, "UTF-8");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String byte2str(byte[] str, int s, int l) {
        return byte2str(str, s, l, "UTF-8");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String toHex(byte[] str) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < str.length; i++) {
            String foo = Integer.toHexString(str[i] & 255);
            StringBuilder sb2 = new StringBuilder();
            sb2.append("0x");
            sb2.append(foo.length() == 1 ? "0" : "");
            sb2.append(foo);
            sb.append(sb2.toString());
            if (i + 1 < str.length) {
                sb.append(":");
            }
        }
        return sb.toString();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void bzero(byte[] foo) {
        if (foo == null) {
            return;
        }
        for (int i = 0; i < foo.length; i++) {
            foo[i] = 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String diffString(String str, String[] not_available) {
        String[] stra = split(str, ",");
        String result = null;
        for (int i = 0; i < stra.length; i++) {
            int j = 0;
            while (true) {
                if (j < not_available.length) {
                    if (stra[i].equals(not_available[j])) {
                        break;
                    }
                    j++;
                } else if (result == null) {
                    result = stra[i];
                } else {
                    result = result + "," + stra[i];
                }
            }
        }
        return result;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String checkTilde(String str) {
        try {
            if (str.startsWith("~")) {
                return str.replace("~", System.getProperty("user.home"));
            }
            return str;
        } catch (SecurityException e) {
            return str;
        }
    }

    private static int skipUTF8Char(byte b) {
        if (((byte) (b & 128)) == 0) {
            return 1;
        }
        if (((byte) (b & 224)) == -64) {
            return 2;
        }
        return ((byte) (b & 240)) == -32 ? 3 : 1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static byte[] fromFile(String _file) throws IOException {
        String _file2 = checkTilde(_file);
        File file = new File(_file2);
        FileInputStream fis = new FileInputStream(_file2);
        try {
            byte[] result = new byte[(int) file.length()];
            int len = 0;
            while (true) {
                int i = fis.read(result, len, result.length - len);
                if (i > 0) {
                    len += i;
                } else {
                    fis.close();
                    return result;
                }
            }
        } finally {
            fis.close();
        }
    }
}
