package com.O000000o.O000000o.O00000o;

import ch.ethz.ssh2.packets.Packets;
import cn.hutool.core.text.CharPool;
import cn.hutool.core.text.CharSequenceUtil;
import java.io.Closeable;
import java.io.IOException;
import java.io.Reader;
import org.apache.commons.lang3.BooleanUtils;
/* compiled from: JsonReader.java */
/* loaded from: classes.dex */
public class O000000o implements Closeable {
    private static final char[] O000000o = ")]}'\n".toCharArray();
    private static final long O00000Oo = -922337203685477580L;
    private static final int O00000o = 1;
    private static final int O00000o0 = 0;
    private static final int O00000oO = 2;
    private static final int O00000oo = 3;
    private static final int O0000O0o = 4;
    private static final int O0000OOo = 5;
    private static final int O0000Oo = 7;
    private static final int O0000Oo0 = 6;
    private static final int O0000OoO = 8;
    private static final int O0000Ooo = 9;
    private static final int O0000o = 14;
    private static final int O0000o0 = 11;
    private static final int O0000o00 = 10;
    private static final int O0000o0O = 12;
    private static final int O0000o0o = 13;
    private static final int O0000oO = 16;
    private static final int O0000oO0 = 15;
    private static final int O0000oOO = 17;
    private static final int O0000oOo = 0;
    private static final int O0000oo = 2;
    private static final int O0000oo0 = 1;
    private static final int O0000ooO = 3;
    private static final int O0000ooo = 4;
    private static final int O000O00o = 7;
    private static final int O00oOooO = 5;
    private static final int O00oOooo = 6;
    private final Reader O000O0OO;
    private int O000OO;
    private long O000OO0o;
    private String O000OOOo;
    private int O000OOo;
    private boolean O000O0Oo = false;
    private final char[] O00oOoOo = new char[1024];
    private int O000O0o0 = 0;
    private int O000O0o = 0;
    private int O000O0oO = 0;
    private int O000O0oo = 0;
    private int O000OO00 = 0;
    private int[] O000OOo0 = new int[32];

    static {
        com.O000000o.O000000o.O00000Oo.O0000O0o.O000000o = new com.O000000o.O000000o.O00000Oo.O0000O0o() { // from class: com.O000000o.O000000o.O00000o.O000000o.1
            @Override // com.O000000o.O000000o.O00000Oo.O0000O0o
            public void O000000o(O000000o o000000o) throws IOException {
                int i;
                if (o000000o instanceof com.O000000o.O000000o.O00000Oo.O000000o.O00000o) {
                    ((com.O000000o.O000000o.O00000Oo.O000000o.O00000o) o000000o).O0000o0O();
                    return;
                }
                int i2 = o000000o.O000OO00;
                if (i2 == 0) {
                    i2 = o000000o.O0000o0O();
                }
                if (i2 == 13) {
                    i = 9;
                } else if (i2 == 12) {
                    i = 8;
                } else if (i2 != 14) {
                    throw new IllegalStateException("Expected a name but was " + o000000o.O00000oo() + "  at line " + o000000o.O0000oOo() + " column " + o000000o.O0000oo0());
                } else {
                    i = 10;
                }
                o000000o.O000OO00 = i;
            }
        };
    }

    public O000000o(Reader reader) {
        this.O000OOo = 0;
        int[] iArr = this.O000OOo0;
        int i = this.O000OOo;
        this.O000OOo = i + 1;
        iArr[i] = 6;
        if (reader == null) {
            throw new NullPointerException("in == null");
        }
        this.O000O0OO = reader;
    }

    private void O000000o(int i) {
        int i2 = this.O000OOo;
        int[] iArr = this.O000OOo0;
        if (i2 == iArr.length) {
            int[] iArr2 = new int[i2 * 2];
            System.arraycopy(iArr, 0, iArr2, 0, i2);
            this.O000OOo0 = iArr2;
        }
        int[] iArr3 = this.O000OOo0;
        int i3 = this.O000OOo;
        this.O000OOo = i3 + 1;
        iArr3[i3] = i;
    }

    private boolean O000000o(char c) throws IOException {
        if (c == '\t' || c == '\n' || c == '\f' || c == '\r' || c == ' ') {
            return false;
        }
        if (c != '#') {
            if (c == ',') {
                return false;
            }
            if (c != '/' && c != '=') {
                if (c == '{' || c == '}' || c == ':') {
                    return false;
                }
                if (c != ';') {
                    switch (c) {
                        case Packets.SSH_MSG_CHANNEL_OPEN_CONFIRMATION /* 91 */:
                        case Packets.SSH_MSG_CHANNEL_WINDOW_ADJUST /* 93 */:
                            return false;
                        case Packets.SSH_MSG_CHANNEL_OPEN_FAILURE /* 92 */:
                            break;
                        default:
                            return true;
                    }
                }
            }
        }
        O0000oo();
        return false;
    }

