package cn.hutool.core.text;
/* loaded from: classes.dex */
public class ASCIIStrCache {
    private static final int ASCII_LENGTH = 128;
    private static final String[] CACHE = new String[128];

    static {
        for (char c = 0; c < 128; c = (char) (c + 1)) {
            CACHE[c] = String.valueOf(c);
        }
    }

    public static String toString(char c) {
        return c < 128 ? CACHE[c] : String.valueOf(c);
    }
}
