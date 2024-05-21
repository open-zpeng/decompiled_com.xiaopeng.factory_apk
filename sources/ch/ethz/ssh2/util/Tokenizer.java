package ch.ethz.ssh2.util;
/* loaded from: classes.dex */
public class Tokenizer {
    public static String[] parseTokens(String source, char delimiter) {
        int numtoken = 1;
        for (int i = 0; i < source.length(); i++) {
            if (source.charAt(i) == delimiter) {
                numtoken++;
            }
        }
        String[] list = new String[numtoken];
        int nextfield = 0;
        for (int i2 = 0; i2 < numtoken; i2++) {
            if (nextfield >= source.length()) {
                list[i2] = "";
            } else {
                int idx = source.indexOf(delimiter, nextfield);
                if (idx == -1) {
                    idx = source.length();
                }
                list[i2] = source.substring(nextfield, idx);
                nextfield = idx + 1;
            }
        }
        return list;
    }
}