    private boolean O000000o(String str) throws IOException {
        while (true) {
            if (this.O000O0o0 + str.length() > this.O000O0o && !O00000Oo(str.length())) {
                return false;
            }
            char[] cArr = this.O00oOoOo;
            int i = this.O000O0o0;
            if (cArr[i] != '\n') {
                for (int i2 = 0; i2 < str.length(); i2++) {
                    if (this.O00oOoOo[this.O000O0o0 + i2] != str.charAt(i2)) {
                        break;
                    }
                }
                return true;
            }
            this.O000O0oO++;
            this.O000O0oo = i + 1;
            this.O000O0o0++;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:26:0x005f, code lost:
        if (r1 != '/') goto L50;
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x0061, code lost:
        r7.O000O0o0 = r4;
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x0064, code lost:
        if (r4 != r2) goto L35;
     */
    /* JADX WARN: Code restructure failed: missing block: B:29:0x0066, code lost:
        r7.O000O0o0--;
        r2 = O00000Oo(2);
        r7.O000O0o0++;
     */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x0074, code lost:
        if (r2 != false) goto L35;
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x0076, code lost:
        return r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x0077, code lost:
        O0000oo();
        r2 = r7.O000O0o0;
        r3 = r0[r2];
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x0080, code lost:
        if (r3 == '*') goto L37;
     */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x0082, code lost:
        if (r3 == '/') goto L45;
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x0084, code lost:
        return r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x0085, code lost:
        r7.O000O0o0 = r2 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x008e, code lost:
        r7.O000O0o0 = r2 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x0098, code lost:
        if (O000000o(cn.hutool.core.util.URLUtil.WAR_URL_SEPARATOR) == false) goto L40;
     */
    /* JADX WARN: Code restructure failed: missing block: B:42:0x00a5, code lost:
        throw O00000Oo("Unterminated comment");
     */
    /* JADX WARN: Code restructure failed: missing block: B:43:0x00a6, code lost:
        r7.O000O0o0 = r4;
     */
    /* JADX WARN: Code restructure failed: missing block: B:44:0x00aa, code lost:
        if (r1 != '#') goto L55;
     */
    /* JADX WARN: Code restructure failed: missing block: B:45:0x00ac, code lost:
        O0000oo();
     */
    /* JADX WARN: Code restructure failed: missing block: B:46:0x00b0, code lost:
        return r1;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private int O00000Oo(boolean r8) throws java.io.IOException {
        /*
            r7 = this;
            char[] r0 = r7.O00oOoOo
        L2:
            int r1 = r7.O000O0o0
        L4:
            int r2 = r7.O000O0o
        L6:
            r3 = 1
            if (r1 != r2) goto L40
            r7.O000O0o0 = r1
            boolean r1 = r7.O00000Oo(r3)
            if (r1 != 0) goto L3c
            if (r8 != 0) goto L15
            r8 = -1
            return r8
        L15:
            java.io.EOFException r8 = new java.io.EOFException
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "End of input at line "
            r0.append(r1)
            int r1 = r7.O0000oOo()
            r0.append(r1)
            java.lang.String r1 = " column "
            r0.append(r1)
            int r1 = r7.O0000oo0()
            r0.append(r1)
            java.lang.String r0 = r0.toString()
            r8.<init>(r0)
            throw r8
        L3c:
            int r1 = r7.O000O0o0
            int r2 = r7.O000O0o
        L40:
            int r4 = r1 + 1
            char r1 = r0[r1]
            r5 = 10
            if (r1 != r5) goto L50
            int r1 = r7.O000O0oO
            int r1 = r1 + r3
            r7.O000O0oO = r1
            r7.O000O0oo = r4
            goto Lb1
        L50:
            r5 = 32
            if (r1 == r5) goto Lb1
            r5 = 13
            if (r1 == r5) goto Lb1
            r5 = 9
            if (r1 != r5) goto L5d
            goto Lb1
        L5d:
            r5 = 47
            if (r1 != r5) goto La6
            r7.O000O0o0 = r4
            r6 = 2
            if (r4 != r2) goto L77
            int r2 = r7.O000O0o0
            int r2 = r2 - r3
            r7.O000O0o0 = r2
            boolean r2 = r7.O00000Oo(r6)
            int r4 = r7.O000O0o0
            int r4 = r4 + r3
            r7.O000O0o0 = r4
            if (r2 != 0) goto L77
            return r1
        L77:
            r7.O0000oo()
            int r2 = r7.O000O0o0
            char r3 = r0[r2]
            r4 = 42
            if (r3 == r4) goto L8e
            if (r3 == r5) goto L85
            return r1
        L85:
            int r2 = r2 + 1
            r7.O000O0o0 = r2
        L89:
            r7.O0000ooO()
            goto L2
        L8e:
            int r2 = r2 + 1
            r7.O000O0o0 = r2
        */
        //  java.lang.String r1 = "*/"
        /*
            boolean r1 = r7.O000000o(r1)
            if (r1 == 0) goto L9f
            int r1 = r7.O000O0o0
            int r1 = r1 + r6
            goto L4
        L9f:
            java.lang.String r8 = "Unterminated comment"
            java.io.IOException r8 = r7.O00000Oo(r8)
            throw r8
        La6:
            r2 = 35
            r7.O000O0o0 = r4
            if (r1 != r2) goto Lb0
            r7.O0000oo()
            goto L89
        Lb0:
            return r1
        Lb1:
            r1 = r4
            goto L6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.O000000o.O000000o.O00000o.O000000o.O00000Oo(boolean):int");
    }

    private IOException O00000Oo(String str) throws IOException {
        throw new O0000O0o(str + " at line " + O0000oOo() + " column " + O0000oo0());
    }

    private String O00000Oo(char c) throws IOException {
        char[] cArr = this.O00oOoOo;
        StringBuilder sb = new StringBuilder();
        while (true) {
            int i = this.O000O0o0;
            int i2 = this.O000O0o;
            while (true) {
                if (i < i2) {
                    int i3 = i + 1;
                    char c2 = cArr[i];
                    if (c2 == c) {
                        this.O000O0o0 = i3;
                        sb.append(cArr, i, (i3 - i) - 1);
                        return sb.toString();
                    } else if (c2 == '\\') {
                        this.O000O0o0 = i3;
                        sb.append(cArr, i, (i3 - i) - 1);
                        sb.append(O0000ooo());
                        break;
                    } else {
                        if (c2 == '\n') {
                            this.O000O0oO++;
                            this.O000O0oo = i3;
                        }
                        i = i3;
                    }
                } else {
                    sb.append(cArr, i, i - i);
                    this.O000O0o0 = i;
                    if (!O00000Oo(1)) {
                        throw O00000Oo("Unterminated string");
                    }
                }
            }
        }
    }

    private boolean O00000Oo(int i) throws IOException {
        int i2;
        char[] cArr = this.O00oOoOo;
        int i3 = this.O000O0oo;
        int i4 = this.O000O0o0;
        this.O000O0oo = i3 - i4;
        int i5 = this.O000O0o;
        if (i5 != i4) {
            this.O000O0o = i5 - i4;
            System.arraycopy(cArr, i4, cArr, 0, this.O000O0o);
        } else {
            this.O000O0o = 0;
        }
        this.O000O0o0 = 0;
        do {
            Reader reader = this.O000O0OO;
            int i6 = this.O000O0o;
            int read = reader.read(cArr, i6, cArr.length - i6);
            if (read == -1) {
                return false;
            }
            this.O000O0o += read;
            if (this.O000O0oO == 0 && (i2 = this.O000O0oo) == 0 && this.O000O0o > 0 && cArr[0] == 65279) {
                this.O000O0o0++;
                this.O000O0oo = i2 + 1;
                i++;
            }
        } while (this.O000O0o < i);
        return true;
    }

    private void O00000o0(char c) throws IOException {
        char[] cArr = this.O00oOoOo;
        while (true) {
            int i = this.O000O0o0;
            int i2 = this.O000O0o;
            while (true) {
                if (i < i2) {
                    int i3 = i + 1;
                    char c2 = cArr[i];
                    if (c2 == c) {
                        this.O000O0o0 = i3;
                        return;
                    } else if (c2 == '\\') {
                        this.O000O0o0 = i3;
                        O0000ooo();
                        break;
                    } else {
                        if (c2 == '\n') {
                            this.O000O0oO++;
                            this.O000O0oo = i3;
                        }
                        i = i3;
                    }
                } else {
                    this.O000O0o0 = i;
                    if (!O00000Oo(1)) {
                        throw O00000Oo("Unterminated string");
                    }
                }
            }
        }
    }

    private int O0000o() throws IOException {
        int i;
        String str;
        String str2;
        char c = this.O00oOoOo[this.O000O0o0];
        if (c == 't' || c == 'T') {
            i = 5;
            str = "true";
            str2 = "TRUE";
        } else if (c == 'f' || c == 'F') {
            i = 6;
            str = BooleanUtils.FALSE;
            str2 = "FALSE";
        } else if (c != 'n' && c != 'N') {
            return 0;
        } else {
            i = 7;
            str = CharSequenceUtil.NULL;
            str2 = "NULL";
        }
        int length = str.length();
        for (int i2 = 1; i2 < length; i2++) {
            if (this.O000O0o0 + i2 >= this.O000O0o && !O00000Oo(i2 + 1)) {
                return 0;
            }
            char c2 = this.O00oOoOo[this.O000O0o0 + i2];
            if (c2 != str.charAt(i2) && c2 != str2.charAt(i2)) {
                return 0;
            }
        }
        if ((this.O000O0o0 + length < this.O000O0o || O00000Oo(length + 1)) && O000000o(this.O00oOoOo[this.O000O0o0 + length])) {
            return 0;
        }
        this.O000O0o0 += length;
        this.O000OO00 = i;
        return i;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int O0000o0O() throws IOException {
        int i;
        int O00000Oo2;
        int[] iArr = this.O000OOo0;
        int i2 = this.O000OOo;
        int i3 = iArr[i2 - 1];
        if (i3 == 1) {
            iArr[i2 - 1] = 2;
        } else if (i3 != 2) {
            if (i3 == 3 || i3 == 5) {
                this.O000OOo0[this.O000OOo - 1] = 4;
                if (i3 == 5 && (O00000Oo2 = O00000Oo(true)) != 44) {
                    if (O00000Oo2 != 59) {
                        if (O00000Oo2 == 125) {
                            this.O000OO00 = 2;
                            return 2;
                        }
                        throw O00000Oo("Unterminated object");
                    }
                    O0000oo();
                }
                int O00000Oo3 = O00000Oo(true);
                if (O00000Oo3 == 34) {
                    i = 13;
                } else if (O00000Oo3 == 39) {
                    O0000oo();
                    i = 12;
                } else if (O00000Oo3 == 125) {
                    if (i3 != 5) {
                        this.O000OO00 = 2;
                        return 2;
                    }
                    throw O00000Oo("Expected name");
                } else {
                    O0000oo();
                    this.O000O0o0--;
                    if (!O000000o((char) O00000Oo3)) {
                        throw O00000Oo("Expected name");
                    }
                    i = 14;
                }
            } else if (i3 == 4) {
                iArr[i2 - 1] = 5;
                int O00000Oo4 = O00000Oo(true);
                if (O00000Oo4 != 58) {
                    if (O00000Oo4 != 61) {
                        throw O00000Oo("Expected ':'");
                    }
                    O0000oo();
                    if (this.O000O0o0 < this.O000O0o || O00000Oo(1)) {
                        char[] cArr = this.O00oOoOo;
                        int i4 = this.O000O0o0;
                        if (cArr[i4] == '>') {
                            this.O000O0o0 = i4 + 1;
                        }
                    }
                }
            } else if (i3 == 6) {
                if (this.O000O0Oo) {
                    O00oOooO();
                }
                this.O000OOo0[this.O000OOo - 1] = 7;
            } else if (i3 == 7) {
                if (O00000Oo(false) == -1) {
                    i = 17;
                } else {
                    O0000oo();
                    this.O000O0o0--;
                }
            } else if (i3 == 8) {
                throw new IllegalStateException("JsonReader is closed");
            }
            this.O000OO00 = i;
            return i;
        } else {
            int O00000Oo5 = O00000Oo(true);
            if (O00000Oo5 != 44) {
                if (O00000Oo5 != 59) {
                    if (O00000Oo5 == 93) {
                        this.O000OO00 = 4;
                        return 4;
                    }
                    throw O00000Oo("Unterminated array");
                }
                O0000oo();
            }
        }
        int O00000Oo6 = O00000Oo(true);
        if (O00000Oo6 != 34) {
            if (O00000Oo6 == 39) {
                O0000oo();
                this.O000OO00 = 8;
                return 8;
            }
            if (O00000Oo6 != 44 && O00000Oo6 != 59) {
                if (O00000Oo6 == 91) {
                    this.O000OO00 = 3;
                    return 3;
                } else if (O00000Oo6 != 93) {
                    if (O00000Oo6 == 123) {
                        this.O000OO00 = 1;
                        return 1;
                    }
                    this.O000O0o0--;
                    if (this.O000OOo == 1) {
                        O0000oo();
                    }
                    int O0000o2 = O0000o();
                    if (O0000o2 != 0) {
                        return O0000o2;
                    }
                    int O0000oO02 = O0000oO0();
                    if (O0000oO02 != 0) {
                        return O0000oO02;
                    }
                    if (!O000000o(this.O00oOoOo[this.O000O0o0])) {
                        throw O00000Oo("Expected value");
                    }
                    O0000oo();
                    i = 10;
                } else if (i3 == 1) {
                    this.O000OO00 = 4;
                    return 4;
                }
            }
            if (i3 == 1 || i3 == 2) {
                O0000oo();
                this.O000O0o0--;
                this.O000OO00 = 7;
                return 7;
            }
            throw O00000Oo("Unexpected value");
        }
        if (this.O000OOo == 1) {
            O0000oo();
        }
        i = 9;
        this.O000OO00 = i;
        return i;
    }

    /* JADX WARN: Code restructure failed: missing block: B:34:0x004b, code lost:
        O0000oo();
     */
    /* JADX WARN: Removed duplicated region for block: B:46:0x007b  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x0085  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private java.lang.String O0000oO() throws java.io.IOException {
        /*
            r6 = this;
            r0 = 0
            r1 = 0
            r2 = r1
        L3:
            r1 = r0
        L4:
            int r3 = r6.O000O0o0
            int r4 = r3 + r1
            int r5 = r6.O000O0o
            if (r4 >= r5) goto L4f
            char[] r4 = r6.O00oOoOo
            int r3 = r3 + r1
            char r3 = r4[r3]
            r4 = 9
            if (r3 == r4) goto L5d
            r4 = 10
            if (r3 == r4) goto L5d
            r4 = 12
            if (r3 == r4) goto L5d
            r4 = 13
            if (r3 == r4) goto L5d
            r4 = 32
            if (r3 == r4) goto L5d
            r4 = 35
            if (r3 == r4) goto L4b
            r4 = 44
            if (r3 == r4) goto L5d
            r4 = 47
            if (r3 == r4) goto L4b
            r4 = 61
            if (r3 == r4) goto L4b
            r4 = 123(0x7b, float:1.72E-43)
            if (r3 == r4) goto L5d
            r4 = 125(0x7d, float:1.75E-43)
            if (r3 == r4) goto L5d
            r4 = 58
            if (r3 == r4) goto L5d
            r4 = 59
            if (r3 == r4) goto L4b
            switch(r3) {
                case 91: goto L5d;
                case 92: goto L4b;
                case 93: goto L5d;
                default: goto L48;
            }
        L48:
            int r1 = r1 + 1
            goto L4
        L4b:
            r6.O0000oo()
            goto L5d
        L4f:
            char[] r3 = r6.O00oOoOo
            int r3 = r3.length
            if (r1 >= r3) goto L5f
            int r3 = r1 + 1
            boolean r3 = r6.O00000Oo(r3)
            if (r3 == 0) goto L5d
            goto L4
        L5d:
            r0 = r1
            goto L79
        L5f:
            if (r2 != 0) goto L66
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
        L66:
            char[] r3 = r6.O00oOoOo
            int r4 = r6.O000O0o0
            r2.append(r3, r4, r1)
            int r3 = r6.O000O0o0
            int r3 = r3 + r1
            r6.O000O0o0 = r3
            r1 = 1
            boolean r1 = r6.O00000Oo(r1)
            if (r1 != 0) goto L3
        L79:
            if (r2 != 0) goto L85
            java.lang.String r1 = new java.lang.String
            char[] r2 = r6.O00oOoOo
            int r3 = r6.O000O0o0
            r1.<init>(r2, r3, r0)
            goto L90
        L85:
            char[] r1 = r6.O00oOoOo
            int r3 = r6.O000O0o0
            r2.append(r1, r3, r0)
            java.lang.String r1 = r2.toString()
        L90:
            int r2 = r6.O000O0o0
            int r2 = r2 + r0
            r6.O000O0o0 = r2
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.O000000o.O000000o.O00000o.O000000o.O0000oO():java.lang.String");
    }

    /* JADX WARN: Code restructure failed: missing block: B:54:0x0096, code lost:
        if (O000000o(r14) != false) goto L75;
     */
    /* JADX WARN: Code restructure failed: missing block: B:55:0x0098, code lost:
        if (r9 != 2) goto L21;
     */
    /* JADX WARN: Code restructure failed: missing block: B:56:0x009a, code lost:
        if (r10 == false) goto L21;
     */
    /* JADX WARN: Code restructure failed: missing block: B:58:0x00a0, code lost:
        if (r11 != Long.MIN_VALUE) goto L15;
     */
    /* JADX WARN: Code restructure failed: missing block: B:59:0x00a2, code lost:
        if (r13 == false) goto L21;
     */
    /* JADX WARN: Code restructure failed: missing block: B:60:0x00a4, code lost:
        if (r13 == false) goto L16;
     */
    /* JADX WARN: Code restructure failed: missing block: B:62:0x00a7, code lost:
        r11 = -r11;
     */
    /* JADX WARN: Code restructure failed: missing block: B:63:0x00a8, code lost:
        r18.O000OO0o = r11;
        r18.O000O0o0 += r3;
        r1 = 15;
     */
    /* JADX WARN: Code restructure failed: missing block: B:64:0x00b1, code lost:
        r18.O000OO00 = r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:65:0x00b3, code lost:
        return r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:66:0x00b4, code lost:
        if (r9 == 2) goto L27;
     */
    /* JADX WARN: Code restructure failed: missing block: B:68:0x00b7, code lost:
        if (r9 == 4) goto L27;
     */
    /* JADX WARN: Code restructure failed: missing block: B:70:0x00ba, code lost:
        if (r9 != 7) goto L26;
     */
    /* JADX WARN: Code restructure failed: missing block: B:72:0x00bd, code lost:
        return 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:74:0x00bf, code lost:
        r18.O000OO = r3;
        r1 = 16;
     */
    /* JADX WARN: Code restructure failed: missing block: B:75:0x00c4, code lost:
        return 0;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private int O0000oO0() throws java.io.IOException {
        /*
            Method dump skipped, instructions count: 242
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.O000000o.O000000o.O00000o.O000000o.O0000oO0():int");
    }

    /* JADX WARN: Code restructure failed: missing block: B:33:0x0048, code lost:
        O0000oo();
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void O0000oOO() throws java.io.IOException {
        /*
            r4 = this;
        L0:
            r0 = 0
        L1:
            int r1 = r4.O000O0o0
            int r2 = r1 + r0
            int r3 = r4.O000O0o
            if (r2 >= r3) goto L51
            char[] r2 = r4.O00oOoOo
            int r1 = r1 + r0
            char r1 = r2[r1]
            r2 = 9
            if (r1 == r2) goto L4b
            r2 = 10
            if (r1 == r2) goto L4b
            r2 = 12
            if (r1 == r2) goto L4b
            r2 = 13
            if (r1 == r2) goto L4b
            r2 = 32
            if (r1 == r2) goto L4b
            r2 = 35
            if (r1 == r2) goto L48
            r2 = 44
            if (r1 == r2) goto L4b
            r2 = 47
            if (r1 == r2) goto L48
            r2 = 61
            if (r1 == r2) goto L48
            r2 = 123(0x7b, float:1.72E-43)
            if (r1 == r2) goto L4b
            r2 = 125(0x7d, float:1.75E-43)
            if (r1 == r2) goto L4b
            r2 = 58
            if (r1 == r2) goto L4b
            r2 = 59
            if (r1 == r2) goto L48
            switch(r1) {
                case 91: goto L4b;
                case 92: goto L48;
                case 93: goto L4b;
                default: goto L45;
            }
        L45:
            int r0 = r0 + 1
            goto L1
        L48:
            r4.O0000oo()
        L4b:
            int r1 = r4.O000O0o0
            int r1 = r1 + r0
            r4.O000O0o0 = r1
            return
        L51:
            int r1 = r1 + r0
            r4.O000O0o0 = r1
            r0 = 1
            boolean r0 = r4.O00000Oo(r0)
            if (r0 != 0) goto L0
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.O000000o.O000000o.O00000o.O000000o.O0000oOO():void");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int O0000oOo() {
        return this.O000O0oO + 1;
    }

    private void O0000oo() throws IOException {
        if (!this.O000O0Oo) {
            throw O00000Oo("Use JsonReader.setLenient(true) to accept malformed JSON");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int O0000oo0() {
        return (this.O000O0o0 - this.O000O0oo) + 1;
    }

    private void O0000ooO() throws IOException {
        char c;
        do {
            if (this.O000O0o0 >= this.O000O0o && !O00000Oo(1)) {
                return;
            }
            char[] cArr = this.O00oOoOo;
            int i = this.O000O0o0;
            this.O000O0o0 = i + 1;
            c = cArr[i];
            if (c == '\n') {
                this.O000O0oO++;
                this.O000O0oo = this.O000O0o0;
                return;
            }
        } while (c != '\r');
    }

    private char O0000ooo() throws IOException {
        int i;
        int i2;
        if (this.O000O0o0 != this.O000O0o || O00000Oo(1)) {
            char[] cArr = this.O00oOoOo;
            int i3 = this.O000O0o0;
            this.O000O0o0 = i3 + 1;
            char c = cArr[i3];
            if (c == '\n') {
                this.O000O0oO++;
                this.O000O0oo = this.O000O0o0;
            } else if (c == 'b') {
                return '\b';
            } else {
                if (c == 'f') {
                    return '\f';
                }
                if (c == 'n') {
                    return '\n';
                }
                if (c == 'r') {
                    return '\r';
                }
                if (c == 't') {
                    return '\t';
                }
                if (c == 'u') {
                    if (this.O000O0o0 + 4 <= this.O000O0o || O00000Oo(4)) {
                        char c2 = 0;
                        int i4 = this.O000O0o0;
                        int i5 = i4 + 4;
                        while (i4 < i5) {
                            char c3 = this.O00oOoOo[i4];
                            char c4 = (char) (c2 << 4);
                            if (c3 < '0' || c3 > '9') {
                                if (c3 >= 'a' && c3 <= 'f') {
                                    i = c3 - 'a';
                                } else if (c3 < 'A' || c3 > 'F') {
                                    throw new NumberFormatException("\\u" + new String(this.O00oOoOo, this.O000O0o0, 4));
                                } else {
                                    i = c3 - 'A';
                                }
                                i2 = i + 10;
                            } else {
                                i2 = c3 - '0';
                            }
                            c2 = (char) (c4 + i2);
                            i4++;
                        }
                        this.O000O0o0 += 4;
                        return c2;
                    }
                    throw O00000Oo("Unterminated escape sequence");
                }
            }
            return c;
        }
        throw O00000Oo("Unterminated escape sequence");
    }

    private void O00oOooO() throws IOException {
        O00000Oo(true);
        this.O000O0o0--;
        int i = this.O000O0o0;
        char[] cArr = O000000o;
        if (i + cArr.length > this.O000O0o && !O00000Oo(cArr.length)) {
            return;
        }
        int i2 = 0;
        while (true) {
            char[] cArr2 = O000000o;
            if (i2 >= cArr2.length) {
                this.O000O0o0 += cArr2.length;
                return;
            } else if (this.O00oOoOo[this.O000O0o0 + i2] != cArr2[i2]) {
                return;
            } else {
                i2++;
            }
        }
    }

    public void O000000o() throws IOException {
        int i = this.O000OO00;
        if (i == 0) {
            i = O0000o0O();
        }
        if (i == 3) {
            O000000o(1);
            this.O000OO00 = 0;
            return;
        }
        throw new IllegalStateException("Expected BEGIN_ARRAY but was " + O00000oo() + " at line " + O0000oOo() + " column " + O0000oo0());
    }

    public final void O000000o(boolean z) {
        this.O000O0Oo = z;
    }

    public void O00000Oo() throws IOException {
        int i = this.O000OO00;
        if (i == 0) {
            i = O0000o0O();
        }
        if (i == 4) {
            this.O000OOo--;
            this.O000OO00 = 0;
            return;
        }
        throw new IllegalStateException("Expected END_ARRAY but was " + O00000oo() + " at line " + O0000oOo() + " column " + O0000oo0());
    }

    public void O00000o() throws IOException {
        int i = this.O000OO00;
        if (i == 0) {
            i = O0000o0O();
        }
        if (i == 2) {
            this.O000OOo--;
            this.O000OO00 = 0;
            return;
        }
        throw new IllegalStateException("Expected END_OBJECT but was " + O00000oo() + " at line " + O0000oOo() + " column " + O0000oo0());
    }

    public void O00000o0() throws IOException {
        int i = this.O000OO00;
        if (i == 0) {
            i = O0000o0O();
        }
        if (i == 1) {
            O000000o(3);
            this.O000OO00 = 0;
            return;
        }
        throw new IllegalStateException("Expected BEGIN_OBJECT but was " + O00000oo() + " at line " + O0000oOo() + " column " + O0000oo0());
    }

    public boolean O00000oO() throws IOException {
        int i = this.O000OO00;
        if (i == 0) {
            i = O0000o0O();
        }
        return (i == 2 || i == 4) ? false : true;
    }

    public O00000o0 O00000oo() throws IOException {
        int i = this.O000OO00;
        if (i == 0) {
            i = O0000o0O();
        }
        switch (i) {
            case 1:
                return O00000o0.BEGIN_OBJECT;
            case 2:
                return O00000o0.END_OBJECT;
            case 3:
                return O00000o0.BEGIN_ARRAY;
            case 4:
                return O00000o0.END_ARRAY;
            case 5:
            case 6:
                return O00000o0.BOOLEAN;
            case 7:
                return O00000o0.NULL;
            case 8:
            case 9:
            case 10:
            case 11:
                return O00000o0.STRING;
            case 12:
            case 13:
            case 14:
                return O00000o0.NAME;
            case 15:
            case 16:
                return O00000o0.NUMBER;
            case 17:
                return O00000o0.END_DOCUMENT;
            default:
                throw new AssertionError();
        }
    }

    public String O0000O0o() throws IOException {
        char c;
        String O00000Oo2;
        int i = this.O000OO00;
        if (i == 0) {
            i = O0000o0O();
        }
        if (i == 14) {
            O00000Oo2 = O0000oO();
        } else {
            if (i == 12) {
                c = CharPool.SINGLE_QUOTE;
            } else if (i != 13) {
                throw new IllegalStateException("Expected a name but was " + O00000oo() + " at line " + O0000oOo() + " column " + O0000oo0());
            } else {
                c = CharPool.DOUBLE_QUOTES;
            }
            O00000Oo2 = O00000Oo(c);
        }
        this.O000OO00 = 0;
        return O00000Oo2;
    }

    public String O0000OOo() throws IOException {
        String str;
        char c;
        int i = this.O000OO00;
        if (i == 0) {
            i = O0000o0O();
        }
        if (i == 10) {
            str = O0000oO();
        } else {
            if (i == 8) {
                c = CharPool.SINGLE_QUOTE;
            } else if (i == 9) {
                c = CharPool.DOUBLE_QUOTES;
            } else if (i == 11) {
                str = this.O000OOOo;
                this.O000OOOo = null;
            } else if (i == 15) {
                str = Long.toString(this.O000OO0o);
            } else if (i != 16) {
                throw new IllegalStateException("Expected a string but was " + O00000oo() + " at line " + O0000oOo() + " column " + O0000oo0());
            } else {
                str = new String(this.O00oOoOo, this.O000O0o0, this.O000OO);
                this.O000O0o0 += this.O000OO;
            }
            str = O00000Oo(c);
        }
        this.O000OO00 = 0;
        return str;
    }

    public void O0000Oo() throws IOException {
        int i = this.O000OO00;
        if (i == 0) {
            i = O0000o0O();
        }
        if (i == 7) {
            this.O000OO00 = 0;
            return;
        }
        throw new IllegalStateException("Expected null but was " + O00000oo() + " at line " + O0000oOo() + " column " + O0000oo0());
    }

    public boolean O0000Oo0() throws IOException {
        int i = this.O000OO00;
        if (i == 0) {
            i = O0000o0O();
        }
        if (i == 5) {
            this.O000OO00 = 0;
            return true;
        } else if (i == 6) {
            this.O000OO00 = 0;
            return false;
        } else {
            throw new IllegalStateException("Expected a boolean but was " + O00000oo() + " at line " + O0000oOo() + " column " + O0000oo0());
        }
    }

    public double O0000OoO() throws IOException {
        String O00000Oo2;
        int i = this.O000OO00;
        if (i == 0) {
            i = O0000o0O();
        }
        if (i == 15) {
            this.O000OO00 = 0;
            return this.O000OO0o;
        }
        if (i == 16) {
            this.O000OOOo = new String(this.O00oOoOo, this.O000O0o0, this.O000OO);
            this.O000O0o0 += this.O000OO;
        } else {
            if (i == 8 || i == 9) {
                O00000Oo2 = O00000Oo(i == 8 ? CharPool.SINGLE_QUOTE : CharPool.DOUBLE_QUOTES);
            } else if (i == 10) {
                O00000Oo2 = O0000oO();
            } else if (i != 11) {
                throw new IllegalStateException("Expected a double but was " + O00000oo() + " at line " + O0000oOo() + " column " + O0000oo0());
            }
            this.O000OOOo = O00000Oo2;
        }
        this.O000OO00 = 11;
        double parseDouble = Double.parseDouble(this.O000OOOo);
        if (this.O000O0Oo || !(Double.isNaN(parseDouble) || Double.isInfinite(parseDouble))) {
            this.O000OOOo = null;
            this.O000OO00 = 0;
            return parseDouble;
        }
        throw new O0000O0o("JSON forbids NaN and infinities: " + parseDouble + " at line " + O0000oOo() + " column " + O0000oo0());
    }

    public long O0000Ooo() throws IOException {
        int i = this.O000OO00;
        if (i == 0) {
            i = O0000o0O();
        }
        if (i == 15) {
            this.O000OO00 = 0;
            return this.O000OO0o;
        }
        if (i == 16) {
            this.O000OOOo = new String(this.O00oOoOo, this.O000O0o0, this.O000OO);
            this.O000O0o0 += this.O000OO;
        } else if (i != 8 && i != 9) {
            throw new IllegalStateException("Expected a long but was " + O00000oo() + " at line " + O0000oOo() + " column " + O0000oo0());
        } else {
            this.O000OOOo = O00000Oo(i == 8 ? CharPool.SINGLE_QUOTE : CharPool.DOUBLE_QUOTES);
            try {
                long parseLong = Long.parseLong(this.O000OOOo);
                this.O000OO00 = 0;
                return parseLong;
            } catch (NumberFormatException e) {
            }
        }
        this.O000OO00 = 11;
        double parseDouble = Double.parseDouble(this.O000OOOo);
        long j = (long) parseDouble;
        if (j == parseDouble) {
            this.O000OOOo = null;
            this.O000OO00 = 0;
            return j;
        }
        throw new NumberFormatException("Expected a long but was " + this.O000OOOo + " at line " + O0000oOo() + " column " + O0000oo0());
    }

    public void O0000o0() throws IOException {
        char c;
        int i = 0;
        do {
            int i2 = this.O000OO00;
            if (i2 == 0) {
                i2 = O0000o0O();
            }
            if (i2 == 3) {
                O000000o(1);
            } else if (i2 == 1) {
                O000000o(3);
            } else {
                if (i2 == 4 || i2 == 2) {
                    this.O000OOo--;
                    i--;
                } else if (i2 == 14 || i2 == 10) {
                    O0000oOO();
                } else {
                    if (i2 == 8 || i2 == 12) {
                        c = CharPool.SINGLE_QUOTE;
                    } else if (i2 == 9 || i2 == 13) {
                        c = CharPool.DOUBLE_QUOTES;
                    } else if (i2 == 16) {
                        this.O000O0o0 += this.O000OO;
                    }
                    O00000o0(c);
                }
                this.O000OO00 = 0;
            }
            i++;
            this.O000OO00 = 0;
        } while (i != 0);
    }

    public int O0000o00() throws IOException {
        int i = this.O000OO00;
        if (i == 0) {
            i = O0000o0O();
        }
        if (i == 15) {
            long j = this.O000OO0o;
            int i2 = (int) j;
            if (j == i2) {
                this.O000OO00 = 0;
                return i2;
            }
            throw new NumberFormatException("Expected an int but was " + this.O000OO0o + " at line " + O0000oOo() + " column " + O0000oo0());
        }
        if (i == 16) {
            this.O000OOOo = new String(this.O00oOoOo, this.O000O0o0, this.O000OO);
            this.O000O0o0 += this.O000OO;
        } else if (i != 8 && i != 9) {
            throw new IllegalStateException("Expected an int but was " + O00000oo() + " at line " + O0000oOo() + " column " + O0000oo0());
        } else {
            this.O000OOOo = O00000Oo(i == 8 ? CharPool.SINGLE_QUOTE : CharPool.DOUBLE_QUOTES);
            try {
                int parseInt = Integer.parseInt(this.O000OOOo);
                this.O000OO00 = 0;
                return parseInt;
            } catch (NumberFormatException e) {
            }
        }
        this.O000OO00 = 11;
        double parseDouble = Double.parseDouble(this.O000OOOo);
        int i3 = (int) parseDouble;
        if (i3 == parseDouble) {
            this.O000OOOo = null;
            this.O000OO00 = 0;
            return i3;
        }
        throw new NumberFormatException("Expected an int but was " + this.O000OOOo + " at line " + O0000oOo() + " column " + O0000oo0());
    }

    public final boolean O0000o0o() {
        return this.O000O0Oo;
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.O000OO00 = 0;
        this.O000OOo0[0] = 8;
        this.O000OOo = 1;
        this.O000O0OO.close();
    }

    public String toString() {
        return getClass().getSimpleName() + " at line " + O0000oOo() + " column " + O0000oo0();
    }
}
