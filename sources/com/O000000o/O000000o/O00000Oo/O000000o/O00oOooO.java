package com.O000000o.O000000o.O00000Oo.O000000o;

import cn.hutool.core.text.CharSequenceUtil;
import com.O000000o.O000000o.O000O00o;
import com.O000000o.O000000o.O000O0OO;
import com.O000000o.O000000o.O000O0o0;
import com.O000000o.O000000o.O000OO0o;
import com.O000000o.O000000o.O000OOo;
import com.O000000o.O000000o.O000o00;
import com.O000000o.O000000o.O000o000;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.BitSet;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.UUID;
/* compiled from: TypeAdapters.java */
/* loaded from: classes.dex */
public final class O00oOooO {
    public static final O000o000<Class> O000000o = new O000o000<Class>() { // from class: com.O000000o.O000000o.O00000Oo.O000000o.O00oOooO.1
        @Override // com.O000000o.O000000o.O000o000
        /* renamed from: O000000o */
        public Class O00000Oo(com.O000000o.O000000o.O00000o.O000000o o000000o) throws IOException {
            if (o000000o.O00000oo() == com.O000000o.O000000o.O00000o.O00000o0.NULL) {
                o000000o.O0000Oo();
                return null;
            }
            throw new UnsupportedOperationException("Attempted to deserialize a java.lang.Class. Forgot to register a type adapter?");
        }

        @Override // com.O000000o.O000000o.O000o000
        public void O000000o(com.O000000o.O000000o.O00000o.O00000o o00000o, Class cls) throws IOException {
            if (cls == null) {
                o00000o.O00000oo();
                return;
            }
            throw new UnsupportedOperationException("Attempted to serialize java.lang.Class: " + cls.getName() + ". Forgot to register a type adapter?");
        }
    };
    public static final O000o00 O00000Oo = O000000o(Class.class, O000000o);
    public static final O000o000<BitSet> O00000o0 = new O000o000<BitSet>() { // from class: com.O000000o.O000000o.O00000Oo.O000000o.O00oOooO.12
        /* JADX WARN: Code restructure failed: missing block: B:17:0x0038, code lost:
            if (java.lang.Integer.parseInt(r1) != 0) goto L20;
         */
        /* JADX WARN: Code restructure failed: missing block: B:19:0x003b, code lost:
            r5 = false;
         */
        /* JADX WARN: Code restructure failed: missing block: B:27:0x0075, code lost:
            if (r8.O0000o00() != 0) goto L20;
         */
        @Override // com.O000000o.O000000o.O000o000
        /* renamed from: O000000o */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public java.util.BitSet O00000Oo(com.O000000o.O000000o.O00000o.O000000o r8) throws java.io.IOException {
            /*
                r7 = this;
                com.O000000o.O000000o.O00000o.O00000o0 r0 = r8.O00000oo()
                com.O000000o.O000000o.O00000o.O00000o0 r1 = com.O000000o.O000000o.O00000o.O00000o0.NULL
                if (r0 != r1) goto Ld
                r8.O0000Oo()
                r8 = 0
                return r8
            Ld:
                java.util.BitSet r0 = new java.util.BitSet
                r0.<init>()
                r8.O000000o()
                com.O000000o.O000000o.O00000o.O00000o0 r1 = r8.O00000oo()
                r2 = 0
                r3 = r2
            L1b:
                com.O000000o.O000000o.O00000o.O00000o0 r4 = com.O000000o.O000000o.O00000o.O00000o0.END_ARRAY
                if (r1 == r4) goto L83
                int[] r4 = com.O000000o.O000000o.O00000Oo.O000000o.O00oOooO.AnonymousClass26.O000000o
                int r5 = r1.ordinal()
                r4 = r4[r5]
                r5 = 1
                if (r4 == r5) goto L71
                r6 = 2
                if (r4 == r6) goto L6c
                r6 = 3
                if (r4 != r6) goto L55
                java.lang.String r1 = r8.O0000OOo()
                int r1 = java.lang.Integer.parseInt(r1)     // Catch: java.lang.NumberFormatException -> L3d
                if (r1 == 0) goto L3b
                goto L77
            L3b:
                r5 = r2
                goto L77
            L3d:
                r8 = move-exception
                com.O000000o.O000000o.O000OOo r8 = new com.O000000o.O000000o.O000OOo
                java.lang.StringBuilder r0 = new java.lang.StringBuilder
                r0.<init>()
                java.lang.String r2 = "Error: Expecting: bitset number value (1, 0), Found: "
                r0.append(r2)
                r0.append(r1)
                java.lang.String r0 = r0.toString()
                r8.<init>(r0)
                throw r8
            L55:
                com.O000000o.O000000o.O000OOo r8 = new com.O000000o.O000000o.O000OOo
                java.lang.StringBuilder r0 = new java.lang.StringBuilder
                r0.<init>()
                java.lang.String r2 = "Invalid bitset value type: "
                r0.append(r2)
                r0.append(r1)
                java.lang.String r0 = r0.toString()
                r8.<init>(r0)
                throw r8
            L6c:
                boolean r5 = r8.O0000Oo0()
                goto L77
            L71:
                int r1 = r8.O0000o00()
                if (r1 == 0) goto L3b
            L77:
                if (r5 == 0) goto L7c
                r0.set(r3)
            L7c:
                int r3 = r3 + 1
                com.O000000o.O000000o.O00000o.O00000o0 r1 = r8.O00000oo()
                goto L1b
            L83:
                r8.O00000Oo()
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.O000000o.O000000o.O00000Oo.O000000o.O00oOooO.AnonymousClass12.O00000Oo(com.O000000o.O000000o.O00000o.O000000o):java.util.BitSet");
        }

        @Override // com.O000000o.O000000o.O000o000
        public void O000000o(com.O000000o.O000000o.O00000o.O00000o o00000o, BitSet bitSet) throws IOException {
            if (bitSet == null) {
                o00000o.O00000oo();
                return;
            }
            o00000o.O00000Oo();
            for (int i = 0; i < bitSet.length(); i++) {
                o00000o.O000000o(bitSet.get(i) ? 1L : 0L);
            }
            o00000o.O00000o0();
        }
    };
    public static final O000o00 O00000o = O000000o(BitSet.class, O00000o0);
    public static final O000o000<Boolean> O00000oO = new O000o000<Boolean>() { // from class: com.O000000o.O000000o.O00000Oo.O000000o.O00oOooO.23
        @Override // com.O000000o.O000000o.O000o000
        /* renamed from: O000000o */
        public Boolean O00000Oo(com.O000000o.O000000o.O00000o.O000000o o000000o) throws IOException {
            if (o000000o.O00000oo() != com.O000000o.O000000o.O00000o.O00000o0.NULL) {
                return o000000o.O00000oo() == com.O000000o.O000000o.O00000o.O00000o0.STRING ? Boolean.valueOf(Boolean.parseBoolean(o000000o.O0000OOo())) : Boolean.valueOf(o000000o.O0000Oo0());
            }
            o000000o.O0000Oo();
            return null;
        }

        @Override // com.O000000o.O000000o.O000o000
        public void O000000o(com.O000000o.O000000o.O00000o.O00000o o00000o, Boolean bool) throws IOException {
            if (bool == null) {
                o00000o.O00000oo();
            } else {
                o00000o.O000000o(bool.booleanValue());
            }
        }
    };
    public static final O000o000<Boolean> O00000oo = new O000o000<Boolean>() { // from class: com.O000000o.O000000o.O00000Oo.O000000o.O00oOooO.27
        @Override // com.O000000o.O000000o.O000o000
        /* renamed from: O000000o */
        public Boolean O00000Oo(com.O000000o.O000000o.O00000o.O000000o o000000o) throws IOException {
            if (o000000o.O00000oo() == com.O000000o.O000000o.O00000o.O00000o0.NULL) {
                o000000o.O0000Oo();
                return null;
            }
            return Boolean.valueOf(o000000o.O0000OOo());
        }

        @Override // com.O000000o.O000000o.O000o000
        public void O000000o(com.O000000o.O000000o.O00000o.O00000o o00000o, Boolean bool) throws IOException {
            o00000o.O00000Oo(bool == null ? CharSequenceUtil.NULL : bool.toString());
        }
    };
    public static final O000o00 O0000O0o = O000000o(Boolean.TYPE, Boolean.class, O00000oO);
    public static final O000o000<Number> O0000OOo = new O000o000<Number>() { // from class: com.O000000o.O000000o.O00000Oo.O000000o.O00oOooO.28
        @Override // com.O000000o.O000000o.O000o000
        /* renamed from: O000000o */
        public Number O00000Oo(com.O000000o.O000000o.O00000o.O000000o o000000o) throws IOException {
            if (o000000o.O00000oo() == com.O000000o.O000000o.O00000o.O00000o0.NULL) {
                o000000o.O0000Oo();
                return null;
            }
            try {
                return Byte.valueOf((byte) o000000o.O0000o00());
            } catch (NumberFormatException e) {
                throw new O000OOo(e);
            }
        }

        @Override // com.O000000o.O000000o.O000o000
        public void O000000o(com.O000000o.O000000o.O00000o.O00000o o00000o, Number number) throws IOException {
            o00000o.O000000o(number);
        }
    };
    public static final O000o00 O0000Oo0 = O000000o(Byte.TYPE, Byte.class, O0000OOo);
    public static final O000o000<Number> O0000Oo = new O000o000<Number>() { // from class: com.O000000o.O000000o.O00000Oo.O000000o.O00oOooO.29
        @Override // com.O000000o.O000000o.O000o000
        /* renamed from: O000000o */
        public Number O00000Oo(com.O000000o.O000000o.O00000o.O000000o o000000o) throws IOException {
            if (o000000o.O00000oo() == com.O000000o.O000000o.O00000o.O00000o0.NULL) {
                o000000o.O0000Oo();
                return null;
            }
            try {
                return Short.valueOf((short) o000000o.O0000o00());
            } catch (NumberFormatException e) {
                throw new O000OOo(e);
            }
        }

        @Override // com.O000000o.O000000o.O000o000
        public void O000000o(com.O000000o.O000000o.O00000o.O00000o o00000o, Number number) throws IOException {
            o00000o.O000000o(number);
        }
    };
    public static final O000o00 O0000OoO = O000000o(Short.TYPE, Short.class, O0000Oo);
    public static final O000o000<Number> O0000Ooo = new O000o000<Number>() { // from class: com.O000000o.O000000o.O00000Oo.O000000o.O00oOooO.30
        @Override // com.O000000o.O000000o.O000o000
        /* renamed from: O000000o */
        public Number O00000Oo(com.O000000o.O000000o.O00000o.O000000o o000000o) throws IOException {
            if (o000000o.O00000oo() == com.O000000o.O000000o.O00000o.O00000o0.NULL) {
                o000000o.O0000Oo();
                return null;
            }
            try {
                return Integer.valueOf(o000000o.O0000o00());
            } catch (NumberFormatException e) {
                throw new O000OOo(e);
            }
        }

        @Override // com.O000000o.O000000o.O000o000
        public void O000000o(com.O000000o.O000000o.O00000o.O00000o o00000o, Number number) throws IOException {
            o00000o.O000000o(number);
        }
    };
    public static final O000o00 O0000o00 = O000000o(Integer.TYPE, Integer.class, O0000Ooo);
    public static final O000o000<Number> O0000o0 = new O000o000<Number>() { // from class: com.O000000o.O000000o.O00000Oo.O000000o.O00oOooO.31
        @Override // com.O000000o.O000000o.O000o000
        /* renamed from: O000000o */
        public Number O00000Oo(com.O000000o.O000000o.O00000o.O000000o o000000o) throws IOException {
            if (o000000o.O00000oo() == com.O000000o.O000000o.O00000o.O00000o0.NULL) {
                o000000o.O0000Oo();
                return null;
            }
            try {
                return Long.valueOf(o000000o.O0000Ooo());
            } catch (NumberFormatException e) {
                throw new O000OOo(e);
            }
        }

        @Override // com.O000000o.O000000o.O000o000
        public void O000000o(com.O000000o.O000000o.O00000o.O00000o o00000o, Number number) throws IOException {
            o00000o.O000000o(number);
        }
    };
    public static final O000o000<Number> O0000o0O = new O000o000<Number>() { // from class: com.O000000o.O000000o.O00000Oo.O000000o.O00oOooO.32
        @Override // com.O000000o.O000000o.O000o000
        /* renamed from: O000000o */
        public Number O00000Oo(com.O000000o.O000000o.O00000o.O000000o o000000o) throws IOException {
            if (o000000o.O00000oo() == com.O000000o.O000000o.O00000o.O00000o0.NULL) {
                o000000o.O0000Oo();
                return null;
            }
            return Float.valueOf((float) o000000o.O0000OoO());
        }

        @Override // com.O000000o.O000000o.O000o000
        public void O000000o(com.O000000o.O000000o.O00000o.O00000o o00000o, Number number) throws IOException {
            o00000o.O000000o(number);
        }
    };
    public static final O000o000<Number> O0000o0o = new O000o000<Number>() { // from class: com.O000000o.O000000o.O00000Oo.O000000o.O00oOooO.2
        @Override // com.O000000o.O000000o.O000o000
        /* renamed from: O000000o */
        public Number O00000Oo(com.O000000o.O000000o.O00000o.O000000o o000000o) throws IOException {
            if (o000000o.O00000oo() == com.O000000o.O000000o.O00000o.O00000o0.NULL) {
                o000000o.O0000Oo();
                return null;
            }
            return Double.valueOf(o000000o.O0000OoO());
        }

        @Override // com.O000000o.O000000o.O000o000
        public void O000000o(com.O000000o.O000000o.O00000o.O00000o o00000o, Number number) throws IOException {
            o00000o.O000000o(number);
        }
    };
    public static final O000o000<Number> O0000o = new O000o000<Number>() { // from class: com.O000000o.O000000o.O00000Oo.O000000o.O00oOooO.3
        @Override // com.O000000o.O000000o.O000o000
        /* renamed from: O000000o */
        public Number O00000Oo(com.O000000o.O000000o.O00000o.O000000o o000000o) throws IOException {
            com.O000000o.O000000o.O00000o.O00000o0 O00000oo2 = o000000o.O00000oo();
            int i = AnonymousClass26.O000000o[O00000oo2.ordinal()];
            if (i != 1) {
                if (i == 4) {
                    o000000o.O0000Oo();
                    return null;
                }
                throw new O000OOo("Expecting number, got: " + O00000oo2);
            }
            return new com.O000000o.O000000o.O00000Oo.O0000OOo(o000000o.O0000OOo());
        }

        @Override // com.O000000o.O000000o.O000o000
        public void O000000o(com.O000000o.O000000o.O00000o.O00000o o00000o, Number number) throws IOException {
            o00000o.O000000o(number);
        }
    };
    public static final O000o00 O0000oO0 = O000000o(Number.class, O0000o);
    public static final O000o000<Character> O0000oO = new O000o000<Character>() { // from class: com.O000000o.O000000o.O00000Oo.O000000o.O00oOooO.4
        @Override // com.O000000o.O000000o.O000o000
        /* renamed from: O000000o */
        public Character O00000Oo(com.O000000o.O000000o.O00000o.O000000o o000000o) throws IOException {
            if (o000000o.O00000oo() == com.O000000o.O000000o.O00000o.O00000o0.NULL) {
                o000000o.O0000Oo();
                return null;
            }
            String O0000OOo2 = o000000o.O0000OOo();
            if (O0000OOo2.length() == 1) {
                return Character.valueOf(O0000OOo2.charAt(0));
            }
            throw new O000OOo("Expecting character, got: " + O0000OOo2);
        }

        @Override // com.O000000o.O000000o.O000o000
        public void O000000o(com.O000000o.O000000o.O00000o.O00000o o00000o, Character ch2) throws IOException {
            o00000o.O00000Oo(ch2 == null ? null : String.valueOf(ch2));
        }
    };
    public static final O000o00 O0000oOO = O000000o(Character.TYPE, Character.class, O0000oO);
    public static final O000o000<String> O0000oOo = new O000o000<String>() { // from class: com.O000000o.O000000o.O00000Oo.O000000o.O00oOooO.5
        @Override // com.O000000o.O000000o.O000o000
        /* renamed from: O000000o */
        public String O00000Oo(com.O000000o.O000000o.O00000o.O000000o o000000o) throws IOException {
            com.O000000o.O000000o.O00000o.O00000o0 O00000oo2 = o000000o.O00000oo();
            if (O00000oo2 != com.O000000o.O000000o.O00000o.O00000o0.NULL) {
                return O00000oo2 == com.O000000o.O000000o.O00000o.O00000o0.BOOLEAN ? Boolean.toString(o000000o.O0000Oo0()) : o000000o.O0000OOo();
            }
            o000000o.O0000Oo();
            return null;
        }

        @Override // com.O000000o.O000000o.O000o000
        public void O000000o(com.O000000o.O000000o.O00000o.O00000o o00000o, String str) throws IOException {
            o00000o.O00000Oo(str);
        }
    };
    public static final O000o000<BigDecimal> O0000oo0 = new O000o000<BigDecimal>() { // from class: com.O000000o.O000000o.O00000Oo.O000000o.O00oOooO.6
        @Override // com.O000000o.O000000o.O000o000
        /* renamed from: O000000o */
        public BigDecimal O00000Oo(com.O000000o.O000000o.O00000o.O000000o o000000o) throws IOException {
            if (o000000o.O00000oo() == com.O000000o.O000000o.O00000o.O00000o0.NULL) {
                o000000o.O0000Oo();
                return null;
            }
            try {
                return new BigDecimal(o000000o.O0000OOo());
            } catch (NumberFormatException e) {
                throw new O000OOo(e);
            }
        }

        @Override // com.O000000o.O000000o.O000o000
        public void O000000o(com.O000000o.O000000o.O00000o.O00000o o00000o, BigDecimal bigDecimal) throws IOException {
            o00000o.O000000o(bigDecimal);
        }
    };
    public static final O000o000<BigInteger> O0000oo = new O000o000<BigInteger>() { // from class: com.O000000o.O000000o.O00000Oo.O000000o.O00oOooO.7
        @Override // com.O000000o.O000000o.O000o000
        /* renamed from: O000000o */
        public BigInteger O00000Oo(com.O000000o.O000000o.O00000o.O000000o o000000o) throws IOException {
            if (o000000o.O00000oo() == com.O000000o.O000000o.O00000o.O00000o0.NULL) {
                o000000o.O0000Oo();
                return null;
            }
            try {
                return new BigInteger(o000000o.O0000OOo());
            } catch (NumberFormatException e) {
                throw new O000OOo(e);
            }
        }

        @Override // com.O000000o.O000000o.O000o000
        public void O000000o(com.O000000o.O000000o.O00000o.O00000o o00000o, BigInteger bigInteger) throws IOException {
            o00000o.O000000o(bigInteger);
        }
    };
    public static final O000o00 O0000ooO = O000000o(String.class, O0000oOo);
    public static final O000o000<StringBuilder> O0000ooo = new O000o000<StringBuilder>() { // from class: com.O000000o.O000000o.O00000Oo.O000000o.O00oOooO.8
        @Override // com.O000000o.O000000o.O000o000
        /* renamed from: O000000o */
        public StringBuilder O00000Oo(com.O000000o.O000000o.O00000o.O000000o o000000o) throws IOException {
            if (o000000o.O00000oo() == com.O000000o.O000000o.O00000o.O00000o0.NULL) {
                o000000o.O0000Oo();
                return null;
            }
            return new StringBuilder(o000000o.O0000OOo());
        }

        @Override // com.O000000o.O000000o.O000o000
        public void O000000o(com.O000000o.O000000o.O00000o.O00000o o00000o, StringBuilder sb) throws IOException {
            o00000o.O00000Oo(sb == null ? null : sb.toString());
        }
    };
    public static final O000o00 O00oOooO = O000000o(StringBuilder.class, O0000ooo);
    public static final O000o000<StringBuffer> O00oOooo = new O000o000<StringBuffer>() { // from class: com.O000000o.O000000o.O00000Oo.O000000o.O00oOooO.9
        @Override // com.O000000o.O000000o.O000o000
        /* renamed from: O000000o */
        public StringBuffer O00000Oo(com.O000000o.O000000o.O00000o.O000000o o000000o) throws IOException {
            if (o000000o.O00000oo() == com.O000000o.O000000o.O00000o.O00000o0.NULL) {
                o000000o.O0000Oo();
                return null;
            }
            return new StringBuffer(o000000o.O0000OOo());
        }

        @Override // com.O000000o.O000000o.O000o000
        public void O000000o(com.O000000o.O000000o.O00000o.O00000o o00000o, StringBuffer stringBuffer) throws IOException {
            o00000o.O00000Oo(stringBuffer == null ? null : stringBuffer.toString());
        }
    };
    public static final O000o00 O000O00o = O000000o(StringBuffer.class, O00oOooo);
    public static final O000o000<URL> O000O0OO = new O000o000<URL>() { // from class: com.O000000o.O000000o.O00000Oo.O000000o.O00oOooO.10
        @Override // com.O000000o.O000000o.O000o000
        /* renamed from: O000000o */
        public URL O00000Oo(com.O000000o.O000000o.O00000o.O000000o o000000o) throws IOException {
            if (o000000o.O00000oo() == com.O000000o.O000000o.O00000o.O00000o0.NULL) {
                o000000o.O0000Oo();
                return null;
            }
            String O0000OOo2 = o000000o.O0000OOo();
            if (CharSequenceUtil.NULL.equals(O0000OOo2)) {
                return null;
            }
            return new URL(O0000OOo2);
        }

        @Override // com.O000000o.O000000o.O000o000
        public void O000000o(com.O000000o.O000000o.O00000o.O00000o o00000o, URL url) throws IOException {
            o00000o.O00000Oo(url == null ? null : url.toExternalForm());
        }
    };
    public static final O000o00 O000O0Oo = O000000o(URL.class, O000O0OO);
    public static final O000o000<URI> O00oOoOo = new O000o000<URI>() { // from class: com.O000000o.O000000o.O00000Oo.O000000o.O00oOooO.11
        @Override // com.O000000o.O000000o.O000o000
        /* renamed from: O000000o */
        public URI O00000Oo(com.O000000o.O000000o.O00000o.O000000o o000000o) throws IOException {
            if (o000000o.O00000oo() == com.O000000o.O000000o.O00000o.O00000o0.NULL) {
                o000000o.O0000Oo();
                return null;
            }
            try {
                String O0000OOo2 = o000000o.O0000OOo();
                if (CharSequenceUtil.NULL.equals(O0000OOo2)) {
                    return null;
                }
                return new URI(O0000OOo2);
            } catch (URISyntaxException e) {
                throw new O000O00o(e);
            }
        }

        @Override // com.O000000o.O000000o.O000o000
        public void O000000o(com.O000000o.O000000o.O00000o.O00000o o00000o, URI uri) throws IOException {
            o00000o.O00000Oo(uri == null ? null : uri.toASCIIString());
        }
    };
    public static final O000o00 O000O0o0 = O000000o(URI.class, O00oOoOo);
    public static final O000o000<InetAddress> O000O0o = new O000o000<InetAddress>() { // from class: com.O000000o.O000000o.O00000Oo.O000000o.O00oOooO.13
        @Override // com.O000000o.O000000o.O000o000
        /* renamed from: O000000o */
        public InetAddress O00000Oo(com.O000000o.O000000o.O00000o.O000000o o000000o) throws IOException {
            if (o000000o.O00000oo() == com.O000000o.O000000o.O00000o.O00000o0.NULL) {
                o000000o.O0000Oo();
                return null;
            }
            return InetAddress.getByName(o000000o.O0000OOo());
        }

        @Override // com.O000000o.O000000o.O000o000
        public void O000000o(com.O000000o.O000000o.O00000o.O00000o o00000o, InetAddress inetAddress) throws IOException {
            o00000o.O00000Oo(inetAddress == null ? null : inetAddress.getHostAddress());
        }
    };
    public static final O000o00 O000O0oO = O00000Oo(InetAddress.class, O000O0o);
    public static final O000o000<UUID> O000O0oo = new O000o000<UUID>() { // from class: com.O000000o.O000000o.O00000Oo.O000000o.O00oOooO.14
        @Override // com.O000000o.O000000o.O000o000
        /* renamed from: O000000o */
        public UUID O00000Oo(com.O000000o.O000000o.O00000o.O000000o o000000o) throws IOException {
            if (o000000o.O00000oo() == com.O000000o.O000000o.O00000o.O00000o0.NULL) {
                o000000o.O0000Oo();
                return null;
            }
            return UUID.fromString(o000000o.O0000OOo());
        }

        @Override // com.O000000o.O000000o.O000o000
        public void O000000o(com.O000000o.O000000o.O00000o.O00000o o00000o, UUID uuid) throws IOException {
            o00000o.O00000Oo(uuid == null ? null : uuid.toString());
        }
    };
    public static final O000o00 O000OO00 = O000000o(UUID.class, O000O0oo);
    public static final O000o00 O000OO0o = new O000o00() { // from class: com.O000000o.O000000o.O00000Oo.O000000o.O00oOooO.15
        @Override // com.O000000o.O000000o.O000o00
        public <T> O000o000<T> O000000o(com.O000000o.O000000o.O0000OOo o0000OOo, com.O000000o.O000000o.O00000o0.O000000o<T> o000000o) {
            if (o000000o.O000000o() != Timestamp.class) {
                return null;
            }
            final O000o000<T> O000000o2 = o0000OOo.O000000o((Class) Date.class);
            return (O000o000<T>) new O000o000<Timestamp>() { // from class: com.O000000o.O000000o.O00000Oo.O000000o.O00oOooO.15.1
                @Override // com.O000000o.O000000o.O000o000
                /* renamed from: O000000o */
                public Timestamp O00000Oo(com.O000000o.O000000o.O00000o.O000000o o000000o2) throws IOException {
                    Date date = (Date) O000000o2.O00000Oo(o000000o2);
                    if (date != null) {
                        return new Timestamp(date.getTime());
                    }
                    return null;
                }

                @Override // com.O000000o.O000000o.O000o000
                public void O000000o(com.O000000o.O000000o.O00000o.O00000o o00000o, Timestamp timestamp) throws IOException {
                    O000000o2.O000000o(o00000o, (com.O000000o.O000000o.O00000o.O00000o) timestamp);
                }
            };
        }
    };
    public static final O000o000<Calendar> O000OO = new O000o000<Calendar>() { // from class: com.O000000o.O000000o.O00000Oo.O000000o.O00oOooO.16
        private static final String O000000o = "year";
        private static final String O00000Oo = "month";
        private static final String O00000o = "hourOfDay";
        private static final String O00000o0 = "dayOfMonth";
        private static final String O00000oO = "minute";
        private static final String O00000oo = "second";

        @Override // com.O000000o.O000000o.O000o000
        /* renamed from: O000000o */
        public Calendar O00000Oo(com.O000000o.O000000o.O00000o.O000000o o000000o) throws IOException {
            if (o000000o.O00000oo() == com.O000000o.O000000o.O00000o.O00000o0.NULL) {
                o000000o.O0000Oo();
                return null;
            }
            o000000o.O00000o0();
            int i = 0;
            int i2 = 0;
            int i3 = 0;
            int i4 = 0;
            int i5 = 0;
            int i6 = 0;
            while (o000000o.O00000oo() != com.O000000o.O000000o.O00000o.O00000o0.END_OBJECT) {
                String O0000O0o2 = o000000o.O0000O0o();
                int O0000o002 = o000000o.O0000o00();
                if (O000000o.equals(O0000O0o2)) {
                    i = O0000o002;
                } else if (O00000Oo.equals(O0000O0o2)) {
                    i2 = O0000o002;
                } else if (O00000o0.equals(O0000O0o2)) {
                    i3 = O0000o002;
                } else if (O00000o.equals(O0000O0o2)) {
                    i4 = O0000o002;
                } else if (O00000oO.equals(O0000O0o2)) {
                    i5 = O0000o002;
                } else if (O00000oo.equals(O0000O0o2)) {
                    i6 = O0000o002;
                }
            }
            o000000o.O00000o();
            return new GregorianCalendar(i, i2, i3, i4, i5, i6);
        }

        @Override // com.O000000o.O000000o.O000o000
        public void O000000o(com.O000000o.O000000o.O00000o.O00000o o00000o, Calendar calendar) throws IOException {
            if (calendar == null) {
                o00000o.O00000oo();
                return;
            }
            o00000o.O00000o();
            o00000o.O000000o(O000000o);
            o00000o.O000000o(calendar.get(1));
            o00000o.O000000o(O00000Oo);
            o00000o.O000000o(calendar.get(2));
            o00000o.O000000o(O00000o0);
            o00000o.O000000o(calendar.get(5));
            o00000o.O000000o(O00000o);
            o00000o.O000000o(calendar.get(11));
            o00000o.O000000o(O00000oO);
            o00000o.O000000o(calendar.get(12));
            o00000o.O000000o(O00000oo);
            o00000o.O000000o(calendar.get(13));
            o00000o.O00000oO();
        }
    };
    public static final O000o00 O000OOOo = O00000Oo(Calendar.class, GregorianCalendar.class, O000OO);
    public static final O000o000<Locale> O000OOo0 = new O000o000<Locale>() { // from class: com.O000000o.O000000o.O00000Oo.O000000o.O00oOooO.17
        @Override // com.O000000o.O000000o.O000o000
        /* renamed from: O000000o */
        public Locale O00000Oo(com.O000000o.O000000o.O00000o.O000000o o000000o) throws IOException {
            if (o000000o.O00000oo() == com.O000000o.O000000o.O00000o.O00000o0.NULL) {
                o000000o.O0000Oo();
                return null;
            }
            StringTokenizer stringTokenizer = new StringTokenizer(o000000o.O0000OOo(), "_");
            String nextToken = stringTokenizer.hasMoreElements() ? stringTokenizer.nextToken() : null;
            String nextToken2 = stringTokenizer.hasMoreElements() ? stringTokenizer.nextToken() : null;
            String nextToken3 = stringTokenizer.hasMoreElements() ? stringTokenizer.nextToken() : null;
            return (nextToken2 == null && nextToken3 == null) ? new Locale(nextToken) : nextToken3 == null ? new Locale(nextToken, nextToken2) : new Locale(nextToken, nextToken2, nextToken3);
        }

        @Override // com.O000000o.O000000o.O000o000
        public void O000000o(com.O000000o.O000000o.O00000o.O00000o o00000o, Locale locale) throws IOException {
            o00000o.O00000Oo(locale == null ? null : locale.toString());
        }
    };
    public static final O000o00 O000OOo = O000000o(Locale.class, O000OOo0);
    public static final O000o000<com.O000000o.O000000o.O00oOooO> O000OOoO = new O000o000<com.O000000o.O000000o.O00oOooO>() { // from class: com.O000000o.O000000o.O00000Oo.O000000o.O00oOooO.18
        @Override // com.O000000o.O000000o.O000o000
        /* renamed from: O000000o */
        public com.O000000o.O000000o.O00oOooO O00000Oo(com.O000000o.O000000o.O00000o.O000000o o000000o) throws IOException {
            switch (AnonymousClass26.O000000o[o000000o.O00000oo().ordinal()]) {
                case 1:
                    return new O000OO0o((Number) new com.O000000o.O000000o.O00000Oo.O0000OOo(o000000o.O0000OOo()));
                case 2:
                    return new O000OO0o(Boolean.valueOf(o000000o.O0000Oo0()));
                case 3:
                    return new O000OO0o(o000000o.O0000OOo());
                case 4:
                    o000000o.O0000Oo();
                    return O000O0OO.O000000o;
                case 5:
                    com.O000000o.O000000o.O0000o00 o0000o00 = new com.O000000o.O000000o.O0000o00();
                    o000000o.O000000o();
                    while (o000000o.O00000oO()) {
                        o0000o00.O000000o(O00000Oo(o000000o));
                    }
                    o000000o.O00000Oo();
                    return o0000o00;
                case 6:
                    O000O0o0 o000O0o0 = new O000O0o0();
                    o000000o.O00000o0();
                    while (o000000o.O00000oO()) {
                        o000O0o0.O000000o(o000000o.O0000O0o(), O00000Oo(o000000o));
                    }
                    o000000o.O00000o();
                    return o000O0o0;
                default:
                    throw new IllegalArgumentException();
            }
        }

        @Override // com.O000000o.O000000o.O000o000
        public void O000000o(com.O000000o.O000000o.O00000o.O00000o o00000o, com.O000000o.O000000o.O00oOooO o00oOooO) throws IOException {
            if (o00oOooO == null || o00oOooO.O0000oO()) {
                o00000o.O00000oo();
            } else if (o00oOooO.O0000oO0()) {
                O000OO0o O0000oo02 = o00oOooO.O0000oo0();
                if (O0000oo02.O0000ooo()) {
                    o00000o.O000000o(O0000oo02.O00000o0());
                } else if (O0000oo02.O00000Oo()) {
                    o00000o.O000000o(O0000oo02.O0000o0());
                } else {
                    o00000o.O00000Oo(O0000oo02.O00000o());
                }
            } else if (o00oOooO.O0000o0o()) {
                o00000o.O00000Oo();
                Iterator<com.O000000o.O000000o.O00oOooO> it = o00oOooO.O0000oOo().iterator();
                while (it.hasNext()) {
                    O000000o(o00000o, it.next());
                }
                o00000o.O00000o0();
            } else if (!o00oOooO.O0000o()) {
                throw new IllegalArgumentException("Couldn't write " + o00oOooO.getClass());
            } else {
                o00000o.O00000o();
                for (Map.Entry<String, com.O000000o.O000000o.O00oOooO> entry : o00oOooO.O0000oOO().O00000Oo()) {
                    o00000o.O000000o(entry.getKey());
                    O000000o(o00000o, entry.getValue());
                }
                o00000o.O00000oO();
            }
        }
    };
    public static final O000o00 O000OOoo = O00000Oo(com.O000000o.O000000o.O00oOooO.class, O000OOoO);
    public static final O000o00 O000Oo00 = O000000o();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* compiled from: TypeAdapters.java */
    /* renamed from: com.O000000o.O000000o.O00000Oo.O000000o.O00oOooO$26  reason: invalid class name */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass26 {
        static final /* synthetic */ int[] O000000o = new int[com.O000000o.O000000o.O00000o.O00000o0.values().length];

        static {
            try {
                O000000o[com.O000000o.O000000o.O00000o.O00000o0.NUMBER.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                O000000o[com.O000000o.O000000o.O00000o.O00000o0.BOOLEAN.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                O000000o[com.O000000o.O000000o.O00000o.O00000o0.STRING.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                O000000o[com.O000000o.O000000o.O00000o.O00000o0.NULL.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                O000000o[com.O000000o.O000000o.O00000o.O00000o0.BEGIN_ARRAY.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                O000000o[com.O000000o.O000000o.O00000o.O00000o0.BEGIN_OBJECT.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                O000000o[com.O000000o.O000000o.O00000o.O00000o0.END_DOCUMENT.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
            try {
                O000000o[com.O000000o.O000000o.O00000o.O00000o0.NAME.ordinal()] = 8;
            } catch (NoSuchFieldError e8) {
            }
            try {
                O000000o[com.O000000o.O000000o.O00000o.O00000o0.END_OBJECT.ordinal()] = 9;
            } catch (NoSuchFieldError e9) {
            }
            try {
                O000000o[com.O000000o.O000000o.O00000o.O00000o0.END_ARRAY.ordinal()] = 10;
            } catch (NoSuchFieldError e10) {
            }
        }
    }

    /* compiled from: TypeAdapters.java */
    /* loaded from: classes.dex */
    private static final class O000000o<T extends Enum<T>> extends O000o000<T> {
        private final Map<String, T> O000000o = new HashMap();
        private final Map<T, String> O00000Oo = new HashMap();

        public O000000o(Class<T> cls) {
            T[] enumConstants;
            try {
                for (T t : cls.getEnumConstants()) {
                    String name = t.name();
                    com.O000000o.O000000o.O000000o.O00000Oo o00000Oo = (com.O000000o.O000000o.O000000o.O00000Oo) cls.getField(name).getAnnotation(com.O000000o.O000000o.O000000o.O00000Oo.class);
                    name = o00000Oo != null ? o00000Oo.O000000o() : name;
                    this.O000000o.put(name, t);
                    this.O00000Oo.put(t, name);
                }
            } catch (NoSuchFieldException e) {
                throw new AssertionError();
            }
        }

        @Override // com.O000000o.O000000o.O000o000
        /* renamed from: O000000o */
        public T O00000Oo(com.O000000o.O000000o.O00000o.O000000o o000000o) throws IOException {
            if (o000000o.O00000oo() == com.O000000o.O000000o.O00000o.O00000o0.NULL) {
                o000000o.O0000Oo();
                return null;
            }
            return this.O000000o.get(o000000o.O0000OOo());
        }

        public void O000000o(com.O000000o.O000000o.O00000o.O00000o o00000o, T t) throws IOException {
            o00000o.O00000Oo(t == null ? null : this.O00000Oo.get(t));
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.O000000o.O000000o.O000o000
        public /* bridge */ /* synthetic */ void O000000o(com.O000000o.O000000o.O00000o.O00000o o00000o, Object obj) throws IOException {
            O000000o(o00000o, (com.O000000o.O000000o.O00000o.O00000o) ((Enum) obj));
        }
    }

    private O00oOooO() {
    }

    public static O000o00 O000000o() {
        return new O000o00() { // from class: com.O000000o.O000000o.O00000Oo.O000000o.O00oOooO.19
            @Override // com.O000000o.O000000o.O000o00
            public <T> O000o000<T> O000000o(com.O000000o.O000000o.O0000OOo o0000OOo, com.O000000o.O000000o.O00000o0.O000000o<T> o000000o) {
                Class O000000o2 = o000000o.O000000o();
                if (!Enum.class.isAssignableFrom(O000000o2) || O000000o2 == Enum.class) {
                    return null;
                }
                if (!O000000o2.isEnum()) {
                    O000000o2 = (Class<? super Object>) O000000o2.getSuperclass();
                }
                return new O000000o(O000000o2);
            }
        };
    }

    public static <TT> O000o00 O000000o(final com.O000000o.O000000o.O00000o0.O000000o<TT> o000000o, final O000o000<TT> o000o000) {
        return new O000o00() { // from class: com.O000000o.O000000o.O00000Oo.O000000o.O00oOooO.20
            @Override // com.O000000o.O000000o.O000o00
            public <T> O000o000<T> O000000o(com.O000000o.O000000o.O0000OOo o0000OOo, com.O000000o.O000000o.O00000o0.O000000o<T> o000000o2) {
                if (o000000o2.equals(com.O000000o.O000000o.O00000o0.O000000o.this)) {
                    return o000o000;
                }
                return null;
            }
        };
    }

    public static <TT> O000o00 O000000o(final Class<TT> cls, final O000o000<TT> o000o000) {
        return new O000o00() { // from class: com.O000000o.O000000o.O00000Oo.O000000o.O00oOooO.21
            @Override // com.O000000o.O000000o.O000o00
            public <T> O000o000<T> O000000o(com.O000000o.O000000o.O0000OOo o0000OOo, com.O000000o.O000000o.O00000o0.O000000o<T> o000000o) {
                if (o000000o.O000000o() == cls) {
                    return o000o000;
                }
                return null;
            }

            public String toString() {
                return "Factory[type=" + cls.getName() + ",adapter=" + o000o000 + "]";
            }
        };
    }

    public static <TT> O000o00 O000000o(final Class<TT> cls, final Class<TT> cls2, final O000o000<? super TT> o000o000) {
        return new O000o00() { // from class: com.O000000o.O000000o.O00000Oo.O000000o.O00oOooO.22
            @Override // com.O000000o.O000000o.O000o00
            public <T> O000o000<T> O000000o(com.O000000o.O000000o.O0000OOo o0000OOo, com.O000000o.O000000o.O00000o0.O000000o<T> o000000o) {
                Class<? super T> O000000o2 = o000000o.O000000o();
                if (O000000o2 == cls || O000000o2 == cls2) {
                    return o000o000;
                }
                return null;
            }

            public String toString() {
                return "Factory[type=" + cls2.getName() + "+" + cls.getName() + ",adapter=" + o000o000 + "]";
            }
        };
    }

    public static <TT> O000o00 O00000Oo(final Class<TT> cls, final O000o000<TT> o000o000) {
        return new O000o00() { // from class: com.O000000o.O000000o.O00000Oo.O000000o.O00oOooO.25
            @Override // com.O000000o.O000000o.O000o00
            public <T> O000o000<T> O000000o(com.O000000o.O000000o.O0000OOo o0000OOo, com.O000000o.O000000o.O00000o0.O000000o<T> o000000o) {
                if (cls.isAssignableFrom(o000000o.O000000o())) {
                    return o000o000;
                }
                return null;
            }

            public String toString() {
                return "Factory[typeHierarchy=" + cls.getName() + ",adapter=" + o000o000 + "]";
            }
        };
    }

    public static <TT> O000o00 O00000Oo(final Class<TT> cls, final Class<? extends TT> cls2, final O000o000<? super TT> o000o000) {
        return new O000o00() { // from class: com.O000000o.O000000o.O00000Oo.O000000o.O00oOooO.24
            @Override // com.O000000o.O000000o.O000o00
            public <T> O000o000<T> O000000o(com.O000000o.O000000o.O0000OOo o0000OOo, com.O000000o.O000000o.O00000o0.O000000o<T> o000000o) {
                Class<? super T> O000000o2 = o000000o.O000000o();
                if (O000000o2 == cls || O000000o2 == cls2) {
                    return o000o000;
                }
                return null;
            }

            public String toString() {
                return "Factory[type=" + cls.getName() + "+" + cls2.getName() + ",adapter=" + o000o000 + "]";
            }
        };
    }
}
