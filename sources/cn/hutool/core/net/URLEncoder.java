package cn.hutool.core.net;

import cn.hutool.core.text.CharPool;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.StrUtil;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.BitSet;
/* loaded from: classes.dex */
public class URLEncoder implements Serializable {
    private static final long serialVersionUID = 1;
    private boolean encodeSpaceAsPlus;
    private final BitSet safeCharacters;
    public static final URLEncoder DEFAULT = createDefault();
    public static final URLEncoder PATH_SEGMENT = createPathSegment();
    public static final URLEncoder FRAGMENT = createFragment();
    public static final URLEncoder QUERY = createQuery();
    public static final URLEncoder ALL = createAll();

    public static URLEncoder createDefault() {
        URLEncoder encoder = new URLEncoder();
        encoder.addSafeCharacter(CharPool.DASHED);
        encoder.addSafeCharacter('.');
        encoder.addSafeCharacter('_');
        encoder.addSafeCharacter('~');
        addSubDelims(encoder);
        encoder.addSafeCharacter(':');
        encoder.addSafeCharacter('@');
        encoder.addSafeCharacter('/');
        return encoder;
    }

    public static URLEncoder createPathSegment() {
        URLEncoder encoder = new URLEncoder();
        encoder.addSafeCharacter(CharPool.DASHED);
        encoder.addSafeCharacter('.');
        encoder.addSafeCharacter('_');
        encoder.addSafeCharacter('~');
        addSubDelims(encoder);
        encoder.addSafeCharacter('@');
        return encoder;
    }

    public static URLEncoder createFragment() {
        URLEncoder encoder = new URLEncoder();
        encoder.addSafeCharacter(CharPool.DASHED);
        encoder.addSafeCharacter('.');
        encoder.addSafeCharacter('_');
        encoder.addSafeCharacter('~');
        addSubDelims(encoder);
        encoder.addSafeCharacter(':');
        encoder.addSafeCharacter('@');
        encoder.addSafeCharacter('/');
        encoder.addSafeCharacter('?');
        return encoder;
    }

    public static URLEncoder createQuery() {
        URLEncoder encoder = new URLEncoder();
        encoder.setEncodeSpaceAsPlus(true);
        encoder.addSafeCharacter('*');
        encoder.addSafeCharacter(CharPool.DASHED);
        encoder.addSafeCharacter('.');
        encoder.addSafeCharacter('_');
        encoder.addSafeCharacter('=');
        encoder.addSafeCharacter(CharPool.AMP);
        return encoder;
    }

    public static URLEncoder createAll() {
        URLEncoder encoder = new URLEncoder();
        encoder.addSafeCharacter('*');
        encoder.addSafeCharacter(CharPool.DASHED);
        encoder.addSafeCharacter('.');
        encoder.addSafeCharacter('_');
        return encoder;
    }

    public URLEncoder() {
        this(new BitSet(256));
        addAlpha();
        addDigit();
    }

    private URLEncoder(BitSet safeCharacters) {
        this.encodeSpaceAsPlus = false;
        this.safeCharacters = safeCharacters;
    }

    public void addSafeCharacter(char c) {
        this.safeCharacters.set(c);
    }

    public void removeSafeCharacter(char c) {
        this.safeCharacters.clear(c);
    }

    public void setEncodeSpaceAsPlus(boolean encodeSpaceAsPlus) {
        this.encodeSpaceAsPlus = encodeSpaceAsPlus;
    }

    public String encode(String path, Charset charset) {
        if (charset == null || StrUtil.isEmpty(path)) {
            return path;
        }
        StringBuilder rewrittenPath = new StringBuilder(path.length());
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(buf, charset);
        for (int i = 0; i < path.length(); i++) {
            int c = path.charAt(i);
            if (this.safeCharacters.get(c)) {
                rewrittenPath.append((char) c);
            } else if (this.encodeSpaceAsPlus && c == 32) {
                rewrittenPath.append('+');
            } else {
                try {
                    writer.write((char) c);
                    writer.flush();
                    byte[] ba = buf.toByteArray();
                    for (byte toEncode : ba) {
                        rewrittenPath.append('%');
                        HexUtil.appendHex(rewrittenPath, toEncode, false);
                    }
                    buf.reset();
                } catch (IOException e) {
                    buf.reset();
                }
            }
        }
        return rewrittenPath.toString();
    }

    private void addAlpha() {
        for (char i = 'a'; i <= 'z'; i = (char) (i + 1)) {
            addSafeCharacter(i);
        }
        for (char i2 = 'A'; i2 <= 'Z'; i2 = (char) (i2 + 1)) {
            addSafeCharacter(i2);
        }
    }

    private void addDigit() {
        for (char i = '0'; i <= '9'; i = (char) (i + 1)) {
            addSafeCharacter(i);
        }
    }

    private static void addSubDelims(URLEncoder encoder) {
        encoder.addSafeCharacter('!');
        encoder.addSafeCharacter('$');
        encoder.addSafeCharacter(CharPool.AMP);
        encoder.addSafeCharacter(CharPool.SINGLE_QUOTE);
        encoder.addSafeCharacter('(');
        encoder.addSafeCharacter(')');
        encoder.addSafeCharacter('*');
        encoder.addSafeCharacter('+');
        encoder.addSafeCharacter(',');
        encoder.addSafeCharacter(';');
        encoder.addSafeCharacter('=');
    }
}
